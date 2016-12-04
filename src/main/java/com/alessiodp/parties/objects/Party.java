package com.alessiodp.parties.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.JSONHandler;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.utils.addon.BanManagerHandler;
import com.alessiodp.parties.utils.tasks.InviteTask;

public class Party {
	private Parties plugin;
	
	private String name;
	private ArrayList<UUID> members;
	private UUID leader;
	
	private ArrayList<Player> onlinePlayers;
	private String description = "";
	private String motd = "";
	private Location home;
	private String prefix = "";
	private String suffix = "";
	private int kills = 0;
	private String password = "";
	private HashMap<UUID, UUID> whoInvite;
	private HashMap<UUID, Integer> invited;


	public Party(String name, Parties plugin) {
		this.plugin = plugin;
		this.name = name;
		members = new ArrayList<UUID>();
		leader = null;
		onlinePlayers = new ArrayList<Player>();
		whoInvite = new HashMap<UUID, UUID>();
		invited = new HashMap<UUID, Integer>();
		LogHandler.log(3, "Initialized party " + name);
	}
	public Party(Party copy){
		plugin = copy.plugin;
		name = copy.name;
		members = copy.members;
		leader = copy.leader;
		onlinePlayers = copy.onlinePlayers;
		description = copy.description;
		motd = copy.motd;
		home = copy.home;
		prefix = copy.prefix;
		suffix = copy.suffix;
		kills = copy.kills;
		password = copy.password;
		whoInvite = copy.whoInvite;
		invited = copy.invited;
		LogHandler.log(3, "Initialized copy of party " + name);
	}
	public void removeAllPlayers(){
		ThePlayer tp;
		for (Player player : onlinePlayers) {
			tp = plugin.getPlayerHandler().getThePlayer(player);
			tp.setHaveParty(false);
			tp.setChatParty(false);
			tp.setPartyName("");
			tp.setRank(Variables.rank_default);
			plugin.getPartyHandler().scoreboard_removePlayer(player);
		}
		LogHandler.log(3, "Removed all players of party " + name);
	}
	public void updateParty(){
		if(Variables.dynmap_enable && Variables.dynmap_showpartyhomes){
			if(members.size() >= Variables.dynmap_settings_minimumplayers)
				if(home != null)
					plugin.getDynmap().addMarker(name, Variables.dynmap_marker_label
							.replace("%party%", name)
							.replace("%prefix%", prefix)
							.replace("%suffix%", suffix)
							.replace("%kills%", kills+""), home);
				else
					plugin
					.getDynmap()
					.removeMarker(
							name
							);
			else
				plugin
				.getDynmap()
				.removeMarker(
						name
						);
		}
		
		if(Variables.database_type.equalsIgnoreCase("none"))
			return;
		plugin.getConfigHandler().getData().updateParty(this);
		LogHandler.log(3, "Updated party " + name);
	}
	public void removeParty(){
		removeAllPlayers();
		plugin.getPartyHandler().listParty.remove(name);
		if(Variables.dynmap_enable && Variables.dynmap_showpartyhomes){
			plugin.getDynmap().removeMarker(name);
		}
		if(Variables.database_type.equalsIgnoreCase("none"))
			return;
		plugin.getConfigHandler().getData().removeParty(this);
		LogHandler.log(3, "Removed party " + name);
	}
	
