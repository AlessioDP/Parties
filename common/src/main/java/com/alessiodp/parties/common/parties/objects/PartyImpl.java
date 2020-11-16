package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.core.common.bootstrap.PluginPlatform;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.parties.api.events.common.party.IPartyGetExperienceEvent;
import com.alessiodp.parties.api.events.common.party.IPartyLevelUpEvent;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode
@ToString
public abstract class PartyImpl implements Party {
	@EqualsAndHashCode.Exclude @ToString.Exclude protected final PartiesPlugin plugin;
	
	// Interface fields
	@Getter private final UUID id;
	@Getter private String name;
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
	
	@EqualsAndHashCode.Exclude @ToString.Exclude @Getter private final ReentrantLock lock = new ReentrantLock();
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
		if (accessible) {
			runnable.run();
		} else {
			lock.lock();
			runnable.run();
			
			updateParty();
			callChange();
			lock.unlock();
		}
	}
	
	/**
	 * Update the party
	 */
	public void updateParty() {
		plugin.getDatabaseManager().updateParty(this);
	}
	
	/**
	 * Setup existing party
	 *
	 * @param name the name of the party
	 * @param leader the leader of the party, null if fixed
	 */
	public void setup(@lombok.NonNull String name, @Nullable String leader) throws IllegalArgumentException {
		lock.lock();
		try {
			this.name = name;
			if (leader == null || leader.isEmpty() || leader.equals("fixed") || leader.equals("00000000-0000-0000-0000-000000000000")) {
				this.leader = null;
			} else {
				this.leader = UUID.fromString(leader);
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Create the party
	 *
	 * @param leader the leader of the party, null if fixed
	 */
	public void create(@NonNull String name, @Nullable String tag, @Nullable PartyPlayerImpl leader) {
		lock.lock();
		this.name = name;
		this.tag = tag;
		
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
		updateParty();
		
		plugin.getPartyManager().addPartyToCache(this);
		callChange();
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_CREATE, getName()), true);
	}
	
	@Override
	public void delete() {
		lock.lock();
		plugin.getPartyManager().removePartyFromCache(this); // Remove from cache
		plugin.getDatabaseManager().removeParty(this); // Remove from database
		
		for (UUID uuid : getMembers()) {
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(uuid);
			pp.removeFromParty(true);
		}
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_DELETE, getName()), true);
	}
	
	@Override
	public void rename(@NonNull String newName) {
		lock.lock();
		String oldName = getName();
		plugin.getPartyManager().removePartyFromCache(this); // Remove from cache
		
		this.name = newName;
		
		plugin.getPartyManager().addPartyToCache(this); // Insert into online list
		callChange();
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_PARTY_RENAME, oldName, getName()), true);
	}
	
	@Override
	public boolean addMember(@NonNull PartyPlayer partyPlayer) {
		boolean ret = false;
		lock.lock();
		if (!isFull()) {
			members.add(partyPlayer.getPlayerUUID());
			onlineMembers.add(partyPlayer);
			
			((PartyPlayerImpl) partyPlayer).addIntoParty(id, ConfigParties.RANK_SET_DEFAULT);
			
			updateParty();
			callChange();
			ret = true;
		}
		lock.unlock();
		return ret;
	}
	
	@Override
	public void removeMember(@NonNull PartyPlayer partyPlayer) {
		updateValue(() -> {
			members.remove(partyPlayer.getPlayerUUID());
			onlineMembers.remove(partyPlayer);
			
			((PartyPlayerImpl) partyPlayer).removeFromParty(true);
		});
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
	
	@NonNull
	@Override
	public Set<PartyPlayer> getOnlineMembers(boolean bypassVanish) {
		HashSet<PartyPlayer> ret = new HashSet<>();
		lock.lock();
		try {
			for (PartyPlayer player : onlineMembers) {
				if (bypassVanish || !((PartyPlayerImpl) player).isVanished())
					ret.add(player);
			}
		} catch (ConcurrentModificationException ex) {
			// Avoiding ConcurrentModificationException if something edits the hashmap
			ex.printStackTrace();
		}
		lock.unlock();
		return Collections.unmodifiableSet(ret);
	}
	
	@Override
	public void changeLeader(@NonNull PartyPlayer leaderPartyPlayer) {
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
		return getProtection();
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
			if (ConfigParties.GENERAL_BROADCAST_USE_TITLES)
				((PartyPlayerImpl) player).sendTitleMessage(message);
			else
				((PartyPlayerImpl) player).sendDirect(message);
			((PartyPlayerImpl) player).playBroadcastSound();
		}
		
		plugin.getPlayerManager().sendSpyMessage(new SpyMessage(plugin)
				.setType(SpyMessage.SpyType.BROADCAST)
				.setMessage(plugin.getJsonHandler().removeJson(message))
				.setParty(this)
				.setPlayer(null));
	}
	
	public void dispatchChatMessage(PartyPlayerImpl sender, String message, boolean dispatchBetweenServers) {
		if (message == null || message.isEmpty())
			return;
		
		for (PartyPlayer player : getOnlineMembers(true)) {
			((PartyPlayerImpl) player).sendDirect(message);
			((PartyPlayerImpl) player).playChatSound();
		}
	}
	
	/**
	 * This method is called when something related to the party is changed
	 */
	public abstract void callChange();
	
	public void addOnlineMember(PartyPlayerImpl partyPlayer) {
		onlineMembers.add(partyPlayer);
	}
	
	public void removeOnlineMember(PartyPlayerImpl partyPlayer) {
		onlineMembers.remove(partyPlayer);
	}
	
	/**
	 * Refresh online player list.
	 * Used when the party is loaded
	 */
	public void refreshOnlineMembers() {
		lock.lock();
		onlineMembers.clear();
		for (UUID u : getMembers()) {
			if (plugin.getOfflinePlayer(u).isOnline()) {
				onlineMembers.add(plugin.getPlayerManager().getPlayer(u));
			}
		}
		
		plugin.getColorManager().loadDynamicColor(this);
		
		callChange();
		lock.unlock();
	}
	
	@Override
	public boolean isFull() {
		return ConfigParties.GENERAL_MEMBERS_LIMIT >= 0 && getMembers().size() >= ConfigParties.GENERAL_MEMBERS_LIMIT;
	}
	
	public void calculateLevels() {
		if (ConfigMain.ADDITIONAL_EXP_ENABLE && ConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE) {
			if (expResult == null || cacheExperience != experience) {
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
			sendExperiencePacket(exp, killer);
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
						sendExperiencePacket(exp, killer);
					}
					IPartyGetExperienceEvent partiesGetExperienceEvent = plugin.getEventManager().preparePartyGetExperienceEvent(this, exp, killer);
					plugin.getEventManager().callEvent(partiesGetExperienceEvent);
					
					// Level up event
					if (expResult.getLevel() > currentLevel) {
							// Send level up message
							broadcastMessage(Messages.ADDCMD_EXP_PARTY_LEVEL_LEVEL_UP, null);
							
							if (plugin.getPlatform() == PluginPlatform.BUNGEECORD) {
								// Send the event to bukkit too
								sendLevelUpPacket(expResult.getLevel());
							}
						
							IPartyLevelUpEvent partiesLevelUpEvent = plugin.getEventManager().prepareLevelUpEvent(this, expResult.getLevel());
							plugin.getEventManager().callEvent(partiesLevelUpEvent);
					}
				});
			});
		}
	}
	
	public abstract void sendExperiencePacket(double newExperience, PartyPlayer killer);
	
	public abstract void sendLevelUpPacket(int newLevel);
	
	
	@Override
	public PartyInvite invitePlayer(@NonNull PartyPlayer invitedPlayer, @Nullable PartyPlayer inviter, boolean sendMessages) {
		PartyInvite ret = null;
		if (!invitedPlayer.isInParty() && !isFull()) {
			ret = new PartyInviteImpl(plugin, this, invitedPlayer, inviter);
			inviteRequests.add(ret);
			invitedPlayer.getPendingInvites().add(ret);
			
			if (sendMessages) {
				if (inviter != null)
					((PartyPlayerImpl) inviter).sendMessage(Messages.MAINCMD_INVITE_SENT, (PartyPlayerImpl) invitedPlayer, this);
				((PartyPlayerImpl) invitedPlayer).sendMessage(Messages.MAINCMD_INVITE_PLAYERINVITED, (PartyPlayerImpl) inviter, this);
			}
			
			plugin.getScheduler().scheduleAsyncLater(
					ret::timeout,
					ConfigParties.GENERAL_INVITE_TIMEOUT,
					TimeUnit.SECONDS
			);
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
					ConfigParties.GENERAL_ASK_TIMEOUT,
					TimeUnit.SECONDS
			);
		}
		return ret;
	}
}
