package com.alessiodp.parties.players.objects;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.internal.JSONHandler;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.interfaces.Rank;
import com.alessiodp.partiesapi.objects.PartyPlayer;

import lombok.Getter;
import lombok.Setter;

public class PartyPlayerEntity extends PartyPlayer {
	private Parties plugin;
	
	@Getter @Setter private UUID createID;
	
	@Getter @Setter private boolean chatParty;
	@Getter @Setter private String lastInvite;
	
	@Getter @Setter private int homeTask;
	@Getter @Setter private Location homeFrom;
	
	@Getter @Setter private int portalTimeoutTask;
	
	@Getter @Setter private Object[] lastCommand;
	
	@Getter private HashSet<String> ignoredParties;
	
	
	public PartyPlayerEntity(UUID uuid, Parties instance) {
		super(uuid, ConfigParties.RANK_SET_DEFAULT);
		plugin = instance;
		createID = UUID.randomUUID();
		chatParty = false;
		lastInvite = "";
		homeTask = -1;
		homeFrom = null;
		portalTimeoutTask = -1;
		lastCommand = null;
		ignoredParties = new HashSet<String>();
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_INIT
				.replace("{player}", getName())
				.replace("{uuid}", getPlayerUUID().toString()), true);
	}
	public PartyPlayerEntity(PartyPlayer pPlayer, Parties instance) {
		super(pPlayer);
		plugin = instance;
		createID = UUID.randomUUID();
		chatParty = false;
		lastInvite = "";
		homeTask = -1;
		homeFrom = null;
		portalTimeoutTask = -1;
		lastCommand = null;
		ignoredParties = new HashSet<String>();
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_INIT
				.replace("{player}", getName())
				.replace("{uuid}", getPlayerUUID().toString()), true);
	}
	
	
	public void updatePlayer() {
		// Update timestamp
		setNameTimestamp(System.currentTimeMillis() / 1000L);
		
		if (!plugin.getDatabaseManager().getDatabaseType().isNone())
			plugin.getDatabaseManager().updatePlayer(this);
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_UPDATED
				.replace("{player}", getName()), true);
	}
	
	public void cleanupPlayer(boolean saveDB) {
		setRank(ConfigParties.RANK_SET_DEFAULT);
		setPartyName("");
		setChatParty(false);
		
		if (saveDB) {
			updatePlayer();
		}
		plugin.getTagManager().removePlayerTag(this, null);
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_CLEANUP
				.replace("{player}", getName()), true);
	}
	
	public LinkedHashMap<String, String> getAllowedCommands() {
		LinkedHashMap<String, String> newCommands = new LinkedHashMap<String, String>();
		Rank r = plugin.getRankManager().searchRankByLevel(getRank());
		Player p = getPlayer();
		
		if (!getPartyName().isEmpty()) {
			// In party
			if (p.hasPermission(PartiesPermission.HELP.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_HELP, Messages.HELP_MAINCMD_HELP);
			if (p.hasPermission(PartiesPermission.SENDMESSAGE.toString()) && r.havePermission(PartiesPermission.PRIVATE_SENDMESSAGE.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_P, Messages.HELP_MAINCMD_P);
			
			// Common commands
			if (p.hasPermission(PartiesPermission.LEAVE.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_LEAVE, Messages.HELP_MAINCMD_LEAVE);
			if (p.hasPermission(PartiesPermission.INVITE.toString()) && r.havePermission(PartiesPermission.PRIVATE_INVITE.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_INVITE, Messages.HELP_MAINCMD_INVITE);
			if (p.hasPermission(PartiesPermission.INFO.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_INFO, Messages.HELP_MAINCMD_INFO);
			
			// Other commands
			if (ConfigParties.LIST_ENABLE && p.hasPermission(PartiesPermission.LIST.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_LIST, Messages.HELP_ADDCMD_LIST);
			if (ConfigParties.GENERAL_CHAT_TOGGLECHATCMD && p.hasPermission(PartiesPermission.CHAT.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_CHAT, Messages.HELP_MAINCMD_CHAT);
			if (ConfigParties.HOME_ENABLE) {
				if (p.hasPermission(PartiesPermission.HOME_OTHERS.toString()))
					newCommands.put(ConfigMain.COMMANDS_CMD_HOME, Messages.HELP_ADDCMD_HOME_OTHERS);
				else if (p.hasPermission(PartiesPermission.HOME.toString()) && r.havePermission(PartiesPermission.PRIVATE_HOME.toString()))
					newCommands.put(ConfigMain.COMMANDS_CMD_HOME, Messages.HELP_ADDCMD_HOME);
				if (p.hasPermission(PartiesPermission.SETHOME.toString()) && r.havePermission(PartiesPermission.PRIVATE_EDIT_HOME.toString()))
					newCommands.put(ConfigMain.COMMANDS_CMD_SETHOME, Messages.HELP_ADDCMD_SETHOME);
			}
			if (ConfigMain.ADDONS_GRIEFPREVENTION_ENABLE && p.hasPermission(PartiesPermission.CLAIM.toString()) && r.havePermission(PartiesPermission.PRIVATE_CLAIM.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_CLAIM, Messages.HELP_ADDCMD_CLAIM);
			
			// Admin commands
			if (ConfigParties.DESC_ENABLE && p.hasPermission(PartiesPermission.DESC.toString()) && r.havePermission(PartiesPermission.PRIVATE_EDIT_DESC.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_DESC, Messages.HELP_ADDCMD_DESC);
			if (ConfigParties.MOTD_ENABLE && p.hasPermission(PartiesPermission.MOTD.toString()) && r.havePermission(PartiesPermission.PRIVATE_EDIT_MOTD.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_MOTD, Messages.HELP_ADDCMD_MOTD);
			if (ConfigParties.TELEPORT_ENABLE && p.hasPermission(PartiesPermission.TELEPORT.toString()) && r.havePermission(PartiesPermission.PRIVATE_ADMIN_TELEPORT.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_TELEPORT, Messages.HELP_ADDCMD_TELEPORT);
			if (ConfigParties.COLOR_ENABLE && ConfigParties.COLOR_COLORCMD && p.hasPermission(PartiesPermission.COLOR.toString()) && r.havePermission(PartiesPermission.PRIVATE_EDIT_COLOR.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_COLOR, Messages.HELP_ADDCMD_COLOR);
			if (ConfigParties.PASSWORD_ENABLE && p.hasPermission(PartiesPermission.PASSWORD.toString()) && r.havePermission(PartiesPermission.PRIVATE_EDIT_PASSWORD.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_PASSWORD, Messages.HELP_ADDCMD_PASSWORD);
			if (p.hasPermission(PartiesPermission.RANK.toString()) && r.havePermission(PartiesPermission.PRIVATE_ADMIN_RANK.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_RANK, Messages.HELP_MAINCMD_RANK);
			if (ConfigMain.ADDITIONAL_TAG_ENABLE && ConfigMain.ADDITIONAL_TAG_TYPE.isCustom() && ConfigMain.ADDITIONAL_TAG_CUSTOM_PREFIX && p.hasPermission(PartiesPermission.PREFIX.toString()) && r.havePermission(PartiesPermission.PRIVATE_EDIT_PREFIX.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_PREFIX, Messages.HELP_ADDCMD_PREFIX);
			if (ConfigMain.ADDITIONAL_TAG_ENABLE && ConfigMain.ADDITIONAL_TAG_TYPE.isCustom() && ConfigMain.ADDITIONAL_TAG_CUSTOM_SUFFIX && p.hasPermission(PartiesPermission.SUFFIX.toString()) && r.havePermission(PartiesPermission.PRIVATE_EDIT_SUFFIX.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_SUFFIX, Messages.HELP_ADDCMD_SUFFIX);
			if (p.hasPermission(PartiesPermission.RENAME_OTHERS.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_RENAME, Messages.HELP_MAINCMD_RENAME_OTHERS);
			else if (p.hasPermission(PartiesPermission.RENAME.toString()) && r.havePermission(PartiesPermission.PRIVATE_ADMIN_RENAME.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_RENAME, Messages.HELP_MAINCMD_RENAME);
			if (p.hasPermission(PartiesPermission.KICK.toString()) && r.havePermission(PartiesPermission.PRIVATE_KICK.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_KICK, Messages.HELP_MAINCMD_KICK);
		} else {
			// Out of party
			if (p.hasPermission(PartiesPermission.CREATE.toString())) {
				if (p.hasPermission(PartiesPermission.ADMIN_CREATE_FIXED.toString()))
					newCommands.put(ConfigMain.COMMANDS_CMD_CREATE, Messages.HELP_MAINCMD_CREATE_FIXED);
				else
					newCommands.put(ConfigMain.COMMANDS_CMD_CREATE, Messages.HELP_MAINCMD_CREATE);
			}
			if (p.hasPermission(PartiesPermission.ACCEPT.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_ACCEPT, Messages.HELP_MAINCMD_ACCEPT);
			if (p.hasPermission(PartiesPermission.DENY.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_DENY, Messages.HELP_MAINCMD_DENY);
			if (ConfigParties.PASSWORD_ENABLE && p.hasPermission(PartiesPermission.JOIN.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_JOIN, Messages.HELP_ADDCMD_JOIN);
			if (p.hasPermission(PartiesPermission.IGNORE.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_IGNORE, Messages.HELP_MAINCMD_IGNORE);
			if (ConfigParties.HOME_ENABLE && p.hasPermission(PartiesPermission.HOME_OTHERS.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_HOME, Messages.HELP_ADDCMD_HOME_OTHERS);
			if (p.hasPermission(PartiesPermission.KICK_OTHERS.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_KICK, Messages.HELP_MAINCMD_KICK);
			if (p.hasPermission(PartiesPermission.RENAME_OTHERS.toString()))
				newCommands.put(ConfigMain.COMMANDS_CMD_RENAME, Messages.HELP_MAINCMD_RENAME_OTHERS);
		}
		if (p.hasPermission(PartiesPermission.ADMIN_SPY.toString()))
			newCommands.put(ConfigMain.COMMANDS_CMD_SPY, Messages.HELP_MAINCMD_SPY);
		if (p.hasPermission(PartiesPermission.ADMIN_DELETE.toString()))
			newCommands.put(ConfigMain.COMMANDS_CMD_DELETE, Messages.HELP_MAINCMD_DELETE);
		if (p.hasPermission(PartiesPermission.ADMIN_RELOAD.toString()))
			newCommands.put(ConfigMain.COMMANDS_CMD_RELOAD, Messages.HELP_MAINCMD_RELOAD);
		if (p.hasPermission(PartiesPermission.ADMIN_MIGRATE.toString()) && !ConfigMain.STORAGE_MIGRATE_ONLYCONSOLE)
			newCommands.put(ConfigMain.COMMANDS_CMD_MIGRATE, Messages.HELP_MAINCMD_MIGRATE);
		
		return newCommands;
	}
	
	public void compareName(String bukkitName) {
		if (getName() != null) {
			if (!getName().equals(bukkitName)) {
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_COMPARENAME_CHANGE
						.replace("{new}", bukkitName)
						.replace("{old}", getName())
						.replace("{uuid}", getPlayerUUID().toString()), true);
				setName(bukkitName);
				updatePlayer();
			}
		} else if(!bukkitName.isEmpty()) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_COMPARENAME_NOTFOUND
					.replace("{uuid}", getPlayerUUID().toString())
					.replace("{player}", bukkitName), true);
			setName(bukkitName);
		}
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(getPlayerUUID());
	}
	
	
	/*
	 * Send Message
	 */
	public void sendNoPermission(PartiesPermission perm) {
		sendMessage(Messages.PARTIES_PERM_NOPERM.replace("%permission%", perm.toString()));
	}
	
	public void sendMessage(String message) {
		sendMessage(message, this);
	}
	public void sendMessage(String message, PartyPlayerEntity victim) {
		if (message == null || message.isEmpty())
			return;
		PartyEntity party = plugin.getPartyManager().getParty(getPartyName());
		String formattedMessage = PartiesUtils.convertAllPlaceholders(message, party, victim);
		send(formattedMessage);
	}
	public void sendMessage(String message, PartyEntity party) {
		if (message == null || message.isEmpty())
			return;
		
		String formattedMessage = PartiesUtils.convertAllPlaceholders(message, party, this);
		send(formattedMessage);
	}
	
	
	private void send(String message) {
		Player player = getPlayer();
		if (player != null) {
			if (JSONHandler.isJSON(message))
				JSONHandler.sendJSON(message, player);
			else
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}
}
