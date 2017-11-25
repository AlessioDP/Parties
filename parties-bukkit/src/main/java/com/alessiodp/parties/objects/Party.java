package com.alessiodp.parties.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitTask;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.JSONHandler;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.utils.addon.BanManagerHandler;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.parties.utils.tasks.InviteTask;
import com.alessiodp.partiesapi.events.PartiesPlayerJoinEvent;
import com.alessiodp.partiesapi.interfaces.Rank;

public class Party {
	private Parties plugin;
	
	private String name;
	private List<UUID> members;
	private UUID leader;
	private boolean fixed;
	
	private List<Player> onlinePlayers;
	private String description;
	private String motd;
	private Location home;
	private String prefix;
	private String suffix;
	private int kills;
	private String password;
	private HashMap<UUID, UUID> whoInvite;
	private HashMap<UUID, Integer> invited;


	public Party(String nm, Parties instance) {
		plugin = instance;
		name = nm;
		members = new ArrayList<UUID>();
		leader = null;
		fixed = false;
		onlinePlayers = new ArrayList<Player>();
		description = "";
		motd = "";
		home = null;
		prefix = "";
		suffix = "";
		kills = 0;
		password = "";
		whoInvite = new HashMap<UUID, UUID>();
		invited = new HashMap<UUID, Integer>();
		LogHandler.log(LogLevel.DEBUG, "Initialized party " + name, true);
	}
	public Party(Party copy) {
		plugin = copy.plugin;
		name = copy.name;
		members = copy.members;
		leader = copy.leader;
		fixed = copy.fixed;
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
		LogHandler.log(LogLevel.DEBUG, "Initialized copy of party " + name, true);
	}
	public void updateParty() {
		if (Variables.dynmap_enable && Variables.dynmap_showpartyhomes) {
			if (members.size() >= Variables.dynmap_settings_minimumplayers)
				if (home != null)
					plugin.getDynmap().addMarker(name, Variables.dynmap_marker_label
							.replace("%party%",	name)
							.replace("%prefix%",prefix)
							.replace("%suffix%",suffix)
							.replace("%kills%",	Integer.toString(kills)), home);
				else
					plugin.getDynmap().removeMarker(name);
			else
				plugin.getDynmap().removeMarker(name);
		}
		
		if (!plugin.getDatabaseType().isNone()) {
			plugin.getDataHandler().updateParty(this, false);
			LogHandler.log(LogLevel.DEBUG, "Updated party " + name, true);
		}
	}
	public void removeParty() {
		plugin.getPartyHandler().getListParties().remove(name.toLowerCase());
		if (Variables.dynmap_enable && Variables.dynmap_showpartyhomes) {
			plugin.getDynmap().removeMarker(name);
		}
		if (!plugin.getDatabaseType().isNone()) {
			plugin.getDataHandler().removeParty(this);
			plugin.getPartyHandler().tag_delete(this);
			LogHandler.log(LogLevel.DEBUG, "Removed party " + name, true);
		}
		
		for (Player player : onlinePlayers) {
			// saveDB is false because getData().removeParty already delete players data
			plugin.getPlayerHandler().getPlayer(player.getUniqueId()).cleanupPlayer(false);
		}
	}
	public void refreshPlayers() {
		List<Player> ret = new ArrayList<Player>();
		for (Player p : plugin.getServer().getOnlinePlayers())
			if (getMembers().contains(p.getUniqueId()))
				ret.add(p);
		setOnlinePlayers(ret);
		plugin.getPartyHandler().tag_refresh(this);
	}
	
