package com.alessiodp.parties.common.players.objects;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.scheduling.ADPScheduler;
import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyAskRequest;
import com.alessiodp.parties.api.interfaces.PartyInvite;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@EqualsAndHashCode
@ToString
public abstract class PartyPlayerImpl implements PartyPlayer {
	@EqualsAndHashCode.Exclude @ToString.Exclude protected final PartiesPlugin plugin;
	
	// Interface fields
	@Getter private final UUID playerUUID;
	@Getter private String name;
	@Getter private int rank;
	@Getter private UUID partyId;
	@Getter private String nickname;
	@Getter private boolean chatParty;
	@Getter private boolean spy;
	@Getter private boolean muted;
	
	// Plugin fields
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final UUID createID;
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final HashSet<PartyAskRequest> pendingAskRequests;
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final HashSet<PartyInvite> pendingInvites;
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final HashSet<PartyTeleportRequest> pendingTeleportRequests;
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final HashSet<UUID> ignoredParties;
	
	
	@Getter @Setter private CancellableTask pendingHomeDelay;
	@Getter @Setter private CancellableTask pendingTeleportDelay;
	
	@EqualsAndHashCode.Exclude @ToString.Exclude protected boolean accessible = false;
	
	protected PartyPlayerImpl(@NonNull PartiesPlugin plugin, @NonNull UUID uuid) {
		this.plugin = plugin;
		
		playerUUID = uuid;
		name = plugin.getOfflinePlayer(uuid).getName();
		if (name == null || name.isEmpty())
			name = LLAPIHandler.getPlayerName(playerUUID); // Use LastLoginAPI to get the name
		rank = ConfigParties.RANK_SET_DEFAULT;
		partyId = null;
		chatParty = false;
		spy = false;
		muted = false;
		
		createID = UUID.randomUUID();
		pendingAskRequests = new HashSet<>();
		pendingInvites = new HashSet<>();
		pendingTeleportRequests = new HashSet<>();
		ignoredParties = new HashSet<>();
	}
	
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	
	protected void updateValue(Runnable runnable) {
		updateValue(runnable, true);
	}
	
	protected void updateValue(Runnable runnable, boolean saveToDatabase) {
		if (accessible) {
			runnable.run();
		} else {
			synchronized (this) {
				runnable.run();
				
				if (saveToDatabase)
					updatePlayer().thenRun(this::sendPacketUpdate).exceptionally(ADPScheduler.exceptionally());
			}
		}
	}
	
	public boolean isPersistent() {
		return getPartyId() != null || isSpy() || isMuted();
	}
	
	public CompletableFuture<Void> updatePlayer() {
		return plugin.getDatabaseManager().updatePlayer(this);
	}
	
