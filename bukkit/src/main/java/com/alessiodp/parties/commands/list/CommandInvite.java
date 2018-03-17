package com.alessiodp.parties.commands.list;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.InviteCooldown;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public class CommandInvite implements ICommand {
	private Parties plugin;
	 
	public CommandInvite(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.INVITE.toString())) {
			pp.sendNoPermission(PartiesPermission.INVITE);
			return false;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_INVITE))
			return false;
		
		if (commandData.getArgs().length != 2) {
			pp.sendMessage(Messages.MAINCMD_INVITE_WRONGCMD);
			return false;
		}
		
		if ((ConfigParties.GENERAL_MEMBERSLIMIT != -1)
				&& (party.getMembers().size() >= ConfigParties.GENERAL_MEMBERSLIMIT)) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYFULL);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		PartyEntity party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		Player invitedPlayer = plugin.getServer().getPlayer(commandData.getArgs()[1]);
		if ((invitedPlayer == null) || (!invitedPlayer.isOnline())) {
			pp.sendMessage(Messages.MAINCMD_INVITE_PLAYEROFFLINE);
			return;
		}
		
		PartyPlayerEntity invitedPp = plugin.getPlayerManager().getPlayer(invitedPlayer.getUniqueId());
		if (!invitedPp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_INVITE_PLAYERINPARTY, invitedPp);
			return;
		}
		
		if (ConfigParties.GENERAL_INVITE_PREVENTINVITEPERM
				&& !invitedPlayer.hasPermission(PartiesPermission.ACCEPT.toString())) {
			pp.sendMessage(Messages.MAINCMD_INVITE_PLAYERNOPERM, invitedPp);
			return;
		}
		
		if (invitedPp.getIgnoredParties().contains(party.getName())) {
			// Invited player ignoring the party, fake sent
			pp.sendMessage(Messages.MAINCMD_INVITE_SENT, invitedPp);
			return;
		}
		
		if (invitedPp.isPreventNotify()
				&& ConfigMain.ADDITIONAL_NOTIFY_ENABLE
				&& ConfigMain.ADDITIONAL_NOTIFY_BLOCK_INVITE) {
			// Invited player has disabled notifications, fake sent
			pp.sendMessage(Messages.MAINCMD_INVITE_SENT, invitedPp);
			return;
		}
		
		boolean isRevoke = false;
		if (invitedPp.getLastInvite().contains(party.getName())) {
			isRevoke = true;
			
			if (!ConfigParties.GENERAL_INVITE_REVOKE) {
				pp.sendMessage(Messages.MAINCMD_INVITE_ALREADYINVITED, invitedPp);
				return;
			}
		}
		
		if (!isRevoke && ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE) {
			// Check invite cooldown
			ArrayList<InviteCooldown> list = plugin.getPlayerManager().getInviteCooldown().get(pp.getPlayerUUID());
			if (list != null) {
				for (InviteCooldown ic : list) {
					long diff = ic.getDiffTime();
					
					if (ic.getType() == InviteCooldown.CooldownType.GLOBAL
							|| ic.getInvited().equals(invitedPp.getPlayerUUID())) {
						if (diff < ic.getType().getTick()) {
							String msg = "";
							switch (ic.getType()) {
							case GLOBAL:
								msg = Messages.MAINCMD_INVITE_COOLDOWN_GLOBAL;	
								break;
							case INDIVIDUAL:
								msg = Messages.MAINCMD_INVITE_COOLDOWN_INDIVIDUAL;
							}
							
							pp.sendMessage(msg.replace("%seconds%", String.valueOf(ic.getType().getTick() - diff)));
							return;
						}
					}
				}
			}
		}
		
		/*
		 * Command starts
		 */
		if (isRevoke) {
			// Revoke
			plugin.getServer().getScheduler().cancelTask(party.getInvited().get(invitedPp.getPlayerUUID()));
			
			party.getInvited().remove(invitedPp.getPlayerUUID());
			party.getWhoInvite().remove(invitedPp.getPlayerUUID());
			invitedPp.setLastInvite("");
			
			pp.sendMessage(Messages.MAINCMD_INVITE_REVOKE_SENT, invitedPp);
			invitedPp.sendMessage(Messages.MAINCMD_INVITE_REVOKE_REVOKED, party);
		} else {
			// Invite
			invitedPp.setLastInvite(party.getName());
			
			pp.sendMessage(Messages.MAINCMD_INVITE_SENT, invitedPp);
			invitedPp.sendMessage(Messages.MAINCMD_INVITE_PLAYERINVITED
					.replace("%player%", pp.getName()), party);
			
			party.invitePlayer(pp.getPlayerUUID(), invitedPlayer.getUniqueId());
			
			if (ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE) {
				if (ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL > 0) {
					new InviteCooldown(plugin, pp.getPlayerUUID())
					.createTask(ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL);
				}
				if (ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL > 0) {
					new InviteCooldown(plugin, pp.getPlayerUUID(), invitedPp.getPlayerUUID())
					.createTask(ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL);
				}
			}
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_INVITE
					.replace("{player}", pp.getName())
					.replace("{victim}", invitedPlayer.getName())
					.replace("{party}", party.getName()), true);
		}
	}
}
