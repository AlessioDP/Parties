package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandKick extends PartiesSubCommand {
	
	public CommandKick(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.KICK,
				PartiesPermission.USER_KICK,
				ConfigMain.COMMANDS_CMD_KICK,
				true
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PLAYER
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_KICK;
		help = Messages.HELP_MAIN_COMMANDS_KICK;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = null;
		if (sender.isPlayer()) {
			partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			if (!sender.hasPermission(PartiesPermission.ADMIN_KICK_OTHERS)) {
				if (!partyPlayer.isInParty()) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return false;
				}
				
				if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_KICK))
					return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		}
		
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		commandData.addPermission(PartiesPermission.ADMIN_KICK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		String playerName = commandData.getArgs()[1];
		UUID playerUUID = null;
		
		Set<UUID> matchingPlayers = LLAPIHandler.getPlayerByName(playerName);
		List<UUID> listPlayers = new LinkedList<>(matchingPlayers);
		listPlayers.removeIf((uuid) -> !((PartiesPlugin) plugin).getPlayerManager().getPlayer(uuid).isInParty());
		Collections.sort(listPlayers);
		
		if (listPlayers.size() > 1) {
			// Multiple players
			// Check if the player have selected someone
			if (commandData.getArgs().length > 2) {
				try {
					int sel = Integer.parseInt(commandData.getArgs()[2]);
					playerUUID = listPlayers.get(sel-1);
				} catch(Exception ignored) {}
			}
			
			// The player didn't select a player
			if (playerUUID == null) {
				for (String str : Messages.MAINCMD_KICK_CONFLICT_CONTENT) {
					if (str.contains("%list_players%")) {
						int i = 1;
						for (UUID uuid : listPlayers) {
							PartyPlayerImpl pp = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(uuid);
							
							sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_CONFLICT_PLAYER
									.replace("%number%", Integer.toString(i))
									.replace("%username%", playerName), pp, ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(pp));
							i++;
						}
					} else {
						sendMessage(sender, partyPlayer, str);
					}
				}
				return;
			}
		} else if (listPlayers.size() == 1) {
			// Player found
			playerUUID = listPlayers.get(0);
		} else {
			// Not found
			sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_PLAYERNOTINPARTY_OTHER
					.replace("%player%", playerName));
			return;
		}
		
		OfflineUser kickedPlayer = plugin.getOfflinePlayer(playerUUID);
		PartyPlayerImpl kickedPp = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(playerUUID);
		
		PartyImpl party = partyPlayer != null ? ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId()) : null;
		
		boolean otherParty = false;
		if (party == null || !party.getMembers().contains(kickedPlayer.getUUID())) {
			// Other party
			otherParty = true;
			if (commandData.havePermission(PartiesPermission.ADMIN_KICK_OTHERS)) {
				party = ((PartiesPlugin) plugin).getPartyManager().getParty(kickedPp.getPartyId());
				
				if (party == null) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_PLAYERNOTINPARTY_OTHER, kickedPp);
					return;
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_PLAYERNOTINPARTY, kickedPp);
				return;
			}
		} else {
			// Same party
			if (partyPlayer.getRank() < kickedPp.getRank() && !commandData.havePermission(PartiesPermission.ADMIN_KICK_OTHERS)) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_PLAYERHIGHERRANK, kickedPp);
				return;
			}
		}
		
		// Command starts
		
		// Calling API event
		IPlayerPreLeaveEvent partiesPreLeaveEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPreLeaveEvent(kickedPp, party, otherParty, partyPlayer);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreLeaveEvent);
		
		if (!partiesPreLeaveEvent.isCancelled()) {
			if (party.getLeader() != null && party.getLeader().equals(kickedPlayer.getUUID())) {
				// Leader
				boolean mustDelete = true;
				// Check if leader can be changed
				if (ConfigParties.GENERAL_MEMBERS_CHANGE_LEADER_ON_LEAVE
						&& party.getMembers().size() > 1) {
					PartyPlayerImpl newLeader = party.findNewLeader();
					
					if (newLeader != null) {
						// Found a new leader
						mustDelete = false;
						
						party.changeLeader(newLeader);
						party.removeMember(kickedPp);
						
						sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_SENT, kickedPp);
						party.broadcastMessage(Messages.MAINCMD_KICK_BROADCAST_LEADER_CHANGED, newLeader);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_KICK_LEADER_CHANGE,
								sender.getName(), kickedPp.getName(), party.getName(), newLeader.getName()), true);
					}
				}
				
				if (mustDelete) {
					// Calling Pre API event
					IPartyPreDeleteEvent partiesPreDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.KICK, kickedPp, partyPlayer);
					((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreDeleteEvent);
					
					if (!partiesPreDeleteEvent.isCancelled()) {
						// Disbanding party
						sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_SENT, kickedPp);
						party.broadcastMessage(Messages.MAINCMD_KICK_BROADCAST_DISBAND, kickedPp);
						
						party.delete();
						
						// Calling Post API event
						IPartyPostDeleteEvent partiesPostDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.KICK, kickedPp, partyPlayer);
						((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostDeleteEvent);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_KICK,
								sender.getName(), kickedPp.getName(), party.getName(), otherParty, true), true);
					} else
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_DELETEEVENT_DENY, party.getId().toString(), sender.getName(), sender.getUUID().toString()), true);
				}
			} else {
				// Normal
				if (kickedPlayer.isOnline()) {
					// Online
					kickedPp.sendMessage(Messages.MAINCMD_KICK_PLAYERKICKED, partyPlayer);
				}
				
				party.removeMember(kickedPp);
				
				sendMessage(sender, partyPlayer, Messages.MAINCMD_KICK_SENT, kickedPp);
				party.broadcastMessage(Messages.MAINCMD_KICK_BROADCAST, kickedPp);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_KICK,
						sender.getName(), kickedPp.getName(), party.getName(), otherParty, false), true);
			}
			
			// Calling API event
			IPlayerPostLeaveEvent partiesPostLeaveEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostLeaveEvent(kickedPp, party, otherParty, partyPlayer);
			((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostLeaveEvent);
		} else
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_LEAVEEVENT_DENY, sender.getUUID().toString(), party.getId().toString()), true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		// Filter players is too expensive. You should call the database to get every existing player.
		List<String> ret = new ArrayList<>();
		for (User u : plugin.getOnlinePlayers()) {
			PartyPlayerImpl p = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(u.getUUID());
			if (p != null && !p.isVanished())
				ret.add(p.getName());
		}
		return plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[1]);
	}
}
