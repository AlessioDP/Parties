package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.api.interfaces.Rank;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public abstract class PartyPlayerImpl implements PartyPlayer {
	protected PartiesPlugin plugin;
	
	// Interface fields
	@Getter @Setter private UUID playerUUID;
	@Getter @Setter private String name;
	@Getter @Setter private long nameTimestamp;
	@Getter @Setter private int rank;
	@Getter @Setter private String partyName;
	@Getter @Setter private boolean spy;
	@Getter @Setter private boolean muted;
	
	@Getter @Setter private UUID createID;
	@Getter @Setter private boolean chatParty;
	@Getter @Setter private String lastInvite;
	@Getter private HashSet<String> ignoredParties;
	
	protected PartyPlayerImpl(PartiesPlugin instance, UUID uuid) {
		plugin = instance;
		
		playerUUID = uuid;
		name = plugin.getOfflinePlayer(uuid).getName();
		if (name == null)
			name = "";
		nameTimestamp = System.currentTimeMillis() / 1000L;
		rank = ConfigParties.RANK_SET_DEFAULT;
		partyName = "";
		spy = false;
		muted = false;
		
		createID = UUID.randomUUID();
		chatParty = false;
		lastInvite = "";
		ignoredParties = new HashSet<>();
		
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
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_CLEANUP
				.replace("{player}", getName()), true);
		setRank(ConfigParties.RANK_SET_DEFAULT);
		setPartyName("");
		setChatParty(false);
		
		if (saveDB) {
			updatePlayer();
		}
	}
	
	public List<PartiesCommand> getAllowedCommands() {
		List<PartiesCommand> ret = new ArrayList<>();
		Rank rank = plugin.getRankManager().searchRankByLevel(getRank());
		User player = plugin.getPlayer(getPlayerUUID());
		
		if (player.hasPermission(PartiesPermission.HELP.toString()))
			ret.add(CommonCommands.HELP);
		
		if (!getPartyName().isEmpty()) {
			// In party
			if (player.hasPermission(PartiesPermission.SENDMESSAGE.toString()) && rank.havePermission(PartiesPermission.PRIVATE_SENDMESSAGE.toString()))
				ret.add(CommonCommands.P);
			
			// Common commands
			if (player.hasPermission(PartiesPermission.LEAVE.toString()))
				ret.add(CommonCommands.LEAVE);
			if (player.hasPermission(PartiesPermission.INVITE.toString()) && rank.havePermission(PartiesPermission.PRIVATE_INVITE.toString()))
				ret.add(CommonCommands.INVITE);
			if (player.hasPermission(PartiesPermission.INFO.toString()))
				ret.add(CommonCommands.INFO);
			
			// Other commands
			if (ConfigParties.GENERAL_CHAT_TOGGLECHATCMD && player.hasPermission(PartiesPermission.CHAT.toString()))
				ret.add(CommonCommands.CHAT);
			
			// Admin commands
			if (ConfigParties.DESC_ENABLE && player.hasPermission(PartiesPermission.DESC.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_DESC.toString()))
				ret.add(CommonCommands.DESC);
			if (ConfigParties.MOTD_ENABLE && player.hasPermission(PartiesPermission.MOTD.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_MOTD.toString()))
				ret.add(CommonCommands.MOTD);
			if (ConfigParties.COLOR_ENABLE && ConfigParties.COLOR_COLORCMD && player.hasPermission(PartiesPermission.COLOR.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_COLOR.toString()))
				ret.add(CommonCommands.COLOR);
			if (ConfigParties.PASSWORD_ENABLE && player.hasPermission(PartiesPermission.PASSWORD.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_PASSWORD.toString()))
				ret.add(CommonCommands.PASSWORD);
			if (player.hasPermission(PartiesPermission.RANK.toString()) && rank.havePermission(PartiesPermission.PRIVATE_ADMIN_RANK.toString()))
				ret.add(CommonCommands.RANK);
			else if (player.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS.toString())
					|| (player.hasPermission(PartiesPermission.RENAME.toString()) && rank.havePermission(PartiesPermission.PRIVATE_ADMIN_RENAME.toString())))
				ret.add(CommonCommands.RENAME);
			if (player.hasPermission(PartiesPermission.KICK.toString()) && rank.havePermission(PartiesPermission.PRIVATE_KICK.toString()))
				ret.add(CommonCommands.KICK);
			if (ConfigParties.TELEPORT_ENABLE && player.hasPermission(PartiesPermission.TELEPORT.toString()) && rank.havePermission(PartiesPermission.PRIVATE_ADMIN_TELEPORT.toString()))
				ret.add(CommonCommands.TELEPORT);
		} else {
			// Out of party
			if (player.hasPermission(PartiesPermission.CREATE.toString())) {
				ret.add(CommonCommands.CREATE);
			}
			if (player.hasPermission(PartiesPermission.ACCEPT.toString()))
				ret.add(CommonCommands.ACCEPT);
			if (player.hasPermission(PartiesPermission.DENY.toString()))
				ret.add(CommonCommands.DENY);
			if (ConfigParties.PASSWORD_ENABLE && player.hasPermission(PartiesPermission.JOIN.toString()))
				ret.add(CommonCommands.JOIN);
			if (player.hasPermission(PartiesPermission.IGNORE.toString()))
				ret.add(CommonCommands.IGNORE);
			if (player.hasPermission(PartiesPermission.INFO_OTHERS.toString()))
				ret.add(CommonCommands.IGNORE);
			if (player.hasPermission(PartiesPermission.MUTE.toString()))
				ret.add(CommonCommands.MUTE);
			if (player.hasPermission(PartiesPermission.ADMIN_KICK_OTHERS.toString()))
				ret.add(CommonCommands.KICK);
			if (player.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS.toString()))
				ret.add(CommonCommands.RENAME);
		}
		if (ConfigParties.LIST_ENABLE && player.hasPermission(PartiesPermission.LIST.toString()))
			ret.add(CommonCommands.LIST);
		if (player.hasPermission(PartiesPermission.ADMIN_SPY.toString()))
			ret.add(CommonCommands.SPY);
		if (player.hasPermission(PartiesPermission.ADMIN_DELETE.toString()))
			ret.add(CommonCommands.DELETE);
		if (player.hasPermission(PartiesPermission.ADMIN_RELOAD.toString()))
			ret.add(CommonCommands.RELOAD);
		if (player.hasPermission(PartiesPermission.ADMIN_MIGRATE.toString()) && !ConfigMain.STORAGE_MIGRATE_ONLYCONSOLE)
			ret.add(CommonCommands.MIGRATE);
		
		return ret;
	}
	
	public void compareName(String serverName) {
		if (getName() != null) {
			if (!getName().equals(serverName)) {
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_COMPARENAME_CHANGE
						.replace("{new}", serverName)
						.replace("{old}", getName())
						.replace("{uuid}", getPlayerUUID().toString()), true);
				setName(serverName);
				updatePlayer();
			}
		} else if(!serverName.isEmpty()) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PLAYER_COMPARENAME_NOTFOUND
					.replace("{uuid}", getPlayerUUID().toString())
					.replace("{player}", serverName), true);
			setName(serverName);
		}
	}
	
	public boolean isVanished() {
		// Handled by Bukkit
		return false;
	}
	
	/*
	 * Send Message
	 */
	public void sendNoPermission(PartiesPermission perm) {
		sendMessage(Messages.PARTIES_PERM_NOPERM
				.replace("%permission%", perm.toString()));
	}
	
	public void sendMessage(String message) {
		sendMessage(message, this);
	}
	public void sendMessage(String message, PartyPlayerImpl victim) {
		if (message == null || message.isEmpty())
			return;
		PartyImpl party = plugin.getPartyManager().getParty(getPartyName());
		String formattedMessage = plugin.getMessageUtils().convertAllPlaceholders(message, party, victim);
		formattedMessage = plugin.getMessageUtils().convertColors(formattedMessage);
		sendDirect(formattedMessage);
	}
	public void sendMessage(String message, PartyImpl party) {
		if (message == null || message.isEmpty())
			return;
		
		String formattedMessage = plugin.getMessageUtils().convertAllPlaceholders(message, party, this);
		formattedMessage = plugin.getMessageUtils().convertColors(formattedMessage);
		sendDirect(formattedMessage);
	}
	
	
	public void sendDirect(String message) {
		User player = plugin.getPlayer(getPlayerUUID());
		if (player != null) {
			player.sendMessage(message, false);
		}
	}
}
