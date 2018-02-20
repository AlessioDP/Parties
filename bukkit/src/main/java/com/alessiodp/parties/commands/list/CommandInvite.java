package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public class CommandInvite implements ICommand {
	private Parties plugin;
	 
	public CommandInvite(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.INVITE.toString())) {
			pp.sendNoPermission(PartiesPermission.INVITE);
			return;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_INVITE))
			return;
		
		if (args.length != 2) {
			pp.sendMessage(Messages.MAINCMD_INVITE_WRONGCMD);
			return;
		}
		
		if ((ConfigParties.GENERAL_MEMBERSLIMIT != -1)
				&& (party.getMembers().size() >= ConfigParties.GENERAL_MEMBERSLIMIT)) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYFULL);
			return;
		}
		
		/*
		 * Command handling
		 */
		Player invitedPlayer = plugin.getServer().getPlayer(args[1]);
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
		
		if (invitedPp.getLastInvite().contains(party.getName()) && !ConfigParties.GENERAL_INVITE_REVOKE) {
			pp.sendMessage(Messages.MAINCMD_INVITE_ALREADYINVITED, invitedPp);
			return;
		}
		
		/*
		 * Command starts
		 */
		if (invitedPp.getLastInvite().contains(party.getName())) {
			// Revoke
			plugin.getServer().getScheduler().cancelTask(party.getInvited().get(invitedPp.getPlayerUUID()));
			
			party.getInvited().remove(invitedPp.getPlayerUUID());
			party.getWhoInvite().remove(invitedPp.getPlayerUUID());
			invitedPp.setLastInvite("");
			
			pp.sendMessage(Messages.MAINCMD_INVITE_REVOKE_SENT, invitedPp);
			invitedPp.sendMessage(Messages.MAINCMD_INVITE_REVOKE_REVOKED, party);
		} else {
			invitedPp.setLastInvite(party.getName());
			
			pp.sendMessage(Messages.MAINCMD_INVITE_SENT, invitedPp);
			invitedPp.sendMessage(Messages.MAINCMD_INVITE_PLAYERINVITED
					.replace("%player%", pp.getName()), party);
			
			party.invitePlayer(p.getUniqueId(), invitedPlayer.getUniqueId());
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_INVITE
					.replace("{player}", p.getName())
					.replace("{victim}", invitedPlayer.getName())
					.replace("{party}", party.getName()), true);
		}
	}
}
