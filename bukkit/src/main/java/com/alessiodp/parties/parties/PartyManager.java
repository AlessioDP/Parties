package com.alessiodp.parties.parties;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

import lombok.Getter;


public class PartyManager {
	private Parties plugin;
	
	@Getter private HashMap<String, PartyEntity> listParties;
	@Getter private HashMap<String, Integer> listPartiesToDelete;
	
	public PartyManager(Parties instance) {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		plugin = instance;
	}
	
	public void reload() {
		listParties = new HashMap<String, PartyEntity>();
		listPartiesToDelete = new HashMap<String, Integer>();
		
		if (ConfigParties.FIXED_ENABLE) {
			List<Party> lst = plugin.getDatabaseManager().getAllFixed().join();
			for (Party party : lst) {
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_FIXED_LOAD
						.replace("{party}", party.getName()), true);
				loadParty(party.getName());
			}
		}
	}
	
	
	public PartyEntity loadParty(String name) {
		// Get the party and save it into the party list
		PartyEntity ret = getParty(name);
		if (ret != null)
			getListParties().put(name.toLowerCase(), ret);
		return ret;
	}
	public void unloadParty(String name) {
		getListParties().remove(name.toLowerCase());
	}
	
	public PartyEntity getParty(String name) {
		// Just get the party without save it into the party list
		PartyEntity ret = null;
		if (name != null && !name.isEmpty()) {
			ret = getListParties().get(name.toLowerCase());
			if (ret == null) {
				Party p = plugin.getDatabaseManager().getParty(name).join();
				if (p != null) {
					ret = new PartyEntity(p, plugin);
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
		PartyEntity party = (PartyEntity) getListParties().get(name.toLowerCase());
		if (party != null || plugin.getDatabaseManager().existParty(name).join())
			ret = true;
		return ret;
	}
	
	
	public void deleteTimedParty(String name, boolean leaderLeft) {
		PartyEntity party = getParty(name);
		if (party != null) {
			// Calling Pre API event
			PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party, PartiesPartyPreDeleteEvent.DeleteCause.TIMEOUT, null, null);
			Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
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
				PartyPlayer tempPlayer = new PartyPlayer(UUID.fromString(Constants.FIXED_VALUE_UUID), 0); // Used to avoid ambiguity with PartiesPartyPostDeleteEvent constructors
				PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.TIMEOUT, null, tempPlayer);
				Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_DELETE_CAUSE
						.replace("{party}", party.getName())
						.replace("{cause}", cause), true);
				
				if (getListPartiesToDelete().containsKey(name.toLowerCase()))
					getListPartiesToDelete().remove(name.toLowerCase());
			} else {
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY_GENERIC
						.replace("{party}", party.getName()), true);
			}
		}
	}
	
	public void resetPendingPartyTask() {
		for (BukkitTask bt : Bukkit.getScheduler().getPendingTasks()) {
			if (bt.getOwner() instanceof Parties) {
				if (getListPartiesToDelete().containsValue(bt.getTaskId())) {
					for (Entry<String, Integer> et : getListPartiesToDelete().entrySet()) {
						if (et.getValue() == bt.getTaskId()) {
							bt.cancel();
							deleteTimedParty(et.getKey(), true);
							break;
						}
					}
				}
			}
		}
	}
}
