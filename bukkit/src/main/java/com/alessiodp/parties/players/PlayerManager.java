package com.alessiodp.parties.players;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.partiesapi.objects.PartyPlayer;

import lombok.Getter;

public class PlayerManager {
	private Parties plugin;
	
	@Getter private HashMap<UUID, PartyPlayerEntity> listPartyPlayers;
	@Getter private HashSet<UUID> listPartyPlayersToDelete;
	
	@Getter private int homeCounts;
	@Getter private HashMap<UUID, Long> chatCooldown;
	@Getter private HashMap<UUID, Long> teleportCooldown;
	
	public PlayerManager(Parties instance) {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		plugin = instance;
	}
	
	public void reload() {
		listPartyPlayers = new HashMap<UUID, PartyPlayerEntity>();
		listPartyPlayersToDelete = new HashSet<UUID>();
		
		homeCounts = 0;
		chatCooldown = new HashMap<UUID, Long>();
		teleportCooldown = new HashMap<UUID, Long>();
		
		plugin.getSpyManager().refreshSpyList();
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			PartyPlayerEntity pp = loadPlayer(p.getUniqueId());
			
			PartyEntity party = plugin.getPartyManager().loadParty(pp.getPartyName());
			if (party != null)
				party.getOnlinePlayers().add(p);
		}
	}
	
	
	public PartyPlayerEntity loadPlayer(UUID uuid) {
		PartyPlayerEntity ret = getPlayer(uuid);
		getListPartyPlayers().put(uuid, ret);
		return ret;
	}
	
	public void unloadPlayer(UUID uuid) {
		getListPartyPlayers().remove(uuid);
	}
	
	public PartyPlayerEntity getPlayer(UUID uuid) {
		PartyPlayerEntity ret = null;
		if (getListPartyPlayers().containsKey(uuid)) {
			ret = getListPartyPlayers().get(uuid);
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_GET_LIST
					.replace("{player}", ret.getName())
					.replace("{party}", ret.getPartyName()), true);
		} else {
			if (ret == null) {
				PartyPlayer pp = plugin.getDatabaseManager().getPlayer(uuid).join();
				if (pp != null) {
					ret = new PartyPlayerEntity(pp, plugin);
					
					// Compare name
					Player pl = ret.getPlayer();
					if (pl != null) {
						ret.compareName(pl.getName());
					}
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_GET_DATABASE
							.replace("{player}", ret.getName())
							.replace("{party}", ret.getPartyName()), true);
				}
			}
			if (ret == null)
				ret = new PartyPlayerEntity(uuid, plugin);
		}
		return ret;
	}
	
	
	/*
	 * PlaceholderAPI, Vault & Tab placeholders handler
	 */
	public String setTabText(String text, PartyPlayerEntity player) {
		text = text
				.replace("%player%", player.getName());
		text = PlaceholderAPIHandler.getPlaceholders(player.getPlayer(), text);
		return text;
	}
	
	
	/*
	 * Home Based
	 */
	public void addHomeCount() {
		homeCounts++;
	}
	public void remHomeCount() {
		homeCounts--;
	}
}
