package com.alessiodp.parties.common.storage;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.DatabaseManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.dispatchers.PartiesFileDispatcher;
import com.alessiodp.parties.common.storage.dispatchers.PartiesNoneDispatcher;
import com.alessiodp.parties.common.storage.dispatchers.PartiesSQLDispatcher;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabase;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class PartiesDatabaseManager extends DatabaseManager implements IPartiesDatabase {
	public PartiesDatabaseManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	public IDatabaseDispatcher initializeDispatcher(StorageType storageType) {
		IDatabaseDispatcher ret = null;
		switch (storageType) {
			case NONE:
				ret = new PartiesNoneDispatcher((PartiesPlugin) plugin);
				break;
			case YAML:
				ret = new PartiesFileDispatcher(plugin, storageType);
				break;
			case MYSQL:
			case SQLITE:
			case H2:
				ret = new PartiesSQLDispatcher(plugin, storageType);
				break;
			default:
				// Unsupported storage type
				plugin.getLoggerManager().printError(Constants.DEBUG_DB_INIT_FAILED_UNSUPPORTED
						.replace("{type}", ConfigMain.STORAGE_TYPE_DATABASE));
				break;
		}
		return ret;
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_UPDATEPLAYER, player.getName(), player.getPlayerUUID().toString()), true);
			
			((IPartiesDatabase) database).updatePlayer(player);
		});
	}
	
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_GETPLAYER, uuid.toString()), true);
			
			return ((IPartiesDatabase) database).getPlayer(uuid);
		}).join();
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_UPDATEPARTY, CommonUtils.getNoEmptyOr(party.getName(), "_"), party.getId()), true);
			
			((IPartiesDatabase) database).updateParty(party);
		});
	}
	
	@Override
	public PartyImpl getParty(UUID id) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_GETPARTY, id.toString()), true);
			
			return ((IPartiesDatabase) database).getParty(id);
		}).join();
	}
	
	@Override
	public PartyImpl getPartyByName(String name) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_GETPARTY, name), true);
			
			return ((IPartiesDatabase) database).getPartyByName(name);
		}).join();
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		plugin.getScheduler().runAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_REMOVEPARTY, CommonUtils.getNoEmptyOr(party.getName(), "_"), party.getId()), true);
			
			((IPartiesDatabase) database).removeParty(party);
		});
	}
	
	@Override
	public boolean existsParty(String name) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_EXISTSPARTY, name), true);
			
			return ((IPartiesDatabase) database).existsParty(name);
		}).join();
	}
	
	@Override
	public boolean existsTag(String tag) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_DB_EXISTSTAG, tag), true);
			
			return ((IPartiesDatabase) database).existsTag(tag);
		}).join();
	}
	
	@Override
	public LinkedHashSet<PartyImpl> getListParties(ListOrder order, int limit, int offset) {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLPARTIES, true);
			
			return ((IPartiesDatabase) database).getListParties(order, limit, offset);
		}).join();
	}
	
	@Override
	public int getListPartiesNumber() {
		return plugin.getScheduler().runSupplyAsync(() -> {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_DB_GETALLPARTIES_NUMBER, true);
			
			return ((IPartiesDatabase) database).getListPartiesNumber();
		}).join();
	}
	
	@Override
	public Set<PartyImpl> getListFixed() {
		return plugin.getScheduler().runSupplyAsync(() -> {
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
