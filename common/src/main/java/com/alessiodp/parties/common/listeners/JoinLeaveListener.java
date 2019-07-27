package com.alessiodp.parties.common.listeners;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.MotdTask;
import com.alessiodp.parties.common.tasks.PartyDeleteTask;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class JoinLeaveListener {
	protected final PartiesPlugin plugin;
	
	/**
	 * Used by Bukkit, Bungeecord
	 */
	protected void onPlayerJoin(User player) {
		// Make it async
		plugin.getScheduler().runAsync(() -> {
			PartyPlayerImpl partyPlayer = plugin.getPlayerManager().loadPlayer(player.getUUID());
			
			// None database: stop deleting player/party
			if (plugin.getDatabaseManager().getDatabaseType() == StorageType.NONE)
				plugin.getPlayerManager().getListPartyPlayersToDelete().remove(partyPlayer.getPlayerUUID());
			
			// Spy listener
			if (partyPlayer.isSpy())
				plugin.getSpyManager().addSpy(partyPlayer.getPlayerUUID());
			
			// Party checking
			if (!partyPlayer.getPartyName().isEmpty() || ConfigParties.FIXED_DEFAULT_ENABLE) {
				PartyImpl party = plugin.getPartyManager().loadParty(partyPlayer.getPartyName());
				if (party != null) {
					// Party found
					party.addOnlineMember(partyPlayer);
					
					if (plugin.getPartyManager().getListPartiesToDelete().containsKey(party.getName().toLowerCase())) {
						plugin.getPartyManager().getListPartiesToDelete().get(party.getName().toLowerCase()).cancel();
						plugin.getPartyManager().getListPartiesToDelete().remove(party.getName().toLowerCase());
						
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_DELETE_STOP
								.replace("{party}", party.getName()), true);
					}
					
					if (!party.getMotd().isEmpty()) {
						plugin.getScheduler().scheduleAsyncLater(new MotdTask(plugin, player.getUUID(), partyPlayer.getCreateID()), ConfigParties.MOTD_DELAY, TimeUnit.MILLISECONDS);
					}
					
					plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_JOIN
							.replace("{player}", player.getName())
							.replace("{party}", party.getName()), true);
				} else {
					// Party not found - checking for default one
					if (ConfigParties.FIXED_DEFAULT_ENABLE
							&& player.hasPermission(PartiesPermission.JOINDEFAULT.toString())
							&& !player.hasPermission(PartiesPermission.ADMIN_JOINDEFAULT_BYPASS.toString())) {
						party = plugin.getPartyManager().loadParty(ConfigParties.FIXED_DEFAULT_PARTY);
						if (party != null) {
							party.addMember(partyPlayer);
							
							if (!party.getMotd().isEmpty()) {
								plugin.getScheduler().scheduleAsyncLater(new MotdTask(plugin, player.getUUID(), partyPlayer.getCreateID()), ConfigParties.MOTD_DELAY, TimeUnit.MILLISECONDS);
							}
							
							partyPlayer.sendMessage(Messages.OTHER_FIXED_DEFAULTJOIN, party);
							plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_JOIN_DEFAULTJOIN
									.replace("{player}", player.getName())
									.replace("{party}", party.getName()), true);
						} else {
							plugin.getLoggerManager().printError(PartiesConstants.DEBUG_PLAYER_JOIN_DEFAULTFAIL
									.replace("{party}", ConfigParties.FIXED_DEFAULT_PARTY));
						}
					}
				}
				
				if (ConfigMain.PARTIES_JOINLEAVEMESSAGES && party != null) {
					party.broadcastMessage(Messages.OTHER_JOINLEAVE_SERVERJOIN, partyPlayer);
				}
			}
			
			if (ConfigMain.PARTIES_UPDATES_WARN
					&& player.hasPermission(PartiesPermission.ADMIN_UPDATES.toString())
					&& !plugin.getAdpUpdater().getFoundVersion().isEmpty()) {
				partyPlayer.sendMessage(Messages.PARTIES_UPDATEAVAILABLE
						.replace("%version%", plugin.getAdpUpdater().getFoundVersion())
						.replace("%thisversion%", plugin.getVersion()));
			}
		});
	}
	
	/**
	 * Used by Bukkit, Bungeecord
	 */
	protected void onPlayerQuit(User player) {
		// Make it async
		plugin.getScheduler().runAsync(() -> {
			PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(player.getUUID());
			boolean removePlFromList = true;
			
			// Spy listener
			if (partyPlayer.isSpy())
				plugin.getSpyManager().removeSpy(partyPlayer.getPlayerUUID());
			
			// Party checking
			if (!partyPlayer.getPartyName().isEmpty()) {
				PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyName());
				if (party != null) {
					party.removeOnlineMember(partyPlayer);
					
					if (ConfigMain.PARTIES_JOINLEAVEMESSAGES) {
						party.broadcastMessage(Messages.OTHER_JOINLEAVE_SERVERLEAVE, partyPlayer);
					}
					
					if (plugin.getDatabaseManager().getDatabaseType() == StorageType.NONE) {
						// Start delete timeout
						if (ConfigMain.STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT
								&& party.getLeader() != null
								&& party.getLeader().equals(partyPlayer.getPlayerUUID())) {
							// Leader left, delete now
							plugin.getPartyManager().deleteTimedParty(party.getName(), true);
						} else if (party.getOnlineMembers(true).size() == 0) {
							// All players left, start timer
							if (ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY > 0) {
								plugin.getPlayerManager().getListPartyPlayersToDelete().add(partyPlayer.getPlayerUUID());
								
								CancellableTask ct = plugin.getScheduler().scheduleAsyncLater(
										new PartyDeleteTask(plugin, party.getName()),
										ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY,
										TimeUnit.SECONDS
								);
								
								plugin.getPartyManager().getListPartiesToDelete().put(party.getName().toLowerCase(), ct);
								removePlFromList = false;
								
								plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_DELETE_START
										.replace("{party}", party.getName())
										.replace("{value}", Integer.toString(ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY )), true);
							} else
								plugin.getPartyManager().deleteTimedParty(party.getName(), false);
						}
					} else {
						if (party.getOnlineMembers(true).size() == 0) {
							plugin.getPartyManager().unloadParty(party.getName());
						}
					}
				}
			}
			if (removePlFromList)
				plugin.getPlayerManager().unloadPlayer(partyPlayer.getPlayerUUID());
		});
	}
}