	/*
	 * Invite
	 */
	public void invitePlayer(UUID from, UUID to){
		BukkitTask it = new InviteTask(this, to).runTaskLater(plugin, Variables.invite_timeout * 20);
		whoInvite.put(to, from);
		invited.put(to, it.getTaskId());
	}
	public void cancelInvite(UUID to){
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		UUID from = whoInvite.get(to);
		
		plugin.getPlayerHandler().getThePlayer(from).sendMessage(Messages.invite_noresponse, Bukkit.getPlayer(to));
		plugin.getPlayerHandler().getThePlayer(to).sendMessage(Messages.invite_timeout, Bukkit.getPlayer(from));
		
		plugin.getPlayerHandler().getThePlayer(to).setInvited("");
		
		whoInvite.remove(to);
		invited.remove(to);
	}
	public void acceptInvite(UUID to){
		//Send accepted
		UUID from = whoInvite.get(to);
		plugin.getPlayerHandler().getThePlayer(from).sendMessage(Messages.accept_inviteaccepted, Bukkit.getPlayer(to));
		
		//Send you accepted
		plugin.getPlayerHandler().getThePlayer(to).sendMessage(Messages.accept_accepted, Bukkit.getPlayer(from));
		
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		whoInvite.remove(to);
		invited.remove(to);
		
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(to);

		tp.setInvited("");

		sendBroadcastParty(Bukkit.getPlayer(to), Messages.accept_playerjoined);
		sendSpyMessage(Bukkit.getPlayer(to), Messages.accept_playerjoined);
		
		members.add(to);
		onlinePlayers.add(Bukkit.getPlayer(to));

		tp.setHaveParty(true);
		tp.setPartyName(name);
		tp.setRank(Variables.rank_default);
		tp.sendMessage(Messages.accept_welcomeplayer);
		
		updateParty();
		tp.updatePlayer();
		
		plugin.getPartyHandler().scoreboard_refreshParty(name);
	}
	public void denyInvite(UUID to){
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		UUID from = whoInvite.get(to);
		
		plugin.getPlayerHandler().getThePlayer(from).sendMessage(Messages.deny_invitedenied, Bukkit.getPlayer(to));
		plugin.getPlayerHandler().getThePlayer(to).sendMessage(Messages.deny_denied, Bukkit.getPlayer(from));
		
		plugin.getPlayerHandler().getThePlayer(to).setInvited("");
		
		whoInvite.remove(to);
		invited.remove(to);
	}
	
	/*
	 * Send Message
	 */
	public void sendPlayerMessage(Player sender, String message) {
		if(message.isEmpty() || message==null)
			return;
		if(Variables.banmanager_enable && Variables.banmanager_muted)
			if(BanManagerHandler.isMuted(sender)){
				return;
			}
		String formattedMessage = Variables.chatformat
				.replace("%player%", sender.getDisplayName())
				.replace("%desc%", description)
				.replace("%party%", name)
				.replace("%world%", sender.getWorld().getName())
				.replace("%group%", plugin.getPlayerHandler().getGroup(sender))
				.replace("%rank%", plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getThePlayer(sender).getRank()).getChat());
		formattedMessage = setVault(formattedMessage, sender);
		formattedMessage = setPlaceholder(formattedMessage, sender);
		
