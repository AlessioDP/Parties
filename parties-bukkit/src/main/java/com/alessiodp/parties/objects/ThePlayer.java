package com.alessiodp.parties.objects;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.JSONHandler;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.interfaces.Rank;

public class ThePlayer {
	private Parties plugin;
	private String name;
	private UUID uuid;
	private UUID createID;
	
	private int rank;
	private String partyName;
	
	private boolean chatParty;
	private String invited;
	
	private int homeTask;
	private Location homeFrom;
	
	private int portalTimeoutTask;
	
	private Object[] lastCommand;
	
	private List<String> ignoredParties;

	public ThePlayer(UUID u, Parties instance) {
		plugin = instance;
		uuid = u;
		name = Bukkit.getOfflinePlayer(u).getName();
		createID = UUID.randomUUID();
		
		rank = Variables.rank_default;
		partyName = "";
		
		chatParty = false;
		invited = "";
		
		homeTask = -1;
		homeFrom = null;
		
		portalTimeoutTask = -1;
		
		lastCommand = null;
		
		ignoredParties = new ArrayList<String>();
		
		LogHandler.log(LogLevel.DEBUG, "Initialized player " + name + "[" + u + "]", true);
	}
	
	public void updatePlayer() {
		if (!plugin.getDatabaseType().isNone())
			plugin.getDatabaseDispatcher().updatePlayer(this);
		LogHandler.log(LogLevel.DEBUG, "Updated player " + name + "["+uuid+"]", true);
	}
	public void cleanupPlayer(boolean saveDB) {
		rank = Variables.rank_default;
		partyName = "";
		chatParty = false;
		
		if (saveDB)
			plugin.getDatabaseDispatcher().removePlayer(uuid);
		plugin.getPartyHandler().tag_removePlayer(Bukkit.getPlayer(uuid), null);
		LogHandler.log(LogLevel.DEBUG, "Cleaned up player " + name + "["+uuid+"]", true);
	}
	public LinkedHashMap<String, String> getAllowedCommands() {
		LinkedHashMap<String, String> newCommands = new LinkedHashMap<String, String>();
		Rank r = plugin.getPartyHandler().searchRank(getRank());
		Player p = getPlayer();
		
		if (!getPartyName().isEmpty()) {
			if (p.hasPermission(PartiesPermissions.HELP.toString()))
				newCommands.put(Variables.command_help, Messages.help_help);
			if (p.hasPermission(PartiesPermissions.SENDMESSAGE.toString()) && r.havePermission(PartiesPermissions.PRIVATE_SENDMESSAGE.toString()))
				newCommands.put(Variables.command_p, Messages.help_p);
			if (p.hasPermission(PartiesPermissions.LEAVE.toString()))
				newCommands.put(Variables.command_leave, Messages.help_leave);
			if (p.hasPermission(PartiesPermissions.INFO.toString()))
				newCommands.put(Variables.command_info, Messages.help_info);
			if (p.hasPermission(PartiesPermissions.MEMBERS.toString()))
				newCommands.put(Variables.command_members, Messages.help_members);
			if (p.hasPermission(PartiesPermissions.CHAT.toString()))
				newCommands.put(Variables.command_chat, Messages.help_chat);
			if (p.hasPermission(PartiesPermissions.LIST.toString()))
				newCommands.put(Variables.command_list, Messages.help_list);
			if (p.hasPermission(PartiesPermissions.HOME_OTHERS.toString()))
				newCommands.put(Variables.command_home, Messages.help_home_others);
			else if (p.hasPermission(PartiesPermissions.HOME.toString()) && r.havePermission(PartiesPermissions.PRIVATE_HOME.toString()))
				newCommands.put(Variables.command_home, Messages.help_home);
			if (p.hasPermission(PartiesPermissions.SETHOME.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_HOME.toString()))
				newCommands.put(Variables.command_sethome, Messages.help_sethome);
			if (Variables.teleport_enable && p.hasPermission(PartiesPermissions.TELEPORT.toString()) && r.havePermission(PartiesPermissions.PRIVATE_ADMIN_TELEPORT.toString()))
				newCommands.put(Variables.command_teleport, Messages.help_teleport);
			if (p.hasPermission(PartiesPermissions.INVITE.toString()) && r.havePermission(PartiesPermissions.PRIVATE_INVITE.toString()))
				newCommands.put(Variables.command_invite, Messages.help_invite);
			if (p.hasPermission(PartiesPermissions.DESC.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_DESC.toString()))
				newCommands.put(Variables.command_desc, Messages.help_desc);
			if (p.hasPermission(PartiesPermissions.MOTD.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_MOTD.toString()))
				newCommands.put(Variables.command_motd, Messages.help_motd);
			if (Variables.password_enable && p.hasPermission(PartiesPermissions.PASSWORD.toString()) && r.havePermission(PartiesPermissions.PRIVATE_EDIT_PASSWORD.toString()))
				newCommands.put(Variables.command_password, Messages.help_password);
			if (p.hasPermission(PartiesPermissions.RANK.toString()) && r.havePermission(PartiesPermissions.PRIVATE_ADMIN_RANK.toString()))
				newCommands.put(Variables.command_rank, Messages.help_rank);
			if (p.hasPermission(PartiesPermissions.PREFIX.toString()) && Variables.tag_enable && !Variables.tag_system && Variables.tag_custom_prefix && r.havePermission(PartiesPermissions.PRIVATE_EDIT_PREFIX.toString()))
				newCommands.put(Variables.command_prefix, Messages.help_prefix);
			if (p.hasPermission(PartiesPermissions.SUFFIX.toString()) && Variables.tag_enable && !Variables.tag_system && Variables.tag_custom_suffix && r.havePermission(PartiesPermissions.PRIVATE_EDIT_SUFFIX.toString()))
				newCommands.put(Variables.command_suffix, Messages.help_suffix);
			if (p.hasPermission(PartiesPermissions.RENAME_OTHERS.toString()))
				newCommands.put(Variables.command_rename, Messages.help_rename_others);
			else if (p.hasPermission(PartiesPermissions.RENAME.toString()) && r.havePermission(PartiesPermissions.PRIVATE_ADMIN_RENAME.toString()))
				newCommands.put(Variables.command_rename, Messages.help_rename);
			if (p.hasPermission(PartiesPermissions.KICK.toString()) && r.havePermission(PartiesPermissions.PRIVATE_KICK.toString()))
				newCommands.put(Variables.command_kick, Messages.help_kick);
			if (p.hasPermission(PartiesPermissions.CLAIM.toString()) && Variables.griefprevention_enable && r.havePermission(PartiesPermissions.PRIVATE_CLAIM.toString()))
				newCommands.put(Variables.command_claim, Messages.help_claim);
		} else {
			if (p.hasPermission(PartiesPermissions.CREATE.toString())) {
				if (p.hasPermission(PartiesPermissions.ADMIN_CREATE_FIXED.toString()))
					newCommands.put(Variables.command_create, Messages.help_createfixed);
				else
					newCommands.put(Variables.command_create, Messages.help_create);
			}
			if (Variables.password_enable && p.hasPermission(PartiesPermissions.JOIN.toString()))
				newCommands.put(Variables.command_join, Messages.help_join);
			if (p.hasPermission(PartiesPermissions.ACCEPT.toString()))
				newCommands.put(Variables.command_accept, Messages.help_accept);
			if (p.hasPermission(PartiesPermissions.DENY.toString()))
				newCommands.put(Variables.command_deny, Messages.help_deny);
			if (p.hasPermission(PartiesPermissions.IGNORE.toString()))
				newCommands.put(Variables.command_ignore, Messages.help_ignore);
			if (p.hasPermission(PartiesPermissions.HOME_OTHERS.toString()))
				newCommands.put(Variables.command_home, Messages.help_home_others);
			if (p.hasPermission(PartiesPermissions.RENAME_OTHERS.toString()))
				newCommands.put(Variables.command_rename, Messages.help_rename_others);
			if (p.hasPermission(PartiesPermissions.KICK_OTHERS.toString()))
				newCommands.put(Variables.command_kick, Messages.help_kick);
		}
		if (p.hasPermission(PartiesPermissions.ADMIN_SPY.toString()))
			newCommands.put(Variables.command_spy, Messages.help_spy);
		if (p.hasPermission(PartiesPermissions.ADMIN_DELETE.toString()))
			newCommands.put(Variables.command_delete, Messages.help_delete);
		if (p.hasPermission(PartiesPermissions.RENAME_OTHERS.toString()))
			newCommands.put(Variables.command_rename, Messages.help_rename);
		if (p.hasPermission(PartiesPermissions.ADMIN_RELOAD.toString()))
			newCommands.put(Variables.command_reload, Messages.help_reload);
		if (p.hasPermission(PartiesPermissions.ADMIN_MIGRATE.toString()) && !Variables.storage_migrate_onlyconsole)
			newCommands.put(Variables.command_migrate, Messages.help_migrate);

		return newCommands;
	}
	/*
	 * Home
	 */
	public int getHomeTask() {
		return homeTask;
	}
	public void setHomeTask(int in) {
		homeTask = in;
	}
	public Location getHomeFrom() {
		return homeFrom;
	}
	public void setHomeFrom(Location loc) {
		homeFrom = loc;
	}
	
