package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.partiesapi.interfaces.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandInvite implements CommandInterface {
	private Parties plugin;
	 
	public CommandInvite(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.INVITE.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.INVITE.toString()));
			return true;
		}
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if (r != null && !p.hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(PartiesPermissions.PRIVATE_INVITE.toString())) {
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_INVITE.toString());
				if (rr != null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_INVITE.toString()));
				return true;
			}
		}
		if (args.length != 2) {
			tp.sendMessage(Messages.invite_wrongcmd);
			return true;
		}
		
		if ((Variables.party_maxmembers != -1) && (party.getMembers().size() >= Variables.party_maxmembers)) {
			tp.sendMessage(Messages.invite_maxplayers);
			return true;
		}
		Player invitedPlayer = plugin.getServer().getPlayer(args[1]);
		if ((invitedPlayer == null) || (!invitedPlayer.isOnline())) {
			tp.sendMessage(Messages.invite_onlyonline);
			return true;
		}
		ThePlayer invitedTp = plugin.getPlayerHandler().getPlayer(invitedPlayer.getUniqueId());
		if (!invitedTp.getPartyName().isEmpty()) {
			tp.sendMessage(Messages.invite_alreadyparty, invitedPlayer);
			return true;
		}
		if (Variables.invite_nopex && !invitedPlayer.hasPermission(PartiesPermissions.ACCEPT.toString())) {
			tp.sendMessage(Messages.invite_playernopex, invitedPlayer);
			return true;
		}
		if (invitedTp.getIgnoredParties().contains(party.getName())) {
			tp.sendMessage(Messages.invite_send, invitedPlayer);
			return true;
		}
		if (invitedTp.getInvite().contains(party.getName()) && !Variables.invite_revoke) {
			tp.sendMessage(Messages.invite_alreadyinvited, invitedPlayer);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		if (invitedTp.getInvite().contains(party.getName())) {
			plugin.getServer().getScheduler().cancelTask(party.getInvitedMap().get(invitedTp.getUUID()));
			
			party.getInvitedMap().remove(invitedTp.getUUID());
			party.getWhoInviteMap().remove(invitedTp.getUUID());
			invitedTp.setInvited("");
			
			tp.sendMessage(Messages.invite_revoked_send.replace("%player%", invitedTp.getName()));
			invitedTp.sendMessage(Messages.invite_revoked_rec, party);
			return true;
		}
		invitedTp.setInvited(party.getName());
		
		tp.sendMessage(Messages.invite_send, invitedPlayer);
		invitedTp.sendMessage(Messages.invite_rec.replace("%player%", tp.getName()), party);
		
		party.invitePlayer(p.getUniqueId(), invitedPlayer.getUniqueId());
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] invited in the party " + party.getName() + " the player " + invitedTp.getName() + "["+invitedTp.getUUID()+"]", true);
		return true;
	}
}
