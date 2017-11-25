package com.alessiodp.parties.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.parties.utils.tasks.MotdTask;
import com.alessiodp.parties.utils.tasks.PartyDeleteTask;

public class JoinLeaveListener implements Listener {
	Parties plugin;
	
	public JoinLeaveListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ThePlayer tp = plugin.getPlayerHandler().loadPlayer(player.getUniqueId());
		
		// None database: stop deleting player/party
		if (plugin.getDatabaseType().isNone()
				&& plugin.getPlayerHandler().getListPlayersToDelete().contains(player.getUniqueId()))
			plugin.getPlayerHandler().getListPlayersToDelete().remove(player.getUniqueId());
		
		// Party checking
		if (!tp.getPartyName().isEmpty() || Variables.default_enable) {
			Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
			if (party != null) {
				// Party found
				party.addOnlinePlayer(player);
				
				if (plugin.getPartyHandler().getListPartiesToDelete().containsKey(party.getName().toLowerCase())) {
					Bukkit.getScheduler().cancelTask(plugin.getPartyHandler().getListPartiesToDelete().get(party.getName().toLowerCase()));
					plugin.getPartyHandler().getListPartiesToDelete().remove(party.getName().toLowerCase());
					
					LogHandler.log(LogLevel.DEBUG, "Stopped PartyDeleteTask for " + party.getName(), true);
				}
				
				if (!party.getMOTD().isEmpty())
					new MotdTask(plugin, player, tp.getCreateID()).runTaskLater(plugin, Variables.motd_delay);
				
				LogHandler.log(LogLevel.DEBUG, player.getName() + "[" + player.getUniqueId() + "] entered in the game, party " + party.getName(), true);
			} else {
				// Party not found - checking for default one
				if (Variables.default_enable) {
					party = plugin.getPartyHandler().loadParty(Variables.default_party);
					if (party != null) {
						party.getMembers().add(tp.getUUID());
						party.addOnlinePlayer(player);
						
						tp.setPartyName(party.getName());
						tp.setRank(Variables.rank_default);
						
						party.updateParty();
						tp.updatePlayer();
						
						if (!party.getMOTD().isEmpty())
							new MotdTask(plugin, player, tp.getCreateID()).runTaskLater(plugin, Variables.motd_delay);
						
						tp.sendMessage(Messages.defaultjoined, party);
						LogHandler.log(LogLevel.MEDIUM, player.getName() + "[" + player.getUniqueId() + "] joined, set default party " + party.getName(), true);
					} else {
						LogHandler.log(LogLevel.BASIC, "Failed load default party", true, ConsoleColors.RED);
					}
				}
			}
			
			if (Variables.joinleavemessages && party != null) {
				if (!Messages.serverjoin.isEmpty())
					party.sendBroadcastParty(player, Messages.serverjoin);
			}
		}
		if (player.hasPermission(PartiesPermissions.ADMIN_UPDATES.toString()) && Variables.warnupdates) {
			if (plugin.isUpdateAvailable()) {
				tp.sendMessage(Messages.updateavailable
						.replace("%version%", plugin.getNewUpdate())
						.replace("%thisversion%", plugin.getDescription().getVersion()));
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		globalQuit(event.getPlayer());
	}
	@EventHandler(ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		globalQuit(event.getPlayer());
	}
	private void globalQuit(Player p) {
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		boolean removePlFromList = true;
		if (!tp.getPartyName().isEmpty()) {
			if (tp.getHomeTask() != -1)
				plugin.getPlayerHandler().remHomeCount();
			
			Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
			if (party != null) {
				party.remOnlinePlayer(p);
				
				if (Variables.joinleavemessages) {
					if (!Messages.serverleave.isEmpty())
						party.sendBroadcastParty(p, Messages.serverleave);
				}
				
				if (plugin.getDatabaseType().isNone()) {
					// Start delete timeout
					if (Variables.database_none_leaderleft && party.getLeader().equals(p.getUniqueId())) {
						// Leader left, delete now
						plugin.getPartyHandler().deleteTimedParty(party.getName(), true);
					} else if (party.getOnlinePlayers().size() == 0) {
						// All players left, start timer
						if (Variables.database_none_delay > 0) {
							plugin.getPlayerHandler().getListPlayersToDelete().add(p.getUniqueId());
							
							PartyDeleteTask task = (PartyDeleteTask) new PartyDeleteTask(party.getName());
							task.runTaskLaterAsynchronously(plugin, Variables.database_none_delay * 20);
							
							plugin.getPartyHandler().getListPartiesToDelete().put(party.getName().toLowerCase(), task.getTaskId());
							removePlFromList = false;
							
							LogHandler.log(LogLevel.DEBUG, "Started PartyDeleteTask for " + Variables.database_none_delay + " seconds", true);
						} else
							plugin.getPartyHandler().deleteTimedParty(party.getName(), false);
					}
				} else {
					if (party.getNumberOnlinePlayers() == 0) {
						plugin.getPartyHandler().unloadParty(party.getName());
					}
				}
			}
		}
		if (removePlFromList)
			plugin.getPlayerHandler().unloadPlayer(p.getUniqueId());
	}
}