	/*
	 * Get and Set
	 */
	public String getName() {return name;}
	public void compareName(String v) {
		if (name != null) {
			if (!name.equals(v)) {
				LogHandler.log(LogLevel.DEBUG, "Player changed name from '" + v + "' to '" + name + "'[" + uuid + "]", true);
				updatePlayer();
			}
		} else if(!v.isEmpty()) {
			LogHandler.log(LogLevel.DEBUG, "Player name not found into the server, getting one from database: '" + v + "' [" + uuid + "]", true);
			name = v;
		}
	}
	public UUID getUUID() {return uuid;}
	
	public String getPartyName() {return partyName;}
	public boolean chatParty() {return chatParty;}
	public String getInvite() {return invited;}
	public List<String> getIgnoredParties() {return ignoredParties;}
	
	public void addIgnoredParty(String name) {ignoredParties.add(name);}
	public void removeIgnoredParty(String name) {
		if (ignoredParties.contains(name))
			ignoredParties.remove(name);
	}
	public void setPartyName(String str) {partyName = str;}
	public void setChatParty(boolean bl) {chatParty = bl;}
	public void setInvited(String str) {invited = str;}
	public Player getPlayer() {return Bukkit.getPlayer(uuid);}
	public int getPortalTimeout() {return portalTimeoutTask;}
	public void setPortalTimeout(int v) {portalTimeoutTask = v;}
	public void putLastCommand(Object[] ob) {lastCommand = ob;}
	public Object[] getLastCommand() {return lastCommand;}
	public UUID getCreateID() {return createID;}
	public int getRank() {return rank;}
	public void setRank(int v) {rank = v;}
	