	/*
	 * Invite
	 */
	public void invitePlayer(UUID from, UUID to) {
		BukkitTask it = new InviteTask(this, to).runTaskLater(plugin, Variables.invite_timeout * 20);
		whoInvite.put(to, from);
		invited.put(to, it.getTaskId());
	}
	public void cancelInvite(UUID to) {
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		UUID from = whoInvite.get(to);
		
		ThePlayer fromTP = plugin.getPlayerHandler().getPlayer(from);
		ThePlayer toTP = plugin.getPlayerHandler().getPlayer(to);
		
		fromTP.sendMessage(Messages.invite_noresponse, toTP.getPlayer());
		toTP.sendMessage(Messages.invite_timeout, fromTP.getPlayer());
		
		toTP.setInvited("");
		
		whoInvite.remove(to);
		invited.remove(to);
	}
	public void acceptInvite(UUID to) {
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		
		UUID from = whoInvite.get(to);
		ThePlayer fromTP = plugin.getPlayerHandler().getPlayer(from);
		ThePlayer toTP = plugin.getPlayerHandler().getPlayer(to);
		
		// Calling API Event
		PartiesPlayerJoinEvent partiesJoinEvent = new PartiesPlayerJoinEvent(toTP.getPlayer(), name, true, from);
		Bukkit.getServer().getPluginManager().callEvent(partiesJoinEvent);
		if (!partiesJoinEvent.isCancelled()) {
			//Send accepted
			fromTP.sendMessage(Messages.accept_inviteaccepted, Bukkit.getPlayer(to));
			
			//Send you accepted
			toTP.sendMessage(Messages.accept_accepted, Bukkit.getPlayer(from));
			
			whoInvite.remove(to);
			invited.remove(to);
	
			toTP.setInvited("");
	
			sendBroadcastParty(toTP.getPlayer(), Messages.accept_playerjoined);
			
			members.add(to);
			onlinePlayers.add(toTP.getPlayer());
	
			toTP.setPartyName(name);
			toTP.setRank(Variables.rank_default);
			toTP.sendMessage(Messages.accept_welcomeplayer);
			
			updateParty();
			toTP.updatePlayer();
			
			plugin.getPartyHandler().tag_addPlayer(toTP.getPlayer(), this);
		} else
			LogHandler.log(LogLevel.DEBUG, "PartiesPlayerJoinEvent is cancelled, ignoring invite accept of " + toTP.getName() + " into " + name, true);
	}
	public void denyInvite(UUID to) {
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		
		UUID from = whoInvite.get(to);
		ThePlayer fromTP = plugin.getPlayerHandler().getPlayer(from);
		ThePlayer toTP = plugin.getPlayerHandler().getPlayer(to);
		
		fromTP.sendMessage(Messages.deny_invitedenied, Bukkit.getPlayer(to));
		toTP.sendMessage(Messages.deny_denied, Bukkit.getPlayer(from));
		
		toTP.setInvited("");
		
		whoInvite.remove(to);
		invited.remove(to);
	}
	
