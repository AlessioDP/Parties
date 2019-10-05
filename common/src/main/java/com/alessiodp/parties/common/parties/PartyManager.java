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
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

public abstract class PartyManager {
	protected final PartiesPlugin plugin;
	
	@Getter private HashMap<String, PartyImpl> listParties;
	@Getter private HashMap<String, CancellableTask> listPartiesToDelete;
	
	// Checks for database saving
	@Getter protected boolean bukkit_killSystem;
	@Getter protected boolean bukkit_expSystem;
	
	protected PartyManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public abstract PartyImpl initializeParty(String partyName);
	
	public void reload() {
		listParties = new HashMap<>();
		listPartiesToDelete = new HashMap<>();
		
		if (ConfigParties.FIXED_ENABLE) {
			List<PartyImpl> lst = plugin.getDatabaseManager().getAllFixed();
			for (PartyImpl party : lst) {
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_FIXED_LOAD
						.replace("{party}", party.getName()), true);
				loadParty(party.getName());
			}
		}
	}
	
	
	public PartyImpl loadParty(String name) {
		// Get the party and save it into the party list
		PartyImpl ret = getParty(name);
		if (ret != null)
			getListParties().put(name.toLowerCase(Locale.ENGLISH), ret);
		return ret;
	}
	public void unloadParty(String name) {
		getListParties().remove(name.toLowerCase(Locale.ENGLISH));
	}
	
	public boolean reloadParty(String name) {
		if (getListParties().containsKey(name)) {
			PartyImpl party = plugin.getDatabaseManager().getParty(name);
			getListParties().put(name, party);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_RELOADED, true);
			return true;
		}
		return false;
	}
	
	public PartyImpl getParty(String name) {
		// Just get the party without save it into the party list
		PartyImpl ret = null;
		if (name != null && !name.isEmpty()) {
			ret = getListParties().get(name.toLowerCase(Locale.ENGLISH));
			if (ret == null) {
				ret = plugin.getDatabaseManager().getParty(name);
				if (ret != null) {
					plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_GET_DATABASE
							.replace("{party}", ret.getName()), true);
				}
			} else
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_GET_LIST
						.replace("{party}", ret.getName()), true);
		}
		
		if (ret != null) {
			ret.refreshOnlineMembers();
		}
		return ret;
	}
	
	public boolean existParty(String name) {
		boolean ret = false;
		PartyImpl party = getListParties().get(name.toLowerCase(Locale.ENGLISH));
		if (party != null || plugin.getDatabaseManager().existsParty(name))
			ret = true;
		return ret;
	}
	
	public PartyImpl getPartyOfPlayer(PartyPlayerImpl player) {
		return player.getPartyName().isEmpty() ? null : getParty(player.getPartyName());
	}
	
	public void deleteTimedParty(String name, boolean leaderLeft) {
		PartyImpl party = getParty(name);
		if (party != null) {
			// Calling Pre API event
			IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.TIMEOUT, null, null);
			plugin.getEventManager().callEvent(partiesPreDeleteEvent);
			
			if (!partiesPreDeleteEvent.isCancelled()) {
				for (UUID u : plugin.getPlayerManager().getListPartyPlayersToDelete()) {
					if (party.getMembers().contains(u))
						plugin.getPlayerManager().getListPartyPlayers().remove(u);
				}
				String cause = "empty";
				if (leaderLeft) {
					party.broadcastMessage(Messages.MAINCMD_LEAVE_DISBANDED, plugin.getPlayerManager().getPlayer(party.getLeader()));
					cause = "leader left";
				}
				
				party.delete();
				// Calling Post API event
				IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.TIMEOUT, null, null);
				plugin.getEventManager().callEvent(partiesPostDeleteEvent);
				
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_DELETE_CAUSE
						.replace("{party}", party.getName())
						.replace("{cause}", cause), true);
				
				getListPartiesToDelete().remove(name.toLowerCase(Locale.ENGLISH));
			} else {
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_DELETEEVENT_DENY_GENERIC
						.replace("{party}", party.getName()), true);
			}
		}
	}
	
	public void resetPendingPartyTask() {
		for (Entry<String, CancellableTask> e : getListPartiesToDelete().entrySet()) {
			e.getValue().cancel();
			deleteTimedParty(e.getKey(), true);
		}
	}
}
