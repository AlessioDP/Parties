package com.alessiodp.parties.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.PartiesPermissions;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PlayerHandler {
	private Parties plugin;
	private HashMap<UUID, ThePlayer> listThePlayer;
	private ArrayList<UUID> listSpyPlayers;
	private int homeCounts;
	
	public PlayerHandler(Parties instance){
		plugin = instance;
		listThePlayer = new HashMap<UUID, ThePlayer>();
		listSpyPlayers = new ArrayList<UUID>();
		homeCounts = 0;
		reloadPlayers();
	}
	public void reloadPlayers(){
		listThePlayer = new HashMap<UUID, ThePlayer>();
		listSpyPlayers = new ArrayList<UUID>();
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			initSpy(p.getUniqueId());
			addPlayer(p.getUniqueId());
			ThePlayer tp = listThePlayer.get(p.getUniqueId());
			Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
			if(party == null){
				tp.setHaveParty(false);
				tp.setRank(Variables.rank_default);
				tp.setInvited("");
				tp.setPartyName("");
			} else {
				if(!party.getOnlinePlayers().contains(p))
					party.getOnlinePlayers().add(p);
				plugin.getPartyHandler().scoreboard_addPlayer(p, tp.getPartyName());
			}
			if(p.hasPermission(PartiesPermissions.ADMIN_UPDATES.toString()) && Variables.warnupdates)
				if(plugin.isUpdateAvailable())
					tp.sendMessage(Messages.availableupdate.replace("%version%", plugin.getNewUpdate()).replace("%thisversion%", plugin.getDescription().getVersion()));
		}
		LogHandler.log(3, "Reloaded list players");
		LogHandler.log(2, "Loaded " + plugin.getPartyHandler().listParty.size() + " parties");
	}
	/*
	 * Gets
	 */
	public HashMap<UUID, ThePlayer> getListPlayers(){return listThePlayer;}
	
	public ThePlayer getThePlayer(UUID uuid){
		if(listThePlayer.get(uuid) == null)
			addPlayer(uuid);
		return listThePlayer.get(uuid);
	}
	public ThePlayer getThePlayer(Player player){
		if(listThePlayer.get(player.getUniqueId()) == null)
			addPlayer(player.getUniqueId());
		return listThePlayer.get(player.getUniqueId());
	}
	public ThePlayer getThePlayer(OfflinePlayer player){
		if(listThePlayer.get(player.getUniqueId()) == null)
			addPlayer(player.getUniqueId());
		return listThePlayer.get(player.getUniqueId());
	}
	public Party getPartyFromPlayer(Player player){
		return plugin.getPartyHandler().loadParty(getThePlayer(player).getPartyName());
	}
	public Party getPartyFromThePlayer(ThePlayer player){
		return plugin.getPartyHandler().loadParty(player.getPartyName());
	}
	public String getGroup(Player player){
		if(plugin.getPex()){
			try{
				return PermissionsEx.getUser(player).getParentIdentifiers().get(0);
			} catch(Throwable ex){}
		} else if(plugin.getGM()){
			final AnjoPermissionsHandler handler = ((GroupManager)plugin.getServer().getPluginManager().getPlugin("GroupManager")).getWorldsHolder().getWorldPermissions(player.getName());
			if (handler != null)
				return handler.getGroup(player.getName());
		}
		return "";
	}
	public String getGroup(OfflinePlayer player){
		if(plugin.getPex()){
			try{
				return PermissionsEx.getUser(player.getName()).getParentIdentifiers().get(0);
			} catch(Throwable  ex){}
			return "";
		} else if(plugin.getGM()){
			final AnjoPermissionsHandler handler = ((GroupManager)plugin.getServer().getPluginManager().getPlugin("GroupManager")).getWorldsHolder().getWorldPermissions(player.getName());
			if (handler != null)
				return handler.getGroup(player.getName());
		}
		return "";
	}
	/*
	 * Player list
	 */
	public void addPlayer(UUID id){
		listThePlayer.put(id, new ThePlayer(id, plugin));
	}
	public void removePlayer(UUID id){
		listThePlayer.remove(id);
	}
	
	/*
	 * Home Based
	 */
	public void addHomeCount(){
		homeCounts++;
	}
	public void remHomeCount(){
		homeCounts--;
	}
	public int getHomeCount(){
		return homeCounts;
	}
	/*
	 *  Spies Based
	 */
	public void sendMessageToSpy(String message, String partyname){
		for(UUID uuid : listSpyPlayers){
			if(Bukkit.getPlayer(uuid) != null){
				if(Bukkit.getPlayer(uuid).hasPermission(PartiesPermissions.ADMIN_SPY.toString())){
					if(!partyname.equalsIgnoreCase(plugin.getPlayerHandler().getThePlayer(Bukkit.getPlayer(uuid)).getPartyName()))
						if(JSONHandler.isJSON(message))
							JSONHandler.sendJSON(message, Bukkit.getPlayer(uuid));
						else
							Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', message));
				}
			}
		}
		plugin.getLogger().info(ChatColor.stripColor(message));
	}
	public boolean isSpy(UUID uuid){
		if(listSpyPlayers.contains(uuid))
			return true;
		if(plugin.getConfigHandler().getData().isSpy(uuid) && !Variables.database_type.equalsIgnoreCase("none")){
			return true;
		}
		return false;
	}
	public void initSpy(UUID uuid){
		if(plugin.getConfigHandler().getData().isSpy(uuid)){
			if(!listSpyPlayers.contains(uuid))
				listSpyPlayers.add(uuid);
		} else if(listSpyPlayers.contains(uuid)){
			listSpyPlayers.remove(uuid);
		}
	}
	public void forceSpy(UUID uuid){
		if(!listSpyPlayers.contains(uuid))
			listSpyPlayers.add(uuid);
	}
	public void removeSpy(UUID uuid){
		if(listSpyPlayers.contains(uuid))
			listSpyPlayers.remove(uuid);
	}
	/*
	 * 
	 */
	public Parties getMain(){
		return plugin;
	}
}
