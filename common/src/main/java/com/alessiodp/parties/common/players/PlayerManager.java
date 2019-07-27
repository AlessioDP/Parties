package com.alessiodp.parties.common.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public abstract class PlayerManager {
	protected final PartiesPlugin plugin;
	
	@Getter private HashMap<UUID, PartyPlayerImpl> listPartyPlayers;
	@Getter private HashSet<UUID> listPartyPlayersToDelete;
	
	
	protected PlayerManager(@NonNull PartiesPlugin instance) {
		plugin = instance;
		listPartyPlayers = new HashMap<>();
		listPartyPlayersToDelete = new HashSet<>();
	}
	
	public void reload() {
		listPartyPlayers.clear();
		listPartyPlayersToDelete.clear();
		
		plugin.getSpyManager().reload();
		
		for (User player : plugin.getOnlinePlayers()) {
			PartyPlayerImpl pp = loadPlayer(player.getUUID());
			
			PartyImpl party = plugin.getPartyManager().loadParty(pp.getPartyName());
			if (party != null)
				party.addOnlineMember(pp);
		}
	}
	
	public abstract PartyPlayerImpl initializePlayer(UUID playerUUID);
	
	public PartyPlayerImpl loadPlayer(UUID uuid) {
		PartyPlayerImpl ret = getPlayer(uuid);
		getListPartyPlayers().put(uuid, ret);
		return ret;
	}
	
	public void unloadPlayer(UUID uuid) {
		getListPartyPlayers().remove(uuid);
	}
	
	public boolean reloadPlayer(UUID uuid) {
		// Reload the player from database
		// Used by packet PLAYER_UPDATED
		if (getListPartyPlayers().containsKey(uuid)) {
			getListPartyPlayers().remove(uuid);
			loadPlayer(uuid);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_RELOADED, true);
			return true;
		}
		return false;
	}
	
	public PartyPlayerImpl getPlayer(UUID uuid) {
		PartyPlayerImpl ret;
		if (getListPartyPlayers().containsKey(uuid)) {
			// Get player from online list
			ret = getListPartyPlayers().get(uuid);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_GET_LIST
					.replace("{player}", ret.getName())
					.replace("{party}", ret.getPartyName()), true);
		} else {
			// Get player from database
			ret = plugin.getDatabaseManager().getPlayer(uuid);
			
			// Load new player
			if (ret == null) {
				ret = initializePlayer(uuid);
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_GET_NEW
						.replace("{player}", ret.getName()), true);
			} else {
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_GET_DATABASE
						.replace("{player}", ret.getName())
						.replace("{party}", ret.getPartyName()), true);
			}
		}
		return ret;
	}
}
