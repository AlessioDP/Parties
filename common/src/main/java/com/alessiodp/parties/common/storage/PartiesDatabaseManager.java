package com.alessiodp.parties.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.DatabaseManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.dispatchers.PartiesYAMLDispatcher;
import com.alessiodp.parties.common.storage.dispatchers.PartiesSQLDispatcher;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabase;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PartiesDatabaseManager extends DatabaseManager {
	public PartiesDatabaseManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	public IDatabaseDispatcher initializeDispatcher(StorageType storageType) {
		IDatabaseDispatcher ret = null;
		switch (storageType) {
			case YAML:
				ret = new PartiesYAMLDispatcher(plugin);
				break;
			case MARIADB:
			case MYSQL:
			case POSTGRESQL:
			case SQLITE:
			case H2:
				ret = new PartiesSQLDispatcher(plugin, storageType);
				break;
			default:
				// Unsupported storage type
				plugin.getLoggerManager().printError(String.format(Constants.DEBUG_DB_INIT_FAILED_UNSUPPORTED, ConfigMain.STORAGE_TYPE_DATABASE));
				break;
		}
		return ret;
	}
	
	public CompletableFuture<Void> updatePlayer(PartyPlayerImpl player) {
		return executeSafelyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_UPDATEPLAYER, player.getName(), player.getPlayerUUID().toString()), true);
			
			((IPartiesDatabase) database).updatePlayer(player);
		});
	}
	
	public PartyPlayerImpl getPlayer(UUID uuid) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_GETPLAYER, uuid.toString()), true);
			
			return ((IPartiesDatabase) database).getPlayer(uuid);
		}).join();
	}
	
	public int getListPlayersInPartyNumber() {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLPLAYERS_NUMBER, true);
			
			return ((IPartiesDatabase) database).getListPlayersInPartyNumber();
		}).join();
	}
	
	public CompletableFuture<Void> updateParty(PartyImpl party) {
		return executeSafelyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_UPDATEPARTY, CommonUtils.getNoEmptyOr(party.getName(), "_"), party.getId()), true);
			
			((IPartiesDatabase) database).updateParty(party);
		});
	}
	
	public PartyImpl getParty(UUID id) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_GETPARTY, id.toString()), true);
			
			return ((IPartiesDatabase) database).getParty(id);
		}).join();
	}
	
	public PartyImpl getPartyByName(String name) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_GETPARTY, name), true);
			
			return ((IPartiesDatabase) database).getPartyByName(name);
		}).join();
	}
	
	public CompletableFuture<Void> removeParty(PartyImpl party) {
		return executeSafelyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_REMOVEPARTY, CommonUtils.getNoEmptyOr(party.getName(), "_"), party.getId()), true);
			
			((IPartiesDatabase) database).removeParty(party);
		});
	}
	
	public boolean existsParty(String name) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_EXISTSPARTY, name), true);
			
			return ((IPartiesDatabase) database).existsParty(name);
		}).join();
	}
	
	public boolean existsTag(String tag) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_EXISTSTAG, tag), true);
			
			return ((IPartiesDatabase) database).existsTag(tag);
		}).join();
	}
	
	public LinkedHashSet<PartyImpl> getListParties(ListOrder order, int limit, int offset) {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLPARTIES, true);
			
			return ((IPartiesDatabase) database).getListParties(order, limit, offset);
		}).join();
	}
	
	public int getListPartiesNumber() {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLPARTIES_NUMBER, true);
			
			return ((IPartiesDatabase) database).getListPartiesNumber();
		}).join();
	}
	
	public Set<PartyImpl> getListFixed() {
		return executeSafelySupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLFIXEDPARTIES, true);
			
			return ((IPartiesDatabase) database).getListFixed();
		}).join();
	}
	
	public enum ListOrder {
		NAME, ONLINE_MEMBERS, MEMBERS, KILLS, EXPERIENCE;
		
		public static ListOrder getType(String type) {
			try {
				return valueOf(CommonUtils.toUpperCase(type));
			} catch (IllegalArgumentException ignored) {}
			return null;
		}
		
		public static ListOrder parse(String type) {
			if (type.equalsIgnoreCase(Messages.PARTIES_SYNTAX_NAME))
				return NAME;
			else if (type.equalsIgnoreCase(Messages.PARTIES_SYNTAX_ONLINE_MEMBERS))
				return ONLINE_MEMBERS;
			else if (type.equalsIgnoreCase(Messages.PARTIES_SYNTAX_MEMBERS))
				return MEMBERS;
			else if (type.equalsIgnoreCase(Messages.PARTIES_SYNTAX_KILLS))
				return KILLS;
			else if (type.equalsIgnoreCase(Messages.PARTIES_SYNTAX_EXPERIENCE))
				return EXPERIENCE;
			return null;
		}
	}
}