	/*
	 * Send Message
	 */
	public void sendPlayerMessage(Player sender, String playerMessage) {
		if (playerMessage == null || playerMessage.isEmpty())
			return;
		if (Variables.banmanager_enable && Variables.banmanager_muted)
			if (BanManagerHandler.isMuted(sender)) {
				return;
			}
		String formattedMessage = convertText(Variables.chat_chatformat, sender);
		String msg = playerMessage;
		if (Variables.chat_allowcolors)
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		
		if (JSONHandler.isJSON(formattedMessage)) {
			for (Player player : onlinePlayers) {
				JSONHandler.sendJSON(ChatColor.translateAlternateColorCodes('&', formattedMessage).replace("%message%", msg), player);
			}
		} else {
			for (Player player : onlinePlayers) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage).replace("%message%", msg));
			}
		}
		plugin.getPlayerHandler().sendMessageToSpy(
				ChatColor.translateAlternateColorCodes('&', convertText(Variables.chat_spychatformat, sender))
				.replace("%message%", playerMessage), name);
	}
	
	public void sendBroadcastParty(Player sender, String message) {
		if (message == null || message.isEmpty())
			return;
		String formattedMessage = Variables.chat_partybroadcastformat
				.replace("%message%", message);
		formattedMessage = convertText(formattedMessage, sender);
		
		if (JSONHandler.isJSON(formattedMessage)) {
			for (Player player : onlinePlayers) {
				JSONHandler.sendJSON(ChatColor.translateAlternateColorCodes('&', formattedMessage), player);
			}
		} else {
			for (Player player : onlinePlayers) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
			}
		}
	}
	public void sendBroadcastParty(OfflinePlayer sender, String message) {
		if (message == null || message.isEmpty())
			return;
		String formattedMessage = Variables.chat_partybroadcastformat
				.replace("%message%", message);
		formattedMessage = convertText(formattedMessage, sender);
		
		if (JSONHandler.isJSON(formattedMessage)) {
			for (Player player : onlinePlayers) {
				JSONHandler.sendJSON(ChatColor.translateAlternateColorCodes('&', formattedMessage), player);
			}
		} else {
			for (Player player : onlinePlayers) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', formattedMessage));
			}
		}
	}
	public String convertText(String text, Player player) {
		text = text
				.replace("%player%",	player.getName() != null ? player.getName() : plugin.getDataHandler().getOldPlayerName(player.getUniqueId()))
				.replace("%sender%",	player.getName() != null ? player.getName() : plugin.getDataHandler().getOldPlayerName(player.getUniqueId()))
				.replace("%world%",		player.getWorld().getName())
				.replace("%desc%",		getDescription())
				.replace("%party%",		getName())
				.replace("%prefix%",	getPrefix())
				.replace("%suffix%",	getSuffix())
				.replace("%kills",		Integer.toString(getKills()))
				.replace("%players%",	Integer.toString(getNumberOnlinePlayers()))
				.replace("%allplayers%",Integer.toString(getMembers().size()))
				.replace("%rank%",		plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getPlayer(player.getUniqueId()).getRank()).getChat());
		text = plugin.getPlayerHandler().setPlaceholder(text, player);
		return text;
	}
	public String convertText(String text, OfflinePlayer player) {
		text = text
				.replace("%player%",	player.getName() != null ? player.getName() : plugin.getDataHandler().getOldPlayerName(player.getUniqueId()))
				.replace("%sender%",	player.getName() != null ? player.getName() : plugin.getDataHandler().getOldPlayerName(player.getUniqueId()))
				.replace("%world%",		"")
				.replace("%desc%",		getDescription())
				.replace("%party%",		getName())
				.replace("%prefix%",	getPrefix())
				.replace("%suffix%",	getSuffix())
				.replace("%kills",		Integer.toString(getKills()))
				.replace("%players%",	Integer.toString(getNumberOnlinePlayers()))
				.replace("%allplayers%",Integer.toString(getMembers().size()))
				.replace("%rank%",		plugin.getPartyHandler().searchRank(plugin.getPlayerHandler().getPlayer(player.getUniqueId()).getRank()).getChat());
		text = plugin.getPlayerHandler().setPlaceholder(text, null);
		return text;
	}
	public void sendFriendlyFireWarn(ThePlayer victim, ThePlayer attacker) {
		if (Variables.friendlyfire_warn) {
			for (Player onlineP : getOnlinePlayers()) {
				if (!onlineP.getUniqueId().equals(attacker.getUUID())) {
					Rank r = plugin.getPartyHandler().searchRank(victim.getRank());
					
					if (r != null && r.havePermission(PartiesPermissions.PRIVATE_WARNONDAMAGE.toString()))
						plugin.getPlayerHandler().getPlayer(onlineP.getUniqueId()).sendMessage(Messages.warnondamage
								.replace("%player%", attacker.getName())
								.replace("%victim%", victim.getName()));
				}
			}
		}
	}
	
	/*
	 * Gets
	 */
	public String getName() {return name;}
	public void setName(String v) {name = v;}
	
	public List<UUID> getMembers() {return members;}
	public void setMembers(List<UUID> ar) {members = ar;}
	
	public UUID getLeader() {return leader;}
	public void setLeader(UUID u) {leader = u;}
	
	public List<Player> getOnlinePlayers() {return onlinePlayers;}
	public void setOnlinePlayers(List<Player> v) {onlinePlayers = v;}
	public void reloadOnlinePlayers() {
		onlinePlayers = new ArrayList<Player>();
		for (UUID u : members) {
			OfflinePlayer op = Bukkit.getOfflinePlayer(u);
			if (op != null && op.isOnline())
				onlinePlayers.add(op.getPlayer());
		}
	}
	public void addOnlinePlayer(Player player) {
		if (!getOnlinePlayers().contains(player)) {
			getOnlinePlayers().add(player);
			plugin.getPartyHandler().tag_addPlayer(player, this);
		}
	}
	public void remOnlinePlayer(Player player) {
		if (getOnlinePlayers().contains(player)) {
			getOnlinePlayers().remove(player);
			plugin.getPartyHandler().tag_removePlayer(player, this);
		}
	}
	public int getNumberOnlinePlayers() {
		int c=0;
		for (Player p : onlinePlayers) {
			if (!isVanished(p))
				c++;
		}
		return c;
	}
	public String getDescription() {return description;}
	public void setDescription(String v) {description = v;}
	public String getMOTD() {return motd;}
	public void setMOTD(String v) {motd = v;}
	public Location getHome() {return home;}
	public void setHome(Location v) {home = v;}
	public String getHomeRaw() {
		Location home = getHome();
		if (home != null)
			return home.getWorld().getName() + "," + home.getBlockX() + "," + home.getBlockY() + "," + home.getBlockZ() + "," + home.getYaw() + "," + home.getPitch();
		return "";
	}
	public void setHomeRaw(String raw) {
		String[] split = raw.split(",");
		try {
			World world = Bukkit.getWorld(split[0]);
			int x = Integer.parseInt(split[1]);
			int y = Integer.parseInt(split[2]);
			int z = Integer.parseInt(split[3]);
			float yaw = Float.parseFloat(split[4]);
			float pitch = Float.parseFloat(split[5]);
			setHome(new Location(world, x, y, z, yaw, pitch));
		} catch(Exception ex) {
			setHome(null);
		}
	}
	public String getPrefix() {return prefix;}
	public void setPrefix(String v) {prefix = v;}
	public String getSuffix() {return suffix;}
	public void setSuffix(String v) {suffix = v;}
	public int getKills() {return kills;}
	public void setKills(int v) {kills = v;}
	public boolean isFixed() {return fixed;}
	public void setFixed(boolean v) {fixed=v;}
	public String getPassword() {return password;}
	public void setPassword(String v) {password = v;}
	public HashMap<UUID, UUID> getWhoInviteMap() {return whoInvite;}
	public HashMap<UUID, Integer> getInvitedMap() {return invited;}
	
	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}
	
}