		if(JSONHandler.isJSON(formattedMessage)){
			String msg = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message));
			for (Player player : onlinePlayers) {
				JSONHandler.sendJSON(formattedMessage.replace("%message%", msg), player);
			}
		} else {
			for (Player player : onlinePlayers) {
			    player.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage).replace("%message%", message));
			}
		}
	}
	
	public void sendBroadcastParty(Player sender, String message) {
		if(message.isEmpty() || message==null)
			return;
		String formattedMessage = Variables.partybroadcastformat
				.replace("%message%", message)
				.replace("%desc%", description)
				.replace("%player%", sender.getName())
				.replace("%group%", plugin.getPlayerHandler().getGroup(sender))
				.replace("%world%", sender.getWorld().getName())
				.replace("%party%", name)
				.replace("%rank%", plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getThePlayer(sender).getRank()).getChat());
		formattedMessage = setVault(formattedMessage, sender);
		formattedMessage = setPlaceholder(formattedMessage, sender);
		
		if(JSONHandler.isJSON(formattedMessage)){
			for (Player player : onlinePlayers) {
				JSONHandler.sendJSON(formattedMessage, player);
			}
		} else {
			for (Player player : onlinePlayers) {
			    player.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
			}
		}
	}
	public void sendBroadcastParty(OfflinePlayer sender, String message) {
		if(message.isEmpty() || message==null)
			return;
		String formattedMessage = Variables.partybroadcastformat
				.replace("%message%", message)
				.replace("%desc%", description)
				.replace("%player%", sender.getName())
				.replace("%group%", plugin.getPlayerHandler().getGroup(sender))
				.replace("%party%", name)
				.replace("%sender%", sender.getName())
				.replace("%rank%", plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getThePlayer(sender).getRank()).getChat());
		formattedMessage = setVault(formattedMessage, null);
		formattedMessage = setPlaceholder(formattedMessage, null);
		
		if(JSONHandler.isJSON(formattedMessage)){
			for (Player player : onlinePlayers) {
				JSONHandler.sendJSON(formattedMessage, player);
			}
		} else {
			for (Player player : onlinePlayers) {
			    player.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
			}
		}
	}
	
	public void sendSpyMessage(Player sender, String message) {
		if(message.isEmpty() || message==null)
			return;

		String formattedMessage = Variables.spychatformat
				.replace("%message%", ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)))
				.replace("%player%", sender.getDisplayName())
				.replace("%party%", name)
				.replace("%desc%", description)
				.replace("%group%", plugin.getPlayerHandler().getGroup(sender))
				.replace("%world%", sender.getWorld().getName())
				.replace("%rank%", plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getThePlayer(sender).getRank()).getChat());
		formattedMessage = setVault(formattedMessage, sender);
		formattedMessage = setPlaceholder(formattedMessage, sender);
		
		plugin.getPlayerHandler().sendMessageToSpy(ChatColor.translateAlternateColorCodes('&', formattedMessage), name);
	}
	public void sendSpyMessage(OfflinePlayer sender, String message) {
		if(message.isEmpty() || message==null)
			return;
		String formattedMessage = Variables.spychatformat
				.replace("%player%", sender.getName())
				.replace("%group%", plugin.getPlayerHandler().getGroup(sender))
				.replace("%desc%", description)
				.replace("%party%", name)
				.replace("%message", message)
				.replace("%rank%", plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getThePlayer(sender).getRank()).getChat());
		formattedMessage = setVault(formattedMessage, null);
		formattedMessage = setPlaceholder(formattedMessage, null);
		
		plugin.getPlayerHandler().sendMessageToSpy(ChatColor.translateAlternateColorCodes('&', formattedMessage), name);
	}
	/*
	 *  Private methods
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
	private String setPlaceholder(String message, Player sender){
		if(plugin.isPlaceholderAPIHooked())
			message = PlaceholderAPI.setPlaceholders(sender, message);
		return message;
	}
	/*
	 * Gets
	 */
	public String getName(){return name;}
	public void setName(String v){name = v;}
	
	public ArrayList<UUID> getMembers(){return members;}
	public void setMembers(ArrayList<UUID> ar){members = ar;}
	
	public UUID getLeader(){return leader;}
	public void setLeader(UUID u){leader = u;}
	
	public ArrayList<Player> getOnlinePlayers(){
		return onlinePlayers;
	}
	public String getDescription(){return description;}
	public void setDescription(String v){description = v;}
	public String getMOTD(){return motd;}
	public void setMOTD(String v){motd = v;}
	public Location getHome(){return home;}
	public void setHome(Location v){home = v;}
	public String getPrefix(){return prefix;}
	public void setPrefix(String v){prefix = v;}
	public String getSuffix(){return suffix;}
	public void setSuffix(String v){suffix = v;}
	public int getKills(){return kills;}
	public void setKills(int v){kills = v;}
	public String getPassword(){return password;}
	public void setPassword(String v){password = v;}
	public HashMap<UUID, UUID> getWhoInviteMap(){return whoInvite;}
	public HashMap<UUID, Integer> getInvitedMap(){return invited;}
}
