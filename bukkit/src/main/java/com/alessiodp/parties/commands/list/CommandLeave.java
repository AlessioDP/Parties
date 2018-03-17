package com.alessiodp.parties.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerLeaveEvent;

public class CommandLeave implements ICommand {
	private Parties plugin;
	 
	public CommandLeave(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.LEAVE.toString())) {
			pp.sendNoPermission(PartiesPermission.LEAVE);
			return false;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		commandData.addPermission(PartiesPermission.KICK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		PartyEntity party = commandData.getParty();
		
		/*
		 * Command starts
		 */
		// Calling API event
		PartiesPlayerLeaveEvent partiesLeaveEvent = new PartiesPlayerLeaveEvent(pp, party, false, null);
		Bukkit.getServer().getPluginManager().callEvent(partiesLeaveEvent);
		if (!partiesLeaveEvent.isCancelled()) {
			if (party.getLeader().equals(pp.getPlayerUUID())) {
				// Is leader
				// Calling Pre API event
				PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party, PartiesPartyPreDeleteEvent.DeleteCause.LEAVE, null, pp);
				Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
				if (!partiesPreDeleteEvent.isCancelled()) {
					// Disbanding party
					pp.sendMessage(Messages.MAINCMD_LEAVE_LEFT, party);
					party.sendBroadcast(pp, Messages.MAINCMD_LEAVE_DISBANDED);
					
					party.removeParty();
					party.callChange();
					
					// Calling Post API event
					PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.LEAVE, null, pp);
					Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
					
					LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_LEAVE_DISBAND
							.replace("{player}", pp.getName())
							.replace("{party}", party.getName()), true);
				} else
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY
							.replace("{party}", party.getName())
							.replace("{player}", pp.getName()), true);
			} else {
				party.getMembers().remove(pp.getPlayerUUID());
				party.getOnlinePlayers().remove(commandData.getPlayer());
				
				pp.cleanupPlayer(true);
		
				pp.sendMessage(Messages.MAINCMD_LEAVE_LEFT, party);
				party.sendBroadcast(pp, Messages.MAINCMD_LEAVE_BROADCAST);
				
				party.updateParty();
				party.callChange();
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_LEAVE
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()), true);
			}
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_LEAVEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", pp.getName()), true);
	}
}
