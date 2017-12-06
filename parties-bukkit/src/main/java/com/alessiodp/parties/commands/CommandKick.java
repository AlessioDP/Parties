package com.alessiodp.parties.commands;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PlayerUtil;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerLeaveEvent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CommandKick implements CommandInterface {
	private Parties plugin;
	 
	public CommandKick(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.KICK.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.KICK.toString()));
			return true;
		}
		if (tp.getPartyName().isEmpty() && !p.hasPermission(PartiesPermissions.KICK_OTHERS.toString())) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		
		if (!PlayerUtil.checkPlayerRankAlerter(tp, PartiesPermissions.PRIVATE_KICK))
			return true;
		
		if (args.length < 2) {
			tp.sendMessage(Messages.kick_wrongcmd);
			return true;
		}

		String playerName = args[1];
		UUID playerUUID = null;
		
		/*
		 * Players conflict handler
		 */
		HashMap<UUID, Long> listPlayersName = plugin.getDatabaseDispatcher().getPlayersFromName(playerName);
		UUID[] array = new UUID[listPlayersName.size()];
		if (listPlayersName.size() < 1) {
			tp.sendMessage(Messages.kick_nomemberother
					.replace("%player%", playerName));
			return true;
		} else if (listPlayersName.size() == 1) {
			playerUUID = listPlayersName.keySet().iterator().next();
		} else {
			StringBuilder sb = new StringBuilder();
			// Found multiple players name
			for (String str : Messages.kick_playersconflict_content) {
				if (str.contains("%list_players%")) {
					int i = 1;
					for (UUID u : listPlayersName.keySet()) {
						String newname = getNameFromMojangAPI(playerName, u);
						if (newname == null)
							newname = playerName;
						Date date = new Date(listPlayersName.get(u).longValue()*1000);
						Time time = new Time(date.getTime());
						sb.append(Messages.kick_playersconflict_player
								.replace("%number%", Integer.toString(i))
								.replace("%username%", newname)
								.replace("%party%", plugin.getDatabaseDispatcher().getPlayerPartyName(u))
								.replace("%date%", date.toString())
								.replace("%time%", time.toString()) + "\n");
						array[i-1] = u;
						i++;
					}
				} else
					sb.append(str + "\n");
			}
			if (args.length == 2) {
				tp.sendMessage(sb.toString());
				return true;
			}
			int sel = -1;
			try {
				sel = Integer.parseInt(args[2]);
				playerUUID = array[sel-1];
			} catch(Exception ex) {
				// Problem with selection
				tp.sendMessage(sb.toString());
				return true;
			}
		}
		OfflinePlayer kickedPlayer = Bukkit.getOfflinePlayer(playerUUID);
		ThePlayer kickedTP = plugin.getPlayerHandler().getPlayer(playerUUID);

		Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
		
		if (party == null || !party.getMembers().contains(kickedPlayer.getUniqueId())) {
			// Other party
			if (p.hasPermission(PartiesPermissions.KICK_OTHERS.toString())) {
				Party party_2 = plugin.getPartyHandler().getParty(kickedTP.getPartyName());
				if (party_2 == null) {
					tp.sendMessage(Messages.kick_nomemberother, kickedPlayer);
					return true;
				}
				
				// Calling API event
				PartiesPlayerLeaveEvent partiesLeaveEvent = new PartiesPlayerLeaveEvent(p, party.getName(), true, p.getUniqueId());
				Bukkit.getServer().getPluginManager().callEvent(partiesLeaveEvent);
				if (!partiesLeaveEvent.isCancelled()) {
					if (party_2.getLeader().equals(kickedPlayer.getUniqueId())) {
						// Is leader
						// Calling Pre API event
						PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party_2.getName(), PartiesPartyPreDeleteEvent.DeleteCause.KICK, kickedTP.getUUID(), p);
						Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
						if (!partiesPreDeleteEvent.isCancelled()) {
							tp.sendMessage(Messages.kick_kicksendother, kickedPlayer);
							party_2.sendBroadcastParty(kickedPlayer, Messages.leave_disbanded);
							
							LogHandler.log(LogLevel.BASIC, "Party " + party_2.getName() + " deleted by kick, by: " + p.getName(), true, ConsoleColors.CYAN);
							
							party_2.removeParty();
							// Calling Post API event
							PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party_2.getName(), PartiesPartyPostDeleteEvent.DeleteCause.KICK, kickedTP.getUUID(), p);
							Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
							
							if (kickedPlayer.isOnline())
								plugin.getPartyHandler().tag_removePlayer(Bukkit.getPlayer(kickedPlayer.getUniqueId()), null);
						} else
							LogHandler.log(LogLevel.DEBUG, "PartiesDeleteEvent is cancelled, ignoring delete of " + party_2.getName(), true);
						return true;
					}
					
					tp.sendMessage(Messages.kick_kicksendother, kickedPlayer);
					if (party_2.getMembers().contains(kickedPlayer.getUniqueId())) {
						party_2.getMembers().remove(kickedPlayer.getUniqueId());
					}
					
					if (kickedPlayer.isOnline())
						kickedTP.sendMessage(Messages.kick_kickedfrom, p);
					kickedTP.cleanupPlayer(true);
					party_2.updateParty();
					
					LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] kicked " + kickedTP.getName() + "[" + kickedPlayer.getUniqueId() + " by " + party_2.getName(), true);
				} else
					LogHandler.log(LogLevel.DEBUG, "PartiesLeaveEvent is cancelled, ignoring kick of " + kickedTP.getName(), true);
			} else {
				tp.sendMessage(Messages.kick_nomember, kickedPlayer);
			}
			return true;
		}
		
		// Same party
		if (tp.getRank() < kickedTP.getRank() && !p.hasPermission(PartiesPermissions.KICK_OTHERS.toString())) {
			tp.sendMessage(Messages.kick_uprank, kickedPlayer);
			return true;
		}
		
		party.getMembers().remove(kickedPlayer.getUniqueId());
		Player kickedPl = kickedTP.getPlayer();
		
		if (kickedPl != null) {
			// Online
			party.remOnlinePlayer(kickedPl);
			
			kickedTP.sendMessage(Messages.kick_kickedfrom, p);
			tp.sendMessage(Messages.kick_kicksend, kickedPl);
			party.sendBroadcastParty(kickedPl, Messages.kick_kickedplayer);
			kickedTP.cleanupPlayer(true);
		} else {
			tp.sendMessage(Messages.kick_kicksend, kickedPlayer);
			party.sendBroadcastParty(kickedPlayer, Messages.kick_kickedplayer);
			kickedTP.cleanupPlayer(true);
		}
		
		party.updateParty();
		LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] kicked "+ kickedTP.getName() +"["+kickedPlayer.getUniqueId()+" by " + party.getName(), true);
		return true;
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
