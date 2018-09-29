package com.alessiodp.parties.common.parties;

import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.api.enums.DeleteCause;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;


public abstract class PartyManager {
	protected PartiesPlugin plugin;
	
	@Getter private HashMap<String, PartyImpl> listParties;
	@Getter private HashMap<String, Integer> listPartiesToDelete;
	
	// Checks for database saving
	@Getter protected boolean bukkit_killSystem;
	@Getter protected boolean bukkit_expSystem;
	
	protected PartyManager(PartiesPlugin instance) {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		plugin = instance;
	}
	
	public abstract PartyImpl initializeParty(String partyName);
	
	public void reload() {
		listParties = new HashMap<>();
		listPartiesToDelete = new HashMap<>();
		
		if (ConfigParties.FIXED_ENABLE) {
			List<PartyImpl> lst = plugin.getDatabaseManager().getAllFixed().join();
			for (PartyImpl party : lst) {
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_FIXED_LOAD
						.replace("{party}", party.getName()), true);
				loadParty(party.getName());
			}
		}
	}
	
	
	public PartyImpl loadParty(String name) {
		// Get the party and save it into the party list
		PartyImpl ret = getParty(name);
		if (ret != null)
			getListParties().put(name.toLowerCase(), ret);
		return ret;
	}
	public void unloadParty(String name) {
		getListParties().remove(name.toLowerCase());
	}
	
	public void reloadParty(String name) {
		if (getListParties().containsKey(name)) {
			PartyImpl party = plugin.getDatabaseManager().getParty(name).join();
			getListParties().put(name, party);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_RELOADED, true);
		}
	}
	
	public PartyImpl getParty(String name) {
		// Just get the party without save it into the party list
		PartyImpl ret = null;
		if (name != null && !name.isEmpty()) {
			ret = getListParties().get(name.toLowerCase());
			if (ret == null) {
				ret = plugin.getDatabaseManager().getParty(name).join();
				if (ret != null) {
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_GET_DATABASE
							.replace("{party}", ret.getName()), true);
				}
			} else
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_GET_LIST
						.replace("{party}", ret.getName()), true);
		}
		
		if (ret != null) {
			ret.refreshPlayers();
		}
		return ret;
	}
	
	public boolean existParty(String name) {
		boolean ret = false;
		PartyImpl party = getListParties().get(name.toLowerCase());
		if (party != null || plugin.getDatabaseManager().existParty(name).join())
			ret = true;
		return ret;
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
					party.sendBroadcast(plugin.getPlayerManager().getPlayer(party.getLeader()), Messages.MAINCMD_LEAVE_DISBANDED);
					cause = "leader left";
				}
				
				party.removeParty();
				// Calling Post API event
				IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.TIMEOUT, null, null);
				plugin.getEventManager().callEvent(partiesPostDeleteEvent);
				
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_DELETE_CAUSE
						.replace("{party}", party.getName())
						.replace("{cause}", cause), true);
				
				getListPartiesToDelete().remove(name.toLowerCase());
			} else {
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY_GENERIC
						.replace("{party}", party.getName()), true);
			}
		}
	}
	
	public void resetPendingPartyTask() {
		for (int taskId : plugin.getPartiesScheduler().getCurrentTasks()) {
			if (getListPartiesToDelete().containsValue(taskId)) {
				for (Entry<String, Integer> et : getListPartiesToDelete().entrySet()) {
					if (et.getValue() == taskId) {
						plugin.getPartiesScheduler().cancelTask(taskId);
						deleteTimedParty(et.getKey(), true);
						break;
					}
				}
			}
		}
	}
}
