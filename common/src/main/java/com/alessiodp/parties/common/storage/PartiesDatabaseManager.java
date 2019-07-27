package com.alessiodp.parties.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.DatabaseManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.NoneDispatcher;
import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.dispatchers.PartiesFileDispatcher;
import com.alessiodp.parties.common.storage.dispatchers.PartiesSQLDispatcher;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabaseDispatcher;

import java.util.List;
import java.util.UUID;

public class PartiesDatabaseManager extends DatabaseManager {
	public PartiesDatabaseManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	public IDatabaseDispatcher initializeDispatcher(StorageType storageType) {
		IDatabaseDispatcher ret = null;
		switch (storageType) {
			case NONE:
				ret = new NoneDispatcher();
				break;
			case YAML:
				ret = new PartiesFileDispatcher(plugin);
				break;
			case MYSQL:
			case SQLITE:
				ret = new PartiesSQLDispatcher(plugin);
				break;
			default:
				// Unsupported storage type
				plugin.getLoggerManager().printError(Constants.DEBUG_DB_INIT_FAILED_UNSUPPORTED
						.replace("{type}", ConfigMain.STORAGE_TYPE_DATABASE));
		}
		
		if (ret != null) {
			ret.init(storageType);
			if (ret.isFailed())
				return null;
		}
		return ret;
	}
	
	public void updatePlayer(PartyPlayerImpl player) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_UPDATEPLAYER
					.replace("{player}", player.getName())
					.replace("{uuid}", player.getPlayerUUID().toString()), true);
			
			((IPartiesDatabaseDispatcher) database).updatePlayer(player);
		}).join();
	}
	
	public PartyPlayerImpl getPlayer(UUID uuid) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETPLAYER
					.replace("{uuid}", uuid.toString()), true);
			
			return ((IPartiesDatabaseDispatcher) database).getPlayer(uuid);
		}).join();
	}
	
	public void updateParty(PartyImpl party) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_UPDATEPARTY
					.replace("{party}", party.getName()), true);
			
			((IPartiesDatabaseDispatcher) database).updateParty(party);
		}).join();
	}
	
	public PartyImpl getParty(String party) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETPARTY
					.replace("{party}", party), true);
			
			return ((IPartiesDatabaseDispatcher) database).getParty(party);
		}).join();
	}
	
	public void renameParty(String prev, String next) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_RENAMEPARTY
					.replace("{party}", prev)
					.replace("{name}", next), true);
			
			((IPartiesDatabaseDispatcher) database).renameParty(prev, next);
		}).join();
	}
	
	public void removeParty(PartyImpl party) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_REMOVEPARTY
					.replace("{party}", party.getName()), true);
			
			((IPartiesDatabaseDispatcher) database).removeParty(party);
		}).join();
	}
	
	public boolean existsParty(String party) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_EXISTSPARTY
					.replace("{party}", party), true);
			
			return ((IPartiesDatabaseDispatcher) database).existParty(party);
		}).join();
	}
	
	public List<PartyImpl> getAllFixed() {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLFIXEDPARTIES, true);
			
			return ((IPartiesDatabaseDispatcher) database).getAllFixed();
		}).join();
	}
	
	public List<PartyImpl> getAllParties() {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLPARTIES, true);
			
			return ((IPartiesDatabaseDispatcher) database).getAllParties();
		}).join();
	}
	
	public List<PartyPlayerImpl> getAllPlayers() {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLPLAYERS, true);
			
			return ((IPartiesDatabaseDispatcher) database).getAllPlayers();
		}).join();
	}
}
