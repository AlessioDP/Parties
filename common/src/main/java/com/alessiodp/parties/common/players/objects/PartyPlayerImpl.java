package com.alessiodp.parties.common.players.objects;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.api.interfaces.Rank;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.players.spy.SpyMessage;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public abstract class PartyPlayerImpl implements PartyPlayer {
	protected final PartiesPlugin plugin;
	
	// Interface fields
	@Getter private UUID playerUUID;
	@Getter private String name;
	@Getter private int rank;
	@Getter private String partyName;
	@Getter private boolean spy;
	@Getter private boolean muted;
	
	// Plugin fields
	@Getter private UUID createID;
	@Getter private boolean chatParty;
	@Getter private HashMap<PartyImpl, UUID> partyInvites;
	@Getter private HashSet<String> ignoredParties;
	protected final ReentrantLock lock = new ReentrantLock();
	
	protected PartyPlayerImpl(@NonNull PartiesPlugin plugin, @NonNull UUID uuid) {
		this.plugin = plugin;
		
		playerUUID = uuid;
		name = plugin.getOfflinePlayer(uuid).getName();
		if (name == null || name.isEmpty())
			name = LLAPIHandler.getPlayerName(playerUUID); // Use LastLoginAPI to get the name
		rank = ConfigParties.RANK_SET_DEFAULT;
		partyName = "";
		spy = false;
		muted = false;
		
		createID = UUID.randomUUID();
		chatParty = false;
		partyInvites = new HashMap<>();
		ignoredParties = new HashSet<>();
	}
	
	protected PartyPlayerImpl(@NonNull PartiesPlugin plugin, @NonNull UUID uuid, String name, int rank, String partyName, boolean spy, boolean muted) {
		this.plugin = plugin;
		
		playerUUID = uuid;
		this.name = name;
		this.rank = rank;
		this.partyName = partyName;
		this.spy = spy;
		this.muted = muted;
		
		createID = UUID.randomUUID();
		chatParty = false;
		partyInvites = new HashMap<>();
		ignoredParties = new HashSet<>();
	}
	
	/**
	 * Get the data from the database
	 */
	public void fromDatabase(String partyName, int rank, boolean spy, boolean mute) {
		lock.lock();
		this.partyName = partyName;
		this.rank = rank;
		this.spy = spy;
		this.muted = mute;
		lock.unlock();
	}
	
	/**
	 * Update the player
	 */
	public void updatePlayer() {
		plugin.getDatabaseManager().updatePlayer(this);
	}
	
	/**
	 * Add into party
	 */
	public void addIntoParty(String partyName, int rank) {
		lock.lock();
		this.partyName = partyName;
		this.rank = rank;
		partyInvites.clear();
		updatePlayer();
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_PARTY_JOIN
				.replace("{player}", getName())
				.replace("{uuid}", getPlayerUUID().toString())
				.replace("{party}", getPartyName()), true);
	}
	
	/**
	 * Remove from party
	 */
	public void removeFromParty(boolean saveDB) {
		lock.lock();
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_CLEANUP
				.replace("{player}", getName()), true);
		rank = ConfigParties.RANK_SET_DEFAULT;
		partyName = "";
		chatParty = false;
		
		if (saveDB) {
			updatePlayer();
		}
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PLAYER_PARTY_LEAVE
				.replace("{player}", getName())
				.replace("{uuid}", getPlayerUUID().toString())
				.replace("{party}", getPartyName()), true);
	}
	
	@Override
	public void setPartyName(@org.checkerframework.checker.nullness.qual.NonNull String partyName) {
		lock.lock();
		this.partyName = partyName;
		updatePlayer();
		lock.unlock();
	}
	
	@Override
	public void setRank(int rank) {
		lock.lock();
		this.rank = rank;
		updatePlayer();
		lock.unlock();
	}
	
	@Override
	public void setSpy(boolean spy) {
		lock.lock();
		this.spy = spy;
		updatePlayer();
		lock.unlock();
	}
	
	@Override
	public void setMuted(boolean muted) {
		lock.lock();
		this.muted = muted;
		updatePlayer();
		lock.unlock();
	}
	
	public void setChatParty(boolean chat) {
		lock.lock();
		this.chatParty = chat;
		updatePlayer();
		lock.unlock();
	}
	
	/**
	 * Send a message
	 */
	public void sendMessage(String message) {
		sendMessage(message, this, plugin.getPartyManager().getPartyOfPlayer(this));
	}
	
	/**
	 * Send a message based on victim
	 */
	public void sendMessage(String message, PartyPlayerImpl victim) {
		sendMessage(message, victim, plugin.getPartyManager().getPartyOfPlayer(this));
	}
	
	/**
	 * Send a message based on party
	 */
	public void sendMessage(String message, PartyImpl party) {
		sendMessage(message, this, party);
	}
	
	/**
	 * Send a message based on both victim and party
	 */
	public void sendMessage(String message, PartyPlayerImpl victim, PartyImpl party) {
		User user = plugin.getPlayer(getPlayerUUID());
		plugin.getMessageUtils().sendMessage(user, message, victim, party);
	}
	
	/**
	 * Perform a party message
	 */
	public void performPartyMessage(String message) {
		if (!message.isEmpty()) {
			PartyImpl party = plugin.getPartyManager().getPartyOfPlayer(this);
			if (party != null) {
				String formattedMessage = plugin.getMessageUtils().convertAllPlaceholders(ConfigParties.GENERAL_CHAT_FORMAT_CHAT, party, this);
				String chatMessage = message;
				
				if (ConfigParties.GENERAL_CHAT_ALLOWCOLORS) {
					User user = plugin.getPlayer(getPlayerUUID());
					if (user != null
							&& user.hasPermission(PartiesPermission.CHAT_COLOR.toString())
							&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_SENDMESSAGE_COLOR)) {
						chatMessage = plugin.getColorUtils().convertColors(chatMessage);
					}
				}
				
				formattedMessage = plugin.getColorUtils().convertColors(formattedMessage).replace("%message%", chatMessage);
				
				party.dispatchChatMessage(this, formattedMessage, true);
				
				plugin.getSpyManager().sendSpyMessage(new SpyMessage(plugin)
						.setType(SpyMessage.SpyType.MESSAGE)
						.setMessage(chatMessage)
						.setParty(party)
						.setPlayer(this));
			}
		}
	}
	
	/**
	 * Player allowed commands
	 */
	public List<ADPCommand> getAllowedCommands() {
		List<ADPCommand> ret = new ArrayList<>();
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
			if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE
					&& ConfigMain.ADDITIONAL_FOLLOW_TOGGLECMD
					&& player.hasPermission(PartiesPermission.FOLLOW.toString())
					&& rank.havePermission(PartiesPermission.PRIVATE_EDIT_FOLLOW.toString()))
				ret.add(CommonCommands.FOLLOW);
			if (ConfigParties.COLOR_ENABLE && ConfigParties.COLOR_COLORCMD && player.hasPermission(PartiesPermission.COLOR.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_COLOR.toString()))
				ret.add(CommonCommands.COLOR);
			if (ConfigParties.PASSWORD_ENABLE && player.hasPermission(PartiesPermission.PASSWORD.toString()) && rank.havePermission(PartiesPermission.PRIVATE_EDIT_PASSWORD.toString()))
				ret.add(CommonCommands.PASSWORD);
			if (player.hasPermission(PartiesPermission.RANK.toString()) && rank.havePermission(PartiesPermission.PRIVATE_ADMIN_RANK.toString()))
				ret.add(CommonCommands.RANK);
			if (player.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS.toString())
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
				ret.add(CommonCommands.INFO);
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
		if (player.hasPermission(PartiesPermission.ADMIN_VERSION.toString()))
			ret.add(CommonCommands.VERSION);
		
		return ret;
	}
	
	/**
	 * Is the player invisible?
	 */
	public abstract boolean isVanished();
}
