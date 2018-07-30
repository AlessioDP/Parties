package com.alessiodp.parties.common.commands.executors;

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
import com.alessiodp.parties.common.user.OfflineUser;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.party.PartiesPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.player.PartiesPlayerLeaveEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

public class CommandKick extends AbstractCommand {
	
	public CommandKick(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.KICK.toString())) {
			pp.sendNoPermission(PartiesPermission.KICK);
			return false;
		}
		
		if (!sender.hasPermission(PartiesPermission.ADMIN_KICK_OTHERS.toString())) {
			if (pp.getPartyName().isEmpty()) {
				pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
			
			if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_KICK))
				return false;
		}
		
		if (commandData.getArgs().length < 2) {
			pp.sendMessage(Messages.MAINCMD_KICK_WRONGCMD);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.ADMIN_KICK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		String playerName = commandData.getArgs()[1];
		UUID playerUUID = null;
		
		// Players conflict handler
		List<PartyPlayerImpl> listPlayers = plugin.getDatabaseManager().getPartyPlayersByName(playerName).join();
		
		if (listPlayers.size() < 1) {
			// Other player not in party
			pp.sendMessage(Messages.MAINCMD_KICK_PLAYERNOTINPARTY_OTHER
					.replace("%player%", playerName));
			return;
		} else if (listPlayers.size() == 1) {
			// Get the player
			playerUUID = listPlayers.get(0).getPlayerUUID();
		} else {
			// Found multiple players name
			
			// Check if the player have selected someone
			if (commandData.getArgs().length > 2) {
				try {
					int sel = Integer.parseInt(commandData.getArgs()[2]);
					playerUUID = listPlayers.get(sel-1).getPlayerUUID();
				} catch(Exception ignored) {}
			}
			
			// The player didn't selected a player
			if (playerUUID == null) {
				for (String str : Messages.MAINCMD_KICK_CONFLICT_CONTENT) {
					if (str.contains("%list_players%")) {
						int i = 1;
						for (PartyPlayerImpl pl : listPlayers) {
							String mojangName = getNameFromMojangAPI(playerName, pl.getPlayerUUID());
							if (mojangName == null)
								mojangName = pl.getName();
							
							Date date = new Date(pl.getNameTimestamp() * 1000);
							Time time = new Time(date.getTime());
							pp.sendMessage(Messages.MAINCMD_KICK_CONFLICT_PLAYER
									.replace("%number%", Integer.toString(i))
									.replace("%old_username%", playerName)
									.replace("%username%", mojangName)
									.replace("%party%", pl.getPartyName())
									.replace("%date%", date.toString())
									.replace("%time%", time.toString()));
							i++;
						}
					} else {
						pp.sendMessage(str);
					}
				}
				return;
			}
		}
		
		OfflineUser kickedPlayer = plugin.getOfflinePlayer(playerUUID);
		PartyPlayerImpl kickedPp = plugin.getPlayerManager().getPlayer(playerUUID);
		
		PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		boolean otherParty = false;
		if (party == null || !party.getMembers().contains(kickedPlayer.getUUID())
				&& commandData.havePermission(PartiesPermission.ADMIN_KICK_OTHERS)) {
			// Other party
			otherParty = true;
			party = plugin.getPartyManager().getParty(kickedPp.getPartyName());
			
			if (party == null) {
				pp.sendMessage(Messages.MAINCMD_KICK_PLAYERNOTINPARTY_OTHER, kickedPp);
				return;
			}
		} else {
			// Same party
			if (!party.getMembers().contains(playerUUID)) {
				pp.sendMessage(Messages.MAINCMD_KICK_PLAYERNOTINPARTY, kickedPp);
				return;
			}
			
			if (pp.getRank() < kickedPp.getRank() && !commandData.havePermission(PartiesPermission.ADMIN_KICK_OTHERS)) {
				pp.sendMessage(Messages.MAINCMD_KICK_PLAYERHIGHERRANK, kickedPp);
				return;
			}
		}
		
		/*
		 * Command starts
		 */
		// Calling API event
		PartiesPlayerLeaveEvent partiesLeaveEvent = plugin.getEventManager().preparePlayerLeaveEvent(kickedPp, party, otherParty, pp);
		plugin.getEventManager().callEvent(partiesLeaveEvent);
		
		if (!partiesLeaveEvent.isCancelled()) {
			if (party.getLeader().equals(kickedPlayer.getUUID())) {
				// Leader
				// Calling Pre API event
				PartiesPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.LEAVE, kickedPp, pp);
				plugin.getEventManager().callEvent(partiesPreDeleteEvent);
				
				if (!partiesPreDeleteEvent.isCancelled()) {
					// Disbanding party
					pp.sendMessage(Messages.MAINCMD_KICK_SENT, kickedPp);
					party.sendBroadcast(pp, Messages.MAINCMD_KICK_BROADCAST_DISBAND);
					
					party.removeParty();
					party.callChange();
					
					// Calling Post API event
					PartiesPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.LEAVE, kickedPp, pp);
					plugin.getEventManager().callEvent(partiesPostDeleteEvent);
					
					LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_KICK_DISBAND
							.replace("{player}", kickedPp.getName())
							.replace("{party}", party.getName())
							.replace("{user}", pp.getName())
							.replace("{other}", Boolean.toString(otherParty)), true);
				} else
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY
							.replace("{party}", party.getName())
							.replace("{player}", pp.getName()), true);
			} else {
				// Normal
				party.getMembers().remove(kickedPp.getPlayerUUID());
				
				if (kickedPlayer.isOnline()) {
					// Online
					party.getOnlinePlayers().remove(kickedPp);
					
					pp.sendMessage(Messages.MAINCMD_KICK_SENT, kickedPp);
					kickedPp.sendMessage(Messages.MAINCMD_KICK_PLAYERKICKED, pp);
					party.sendBroadcast(kickedPp, Messages.MAINCMD_KICK_BROADCAST);
					
					kickedPp.cleanupPlayer(true);
					party.callChange();
				} else {
					pp.sendMessage(Messages.MAINCMD_KICK_SENT, kickedPp);
					party.sendBroadcast(kickedPp, Messages.MAINCMD_KICK_BROADCAST);
					
					kickedPp.cleanupPlayer(true);
				}
				
				party.updateParty();
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_KICK
						.replace("{player}", kickedPp.getName())
						.replace("{party}", party.getName())
						.replace("{user}", pp.getName())
						.replace("{other}", Boolean.toString(otherParty)), true);
			}
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_LEAVEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", pp.getName()), true);
	}
	
	
	private String getNameFromMojangAPI(String player, UUID uuid) {
		String name = null;
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/"+player+"?at=0");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("GET");
			con.setDoInput(true);
			InputStreamReader in = new InputStreamReader(con.getInputStream());
			
			//Read the result
			JsonParser parser = new JsonParser();
			JsonElement result = parser.parse(in);
			
			if (result.getAsJsonObject().get("id").getAsString().equals(uuid.toString().replaceAll("-", "")))
				name = result.getAsJsonObject().get("name").getAsString();
			in.close();
		} catch(Exception ex) {
			// Name up to date
		}
		return name;
	}
}
