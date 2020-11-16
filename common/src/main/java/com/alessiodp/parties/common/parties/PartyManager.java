package com.alessiodp.parties.common.parties;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

public abstract class PartyManager {
	protected final PartiesPlugin plugin;
	
	@Getter private final HashMap<UUID, PartyImpl> cacheParties;
	private final HashMap<String, UUID> cachePartiesNames;
	@Getter private final HashMap<UUID, CancellableTask> cachePartiesToDelete;
	
	// Checks for database saving
	@Getter protected boolean bukkit_killSystem;
	@Getter protected boolean bukkit_expSystem;
	
	protected PartyManager(PartiesPlugin instance) {
		plugin = instance;
		cacheParties = new HashMap<>();
		cachePartiesNames = new HashMap<>();
		cachePartiesToDelete = new HashMap<>();
	}
	
	public PartyImpl initializeParty() {
		return initializeParty(UUID.randomUUID());
	}
	
	public abstract PartyImpl initializeParty(UUID id);
	
	public void reload() {
		cacheParties.clear();
		cachePartiesNames.clear();
		cachePartiesToDelete.clear();
		
		if (ConfigParties.ADDITIONAL_FIXED_ENABLE) {
			plugin.getDatabaseManager().getListFixed().forEach(this::addPartyToCache);
		}
	}
	
	protected PartyImpl getPartyFromCache(UUID id) {
		return id != null ? cacheParties.get(id): null;
	}
	
	protected PartyImpl getPartyFromCache(String name) {
		return name != null ? getPartyFromCache(cachePartiesNames.get(name.toLowerCase(Locale.ENGLISH))) : null;
	}
	
	public void addPartyToCache(PartyImpl party) {
		if (party != null) {
			cacheParties.put(party.getId(), party);
			if (party.getName() != null)
				cachePartiesNames.put(party.getName().toLowerCase(Locale.ENGLISH), party.getId());
		}
	}
	
	public void removePartyFromCache(PartyImpl party) {
		if (party != null) {
			removePartyFromCache(party.getId());
		}
	}
	
	public void removePartyFromCache(UUID id) {
		if (id != null) {
			PartyImpl party = cacheParties.remove(id);
			if (party != null && party.getName() != null)
				cachePartiesNames.remove(party.getName().toLowerCase(Locale.ENGLISH));
		}
	}
	
	public PartyImpl loadParty(String name) {
		// Get the party and save it into the party list
		PartyImpl ret = getParty(name);
		addPartyToCache(ret);
		return ret;
	}
	
	public PartyImpl loadParty(UUID id) {
		// Get the party and save it into the party list
		PartyImpl ret = getParty(id);
		addPartyToCache(ret);
		return ret;
	}
	
	public void unloadParty(PartyImpl party) {
		removePartyFromCache(party);
	}
	
	public boolean reloadParty(UUID id) {
		PartyImpl party = getPartyFromCache(id);
		if (party != null) {
			removePartyFromCache(party);
			party =  plugin.getDatabaseManager().getParty(id);
			addPartyToCache(party);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_RELOADED, party.getName()), true);
			return true;
		}
		return false;
	}
	
	public PartyImpl getParty(UUID id) {
		PartyImpl ret = null;
		if (id != null) {
			// Load from cache
			ret = getPartyFromCache(id);
			if (ret == null) {
				// Load from db
				ret = plugin.getDatabaseManager().getParty(id);
				if (ret != null) {
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_DATABASE, ret.getName()), true);
				}
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_LIST, ret.getName()), true);
		}
		
		if (ret != null) {
			ret.refreshOnlineMembers();
		}
		return ret;
	}
	
	public PartyImpl getParty(String name) {
		PartyImpl ret = null;
		if (name != null && !name.isEmpty()) {
			// Load from cache
			ret = getPartyFromCache(name);
			if (ret == null) {
				// Load from db
				ret = plugin.getDatabaseManager().getPartyByName(name);
				if (ret != null) {
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_DATABASE, ret.getName()), true);
				}
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_LIST, ret.getName()), true);
		}
		
		if (ret != null) {
			ret.refreshOnlineMembers();
		}
		return ret;
	}
	
	public boolean existsParty(String name) {
		PartyImpl party = getPartyFromCache(name);
		return party != null || plugin.getDatabaseManager().existsParty(name);
	}
	
	public boolean existsTag(String tag) {
		return getCacheParties().values().stream().anyMatch(p -> tag.equalsIgnoreCase(p.getTag()))
				|| plugin.getDatabaseManager().existsTag(tag);
	}
	
	public PartyImpl getPartyOfPlayer(PartyPlayerImpl player) {
		return getParty(player.getPartyId());
	}
	
	public void deleteTimedParty(UUID id, boolean leaderLeft) {
		PartyImpl party = getParty(id);
		if (party != null) {
			// Calling Pre API event
			IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.TIMEOUT, null, null);
			plugin.getEventManager().callEvent(partiesPreDeleteEvent);
			
			if (!partiesPreDeleteEvent.isCancelled()) {
				plugin.getPlayerManager().getCachePlayersToDelete().removeIf(u -> party.getMembers().contains(u));
				String cause = "empty";
				if (leaderLeft) {
					party.broadcastMessage(Messages.MAINCMD_LEAVE_DISBANDED, plugin.getPlayerManager().getPlayer(party.getLeader()));
					cause = "leader left";
				}
				
				party.delete();
				// Calling Post API event
				IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.TIMEOUT, null, null);
				plugin.getEventManager().callEvent(partiesPostDeleteEvent);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_DELETE_CAUSE, party.getName(), cause), true);
				
				cachePartiesToDelete.remove(id);
			} else {
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_DELETEEVENT_DENY_GENERIC, party.getName()), true);
			}
		}
	}
	
	public void resetPendingPartyTask() {
		for (Entry<UUID, CancellableTask> e : cachePartiesToDelete.entrySet()) {
			e.getValue().cancel();
			deleteTimedParty(e.getKey(), true);
		}
	}
}
