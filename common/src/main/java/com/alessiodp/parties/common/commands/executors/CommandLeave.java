package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.api.enums.DeleteCause;

public class CommandLeave extends AbstractCommand {
	
	public CommandLeave(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.LEAVE.toString())) {
			pp.sendNoPermission(PartiesPermission.LEAVE);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		commandData.addPermission(PartiesPermission.ADMIN_KICK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command starts
		 */
		// Calling API event
		IPlayerPreLeaveEvent partiesPreLeaveEvent = plugin.getEventManager().preparePlayerPreLeaveEvent(pp, party, false, null);
		plugin.getEventManager().callEvent(partiesPreLeaveEvent);
		
		if (!partiesPreLeaveEvent.isCancelled()) {
			if (party.getLeader().equals(pp.getPlayerUUID())) {
				// Is leader
				// Calling Pre API event
				IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.LEAVE, null, pp);
				plugin.getEventManager().callEvent(partiesPreDeleteEvent);
				
				if (!partiesPreDeleteEvent.isCancelled()) {
					// Disbanding party
					pp.sendMessage(Messages.MAINCMD_LEAVE_LEFT, party);
					party.sendBroadcast(pp, Messages.MAINCMD_LEAVE_DISBANDED);
					
					party.removeParty();
					party.callChange();
					
					// Calling Post API event
					IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.LEAVE, null, pp);
					plugin.getEventManager().callEvent(partiesPostDeleteEvent);
					
					LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_LEAVE_DISBAND
							.replace("{player}", pp.getName())
							.replace("{party}", party.getName()), true);
				} else
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY
							.replace("{party}", party.getName())
							.replace("{player}", pp.getName()), true);
			} else {
				party.getMembers().remove(pp.getPlayerUUID());
				party.getOnlinePlayers().remove(pp);
				
				pp.cleanupPlayer(true);
		
				pp.sendMessage(Messages.MAINCMD_LEAVE_LEFT, party);
				party.sendBroadcast(pp, Messages.MAINCMD_LEAVE_BROADCAST);
				
				party.updateParty();
				party.callChange();
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_LEAVE
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()), true);
			}
			
			// Calling API event
			IPlayerPostLeaveEvent partiesPostLeaveEvent = plugin.getEventManager().preparePlayerPostLeaveEvent(pp, party, false, null);
			plugin.getEventManager().callEvent(partiesPostLeaveEvent);
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_LEAVEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", pp.getName()), true);
	}
}
