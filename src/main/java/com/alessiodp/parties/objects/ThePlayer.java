package com.alessiodp.parties.objects;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.UUID;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.JSONHandler;
import com.alessiodp.parties.handlers.LogHandler;

public class ThePlayer {
	private Parties plugin;
	private String name;
	private UUID uuid;
	
	private ArrayList<String> ignoredParties;
	
	private boolean haveParty = false;
	private int rank;
	private String partyName = "";
	
	public int getRank(){return rank;}
	public void setRank(int v){rank = v;}
	
	private boolean chatParty = false;
	private String invited = "";
	
    private int homeTask = -1;
    private Location homeFrom;
    
    private int portalTimeoutTask = -1;
    
    // Confirm command packet: [timestamp, cmd, pass]
    private Object[] lastCommand;
    
    private UUID createID;

	public ThePlayer(UUID uuid, Parties instance) {
		plugin = instance;
		this.uuid = uuid;
		name = Bukkit.getOfflinePlayer(uuid).getName();
		ignoredParties = new ArrayList<String>();
		if(Variables.database_type.equalsIgnoreCase("none")){
			for(Entry<String, Party> entry : plugin.getPartyHandler().listParty.entrySet()){
				Party party = entry.getValue();
				if(party != null){
					rank = Variables.rank_default;
					haveParty = true;
					partyName = entry.getKey();
				}
			}
		} else {
			partyName = plugin.getConfigHandler().getData().getPlayerPartyName(uuid);
			if(!partyName.isEmpty()){
				Party party = plugin.getPartyHandler().loadParty(partyName);
				if(party != null){
					haveParty = true;
					rank = plugin.getConfigHandler().getData().getRank(uuid);
				}
			}
		}
		createID = UUID.randomUUID();
		LogHandler.log(3, "Initialized player " + name + "["+uuid+"]");
	}
	public void updatePlayer(){
		if(!Variables.database_type.equalsIgnoreCase("none"))
			plugin.getConfigHandler().getData().updatePlayer(this);
		LogHandler.log(3, "Updated player " + name + "["+uuid+"]");
	}
	public void removePlayer(){
		haveParty = false;
		rank = Variables.rank_default;
		partyName = "";
		chatParty = false;
		
		if(!Variables.database_type.equalsIgnoreCase("none"))
			plugin.getConfigHandler().getData().removePlayer(uuid);
		LogHandler.log(3, "Removed player " + name + "["+uuid+"]");
	}
	/*
	 * Home
	 */
	public int getHomeTask(){
		return homeTask;
	}
	public void setHomeTask(int in){
		homeTask = in;
	}
	public Location getHomeFrom(){
		return homeFrom;
	}
	public void setHomeFrom(Location loc){
		homeFrom = loc;
	}
	
	/*
	 * Player Info
	 */
	public String getName() {
		return name;
	}

	public UUID getUUID() {
		return uuid;
	}
	
	/*
	 * Get and Set
	 */
	public String getPartyName(){return partyName;}
	public boolean haveParty(){return haveParty;}
	public boolean chatParty(){return chatParty;}
	public String getInvite(){return invited;}
	public ArrayList<String> getIgnoredParties(){return ignoredParties;}
	
