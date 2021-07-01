package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.enums.DeleteCause;

public class CommandLeave extends PartiesSubCommand {
	
	public CommandLeave(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.LEAVE,
				PartiesPermission.USER_LEAVE,
				ConfigMain.COMMANDS_SUB_LEAVE,
				false
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_LEAVE;
		help = Messages.HELP_MAIN_COMMANDS_LEAVE;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
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
		IPlayerPreLeaveEvent partiesPreLeaveEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPreLeaveEvent(partyPlayer, party, LeaveCause.LEAVE, partyPlayer);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreLeaveEvent);
		
		if (!partiesPreLeaveEvent.isCancelled()) {
			if (party.getLeader() != null && party.getLeader().equals(sender.getUUID())) {
				// Is leader
				boolean mustDelete = true;
				// Check if leader can be changed
				if (ConfigParties.GENERAL_MEMBERS_ON_PARTY_LEAVE_CHANGE_LEADER
						&& party.getMembers().size() > 1) {
					PartyPlayerImpl newLeader = party.findNewLeader();
					
					if (newLeader != null) {
						// Found a new leader
						mustDelete = false;
						
						party.changeLeader(newLeader);
						party.removeMember(partyPlayer, LeaveCause.LEAVE, partyPlayer);
						
						sendMessage(sender, partyPlayer, Messages.MAINCMD_LEAVE_LEFT, party);
						party.broadcastMessage(Messages.MAINCMD_LEAVE_LEADER_CHANGED, newLeader);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_LEAVE_LEADER_CHANGE,
								partyPlayer.getName(), party.getName() != null ? party.getName() : "_", newLeader.getName()), true);
					}
				}
				
				if (mustDelete) {
					// Calling Pre API event
					IPartyPreDeleteEvent partiesPreDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.LEAVE, null, partyPlayer);
					((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreDeleteEvent);
					
					if (!partiesPreDeleteEvent.isCancelled()) {
						// Disbanding party
						sendMessage(sender, partyPlayer, Messages.MAINCMD_LEAVE_LEFT, party);
						party.broadcastMessage(Messages.MAINCMD_LEAVE_DISBANDED, partyPlayer);
						
						party.removeMember(partyPlayer, LeaveCause.LEAVE, partyPlayer); // Remove player for execute event
						party.delete(DeleteCause.LEAVE, partyPlayer, partyPlayer);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_LEAVE,
								partyPlayer.getName(), party.getName() != null ? party.getName() : "_", true), true);
					} else
						plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_API_DELETEEVENT_DENY, party.getId().toString(), sender.getName(), sender.getUUID().toString()), true);
				}
			} else {
				party.removeMember(partyPlayer, LeaveCause.LEAVE, partyPlayer);
		
				sendMessage(sender, partyPlayer, Messages.MAINCMD_LEAVE_LEFT, party);
				party.broadcastMessage(Messages.MAINCMD_LEAVE_BROADCAST, partyPlayer);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_LEAVE, partyPlayer.getName(), party.getId().toString(), false), true);
			}
		} else
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_LEAVEEVENT_DENY, sender.getUUID().toString(), party.getId().toString()), true);
	}
}