	/*
	 * Send Message
	 */
	public void sendMessage(String message) {
		if (message == null || message.isEmpty())
			return;
		String formattedMessage = message
				.replace("%player%",	getName())
				.replace("%sender%",	getName())
				.replace("%world%",		getPlayer().getWorld().getName());
		
		formattedMessage = convertPartyText(formattedMessage, getPlayer(), null);
		
		send(formattedMessage);
	}
	public void sendMessage(String message, Player victim) {
		if (message == null || message.isEmpty())
			return;
		String formattedMessage = message
				.replace("%player%",	victim.getName())
				.replace("%sender%",	getName())
				.replace("%world%",		victim.getWorld().getName());
		
		formattedMessage = convertPartyText(formattedMessage, victim, null);
		
		send(formattedMessage);
	}
	public void sendMessage(String message, OfflinePlayer victim) {
		if (message == null || message.isEmpty())
			return;
		String formattedMessage = message
				.replace("%player%",	victim.getName() != null ? victim.getName() : plugin.getDatabaseDispatcher().getOldPlayerName(victim.getUniqueId()))
				.replace("%sender%",	getName())
				.replace("%world%",		"");
		
		formattedMessage = convertPartyText(formattedMessage, victim);
		
		send(formattedMessage);
	}
	public void sendMessage(String message, Party party) {
		if (message == null || message.isEmpty())
			return;
		String formattedMessage = message
				.replace("%player%",	getName())
				.replace("%sender%",	getName())
				.replace("%world%",		getPlayer().getWorld().getName());
		
		formattedMessage = convertPartyText(message, getPlayer(), party);
		
		send(formattedMessage);
	}
	
	/*
	 * Placeholders handler
	 */
	public String convertPartyText(String message, Player player, Party party) {
		String formattedMessage = message;
		if (party == null)
			party = plugin.getPartyHandler().getParty(getPartyName());
		if (party != null)
			formattedMessage = party.convertText(formattedMessage, getPlayer());
		else {
			formattedMessage = message
					.replace("%desc%",		"")
					.replace("%party%",		"")
					.replace("%prefix%",	"")
					.replace("%suffix%",	"")
					.replace("%kills",		"")
					.replace("%players%",	"")
					.replace("%allplayers%","")
					.replace("%rank%",		"");
			formattedMessage = plugin.getPlayerHandler().setPlaceholder(formattedMessage, player);
		}
		return formattedMessage;
	}
	private String convertPartyText(String message, OfflinePlayer player) {
		String formattedMessage = message;
		String partyname = plugin.getDatabaseDispatcher().getPlayerPartyName(player.getUniqueId());
		if (!partyname.isEmpty()) {
			Party party = plugin.getPartyHandler().getParty(partyname);
			if (party != null)
				formattedMessage = party.convertText(formattedMessage, player);
			else {
				formattedMessage = message
						.replace("%desc%",		"")
						.replace("%party%",		"")
						.replace("%prefix%",	"")
						.replace("%suffix%",	"")
						.replace("%kills",		"")
						.replace("%players%",	"")
						.replace("%allplayers%","")
						.replace("%rank%",		"");
				formattedMessage = plugin.getPlayerHandler().setPlaceholder(formattedMessage, null);
			}
		}
		return formattedMessage;
	}
	
	private void send(String message) {
		if (JSONHandler.isJSON(message))
			JSONHandler.sendJSON(message, getPlayer());
		else
			getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
}
