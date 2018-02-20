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
import com.alessiodp.parties.addons.internal.ADPUpdater;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.tasks.MotdTask;
import com.alessiodp.parties.tasks.PartyDeleteTask;
import com.alessiodp.parties.utils.ConsoleColor;

public class JoinLeaveListener implements Listener {
	Parties plugin;
	
	public JoinLeaveListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Make it async
		plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
			Player player = event.getPlayer();
			PartyPlayerEntity pp = plugin.getPlayerManager().loadPlayer(player.getUniqueId());
			
			// None database: stop deleting player/party
			if (plugin.getDatabaseManager().getDatabaseType().isNone()
					&& plugin.getPlayerManager().getListPartyPlayersToDelete().contains(player.getUniqueId()))
				plugin.getPlayerManager().getListPartyPlayersToDelete().remove(player.getUniqueId());
			
			// Spy listener
			if (pp.isSpy())
				plugin.getSpyManager().addSpy(pp.getPlayerUUID());
			
			// Party checking
			if (!pp.getPartyName().isEmpty() || ConfigParties.FIXED_DEFAULT_ENABLE) {
				PartyEntity party = plugin.getPartyManager().loadParty(pp.getPartyName());
				if (party != null) {
					// Party found
					party.getOnlinePlayers().add(player);
					
					if (plugin.getPartyManager().getListPartiesToDelete().containsKey(party.getName().toLowerCase())) {
						Bukkit.getScheduler().cancelTask(plugin.getPartyManager().getListPartiesToDelete().get(party.getName().toLowerCase()));
						plugin.getPartyManager().getListPartiesToDelete().remove(party.getName().toLowerCase());
						
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_DELETE_STOP
								.replace("{party}", party.getName()), true);
					}
					
					if (!party.getMotd().isEmpty())
						new MotdTask(plugin, player, pp.getCreateID()).runTaskLater(plugin, ConfigParties.MOTD_DELAY);
					
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_JOIN
							.replace("{player}", player.getName())
							.replace("{party}", party.getName()), true);
				} else {
					// Party not found - checking for default one
					if (ConfigParties.FIXED_DEFAULT_ENABLE) {
						party = plugin.getPartyManager().loadParty(ConfigParties.FIXED_DEFAULT_PARTY);
						if (party != null) {
							party.getMembers().add(pp.getPlayerUUID());
							party.getOnlinePlayers().add(player);
							
							pp.setPartyName(party.getName());
							pp.setRank(ConfigParties.RANK_SET_DEFAULT);
							
							party.updateParty();
							pp.updatePlayer();
							
							if (!party.getMotd().isEmpty())
								new MotdTask(plugin, player, pp.getCreateID()).runTaskLater(plugin, ConfigParties.MOTD_DELAY);
							
							pp.sendMessage(Messages.OTHER_FIXED_DEFAULTJOIN, party);
							LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_PLAYER_JOIN_DEFAULTJOIN
									.replace("{player}", player.getName())
									.replace("{party}", party.getName()), true);
						} else {
							LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_PLAYER_JOIN_DEFAULTFAIL
									.replace("{party}", ConfigParties.FIXED_DEFAULT_PARTY), true, ConsoleColor.RED);
						}
					}
				}
				
				if (ConfigMain.PARTIES_JLMESSAGES && party != null) {
					party.sendBroadcast(pp, Messages.OTHER_JOINLEAVE_SERVERJOIN);
				}
			}
			if (ConfigMain.PARTIES_UPDATES_WARN && player.hasPermission(PartiesPermission.ADMIN_UPDATES.toString())) {
				if (!ADPUpdater.getFoundVersion().isEmpty()) {
					pp.sendMessage(Messages.PARTIES_UPDATEAVAILABLE
							.replace("%version%", ADPUpdater.getFoundVersion())
							.replace("%thisversion%", plugin.getDescription().getVersion()));
				}
			}
		});
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
		// Make it async
		plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
			boolean removePlFromList = true;
			
			// Spy listener
			if (pp.isSpy())
				plugin.getSpyManager().removeSpy(pp.getPlayerUUID());
			
			// Party checking
			if (!pp.getPartyName().isEmpty()) {
				if (pp.getHomeTask() != -1)
					plugin.getPlayerManager().remHomeCount();
				
				PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
				if (party != null) {
					party.getOnlinePlayers().remove(p);
					
					if (ConfigMain.PARTIES_JLMESSAGES) {
						party.sendBroadcast(pp, Messages.OTHER_JOINLEAVE_SERVERLEAVE);
					}
					
					if (plugin.getDatabaseManager().getDatabaseType().isNone()) {
						// Start delete timeout
						if (ConfigMain.STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT && party.getLeader().equals(p.getUniqueId())) {
							// Leader left, delete now
							plugin.getPartyManager().deleteTimedParty(party.getName(), true);
						} else if (party.getOnlinePlayers().size() == 0) {
							// All players left, start timer
							if (ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY > 0) {
								plugin.getPlayerManager().getListPartyPlayersToDelete().add(p.getUniqueId());
								
								PartyDeleteTask task = (PartyDeleteTask) new PartyDeleteTask(party.getName());
								task.runTaskLaterAsynchronously(plugin, ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY * 20);
								
								plugin.getPartyManager().getListPartiesToDelete().put(party.getName().toLowerCase(), task.getTaskId());
								removePlFromList = false;
								
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_DELETE_START
										.replace("{party}", party.getName())
										.replace("{value}", Integer.toString(ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY * 20)), true);
							} else
								plugin.getPartyManager().deleteTimedParty(party.getName(), false);
						}
					} else {
						if (party.getNumberOnlinePlayers() == 0) {
							plugin.getPartyManager().unloadParty(party.getName());
						}
					}
				}
			}
			if (removePlFromList)
				plugin.getPlayerManager().unloadPlayer(p.getUniqueId());
		});
	}
}
