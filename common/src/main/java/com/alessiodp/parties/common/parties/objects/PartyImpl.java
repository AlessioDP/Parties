package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.core.common.bootstrap.PluginPlatform;
import com.alessiodp.core.common.scheduling.ADPScheduler;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.enums.LeaveCause;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPostRenameEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostInviteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreChatEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreInviteEvent;
import com.alessiodp.parties.api.interfaces.PartyAskRequest;
import com.alessiodp.parties.api.interfaces.PartyColor;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyInvite;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyAskRequestImpl;
import com.alessiodp.parties.common.players.objects.PartyInviteImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.SpyMessage;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.utils.PasswordUtils;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@EqualsAndHashCode
@ToString
public abstract class PartyImpl implements Party {
	@EqualsAndHashCode.Exclude @ToString.Exclude protected final PartiesPlugin plugin;
	
	// Interface fields
	@Getter private final UUID id;
	@Nullable @Getter private String name;
	@Nullable @Getter private String tag;
	@Getter private Set<UUID> members;
	@Nullable @Getter private UUID leader;
	@Nullable @Getter private String description;
	@Nullable @Getter private String motd;
	@Getter private Set<PartyHome> homes;
	@Nullable @Getter private PartyColor color;
	@Getter private int kills;
	@Nullable @Getter private String password;
	private boolean protection;
	@Getter private double experience;
	private boolean followEnabled;
	
	// Plugin fields
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter @Setter private PartyColor dynamicColor;
	@EqualsAndHashCode.Exclude @ToString.Exclude protected ExpResult expResult;
	@EqualsAndHashCode.Exclude @ToString.Exclude protected double cacheExperience;
	@EqualsAndHashCode.Exclude @ToString.Exclude private final HashSet<PartyPlayer> onlineMembers;
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final HashSet<PartyAskRequest> askRequests;
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final HashSet<PartyInvite> inviteRequests;
	
	@EqualsAndHashCode.Exclude @ToString.Exclude protected boolean accessible = false;
	
	protected PartyImpl(@NonNull PartiesPlugin plugin, @NonNull UUID id) {
		this.plugin = plugin;
		
		this.id = id;
		name = null;
		tag = null;
		members = new HashSet<>();
		leader = null;
		description = null;
		motd = null;
		homes = new HashSet<>();
		color = null;
		kills = 0;
		password = null;
		protection = false;
		experience = 0;
		followEnabled = false;
		
		expResult = new ExpResult();
		cacheExperience = -1;
		onlineMembers = new HashSet<>();
		askRequests = new HashSet<>();
		inviteRequests = new HashSet<>();
	}
	
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	
	protected void updateValue(Runnable runnable) {
		updateValue(runnable, null);
	}
	
	protected void updateValue(Runnable runnable, Runnable bungeecordSync) {
		if (accessible) {
			runnable.run();
		} else {
			synchronized (this) {
				runnable.run();
				
				updateParty().thenRun(this::sendPacketUpdate).exceptionally(ADPScheduler.exceptionally());
			}
		}
	}
	
	/**
	 * Update the party
	 */
	public CompletableFuture<Void> updateParty() {
		return plugin.getDatabaseManager().updateParty(this);
	}
	
	/**
	 * Setup existing party
	 *
	 * @param name the name of the party
	 * @param leader the leader of the party, null if fixed
	 */
	public void setup(@Nullable String name, @Nullable String leader) throws IllegalArgumentException {
		synchronized (this) {
			this.name = name;
			if (leader == null || leader.isEmpty() || leader.equals("fixed") || leader.equals("00000000-0000-0000-0000-000000000000")) {
				this.leader = null;
			} else {
				this.leader = UUID.fromString(leader);
			}
		}
	}
	
