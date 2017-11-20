package com.alessiodp.parties.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlayerHandler {
	private Parties plugin;
	private HashMap<UUID, ThePlayer> listThePlayer;
	private List<UUID> listThePlayerToDelete;
	private List<UUID> listSpyPlayers;
	private int homeCounts;
	private HashMap<UUID, Long> chatCooldown;
	private HashMap<UUID, Long> teleportCooldown;
	
	public PlayerHandler(Parties instance) {
		plugin = instance;
		listThePlayer = new HashMap<UUID, ThePlayer>();
		listThePlayerToDelete = new ArrayList<UUID>();
		listSpyPlayers = new ArrayList<UUID>();
		homeCounts = 0;
		chatCooldown = new HashMap<UUID, Long>();
		teleportCooldown = new HashMap<UUID, Long>();
	}
	
	public void init() {
		LogHandler.log(LogLevel.DEBUG, "Initializing PlayerHandler", true);
		reloadPlayers();
	}
	
	public void reloadPlayers() {
		LogHandler.log(LogLevel.DEBUG, "Reloading player list", true);
		listThePlayer = new HashMap<UUID, ThePlayer>();
		listThePlayerToDelete = new ArrayList<UUID>();
		initSpies();
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			ThePlayer tp = loadPlayer(p.getUniqueId());
			
			Party party = tp.getPartyName().isEmpty() ? null :plugin.getPartyHandler().loadParty(tp.getPartyName());
			if (party != null)
				party.addOnlinePlayer(p);
			
			if (p.hasPermission(PartiesPermissions.ADMIN_UPDATES.toString()) && Variables.warnupdates)
				if (plugin.isUpdateAvailable())
					tp.sendMessage(Messages.updateavailable.replace("%version%", plugin.getNewUpdate()).replace("%thisversion%", plugin.getDescription().getVersion()));
		}
		LogHandler.log(LogLevel.MEDIUM, "Loaded " + Integer.toString(listThePlayer.size()) + " players and " + Integer.toString(plugin.getPartyHandler().getListParties().size()) + " parties", true);
	}
	public void alertNewVersion() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.hasPermission(PartiesPermissions.ADMIN_UPDATES.toString()) && Variables.warnupdates)
				if (plugin.isUpdateAvailable())
					getPlayer(p.getUniqueId()).sendMessage(Messages.updateavailable.replace("%version%", plugin.getNewUpdate()).replace("%thisversion%", plugin.getDescription().getVersion()));
		}
	}
	
	public ThePlayer loadPlayer(UUID uuid) {
		ThePlayer ret = getPlayer(uuid);
		getListPlayers().put(uuid, ret);
		return ret;
	}
	public void unloadPlayer(UUID uuid) {
		getListPlayers().remove(uuid);
	}
	
	public ThePlayer getPlayer(UUID uuid) {
		ThePlayer ret = null;
		if (getListPlayers().containsKey(uuid)) {
			ret = getListPlayers().get(uuid);
			LogHandler.log(LogLevel.DEBUG, "Got player " + ret.getName() + " from list [party:" + ret.getPartyName()+ "]", true);
		} else {
			if (ret == null && !plugin.getDatabaseType().isNone()) {
				ret = plugin.getDataHandler().getPlayer(uuid, false);
				if (ret != null)
					LogHandler.log(LogLevel.DEBUG, "Got player " + ret.getName() + " from database [party:" + ret.getPartyName()+ "]", true);
			}
			if (ret == null)
				ret = new ThePlayer(uuid, plugin);
		}
		return ret;
	}
	
	public List<UUID> getListPlayersToDelete() {
		return listThePlayerToDelete;
	}
	/*
	 * PlaceholderAPI, Vault & Tab placeholders handler
	 */
	public String setTabText(String text, Player player) {
		text = text
				.replace("%player%", player.getDisplayName())
				.replace("%world%", player.getWorld().getName());
		text = setPlaceholder(text, player);
		return text;
	}
	public String setPlaceholder(String message, Player sender) {
		if (plugin.isPlaceholderAPIHooked())
			message = PlaceholderAPI.setPlaceholders(sender, message);
		return message;
	}
	/*
	 * Gets
	 */
	public HashMap<UUID, ThePlayer> getListPlayers() {return listThePlayer;}
	public HashMap<UUID, Long> getChatCooldown() {
		return chatCooldown;
	}
	public HashMap<UUID, Long> getTeleportCooldown() {
		return teleportCooldown;
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
	public int getHomeCount() {
		return homeCounts;
	}
	/*
	 * Spies Based
	 */
	public void sendMessageToSpy(String message, String partyName) {
		for (UUID uuid : listSpyPlayers) {
			Player pl = Bukkit.getPlayer(uuid);
			if (pl != null) {
				if (pl.hasPermission(PartiesPermissions.ADMIN_SPY.toString())) {
					if (getPlayer(uuid).getPartyName().equalsIgnoreCase(partyName)) {
						if (JSONHandler.isJSON(message))
							JSONHandler.sendJSON(message, pl);
						else
							pl.sendMessage(message);
					}
				}
			}
		}
	}
	public void initSpies() {
		listSpyPlayers = plugin.getDataHandler().getSpies(false);
	}
	public boolean isSpy(UUID uuid) {
		boolean ret = false;
		if (listSpyPlayers.contains(uuid))
			ret = true;
		return ret;
	}
	public void setSpy(UUID uuid, boolean add) {
		boolean changed = false;
		if (add && !listSpyPlayers.contains(uuid)) {
			listSpyPlayers.add(uuid);
			changed = true;
		} else if (!add && listSpyPlayers.contains(uuid)){
			listSpyPlayers.remove(uuid);
			changed = true;
		}
		if (changed)
			plugin.getDataHandler().updateSpies(listSpyPlayers, false);
	}
	/*
	 * 
	 */
	public Parties getMain() {
		return plugin;
	}
}
