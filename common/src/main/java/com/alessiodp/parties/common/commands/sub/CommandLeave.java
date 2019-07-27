package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.enums.DeleteCause;
import lombok.Getter;

public class CommandLeave extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandLeave(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.LEAVE.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.LEAVE);
			return false;
		}
		
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		((PartiesCommandData) commandData).setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		
		// Calling API event
		IPlayerPreLeaveEvent partiesPreLeaveEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPreLeaveEvent(partyPlayer, party, false, null);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreLeaveEvent);
		
		if (!partiesPreLeaveEvent.isCancelled()) {
			if (party.getLeader() != null && party.getLeader().equals(sender.getUUID())) {
				// Is leader
				// Calling Pre API event
				IPartyPreDeleteEvent partiesPreDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.LEAVE, null, partyPlayer);
				((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreDeleteEvent);
				
				if (!partiesPreDeleteEvent.isCancelled()) {
					// Disbanding party
					sendMessage(sender, partyPlayer, Messages.MAINCMD_LEAVE_LEFT, party);
					party.broadcastMessage(Messages.MAINCMD_LEAVE_DISBANDED, partyPlayer);
					
					party.delete();
					
					// Calling Post API event
					IPartyPostDeleteEvent partiesPostDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.LEAVE, null, partyPlayer);
					((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostDeleteEvent);
					
					plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_LEAVE_DISBAND
							.replace("{player}", sender.getName())
							.replace("{party}", party.getName()), true);
				} else
					plugin.getLoggerManager().log(PartiesConstants.DEBUG_API_DELETEEVENT_DENY
							.replace("{party}", party.getName())
							.replace("{player}", sender.getName()), true);
			} else {
				party.getMembers().remove(sender.getUUID());
				party.removeMember(partyPlayer);
		
				sendMessage(sender, partyPlayer, Messages.MAINCMD_LEAVE_LEFT, party);
				party.broadcastMessage(Messages.MAINCMD_LEAVE_BROADCAST, partyPlayer);
				
				plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_LEAVE
						.replace("{player}", sender.getName())
						.replace("{party}", party.getName()), true);
			}
			
			// Calling API event
			IPlayerPostLeaveEvent partiesPostLeaveEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostLeaveEvent(partyPlayer, party, false, null);
			((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostLeaveEvent);
		} else
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_LEAVEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", sender.getName()), true);
	}
}
