package com.alessiodp.parties.common.listeners;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
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
				plugin.getPlayerManager().getCachePlayersToDelete().remove(partyPlayer.getPlayerUUID());
			
			// Party checking
			PartyImpl party = plugin.getPartyManager().loadParty(partyPlayer.getPartyId(), true);
			if (party != null) {
				// Party found
				party.addOnlineMember(partyPlayer);
				
				if (plugin.getPartyManager().getCachePartiesToDelete().containsKey(party.getId())) {
					plugin.getPartyManager().getCachePartiesToDelete().get(party.getId()).cancel();
					plugin.getPartyManager().getCachePartiesToDelete().remove(party.getId());
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_DELETE_STOP, party.getName()), true);
				}
				
				if (ConfigParties.ADDITIONAL_MOTD_ENABLE && party.getMotd() != null && !plugin.isBungeeCordEnabled()) {
					plugin.getScheduler().scheduleAsyncLater(new MotdTask(plugin, player.getUUID(), partyPlayer.getCreateID()), ConfigParties.ADDITIONAL_MOTD_DELAY, TimeUnit.MILLISECONDS);
				}
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_JOIN, player.getName(),
						party.getId() != null ? (party.getName() + "|" + party.getId().toString()) : "none"), true);
			} else if (ConfigParties.ADDITIONAL_FIXED_DEFAULT_ENABLE
					&& player.hasPermission(PartiesPermission.USER_JOINDEFAULT)
					&& !player.hasPermission(PartiesPermission.ADMIN_JOINDEFAULT_BYPASS)) {
				// Party not found - checking for default one
				party = plugin.getPartyManager().loadParty(ConfigParties.ADDITIONAL_FIXED_DEFAULT_PARTY);
				if (party != null) {
					party.addMember(partyPlayer, JoinCause.OTHERS, null);
					
					if (ConfigParties.ADDITIONAL_MOTD_ENABLE && party.getMotd() != null) {
						plugin.getScheduler().scheduleAsyncLater(new MotdTask(plugin, player.getUUID(), partyPlayer.getCreateID()), ConfigParties.ADDITIONAL_MOTD_DELAY, TimeUnit.MILLISECONDS);
					}
					
					partyPlayer.sendMessage(Messages.OTHER_FIXED_DEFAULTJOIN, party);
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_JOIN_DEFAULTJOIN, player.getName(),
							party.getName() + "|" + party.getId().toString()), true);
				} else {
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_JOIN_DEFAULTFAIL, ConfigParties.ADDITIONAL_FIXED_DEFAULT_PARTY), true);
				}
			}
			
			if (ConfigParties.GENERAL_JOIN_LEAVE_MESSAGES && party != null) {
				party.broadcastMessage(Messages.OTHER_JOINLEAVE_SERVERJOIN, partyPlayer);
			}
			
			plugin.getLoginAlertsManager().sendAlerts(player);
			
			onJoinComplete(partyPlayer);
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
			
			// Party checking
			PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyId());
			if (party != null) {
				party.removeOnlineMember(partyPlayer);
				
				if (ConfigParties.GENERAL_JOIN_LEAVE_MESSAGES) {
					party.broadcastMessage(Messages.OTHER_JOINLEAVE_SERVERLEAVE, partyPlayer);
				}
				
				if (plugin.getDatabaseManager().getDatabaseType() == StorageType.NONE) {
					// Start delete timeout
					if (ConfigMain.STORAGE_SETTINGS_NONE_DISBANDONLEADERLEFT
							&& party.getLeader() != null
							&& party.getLeader().equals(partyPlayer.getPlayerUUID())) {
						// Leader left, delete now
						plugin.getPartyManager().deleteTimedParty(party.getId(), true);
					} else if (party.getOnlineMembers(true).size() == 0) {
						// All players left, start timer
						if (ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY > 0) {
							plugin.getPlayerManager().getCachePlayersToDelete().add(partyPlayer.getPlayerUUID());
							
							CancellableTask ct = plugin.getScheduler().scheduleAsyncLater(
									new PartyDeleteTask(plugin, party.getId()),
									ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY,
									TimeUnit.SECONDS
							);
							
							plugin.getPartyManager().getCachePartiesToDelete().put(party.getId(), ct);
							removePlFromList = false;
							
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_DELETE_START, party.getName(), Integer.toString(ConfigMain.STORAGE_SETTINGS_NONE_DELAYDELETEPARTY)), true);
						} else
							plugin.getPartyManager().deleteTimedParty(party.getId(), false);
					}
				} else {
					if (party.getOnlineMembers(true).size() == 0) {
						plugin.getPartyManager().unloadParty(party);
					}
				}
			}
			if (removePlFromList)
				plugin.getPlayerManager().unloadPlayer(partyPlayer.getPlayerUUID());
			
			onLeaveComplete(partyPlayer);
		});
	}
	
	
	protected abstract void onJoinComplete(PartyPlayerImpl partyPlayer);
	
	protected abstract void onLeaveComplete(PartyPlayerImpl partyPlayer);
}