	public void addIntoParty(UUID party, int rank) {
		updateValue(() -> {
			this.partyId = party;
			this.rank = rank;
			this.nickname = null;
			pendingAskRequests.clear();
			pendingInvites.clear();
		});
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_PARTY_JOIN, getName(), getPartyId().toString(), getPlayerUUID().toString()), true);
	}
	
	public void removeFromParty(boolean saveToDatabase) {
		UUID oldPartyId = partyId;
		updateValue(() -> {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_CLEANUP, getName(), getPlayerUUID()), true);
			rank = ConfigParties.RANK_SET_DEFAULT;
			partyId = null;
			nickname = null;
			chatParty = false;
			resetPendingDelays();
		}, saveToDatabase);
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PLAYER_PARTY_LEAVE, getName(), oldPartyId != null ? oldPartyId.toString() : "none", getPlayerUUID().toString()), true);
	}
	
	public void resetPendingDelays() {
		if (pendingHomeDelay != null) {
			pendingHomeDelay.cancel();
			pendingHomeDelay = null;
		}
		if (pendingTeleportDelay != null) {
			pendingTeleportDelay.cancel();
			pendingTeleportDelay = null;
		}
	}
	
	@Override
	public @Nullable String getPartyName() {
		if (isInParty()) {
			Party party = plugin.getPartyManager().getParty(getPartyId());
			if (party != null)
				return party.getName();
		}
		// Will be deprecated! This will return null.
		return "";
	}
	
	@Override
	public PartyAskRequest askToJoin(@org.checkerframework.checker.nullness.qual.NonNull Party party, boolean sendMessages) {
		return ((PartyImpl) party).askToJoin(this, sendMessages);
	}
	
	@Override
	public void setPartyId(@Nullable UUID partyId) {
		updateValue(() -> {
			this.partyId = partyId;
		});
	}
	
	@Override
	public void setRank(int rank) {
		updateValue(() -> {
			this.rank = rank;
		});
	}
	
	@Override
	public void setNickname(String nickname) {
		updateValue(() -> {
			this.nickname = nickname;
		});
	}
	
	@Override
	public void setSpy(boolean spy) {
		updateValue(() -> {
			this.spy = spy;
			if (spy)
				plugin.getPlayerManager().getCacheSpies().add(playerUUID);
			else
				plugin.getPlayerManager().getCacheSpies().remove(playerUUID);
		});
	}
	
	@Override
	public void setMuted(boolean muted) {
		updateValue(() -> {
			this.muted = muted;
		});
	}
	
	public void setChatParty(boolean chatParty) {
		updateValue(() -> {
			this.chatParty = chatParty;
		});
	}
	
	public void sendDirect(String message) {
		User player = plugin.getPlayer(getPlayerUUID());
		if (player != null) {
			player.sendMessage(message, false);
		}
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
	 * Send a title message
	 */
	public void sendTitleMessage(String message) {
		User player = plugin.getPlayer(getPlayerUUID());
		if (player != null) {
			player.sendTitle(message, ConfigParties.GENERAL_BROADCAST_TITLES_FADE_IN_TIME, ConfigParties.GENERAL_BROADCAST_TITLES_SHOW_TIME, ConfigParties.GENERAL_BROADCAST_TITLES_FADE_OUT_TIME);
		}
	}
	
	public abstract void playSound(String sound, double volume, double pitch);
	
	public abstract void playChatSound();
	
	public abstract void playBroadcastSound();
	
	/**
	 * Perform a party message
	 */
	public boolean performPartyMessage(String message) {
		if (!message.isEmpty()) {
			PartyImpl party = plugin.getPartyManager().getPartyOfPlayer(this);
			if (party != null) {
				String formattedMessage = plugin.getMessageUtils().convertPlaceholders(Messages.PARTIES_FORMATS_PARTY_CHAT, this, party);
				String chatMessage = message;
				
				if (ConfigParties.GENERAL_CHAT_ALLOWCOLORS) {
					User user = plugin.getPlayer(getPlayerUUID());
					if (user != null
							&& user.hasPermission(PartiesPermission.USER_CHAT_COLOR)
							&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_SENDMESSAGE_COLOR)) {
						chatMessage = Color.translateAlternateColorCodes(chatMessage);
					}
				}
				
				formattedMessage = Color.translateAlternateColorCodes(formattedMessage);
				if (party.dispatchChatMessage(this, formattedMessage, chatMessage, true)) {
					plugin.getPlayerManager().sendSpyMessage(new SpyMessage(plugin)
							.setType(SpyMessage.SpyType.MESSAGE)
							.setMessage(plugin.getJsonHandler().removeJson(chatMessage))
							.setParty(party)
							.setPlayer(this));
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Player allowed commands
	 */
	public Set<ADPCommand> getAllowedCommands() {
		Set<ADPCommand> ret = new HashSet<>();
		User player = plugin.getPlayer(getPlayerUUID());
		
		if (player.hasPermission(PartiesPermission.USER_HELP))
			ret.add(CommonCommands.HELP);
		
		if (partyId != null) {
			// In party
			if (player.hasPermission(PartiesPermission.USER_SENDMESSAGE)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_SENDMESSAGE))
				ret.add(CommonCommands.P);
			
			// Common commands
			if (player.hasPermission(PartiesPermission.USER_LEAVE))
				ret.add(CommonCommands.LEAVE);
			if (player.hasPermission(PartiesPermission.USER_INVITE)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_INVITE))
				ret.add(CommonCommands.INVITE);
			if (player.hasPermission(PartiesPermission.USER_INFO))
				ret.add(CommonCommands.INFO);
			
			// Other commands
			if (ConfigParties.GENERAL_CHAT_TOGGLECOMMAND
					&& player.hasPermission(PartiesPermission.USER_CHAT))
				ret.add(CommonCommands.CHAT);
			if (ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE
					&& ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE.equalsIgnoreCase("command")
					&& player.hasPermission(PartiesPermission.USER_PROTECTION)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_PROTECTION))
				ret.add(CommonCommands.PROTECTION);
			if (ConfigParties.ADDITIONAL_HOME_ENABLE) {
				if (player.hasPermission(PartiesPermission.ADMIN_HOME_OTHERS)
						|| (player.hasPermission(PartiesPermission.USER_HOME) && plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_HOME)))
					ret.add(CommonCommands.HOME);
				if (player.hasPermission(PartiesPermission.USER_SETHOME) && plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_HOME))
					ret.add(CommonCommands.SETHOME);
			}
			
			// Admin commands
			if (ConfigParties.ADDITIONAL_ASK_ENABLE
					|| (ConfigParties.ADDITIONAL_TELEPORT_ENABLE && ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE)) {
				if (player.hasPermission(PartiesPermission.USER_ACCEPT)
						&& (plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_ASK_ACCEPT) || plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_TELEPORT_ACCEPT)))
					ret.add(CommonCommands.ACCEPT);
				if (player.hasPermission(PartiesPermission.USER_DENY)
						&& (plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_ASK_DENY) || plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_TELEPORT_DENY)))
					ret.add(CommonCommands.DENY);
			}
			if (ConfigParties.ADDITIONAL_DESC_ENABLE
					&& player.hasPermission(PartiesPermission.USER_DESC)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_DESC))
				ret.add(CommonCommands.DESC);
			if (ConfigParties.ADDITIONAL_MOTD_ENABLE
					&& player.hasPermission(PartiesPermission.USER_MOTD)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_MOTD))
				ret.add(CommonCommands.MOTD);
			if (ConfigParties.ADDITIONAL_NICKNAME_ENABLE
					&& player.hasPermission(PartiesPermission.USER_NICKNAME)
					&& (plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_NICKNAME_OWN) || plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_NICKNAME_OTHERS)))
				ret.add(CommonCommands.NICKNAME);
			if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE
					&& ConfigMain.ADDITIONAL_FOLLOW_TOGGLECMD
					&& player.hasPermission(PartiesPermission.USER_FOLLOW)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_FOLLOW))
				ret.add(CommonCommands.FOLLOW);
			if (ConfigParties.ADDITIONAL_COLOR_ENABLE
					&& ConfigParties.ADDITIONAL_COLOR_COLORCMD
					&& player.hasPermission(PartiesPermission.USER_COLOR)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_COLOR))
				ret.add(CommonCommands.COLOR);
			if (ConfigParties.ADDITIONAL_JOIN_ENABLE
					&& ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE
					&& player.hasPermission(PartiesPermission.USER_PASSWORD)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_PASSWORD))
				ret.add(CommonCommands.PASSWORD);
			if (player.hasPermission(PartiesPermission.USER_RANK)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_ADMIN_RANK))
				ret.add(CommonCommands.RANK);
			if (player.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS)
					|| (player.hasPermission(PartiesPermission.USER_RENAME) && plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_ADMIN_RENAME)))
				ret.add(CommonCommands.RENAME);
			if (ConfigParties.ADDITIONAL_TAG_ENABLE && (player.hasPermission(PartiesPermission.USER_TAG) && plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_EDIT_TAG))
					|| player.hasPermission(PartiesPermission.ADMIN_TAG_OTHERS))
				ret.add(CommonCommands.TAG);
			if (player.hasPermission(PartiesPermission.USER_KICK)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_KICK))
				ret.add(CommonCommands.KICK);
			if (ConfigParties.ADDITIONAL_TELEPORT_ENABLE
					&& player.hasPermission(PartiesPermission.USER_TELEPORT)
					&& plugin.getRankManager().checkPlayerRank(this, PartiesPermission.PRIVATE_ADMIN_TELEPORT))
				ret.add(CommonCommands.TELEPORT);
		} else {
			// Out of party
			if (player.hasPermission(PartiesPermission.USER_CREATE))
				ret.add(CommonCommands.CREATE);
			if (ConfigParties.GENERAL_INVITE_AUTO_CREATE_PARTY_UPON_INVITE
					&& player.hasPermission(PartiesPermission.USER_INVITE)
					&& player.hasPermission(PartiesPermission.USER_CREATE))
				ret.add(CommonCommands.INVITE);
			if (player.hasPermission(PartiesPermission.USER_ACCEPT))
				ret.add(CommonCommands.ACCEPT);
			if (player.hasPermission(PartiesPermission.USER_DENY))
				ret.add(CommonCommands.DENY);
			if (ConfigParties.ADDITIONAL_ASK_ENABLE
					&& player.hasPermission(PartiesPermission.USER_ASK))
				ret.add(CommonCommands.ASK);
			if (ConfigParties.ADDITIONAL_JOIN_ENABLE
					&& player.hasPermission(PartiesPermission.USER_JOIN))
				ret.add(CommonCommands.JOIN);
			if (ConfigParties.ADDITIONAL_TAG_ENABLE
					&& player.hasPermission(PartiesPermission.ADMIN_TAG_OTHERS))
				ret.add(CommonCommands.TAG);
			if (ConfigParties.ADDITIONAL_NICKNAME_ENABLE
					&& player.hasPermission(PartiesPermission.ADMIN_NICKNAME_OTHERS))
				ret.add(CommonCommands.NICKNAME);
			if (player.hasPermission(PartiesPermission.USER_IGNORE))
				ret.add(CommonCommands.IGNORE);
			if (player.hasPermission(PartiesPermission.USER_INFO_OTHERS))
				ret.add(CommonCommands.INFO);
			if (player.hasPermission(PartiesPermission.USER_MUTE))
				ret.add(CommonCommands.MUTE);
			if (player.hasPermission(PartiesPermission.ADMIN_KICK_OTHERS))
				ret.add(CommonCommands.KICK);
			if (player.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS))
				ret.add(CommonCommands.RENAME);
			
		}
		if (ConfigParties.ADDITIONAL_FIXED_ENABLE
				&& player.hasPermission(PartiesPermission.ADMIN_CREATE_FIXED))
			ret.add(CommonCommands.CREATEFIXED);
		if (ConfigParties.ADDITIONAL_LIST_ENABLE
				&& player.hasPermission(PartiesPermission.USER_LIST))
			ret.add(CommonCommands.LIST);
		if (player.hasPermission(PartiesPermission.ADMIN_SPY))
			ret.add(CommonCommands.SPY);
		if (player.hasPermission(PartiesPermission.ADMIN_DELETE))
			ret.add(CommonCommands.DELETE);
		if (player.hasPermission(PartiesPermission.ADMIN_RELOAD))
			ret.add(CommonCommands.RELOAD);
		if (player.hasPermission(PartiesPermission.ADMIN_VERSION))
			ret.add(CommonCommands.VERSION);
		if (ConfigMain.PARTIES_DEBUG_COMMAND
				&& player.hasPermission(PartiesPermission.ADMIN_DEBUG))
			ret.add(CommonCommands.DEBUG);
		
		return ret;
	}
	
	public abstract void sendPacketUpdate();
	
	/**
	 * Is the player invisible?
	 */
	public abstract boolean isVanished();
	
	/**
	 * Is the player chat muted?
	 *
	 * @return Returns true if the player chat is muted
	 */
	public boolean isChatMuted() {
		return false;
	}
}