	/**
	 * Create the party
	 *
	 * @param name the name of the party
	 * @param leader the leader of the party, null if fixed
	 * @param creator who is creating the party
	 */
	public void create(@Nullable String name, @Nullable PartyPlayerImpl leader, @Nullable PartyPlayerImpl creator) {
		CompletableFuture<Void> futureAfterUpdate;
		synchronized (this) {
			this.name = name;
			
			if (leader == null) {
				// Fixed
				this.leader = null;
			} else {
				// Normal
				this.leader = leader.getPlayerUUID();
				members.add(leader.getPlayerUUID());
				
				if (plugin.getOfflinePlayer(leader.getPlayerUUID()).isOnline())
					onlineMembers.add(leader);
				
				// Update player
				leader.addIntoParty(id, ConfigParties.RANK_SET_HIGHER);
				
			}
			futureAfterUpdate = updateParty();
			
			plugin.getPartyManager().addPartyToCache(this);
		}
		
		// Send sync packet + event
		futureAfterUpdate.thenRun(() -> sendPacketCreate(creator)).exceptionally(ADPScheduler.exceptionally());
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_CREATE, getName() != null ? getName() : "_"), true);
	}
	
	@Override
	public void delete() {
		delete(DeleteCause.OTHERS, null, null);
	}
	
	public void delete(DeleteCause cause, PartyPlayerImpl kicked, PartyPlayerImpl commandSender) {
		synchronized (this) {
			plugin.getPartyManager().removePartyFromCache(this); // Remove from cache
			plugin.getDatabaseManager().removeParty(this); // Remove from database
			
			for (UUID uuid : getMembers()) {
				PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(uuid);
				pp.removeFromParty(true);
			}
		}
		
		// Start cooldown on leave
		if (ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE && (ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_GLOBAL > 0 || ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_INDIVIDUAL > 0)) {
			plugin.getCooldownManager().startInviteAfterLeave(kicked.getPlayerUUID(), getId(), ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_GLOBAL);
			plugin.getCooldownManager().startInviteAfterLeave(kicked.getPlayerUUID(), getId(), ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_INDIVIDUAL);
		}
		
		// Send sync packet + event
		plugin.getScheduler().runAsync(() -> {
			sendPacketDelete(cause, kicked, commandSender);
		});
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_DELETE, getName() != null ? getName() : "_"), true);
	}
	
	@Override
	public void rename(@Nullable String newName) {
		rename(newName, null, true);
	}
	
	public void rename(@Nullable String newName, PartyPlayerImpl player, boolean isAdmin) {
		CompletableFuture<Void> futureAfterUpdate;
		String oldName = getName();
		synchronized (this) {
			plugin.getPartyManager().removePartyFromCache(this); // Remove from cache
			
			this.name = newName;
			
			plugin.getPartyManager().addPartyToCache(this); // Insert into online list
			
			futureAfterUpdate = updateParty();
		}
		
		// Send sync packet + event
		futureAfterUpdate.thenRun(() -> sendPacketRename(oldName, getName(), player, isAdmin)).exceptionally(ADPScheduler.exceptionally());
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_RENAME, oldName, getName() != null ? getName() : "_"), true);
	}
	
	@Override
	public boolean addMember(@org.checkerframework.checker.nullness.qual.NonNull PartyPlayer partyPlayer) {
		return addMember(partyPlayer, JoinCause.OTHERS, null);
	}
	
	public boolean addMember(@org.checkerframework.checker.nullness.qual.NonNull PartyPlayer partyPlayer, JoinCause cause, PartyPlayerImpl inviter) {
		boolean ret = false;
		CompletableFuture<Void> futureAfterUpdate = null;
		synchronized (this) {
			if (!isFull() && !members.contains(partyPlayer.getPlayerUUID())) {
				members.add(partyPlayer.getPlayerUUID());
				onlineMembers.add(partyPlayer);
				
				((PartyPlayerImpl) partyPlayer).addIntoParty(id, ConfigParties.RANK_SET_DEFAULT);
				
				futureAfterUpdate = updateParty();
				ret = true;
			}
		}
		
		if (ret) {
			// Send sync packet + event
			futureAfterUpdate.thenRun(() -> sendPacketAddMember((PartyPlayerImpl) partyPlayer, cause, inviter)).exceptionally(ADPScheduler.exceptionally());
		}
		return ret;
	}
	
	@Override
	public boolean removeMember(@org.checkerframework.checker.nullness.qual.NonNull PartyPlayer partyPlayer) {
		return removeMember(partyPlayer, LeaveCause.OTHERS, null);
	}
	
	public boolean removeMember(@org.checkerframework.checker.nullness.qual.NonNull PartyPlayer partyPlayer, LeaveCause cause, PartyPlayer kicker) {
		boolean ret = false;
		CompletableFuture<Void> future = null;
		synchronized (this) {
			if (members.contains(partyPlayer.getPlayerUUID())) {
				members.remove(partyPlayer.getPlayerUUID());
				onlineMembers.remove(partyPlayer);
				
				((PartyPlayerImpl) partyPlayer).removeFromParty(true);
				
				future = updateParty();
				ret = true;
			}
		}
		
		// Start cooldown on leave
		if (ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE && (ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_GLOBAL > 0 || ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_INDIVIDUAL > 0)) {
			plugin.getCooldownManager().startInviteAfterLeave(partyPlayer.getPlayerUUID(), null, ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_GLOBAL);
			plugin.getCooldownManager().startInviteAfterLeave(partyPlayer.getPlayerUUID(), getId(), ConfigParties.GENERAL_INVITE_COOLDOWN_ON_LEAVE_INDIVIDUAL);
		}
		
		if (ret) {
			// Send sync packet + event
			future.thenRun(() -> sendPacketRemoveMember((PartyPlayerImpl) partyPlayer, cause, (PartyPlayerImpl) kicker)).exceptionally(ADPScheduler.exceptionally());
		}
		return ret;
	}
	
	/**
	 * Set members field
	 *
	 * @param members the members to set
	 */
	public void setMembers(@NonNull Set<UUID> members) {
		updateValue(() -> {
			this.members = new HashSet<>(members); // Avoid immutable sets from db
		});
	}
	
	@org.checkerframework.checker.nullness.qual.NonNull
	@Override
	public Set<PartyPlayer> getOnlineMembers(boolean bypassVanish) {
		HashSet<PartyPlayer> ret = new HashSet<>();
		synchronized (this) {
			try {
				for (PartyPlayer player : onlineMembers) {
					if (bypassVanish || !((PartyPlayerImpl) player).isVanished())
						ret.add(player);
				}
			} catch (ConcurrentModificationException ex) {
				// Avoiding ConcurrentModificationException if something edits the hashmap
				ex.printStackTrace();
			}
		}
		return Collections.unmodifiableSet(ret);
	}
	
	@Override
	public void changeLeader(@org.checkerframework.checker.nullness.qual.NonNull PartyPlayer leaderPartyPlayer) {
		updateValue(() -> {
			UUID oldLeader = this.leader;
			this.leader = leaderPartyPlayer.getPlayerUUID();
			
			plugin.getPlayerManager().getPlayer(oldLeader).setRank(leaderPartyPlayer.getRank());
			leaderPartyPlayer.setRank(ConfigParties.RANK_SET_HIGHER);
		});
	}
	
	public PartyPlayerImpl findNewLeader() {
		PartyPlayerImpl ret = null;
		for (UUID u : getMembers()) {
			if (!u.equals(getLeader())) {
				PartyPlayerImpl p = plugin.getPlayerManager().getPlayer(u);
				if (p != null && (ret == null || p.getRank() > ret.getRank())) {
					ret = p;
				}
			}
		}
		return ret;
	}
	
	@Override
	public boolean isFixed() {
		return leader == null;
	}
	
	@Override
	public void setFixed(boolean fixed, @Nullable PartyPlayer newLeader) {
		if (fixed != isFixed()) {
			updateValue(() -> {
				if (fixed) {
					UUID oldLeader = this.leader;
					this.leader = null;
					
					plugin.getPlayerManager().getPlayer(oldLeader).setRank(ConfigParties.RANK_SET_DEFAULT);
				} else if (newLeader != null) {
					this.leader = newLeader.getPlayerUUID();
					
					newLeader.setRank(ConfigParties.RANK_SET_HIGHER);
				}
			});
		}
	}
	
	@Override
	public void setTag(@Nullable String tag) {
		updateValue(() -> {
			this.tag = tag;
		});
	}
	
	@Override
	public void setDescription(@Nullable String description) {
		updateValue(() -> {
			this.description = description;
		});
	}
	
	@Override
	public void setMotd(@Nullable String motd) {
		updateValue(() -> {
			this.motd = motd;
		});
	}
	
	@Override
	public void setHomes(@Nullable Set<PartyHome> homes) {
		updateValue(() -> {
			this.homes = homes;
		});
	}
	
	@Override
	public void setColor(@Nullable PartyColor color) {
		updateValue(() -> {
			this.color = color;
		});
	}
	
	@Override
	public void setKills(int kills) {
		updateValue(() -> {
			this.kills = kills;
		});
	}
	
	@Override
	public void setPassword(@Nullable String password) {
		updateValue(() -> {
			this.password = password;
		});
	}
	
	@Override
	public boolean setPasswordUnhashed(@Nullable String password) {
		if (PasswordUtils.isValid(password)) {
			setPassword(PasswordUtils.hashText(password));
			return true;
		}
		return false;
	}
	
	@Override
	public void setProtection(boolean protection) {
		updateValue(() -> {
			this.protection = protection;
		});
	}
	
	@Override
	public void setExperience(double experience) {
		updateValue(() -> {
			this.experience = experience;
			
			calculateLevels();
		});
	}
	
	@Override
	public boolean isFollowEnabled() {
		boolean ret = false;
		if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE) {
			if (ConfigMain.ADDITIONAL_FOLLOW_TOGGLECMD) {
				ret = followEnabled;
			} else {
				ret = true;
			}
		}
		return ret;
	}
	
	@Override
	public void setFollowEnabled(boolean follow) {
		updateValue(() -> {
			this.followEnabled = follow;
		});
	}
	
	@Override
	public int getLevel() {
		if (expResult == null)
			calculateLevels();
		return expResult != null ? expResult.getLevel() : 1;
	}
	
	@Override
	public double getLevelExperience() {
		if (expResult == null)
			calculateLevels();
		return expResult != null ? expResult.getLevelExperience() : 0;
	}
	
	@Override
	public double getLevelUpCurrent() {
		if (expResult == null)
			calculateLevels();
		return expResult != null ? expResult.getLevelUpCurrent() : 0;
	}
	
	@Override
	public double getLevelUpNecessary() {
		if (expResult == null)
			calculateLevels();
		return expResult != null ? expResult.getLevelUpNecessary() : 0;
	}
	
	@Override
	public boolean getProtection() {
		return protection;
	}
	
	@Override
	public boolean isFriendlyFireProtected() {
		boolean ret = false;
		if (ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE) {
			if (ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE.equalsIgnoreCase("command")) {
				// Command
				ret = getProtection();
			} else {
				// Global
				ret = true;
			}
		}
		return ret;
	}
	
	
	@Override
	public void broadcastMessage(@Nullable String message, @Nullable PartyPlayer partyPlayer) {
		String formattedMessage = parseBroadcastMessage(message, partyPlayer);
		if (formattedMessage != null) {
			broadcastDirectMessage(formattedMessage, true);
		}
	}
	
	public void broadcastMessageOnlyTo(@Nullable String message, @Nullable PartyPlayer partyPlayer, @NonNull PartiesPermission... partyRanks) {
		String formattedMessage = parseBroadcastMessage(message, partyPlayer);
		if (formattedMessage != null) {
			for (PartyPlayer player : getOnlineMembers(true)) {
				boolean haveRankPermission = false;
				for (PartiesPermission partyRank : partyRanks) {
					if (plugin.getRankManager().checkPlayerRank((PartyPlayerImpl) player, partyRank)) {
						haveRankPermission = true;
						break;
					}
				}
				if (haveRankPermission) {
					((PartyPlayerImpl) player).sendMessage(message, (PartyPlayerImpl) partyPlayer, this);
				}
			}
			
			plugin.getPlayerManager().sendSpyMessage(new SpyMessage(plugin)
					.setType(SpyMessage.SpyType.BROADCAST)
					.setMessage(plugin.getJsonHandler().removeJson(message))
					.setParty(this)
					.setPlayer(null));
		}
	}
	
	private String parseBroadcastMessage(@Nullable String message, @Nullable PartyPlayer partyPlayer) {
		String ret = null;
		if (message != null && !message.isEmpty()) {
			ret = Color.translateAlternateColorCodes(
					plugin.getMessageUtils().convertPlaceholders(message, (PartyPlayerImpl) partyPlayer, this)
			);
		}
		return ret;
	}
	
	/**
	 * Send a broadcast message to the party without format it.
	 *
	 * @param dispatchBetweenServers Is used by sub classes
	 */
	public void broadcastDirectMessage(String message, boolean dispatchBetweenServers) {
		if (message == null || message.isEmpty())
			return;
		
		for (PartyPlayer player : getOnlineMembers(true)) {
			if (ConfigParties.GENERAL_BROADCAST_TITLES_ENABLE)
				((PartyPlayerImpl) player).sendTitleMessage(message);
			
			if (!ConfigParties.GENERAL_BROADCAST_TITLES_ENABLE || ConfigParties.GENERAL_BROADCAST_TITLES_SEND_NORMAL_MESSAGE)
				((PartyPlayerImpl) player).sendDirect(message);
			
			((PartyPlayerImpl) player).playBroadcastSound();
		}
		
		plugin.getPlayerManager().sendSpyMessage(new SpyMessage(plugin)
				.setType(SpyMessage.SpyType.BROADCAST)
				.setMessage(plugin.getJsonHandler().removeJson(message))
				.setParty(this)
				.setPlayer(null));
	}
	
	public boolean dispatchChatMessage(PartyPlayerImpl sender, String formattedMessage, String chatMessage, boolean dispatchBetweenServers) {
		if (formattedMessage != null && !formattedMessage.isEmpty() && chatMessage != null && !chatMessage.isEmpty()) {
			IPlayerPreChatEvent partiesPreChatEvent = plugin.getEventManager().preparePlayerPreChatEvent(sender, this, chatMessage);
			plugin.getEventManager().callEvent(partiesPreChatEvent);
			
			if (!partiesPreChatEvent.isCancelled()) {
				String newChatMessage = partiesPreChatEvent.getMessage();
				String newFormattedMessage = formattedMessage.replace("%message%", newChatMessage);
				for (PartyPlayer player : getOnlineMembers(true)) {
					((PartyPlayerImpl) player).sendDirect(newFormattedMessage);
					((PartyPlayerImpl) player).playChatSound();
				}
				
				sendPacketChat(sender, newChatMessage);
				
				return true;
			} else {
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_CHATEVENT_DENY, sender.getName(), chatMessage), true);
			}
		}
		return false;
	}
	
	public void addOnlineMember(PartyPlayerImpl partyPlayer) {
		synchronized (this) {
			onlineMembers.add(partyPlayer);
		}
	}
	
	public void removeOnlineMember(PartyPlayerImpl partyPlayer) {
		synchronized (this) {
			onlineMembers.remove(partyPlayer);
		}
	}
	
	/**
	 * Refresh party dynamic data.
	 * Used when the party is loaded
	 */
	public void refresh() {
		synchronized (this) {
			onlineMembers.clear();
			if (plugin.getPartyManager().getCacheParties().containsKey(getId())) {
				// Load players only if the party is online (in cache)
				// A party is always in cache if a player is online
				for (UUID u : getMembers()) {
					PartyPlayerImpl player = plugin.getPlayerManager().getCachePlayers().get(u);
					if (player != null) {
						onlineMembers.add(player);
					}
				}
			}
			
			// Load color
			plugin.getColorManager().loadDynamicColor(this);
		}
	}
	
	@Override
	public boolean isFull() {
		return ConfigParties.GENERAL_MEMBERS_LIMIT >= 0 && getMembers().size() >= ConfigParties.GENERAL_MEMBERS_LIMIT;
	}
	
	public void calculateLevels() {
		if (ConfigMain.ADDITIONAL_EXP_ENABLE && ConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE
				&& (expResult == null || cacheExperience != experience)) {
			try {
				// Set the new level of the party
				expResult = plugin.getExpManager().calculateLevel(experience);
				// Update experience stamp
				cacheExperience = experience;
			} catch (Exception ex) {
				plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_EXP_LEVELERROR, getId().toString(), ex.getMessage() != null ? ex.getMessage() : ex.toString()));
			}
		}
	}
	
	/**
	 * Get current color
	 *
	 * @return Returns the currently used color
	 */
	public PartyColor getCurrentColor() {
		if (getColor() != null)
			return getColor();
		return getDynamicColor();
	}
	
	@Override
	public void giveExperience(double exp) {
		this.giveExperience(exp, null);
	}
	
	/**
	 * Give party experience
	 *
	 * @param exp The experience number to give
	 * @param killer The killer of the entity
	 */
	public void giveExperience(double exp, @Nullable PartyPlayer killer) {
		if (plugin.isBungeeCordEnabled()) {
			// Bukkit with BC enabled: just send the event to BC
			sendPacketExperience(exp, killer);
		} else {
			updateValue(() -> {
				if (expResult == null)
					calculateLevels();
				int currentLevel = expResult.getLevel();
				
				experience = experience + exp;
				// Update party level directly after got experience
				calculateLevels();
				
				plugin.getScheduler().runAsync(() -> {
					// Experience event
					if (plugin.getPlatform() == PluginPlatform.BUNGEECORD) {
						// Send the experience event to bukkit too
						sendPacketExperience(exp, killer);
					}
					IPartyGetExperienceEvent partiesGetExperienceEvent = plugin.getEventManager().preparePartyGetExperienceEvent(this, exp, killer);
					plugin.getEventManager().callEvent(partiesGetExperienceEvent);
					
					// Level up event
					if (expResult.getLevel() > currentLevel) {
						// Send level up message
						broadcastMessage(Messages.ADDCMD_EXP_PARTY_LEVEL_LEVEL_UP, null);
						
						if (plugin.getPlatform() == PluginPlatform.BUNGEECORD) {
							// Send the event to bukkit too
							sendPacketLevelUp(expResult.getLevel());
						}
					
						IPartyLevelUpEvent partiesLevelUpEvent = plugin.getEventManager().prepareLevelUpEvent(this, expResult.getLevel());
						plugin.getEventManager().callEvent(partiesLevelUpEvent);
					}
				});
			});
		}
	}
	
	@Override
	public PartyInvite invitePlayer(@org.checkerframework.checker.nullness.qual.NonNull PartyPlayer invitedPlayer, @Nullable PartyPlayer inviter, boolean sendMessages) {
		PartyInvite ret = null;
		
		IPlayerPreInviteEvent playerPreInviteEvent = plugin.getEventManager().preparePlayerPreInviteEvent(invitedPlayer, inviter, this);
		plugin.getEventManager().callEvent(playerPreInviteEvent);
		
		if (!playerPreInviteEvent.isCancelled()) {
			if (!invitedPlayer.isInParty() && !isFull()) {
				ret = new PartyInviteImpl(plugin, this, invitedPlayer, inviter);
				inviteRequests.add(ret);
				invitedPlayer.getPendingInvites().add(ret);
				
				if (sendMessages) {
					if (inviter != null)
						((PartyPlayerImpl) inviter).sendMessage(Messages.MAINCMD_INVITE_SENT, (PartyPlayerImpl) invitedPlayer, this);
					((PartyPlayerImpl) invitedPlayer).sendMessage(Messages.MAINCMD_INVITE_PLAYERINVITED, (PartyPlayerImpl) inviter, this);
				}
				
				// Call API event and sync with bukkit servers
				sendPacketInvite(invitedPlayer, inviter);
				
				IPlayerPostInviteEvent playerPostInviteEvent = plugin.getEventManager().preparePlayerPostInviteEvent(invitedPlayer, inviter, this);
				plugin.getEventManager().callEvent(playerPostInviteEvent);
				
				plugin.getScheduler().scheduleAsyncLater(
						ret::timeout,
						ConfigParties.GENERAL_INVITE_TIMEOUT,
						TimeUnit.SECONDS
				);
			}
		} else {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_INVITEEVENT_DENY, invitedPlayer.getPlayerUUID().toString(), getId().toString(), inviter != null ? inviter.getPlayerUUID().toString() : "none"), true);
		}
		return ret;
	}
	
	public PartyAskRequest askToJoin(@NonNull PartyPlayer asker, boolean sendMessages) {
		PartyAskRequest ret = null;
		if (!asker.isInParty() && !isFull()) {
			ret = new PartyAskRequestImpl(plugin, this, asker);
			askRequests.add(ret);
			asker.getPendingAskRequests().add(ret);
			
			if (sendMessages) {
				((PartyPlayerImpl) asker).sendMessage(Messages.ADDCMD_ASK_SENT, this);
				
				broadcastMessageOnlyTo(Messages.ADDCMD_ASK_RECEIVED, asker, PartiesPermission.PRIVATE_ASK_ACCEPT, PartiesPermission.PRIVATE_ASK_DENY);
			}
			
			plugin.getScheduler().scheduleAsyncLater(
					ret::timeout,
					ConfigParties.ADDITIONAL_ASK_TIMEOUT,
					TimeUnit.SECONDS
			);
		}
		return ret;
	}
	
	public void memberJoinTimeout(PartyPlayerImpl joinedPlayer) {
		if (plugin.getPartyManager().getCacheMembersTimedOut().containsKey(joinedPlayer.getPlayerUUID())) {
			plugin.getPartyManager().getCacheMembersTimedOut().get(joinedPlayer.getPlayerUUID()).cancel();
			plugin.getPartyManager().getCacheMembersTimedOut().remove(joinedPlayer.getPlayerUUID());
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_TIMEOUT_STOP, joinedPlayer.getPlayerUUID().toString()), true);
		}
	}
	
	public boolean memberLeftTimeout(PartyPlayerImpl kickedPlayer) {
		return memberLeftTimeout(kickedPlayer, ConfigParties.GENERAL_MEMBERS_ON_LEAVE_DELAY);
	}
	
	/**
	 * Handle member left from the server
	 * @param kickedPlayer The player that left the server
	 * @param delay Delay of time out
	 * @return If the timeout started
	 */
	public boolean memberLeftTimeout(PartyPlayerImpl kickedPlayer, int delay) {
		boolean ret = false;
		if (ConfigParties.GENERAL_MEMBERS_ON_LEAVE_CHANGE_LEADER || ConfigParties.GENERAL_MEMBERS_ON_LEAVE_KICK_FROM_PARTY) {
			if (delay > 0) {
				plugin.getPartyManager().getCacheMembersTimedOut().put(kickedPlayer.getPlayerUUID(), plugin.getScheduler().scheduleAsyncLater(() -> plugin.getPartyManager().removePlayerTimedOut(kickedPlayer, this), delay, TimeUnit.SECONDS));
				ret = true;
			} else
				plugin.getPartyManager().removePlayerTimedOut(kickedPlayer, this);
		}
		return ret;
	}
	
	public abstract void sendPacketUpdate();
	
	public void sendPacketCreate(PartyPlayerImpl creator) {
		// Calling API event
		IPartyPostCreateEvent partiesPostEvent = plugin.getEventManager().preparePartyPostCreateEvent(creator, this);
		plugin.getEventManager().callEvent(partiesPostEvent);
	}
	
	public void sendPacketDelete(DeleteCause cause, PartyPlayerImpl kicked, PartyPlayerImpl commandSender) {
		// Calling API event
		IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(this, cause, kicked, commandSender);
		plugin.getEventManager().callEvent(partiesPostDeleteEvent);
	}
	
	public void sendPacketRename(String oldName, String newName, PartyPlayerImpl player, boolean isAdmin) {
		// Calling API event
		IPartyPostRenameEvent partiesPostRenameEvent = plugin.getEventManager().preparePartyPostRenameEvent(this, oldName, getName(), player, isAdmin);
		plugin.getEventManager().callEvent(partiesPostRenameEvent);
	}
	
	public void sendPacketAddMember(PartyPlayerImpl player, JoinCause cause, PartyPlayerImpl inviter) {
		// Calling API Event
		IPlayerPostJoinEvent partiesPostJoinEvent = plugin.getEventManager().preparePlayerPostJoinEvent(player, this, cause, inviter);
		plugin.getEventManager().callEvent(partiesPostJoinEvent);
	}
	
	public void sendPacketRemoveMember(PartyPlayerImpl player, LeaveCause cause, PartyPlayerImpl kicker) {
		// Calling API Event
		IPlayerPostLeaveEvent partiesPostLeaveEvent = plugin.getEventManager().preparePlayerPostLeaveEvent(player, this, cause, kicker);
		plugin.getEventManager().callEvent(partiesPostLeaveEvent);
	}
	
	public void sendPacketChat(PartyPlayerImpl player, String message) {
		// Calling API Event
		IPlayerPostChatEvent partiesPostChatEvent = plugin.getEventManager().preparePlayerPostChatEvent(player, this, message);
		plugin.getEventManager().callEvent(partiesPostChatEvent);
	}
	
	public void sendPacketInvite(PartyPlayer invitedPlayer, PartyPlayer inviter) {
		// Calling API Event
		IPlayerPostInviteEvent playerPostInviteEvent = plugin.getEventManager().preparePlayerPostInviteEvent(invitedPlayer, inviter, this);
		plugin.getEventManager().callEvent(playerPostInviteEvent);
	}
	
	public abstract void sendPacketExperience(double newExperience, PartyPlayer killer);
	
	public abstract void sendPacketLevelUp(int newLevel);
	
	protected byte[] makeRawDelete(DeleteCause cause, PartyPlayerImpl kicked, PartyPlayerImpl commandSender) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(cause.name());
		raw.writeUTF(kicked.getPlayerUUID().toString());
		raw.writeUTF(commandSender != null ? commandSender.getPlayerUUID().toString() : "");
		return raw.toByteArray();
	}
	
	protected byte[] makeRawRename(String oldName, String newName, PartyPlayerImpl player, boolean isAdmin) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(oldName);
		raw.writeUTF(newName);
		raw.writeUTF(player != null ? player.getPlayerUUID().toString() : "");
		raw.writeBoolean(isAdmin);
		return raw.toByteArray();
	}
	
	protected byte[] makeRawAddMember(PartyPlayerImpl player, JoinCause cause, PartyPlayerImpl inviter) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(player.getPlayerUUID().toString());
		raw.writeUTF(cause.name());
		raw.writeUTF(inviter != null ? inviter.getPlayerUUID().toString() : "");
		return raw.toByteArray();
	}
	
	protected byte[] makeRawRemoveMember(PartyPlayerImpl player, LeaveCause cause, PartyPlayerImpl kicker) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(player.getPlayerUUID().toString());
		raw.writeUTF(cause.name());
		raw.writeUTF(kicker != null ? kicker.getPlayerUUID().toString() : "");
		return raw.toByteArray();
	}
	
	protected byte[] makeRawInvite(PartyPlayer invitedPlayer, PartyPlayer inviter) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(invitedPlayer.getPlayerUUID().toString());
		raw.writeUTF(inviter != null ? inviter.getPlayerUUID().toString() : "");
		return raw.toByteArray();
	}
}
