package com.alessiodp.parties.common.listeners;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.ADPUpdater;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.MotdTask;
import com.alessiodp.parties.common.tasks.PartyDeleteTask;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.ConsoleColor;

public abstract class JoinLeaveListener {
	protected PartiesPlugin plugin;
	
	protected JoinLeaveListener(PartiesPlugin instance) {
		plugin = instance;
	}
	
	/**
	 * Used by Bukkit, Bungeecord
	 */
	protected void onPlayerJoin(User player) {
		// Make it async
		plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
			PartyPlayerImpl pp = plugin.getPlayerManager().loadPlayer(player.getUUID());
			
			// None database: stop deleting player/party
			if (plugin.getDatabaseManager().getDatabaseType().isNone())
				plugin.getPlayerManager().getListPartyPlayersToDelete().remove(pp.getPlayerUUID());
			
			// Spy listener
			if (pp.isSpy())
				plugin.getSpyManager().addSpy(pp.getPlayerUUID());
			
			// Party checking
			if (!pp.getPartyName().isEmpty() || ConfigParties.FIXED_DEFAULT_ENABLE) {
				PartyImpl party = plugin.getPartyManager().loadParty(pp.getPartyName());
				if (party != null) {
					// Party found
					party.getOnlinePlayers().add(pp);
					
					if (plugin.getPartyManager().getListPartiesToDelete().containsKey(party.getName().toLowerCase())) {
						plugin.getPartiesScheduler().cancelTask(plugin.getPartyManager().getListPartiesToDelete().get(party.getName().toLowerCase()));
						plugin.getPartyManager().getListPartiesToDelete().remove(party.getName().toLowerCase());
						
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_DELETE_STOP
								.replace("{party}", party.getName()), true);
					}
					
					if (!party.getMotd().isEmpty()) {
						plugin.getPartiesScheduler().scheduleTaskLater(new MotdTask(plugin, player.getUUID(), pp.getCreateID()), ConfigParties.MOTD_DELAY / 20L);
					}
					
					// Update timestamp
					pp.updateNameTimestamp(System.currentTimeMillis() / 1000L);
					
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_JOIN
							.replace("{player}", player.getName())
							.replace("{party}", party.getName()), true);
				} else {
					// Party not found - checking for default one
					if (ConfigParties.FIXED_DEFAULT_ENABLE
							&& player.hasPermission(PartiesPermission.JOINDEFAULT.toString())
							&& !player.hasPermission(PartiesPermission.ADMIN_JOINDEFAULT_BYPASS.toString())) {
						party = plugin.getPartyManager().loadParty(ConfigParties.FIXED_DEFAULT_PARTY);
						if (party != null) {
							party.getMembers().add(pp.getPlayerUUID());
							party.getOnlinePlayers().add(pp);
							
							pp.setPartyName(party.getName());
							pp.setRank(ConfigParties.RANK_SET_DEFAULT);
							
							party.updateParty();
							pp.updatePlayer();
							
							if (!party.getMotd().isEmpty()) {
								plugin.getPartiesScheduler().scheduleTaskLater(new MotdTask(plugin, player.getUUID(), pp.getCreateID()), ConfigParties.MOTD_DELAY / 20L);
							}
							
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
							.replace("%thisversion%", plugin.getVersion()));
				}
			}
		});
	}
	
	/**
	 * Used by Bukkit, Bungeecord
	 */
	protected void onPlayerQuit(User player) {
		// Make it async
		plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(player.getUUID());
			boolean removePlFromList = true;
			
			// Spy listener
			if (pp.isSpy())
				plugin.getSpyManager().removeSpy(pp.getPlayerUUID());
			
			// Party checking
			if (!pp.getPartyName().isEmpty()) {
				PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
				if (party != null) {
					party.getOnlinePlayers().remove(pp);
					
					if (ConfigMain.PARTIES_JLMESSAGES) {
						party.sendBroadcast(pp, Messages.OTHER_JOINLEAVE_SERVERLEAVE);
					}
					
					if (plugin.getDatabaseManager().getDatabaseType().isNone()) {
						// Start delete timeout
						if (ConfigMain.STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT && party.getLeader().equals(pp.getPlayerUUID())) {
							// Leader left, delete now
							plugin.getPartyManager().deleteTimedParty(party.getName(), true);
						} else if (party.getOnlinePlayers().size() == 0) {
							// All players left, start timer
							if (ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY > 0) {
								plugin.getPlayerManager().getListPartyPlayersToDelete().add(pp.getPlayerUUID());
								
								int taskId = plugin.getPartiesScheduler().scheduleTaskLater(new PartyDeleteTask(plugin, party.getName()), ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY);
								
								plugin.getPartyManager().getListPartiesToDelete().put(party.getName().toLowerCase(), taskId);
								removePlFromList = false;
								
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_DELETE_START
										.replace("{party}", party.getName())
										.replace("{value}", Integer.toString(ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY )), true);
							} else
								plugin.getPartyManager().deleteTimedParty(party.getName(), false);
						}
					} else {
						if (party.getNumberOnlinePlayers() == 0) {
							plugin.getPartyManager().unloadParty(party.getName());
						}
					}
					
					// Update timestamp
					pp.updateNameTimestamp(System.currentTimeMillis() / 1000L);
				}
			}
			if (removePlFromList)
				plugin.getPlayerManager().unloadPlayer(pp.getPlayerUUID());
		});
	}
}