	public void addIgnoredParty(String name){ignoredParties.add(name);}
	public void removeIgnoredParty(String name){
		if(ignoredParties.contains(name))
			ignoredParties.remove(name);
	}
	public void setHaveParty(boolean bl){haveParty = bl;}
	public void setPartyName(String str){partyName = str;}
	public void setChatParty(boolean bl){chatParty = bl;}
	public void setInvited(String str){invited = str;}
	public Player getPlayer() {return Bukkit.getPlayer(uuid);}
	public int getPortalTimeout(){return portalTimeoutTask;}
	public void setPortalTimeout(int v){portalTimeoutTask = v;}
	public void putLastCommand(Object[] ob){lastCommand = ob;}
	public Object[] getLastCommand(){return lastCommand;}
	public UUID getCreateID(){return createID;}
	/*
	 * Send Message
	 */
	public void sendMessage(String message) {
		if(message.isEmpty() || message==null)
			return;
		String formattedMessage = message
				.replace("%sender%", name)
				.replace("%world%", getPlayer()
						.getWorld()
						.getName());
			Party party = plugin.getPartyHandler().loadParty(partyName);
			if(party != null){
				formattedMessage = formattedMessage
						.replace("%party%", party.getName())
						.replace("%desc%", party.getDescription())
						.replace("%motd%", party.getMOTD())
						.replace("%kills%", ""+party.getKills())
						.replace("%prefix%", party.getPrefix())
						.replace("%suffix%", party.getSuffix())
						.replace("%players%", ""+party.getOnlinePlayers())
						.replace("%rank%", plugin.getPartyHandler().searchRank(rank).getChat());
			} else {
				formattedMessage = formattedMessage
						.replace("%party%", "")
						.replace("%rank%", "")
						.replace("%desc%", "")
						.replace("%motd%", "")
						.replace("%kills%", "")
						.replace("%prefix%", "")
						.replace("%suffix%", "")
						.replace("%players%", "");	
			}
		formattedMessage = setPEX(formattedMessage, getPlayer());
		formattedMessage = setVault(formattedMessage, getPlayer());
		formattedMessage = setPlaceholder(formattedMessage, getPlayer());
		
		send(formattedMessage);
	}
	public void sendMessage(String message, Player victim) {
		if(message.isEmpty() || message==null)
			return;
		String formattedMessage = message
				.replace("%sender%", name)
				.replace("%player%", victim.getName())
				.replace("%world%", victim.getWorld().getName());
			Party party = plugin.getPlayerHandler().getPartyFromPlayer(victim);
			if(party != null){
				formattedMessage = formattedMessage
						.replace("%party%", party.getName())
						.replace("%desc%", party.getDescription())
						.replace("%motd%", party.getMOTD())
						.replace("%kills%", ""+party.getKills())
						.replace("%prefix%", party.getPrefix())
						.replace("%suffix%", party.getSuffix())
						.replace("%players%", ""+party.getOnlinePlayers())
						.replace("%rank%", plugin.getPartyHandler().searchRank(rank).getChat());
			} else {
				formattedMessage = formattedMessage
						.replace("%party%", "")
						.replace("%rank%", "")
						.replace("%desc%", "")
						.replace("%motd%", "")
						.replace("%kills%", "")
						.replace("%prefix%", "")
						.replace("%suffix%", "")
						.replace("%players%", "");	
			}
		formattedMessage = setPEX(formattedMessage, getPlayer());
		formattedMessage = setVault(formattedMessage, getPlayer());
		formattedMessage = setPlaceholder(formattedMessage, getPlayer());
		
		send(formattedMessage);
	}
	public void sendMessage(String message, OfflinePlayer victim) {
		if(message.isEmpty() || message==null)
			return;
		String formattedMessage = message
				.replace("%sender%", name)
				.replace("%player%", victim.getName());
		if(!plugin.getConfigHandler().getData().getPlayerPartyName(victim.getUniqueId()).isEmpty()){
			Party party = plugin.getPartyHandler().loadParty(plugin.getConfigHandler().getData().getPlayerPartyName(victim.getUniqueId()));
			if(party != null){
				formattedMessage = formattedMessage
						.replace("%party%", party.getName())
						.replace("%desc%", party.getDescription())
						.replace("%motd%", party.getMOTD())
						.replace("%kills%", ""+party.getKills())
						.replace("%prefix%", party.getPrefix())
						.replace("%suffix%", party.getSuffix())
						.replace("%players%", ""+party.getOnlinePlayers())
						.replace("%rank%", plugin.getPartyHandler().searchRank(rank).getChat());
			} else {
				formattedMessage = formattedMessage
						.replace("%party%", "")
						.replace("%rank%", "")
						.replace("%desc%", "")
						.replace("%motd%", "")
						.replace("%kills%", "")
						.replace("%prefix%", "")
						.replace("%suffix%", "")
						.replace("%players%", "");
			}
		}
		formattedMessage = setPEX(formattedMessage, getPlayer());
		formattedMessage = setVault(formattedMessage, getPlayer());
		formattedMessage = setPlaceholder(formattedMessage, getPlayer());
		
		send(formattedMessage);
	}
	public void sendMessage(String message, Party party) {
		if(message.isEmpty() || message==null)
			return;
		String formattedMessage = message.replace("%sender%", name);
		if(party != null){
				formattedMessage = formattedMessage
						.replace("%party%", party.getName())
						.replace("%desc%", party.getDescription())
						.replace("%motd%", party.getMOTD())
						.replace("%kills%", ""+party.getKills())
						.replace("%prefix%", party.getPrefix())
						.replace("%suffix%", party.getSuffix())
						.replace("%players%", ""+party.getOnlinePlayers());
			} else {
				formattedMessage = formattedMessage
						.replace("%party%", "")
						.replace("%rank%", "")
						.replace("%desc%", "")
						.replace("%motd%", "")
						.replace("%kills%", "")
						.replace("%prefix%", "")
						.replace("%suffix%", "")
						.replace("%players%", "");	
			}
		formattedMessage = setPEX(formattedMessage, getPlayer());
		formattedMessage = setVault(formattedMessage, getPlayer());
		formattedMessage = setPlaceholder(formattedMessage, getPlayer());
		
		send(formattedMessage);
	}
	/*
	 * Private methods
	 */
	private String setVault(String message, Player sender){
		if(Variables.vault_enable)
			if(plugin.getVaultChat() != null){
				if(sender == null){
					message = message
							.replace("%vault_prefix%", "")
							.replace("%vault_suffix%", "");
				} else {
					message = message
							.replace("%vault_prefix%", plugin.getVaultChat().getPlayerPrefix(sender))
							.replace("%vault_suffix%", plugin.getVaultChat().getPlayerPrefix(sender));
				}
			}
		return message;
	}
	private String setPEX(String message, Player sender){
		if(plugin.getPex())
			return message.replace("%group%", plugin.getPlayerHandler().getGroup(getPlayer()));
		return message.replace("%group%", "");
	}
	private String setPlaceholder(String message, Player sender){
		if(plugin.isPlaceholderAPIHooked())
			message = PlaceholderAPI.setPlaceholders(sender, message);
		return message;
	}
	
	private void send(String message){
		if(JSONHandler.isJSON(message))
			JSONHandler.sendJSON(message, getPlayer());
		else
			getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
}
