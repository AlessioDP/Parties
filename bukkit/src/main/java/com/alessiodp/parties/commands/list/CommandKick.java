package com.alessiodp.parties.commands.list;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerLeaveEvent;
import com.alessiodp.partiesapi.objects.PartyPlayer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CommandKick implements ICommand {
	private Parties plugin;
	 
	public CommandKick(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.KICK.toString())) {
			pp.sendNoPermission(PartiesPermission.KICK);
			return;
		}
		
		if (!p.hasPermission(PartiesPermission.KICK_OTHERS.toString())) {
			if (pp.getPartyName().isEmpty()) {
				pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
				return;
			}
			
			if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_KICK))
				return;
		}
		
		if (args.length < 2) {
			pp.sendMessage(Messages.MAINCMD_KICK_WRONGCMD);
			return;
		}
		
		/*
		 * Command handling
		 */
		String playerName = args[1];
		UUID playerUUID = null;
		
		// Players conflict handler
		List<PartyPlayer> listPlayers = plugin.getDatabaseManager().getPartyPlayersByName(playerName).join();
		UUID[] playerSelection = new UUID[listPlayers.size()];
		
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
			StringBuilder sb = new StringBuilder();
			for (String str : Messages.MAINCMD_KICK_CONFLICT_CONTENT) {
				if (str.contains("%list_players%")) {
					int i = 1;
					for (PartyPlayer pl : listPlayers) {
						String mojangName = getNameFromMojangAPI(playerName, pl.getPlayerUUID());
						if (mojangName == null)
							mojangName = pl.getName();
						
						Date date = new Date(pl.getNameTimestamp() * 1000);
						Time time = new Time(date.getTime());
						sb.append(Messages.MAINCMD_KICK_CONFLICT_PLAYER
								.replace("%number%", Integer.toString(i))
								.replace("%username%", mojangName)
								.replace("%party%", pl.getPartyName())
								.replace("%date%", date.toString())
								.replace("%time%", time.toString()) + "\n");
						playerSelection[i-1] = pl.getPlayerUUID();
						i++;
					}
				} else
					sb.append(str + "\n");
			}
			
			// Selection handling
			if (args.length == 2) {
				// Print content
				pp.sendMessage(sb.toString());
				return;
			}
			int sel = -1;
			try {
				sel = Integer.parseInt(args[2]);
				playerUUID = playerSelection[sel-1];
			} catch(Exception ex) {
				// Problem with selection, print content
				pp.sendMessage(sb.toString());
				return;
			}
		}
		
		OfflinePlayer kickedPlayer = Bukkit.getOfflinePlayer(playerUUID);
		PartyPlayerEntity kickedPp = plugin.getPlayerManager().getPlayer(playerUUID);
		
		PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		boolean otherParty = false;
		if (party == null || !party.getMembers().contains(kickedPlayer.getUniqueId())
				&& p.hasPermission(PartiesPermission.KICK_OTHERS.toString())) {
			// Other party
			otherParty = true;
			party = plugin.getPartyManager().getParty(kickedPp.getPartyName());
			
			if (party == null) {
				pp.sendMessage(Messages.MAINCMD_KICK_PLAYERNOTINPARTY_OTHER, kickedPp);
				return;
			}
		} else {
			// Same party
			if (party == null || !party.getMembers().contains(playerUUID)) {
				pp.sendMessage(Messages.MAINCMD_KICK_PLAYERNOTINPARTY, kickedPp);
				return;
			}
			
			if (pp.getRank() < kickedPp.getRank() && !p.hasPermission(PartiesPermission.KICK_OTHERS.toString())) {
				pp.sendMessage(Messages.MAINCMD_KICK_PLAYERHIGHERRANK, kickedPp);
				return;
			}
		}
		
		/*
		 * Command starts
		 */
		// Calling API event
		PartiesPlayerLeaveEvent partiesLeaveEvent = new PartiesPlayerLeaveEvent(kickedPp, party, otherParty, pp);
		Bukkit.getServer().getPluginManager().callEvent(partiesLeaveEvent);
		if (!partiesLeaveEvent.isCancelled()) {
			if (party.getLeader().equals(kickedPlayer.getUniqueId())) {
				// Leader
				// Calling Pre API event
				PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party, PartiesPartyPreDeleteEvent.DeleteCause.LEAVE, kickedPp, pp);
				Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
				if (!partiesPreDeleteEvent.isCancelled()) {
					// Disbanding party
					pp.sendMessage(Messages.MAINCMD_KICK_SENT, kickedPp);
					party.sendBroadcast(pp, Messages.MAINCMD_KICK_BROADCAST_DISBAND);
					
					party.removeParty();
					party.callChange();
					
					// Calling Post API event
					PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.LEAVE, kickedPp, pp);
					Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
					
					LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_KICK_DISBAND
							.replace("{player}", kickedPp.getName())
							.replace("{party}", party.getName())
							.replace("{sender}", p.getName())
							.replace("{other}", Boolean.toString(otherParty)), true);
				} else
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY
							.replace("{party}", party.getName())
							.replace("{player}", p.getName()), true);
			} else {
				// Normal
				party.getMembers().remove(kickedPlayer.getUniqueId());
				Player kickedPl = kickedPp.getPlayer();
				
				if (kickedPl != null) {
					// Online
					party.getOnlinePlayers().remove(kickedPl);
					
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
						.replace("{sender}", p.getName())
						.replace("{other}", Boolean.toString(otherParty)), true);
			}
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_LEAVEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", p.getName()), true);
	}
	
	
	public String getNameFromMojangAPI(String player, UUID uuid) {
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
			
			if (result.getAsJsonObject().get("id").getAsString().toString().equals(uuid.toString().replaceAll("-", "")))
				name = result.getAsJsonObject().get("name").getAsString().toString();
			in.close();
		} catch(Exception ex) {
			// Name up to date
		}
		return name;
	}
}
