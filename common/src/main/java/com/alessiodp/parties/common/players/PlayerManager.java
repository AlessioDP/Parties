package com.alessiodp.parties.common.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.SpyMessage;
import com.alessiodp.parties.common.utils.PartiesPermission;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public abstract class PlayerManager {
	protected final PartiesPlugin plugin;
	
	@Getter private final HashMap<UUID, PartyPlayerImpl> cachePlayers;
	@Getter private final HashSet<UUID> cachePlayersToDelete;
	@Getter private final HashSet<UUID> cacheSpies;
	
	
	protected PlayerManager(@NonNull PartiesPlugin instance) {
		plugin = instance;
		cachePlayers = new HashMap<>();
		cachePlayersToDelete = new HashSet<>();
		cacheSpies = new HashSet<>();
	}
	
	public void reload() {
		cachePlayers.clear();
		cachePlayersToDelete.clear();
		cacheSpies.clear();
		
		for (User player : plugin.getOnlinePlayers()) {
			PartyPlayerImpl pp = loadPlayer(player.getUUID());
			
			PartyImpl party = plugin.getPartyManager().loadParty(pp.getPartyId());
			if (party != null)
				party.addOnlineMember(pp);
			
			plugin.getLoginAlertsManager().sendAlerts(player);
		}
	}
	
	public abstract PartyPlayerImpl initializePlayer(UUID playerUUID);
	
	public PartyPlayerImpl loadPlayer(UUID uuid) {
		PartyPlayerImpl ret = getPlayer(uuid);
		if (ret != null) {
			getCachePlayers().put(uuid, ret);
			if (ret.isSpy())
				getCacheSpies().add(uuid);
		}
		return ret;
	}
	
	public void unloadPlayer(UUID uuid) {
		getCachePlayers().remove(uuid);
		getCacheSpies().remove(uuid);
	}
	
	public boolean reloadPlayer(UUID uuid) {
		// Reload the player from database
		// Used by packet PLAYER_UPDATED
		if (getCachePlayers().containsKey(uuid)) {
			unloadPlayer(uuid);
			loadPlayer(uuid);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_RELOADED, uuid.toString()), true);
			return true;
		}
		return false;
	}
	
	public PartyPlayerImpl getPlayer(UUID uuid) {
		PartyPlayerImpl ret = null;
		if (uuid != null) {
			ret = getCachePlayers().get(uuid);
			if (ret != null) {
				// Get player from online list
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_GET_LIST, ret.getName(),
						ret.getPartyId() != null ? (ret.getPartyId().toString()) : "none", ret.getPlayerUUID()), true);
			} else {
				// Get player from database
				ret = plugin.getDatabaseManager().getPlayer(uuid);
				
				// Load new player
				if (ret == null) {
					ret = initializePlayer(uuid);
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_GET_NEW, ret.getName(), ret.getPlayerUUID()), true);
				} else {
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_GET_DATABASE, ret.getName(),
							ret.getPartyId() != null ? (ret.getPartyId().toString()) : "none", ret.getPlayerUUID()), true);
				}
			}
		}
		return ret;
	}
	
	public void sendSpyMessage(SpyMessage message) {
		if (message.getMessage() != null && !message.getMessage().isEmpty()) {
			UUID skip = message.getPlayer() != null ? message.getPlayer().getPlayerUUID() : null;
			for (UUID uuid : getCacheSpies()) {
				if (!uuid.equals(skip) && !message.getParty().getMembers().contains(uuid)) {
					User player = plugin.getPlayer(uuid);
					if (player != null && player.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
						player.sendMessage(message.toMessage(), false);
					}
				}
			}
		}
	}
}
