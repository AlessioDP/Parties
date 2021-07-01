package com.alessiodp.parties.common.parties;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.UUID;

public abstract class PartyManager {
	protected final PartiesPlugin plugin;
	
	@Getter private final HashMap<UUID, PartyImpl> cacheParties;
	private final HashMap<String, UUID> cachePartiesNames;
	@Getter private final HashMap<UUID, CancellableTask> cacheMembersTimedOut;
	
	protected PartyManager(PartiesPlugin instance) {
		plugin = instance;
		cacheParties = new HashMap<>();
		cachePartiesNames = new HashMap<>();
		cacheMembersTimedOut = new HashMap<>();
	}
	
	public PartyImpl initializeParty() {
		return initializeParty(UUID.randomUUID());
	}
	
	public abstract PartyImpl initializeParty(UUID id);
	
	public void reload() {
		cacheParties.clear();
		cachePartiesNames.clear();
		cacheMembersTimedOut.clear();
		
		if (ConfigParties.ADDITIONAL_FIXED_ENABLE) {
			plugin.getDatabaseManager().getListFixed().forEach(this::addPartyToCache);
		}
	}
	
	protected PartyImpl getPartyFromCache(UUID id) {
		return id != null ? cacheParties.get(id): null;
	}
	
	protected PartyImpl getPartyFromCache(String name) {
		return name != null ? getPartyFromCache(cachePartiesNames.get(CommonUtils.toLowerCase(name))) : null;
	}
	
	public void addPartyToCache(PartyImpl party) {
		if (party != null) {
			cacheParties.put(party.getId(), party);
			if (party.getName() != null)
				cachePartiesNames.put(CommonUtils.toLowerCase(party.getName()), party.getId());
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
				cachePartiesNames.remove(CommonUtils.toLowerCase(party.getName()));
		}
	}
	
	public PartyImpl loadParty(String name) {
		// Get the party and save it into the party list
		PartyImpl ret = getParty(name);
		addPartyToCache(ret);
		return ret;
	}
	
	public PartyImpl loadParty(UUID id) {
		return loadParty(id, false);
	}
	
	public PartyImpl loadParty(UUID id, boolean syncServers) {
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
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_RELOADED, party.getName() != null ? party.getName() : "_"), true);
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
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_DATABASE, ret.getName() != null ? ret.getName() : "_"), true);
				}
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_LIST, ret.getName() != null ? ret.getName() : "_"), true);
		}
		
		if (ret != null) {
			ret.refresh();
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
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_DATABASE, ret.getName() != null ? ret.getName() : "_"), true);
				}
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_GET_LIST, ret.getName() != null ? ret.getName() : "_"), true);
		}
		
		if (ret != null) {
			ret.refresh();
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
	
	
	public void removePlayerTimedOut(PartyPlayerImpl player, PartyImpl party) {
		boolean mustDelete = false;
		
		// Calling Pre API event
		IPlayerPreLeaveEvent partiesPreLeaveEvent = plugin.getEventManager().preparePlayerPreLeaveEvent(player, party, LeaveCause.TIMEOUT, null);
		plugin.getEventManager().callEvent(partiesPreLeaveEvent);
		
		if (!partiesPreLeaveEvent.isCancelled()) {
			
			if (party.getOnlineMembers(true).size() == 0) {
				mustDelete = true;
			} else if (party.getLeader() != null && party.getLeader().equals(player.getPlayerUUID())) {
				mustDelete = true;
				// Leader
				if (ConfigParties.GENERAL_MEMBERS_ON_LEAVE_CHANGE_LEADER) {
					// Change leader
					PartyPlayerImpl newLeader = party.findNewLeader();
					
					if (newLeader != null) {
						mustDelete = false;
						party.changeLeader(newLeader);
						
						party.broadcastMessage(Messages.MAINCMD_KICK_BROADCAST_LEADER_CHANGED, newLeader);
						
						plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_TIMEOUT_CHANGE_LEADER, party.getId().toString(), newLeader.getPlayerUUID().toString()), true);
					}
				}
			}
			
			if (party.getOnlineMembers(true).size() == 0) {
				mustDelete = true;
			}
			
			if (party.isFixed()) {
				mustDelete = false;
			}
			
			if (mustDelete) {
				// Calling Pre API event
				IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.TIMEOUT, player, null);
				plugin.getEventManager().callEvent(partiesPreDeleteEvent);
				
				if (!partiesPreDeleteEvent.isCancelled()) {
					// Delete party
					party.delete(DeleteCause.TIMEOUT, player, player);
					
					// Calling Post API Delete event
					IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party, DeleteCause.TIMEOUT, player, null);
					plugin.getScheduler().runAsync(() -> plugin.getEventManager().callEvent(partiesPostDeleteEvent));
					
					// Calling Post API Leave event
					IPlayerPostLeaveEvent partiesPostLeaveEvent = plugin.getEventManager().preparePlayerPostLeaveEvent(player, party, LeaveCause.TIMEOUT, null);
					plugin.getScheduler().runAsync(() -> plugin.getEventManager().callEvent(partiesPostLeaveEvent));
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_TIMEOUT_DELETE, party.getId().toString(), player.getPlayerUUID().toString()), true);
				} else {
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_DELETEEVENT_DENY_GENERIC, party.getId().toString()), true);
				}
			} else {
				// Kick member
				party.removeMember(player, LeaveCause.TIMEOUT, player);
				
				// Calling Post API Leave event
				IPlayerPostLeaveEvent partiesPostLeaveEvent = plugin.getEventManager().preparePlayerPostLeaveEvent(player, party, LeaveCause.TIMEOUT, null);
				plugin.getScheduler().runAsync(() -> plugin.getEventManager().callEvent(partiesPostLeaveEvent));
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_TIMEOUT_KICK, player.getPlayerUUID().toString(), party.getId().toString()), true);
			}
		} else {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_LEAVEEVENT_DENY, player.getPlayerUUID().toString(), party.getId().toString()), true);
		}
	}
	
	public void disbandLoadedParties() {
		if (ConfigParties.GENERAL_MEMBERS_DISBAND_PARTIES_ON_DISABLE) {
			// Disband all loaded parties
			HashSet<PartyImpl> cachedParties = new HashSet<>(cacheParties.values());
			cachedParties.forEach(party -> {
				party.delete(DeleteCause.TIMEOUT, null, null);
			});
			cacheParties.clear();
			
			// Remove all timed out caches
			for (Entry<UUID, CancellableTask> e : cacheMembersTimedOut.entrySet()) {
				e.getValue().cancel();
			}
			cacheMembersTimedOut.clear();
		}
	}
}
