package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.InviteCooldown;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.PartiesUtils;

import java.util.List;

public class CommandInvite extends AbstractCommand {
	
	public CommandInvite(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.INVITE.toString())) {
			pp.sendNoPermission(PartiesPermission.INVITE);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_INVITE))
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
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		User invitedPlayer = plugin.getPlayerByName(commandData.getArgs()[1]);
		if (invitedPlayer == null) {
			pp.sendMessage(Messages.MAINCMD_INVITE_PLAYEROFFLINE);
			return;
		}
		
		PartyPlayerImpl invitedPp = plugin.getPlayerManager().getPlayer(invitedPlayer.getUUID());
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
		
		if (invitedPp.isMuted()
				&& ConfigMain.ADDITIONAL_MUTE_ENABLE
				&& ConfigMain.ADDITIONAL_MUTE_BLOCK_INVITE) {
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
			List<InviteCooldown> list = plugin.getCooldownManager().getInviteCooldown().get(pp.getPlayerUUID());
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
			plugin.getPartiesScheduler().cancelTask(party.getInviteTasks().get(invitedPp.getPlayerUUID()));
			
			party.getInviteTasks().remove(invitedPp.getPlayerUUID());
			party.getInviteMap().remove(invitedPp.getPlayerUUID());
			
			invitedPp.setLastInvite("");
			
			pp.sendMessage(Messages.MAINCMD_INVITE_REVOKE_SENT, invitedPp);
			invitedPp.sendMessage(Messages.MAINCMD_INVITE_REVOKE_REVOKED, party);
		} else {
			// Invite
			invitedPp.setLastInvite(party.getName());
			
			pp.sendMessage(Messages.MAINCMD_INVITE_SENT, invitedPp);
			invitedPp.sendMessage(Messages.MAINCMD_INVITE_PLAYERINVITED
					.replace("%player%", pp.getName()), party);
			
			party.invitePlayer(pp.getPlayerUUID(), invitedPp.getPlayerUUID());
			
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
					.replace("{victim}", invitedPp.getName())
					.replace("{party}", party.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return PartiesUtils.tabCompletePlayerList(plugin, args, 1);
	}
}
