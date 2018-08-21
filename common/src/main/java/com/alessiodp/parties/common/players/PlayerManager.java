package com.alessiodp.parties.common.players;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public abstract class PlayerManager {
	protected PartiesPlugin plugin;
	
	@Getter private HashMap<UUID, PartyPlayerImpl> listPartyPlayers;
	@Getter private HashSet<UUID> listPartyPlayersToDelete;
	
	
	protected PlayerManager(PartiesPlugin instance) {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		plugin = instance;
	}
	
	public abstract PartyPlayerImpl initializePlayer(UUID playerUUID);
	
	public void reload() {
		listPartyPlayers = new HashMap<>();
		listPartyPlayersToDelete = new HashSet<>();
		
		plugin.getSpyManager().reload();
		
		for (User player : plugin.getOnlinePlayers()) {
			PartyPlayerImpl pp = loadPlayer(player.getUUID());
			
			PartyImpl party = plugin.getPartyManager().loadParty(pp.getPartyName());
			if (party != null)
				party.getOnlinePlayers().add(pp);
		}
	}
	
	
	public PartyPlayerImpl loadPlayer(UUID uuid) {
		PartyPlayerImpl ret = getPlayer(uuid);
		getListPartyPlayers().put(uuid, ret);
		return ret;
	}
	
	public void unloadPlayer(UUID uuid) {
		getListPartyPlayers().remove(uuid);
	}
	
	public PartyPlayerImpl getPlayer(UUID uuid) {
		PartyPlayerImpl ret;
		if (getListPartyPlayers().containsKey(uuid)) {
			// Get player from online list
			ret = getListPartyPlayers().get(uuid);
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_GET_LIST
					.replace("{player}", ret.getName())
					.replace("{party}", ret.getPartyName()), true);
		} else {
			// Get player from database
			ret = plugin.getDatabaseManager().getPlayer(uuid).join();
			if (ret != null) {
				// Compare name
				User user = plugin.getPlayer(ret.getPlayerUUID());
				if (user != null) {
					ret.compareName(user.getName());
				}
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_GET_DATABASE
						.replace("{player}", ret.getName())
						.replace("{party}", ret.getPartyName()), true);
			}
			
			// Load new player
			if (ret == null)
				ret = initializePlayer(uuid);
		}
		return ret;
	}
}
