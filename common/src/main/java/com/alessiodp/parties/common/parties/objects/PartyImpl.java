package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.Color;
import com.alessiodp.parties.api.interfaces.HomeLocation;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.InviteCooldown;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.spy.SpyMessage;
import com.alessiodp.parties.common.tasks.InviteTask;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public abstract class PartyImpl implements Party {
	protected final PartiesPlugin plugin;
	
	// Interface fields
	@Getter private String name;
	@Getter private List<UUID> members;
	@Getter private UUID leader;
	@Getter private boolean fixed;
	@Getter private String description;
	@Getter private String motd;
	@Getter private HomeLocation home;
	@Getter private Color color;
	@Getter private int kills;
	@Getter private String password;
	private boolean protection;
	@Getter private double experience;
	private boolean followEnabled;
	
	// Plugin fields
	@Getter @Setter private Color dynamicColor;
	@Getter protected ExpResult expResult;
	private HashSet<PartyPlayerImpl> onlineMembers;
	private HashMap<UUID, PartyInvite> inviteList;
	@Getter private final ReentrantLock lock = new ReentrantLock();
	
	protected PartyImpl(@NonNull PartiesPlugin plugin, @NonNull String name) {
		this.plugin = plugin;
		
		this.name = name;
		members = new ArrayList<>();
		leader = null;
		fixed = false;
		description = "";
		motd = "";
		home = null;
		color = null;
		kills = 0;
		password = "";
		protection = false;
		experience = 0;
		followEnabled = false;
		
		expResult = new ExpResult();
		onlineMembers = new HashSet<>();
		inviteList = new HashMap<>();
	}
	
	protected PartyImpl(@NonNull PartiesPlugin plugin, @NonNull String name, List<UUID> members, UUID leader, boolean fixed, String description, String motd, HomeLocationImpl home, ColorImpl color, int kills, String password, boolean protection, double experience, boolean followEnabled) {
		this.plugin = plugin;
		
		this.name = name;
		this.members = members;
		this.leader = leader;
		this.fixed = fixed;
		this.description = description;
		this.motd = motd;
		this.home = home;
		this.color = color;
		this.kills = kills;
		this.password = password;
		this.protection = protection;
		this.experience = experience;
		this.followEnabled = followEnabled;
		
		expResult = new ExpResult();
		onlineMembers = new HashSet<>();
		inviteList = new HashMap<>();
	}
	
	public void fromDatabase(UUID leader, List<UUID> members, boolean fixed, String description, String motd, Color color, int kills, String password, HomeLocationImpl home, boolean protection, double experience, boolean follow) {
		lock.lock();
		this.leader = leader;
		this.members = members;
		this.fixed = fixed;
		this.description = description;
		this.motd = motd;
		this.color = color;
		this.kills = kills;
		this.password = password;
		this.home = home;
		this.protection = protection;
		this.experience = experience;
		this.followEnabled = follow;
		lock.unlock();
	}
	
	/**
	 * Update the party
	 */
	public void updateParty() {
		plugin.getDatabaseManager().updateParty(this);
	}
	
	/**
	 * Create the party
	 *
	 * @param leader the leader of the party, null if fixed
	 */
	public void create(@Nullable PartyPlayerImpl leader) {
		lock.lock();
		if (leader == null) {
			// Fixed
			this.leader = UUID.fromString(PartiesConstants.FIXED_VALUE_UUID);
			this.fixed = true;
		} else {
			// Normal
			this.leader = leader.getPlayerUUID();
			members.add(leader.getPlayerUUID());
			
			if (plugin.getOfflinePlayer(leader.getPlayerUUID()).isOnline())
				onlineMembers.add(leader);
			
			// Update player
			leader.setPartyName(name);
			leader.setRank(ConfigParties.RANK_SET_HIGHER);
			leader.updatePlayer();
			
		}
		updateParty();
		
		plugin.getPartyManager().getListParties().put(getName().toLowerCase(Locale.ENGLISH), this);
		callChange();
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_CREATE.replace("{party}", getName()), true);
	}
	
	@Override
	public void delete() {
		lock.lock();
		plugin.getPartyManager().getListParties().remove(getName().toLowerCase(Locale.ENGLISH)); // Remove from online list
		plugin.getDatabaseManager().removeParty(this); // Remove from database
		
		for (UUID uuid : getMembers()) {
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(uuid);
			pp.removeFromParty(true);
		}
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_DELETE.replace("{party}", getName()), true);
	}
	
	@Override
	public void rename(@NonNull String newName) {
		lock.lock();
		String oldName = getName();
		plugin.getPartyManager().getListParties().remove(oldName.toLowerCase(Locale.ENGLISH)); // Remove from online list
		
		plugin.getDatabaseManager().renameParty(oldName, newName); // Rename via database
		
		// For each online player rename the party
		for (PartyPlayer partyPlayer : getOnlineMembers(true)) {
			partyPlayer.setPartyName(newName);
		}
		
		this.name = newName; // Change name
		
		plugin.getPartyManager().getListParties().put(newName.toLowerCase(Locale.ENGLISH), this); // Insert into online list
		callChange();
		lock.unlock();
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_PARTY_RENAME
				.replace("{party}", oldName)
				.replace("{name}", getName()), true);
	}
	
	@Override
	public boolean addMember(@NonNull PartyPlayer partyPlayer) {
		boolean ret = false;
		lock.lock();
		if ((ConfigParties.GENERAL_MEMBERSLIMIT < 0)
				|| (members.size() < ConfigParties.GENERAL_MEMBERSLIMIT)) {
			members.add(partyPlayer.getPlayerUUID());
			onlineMembers.add((PartyPlayerImpl) partyPlayer);
			
			((PartyPlayerImpl) partyPlayer).addIntoParty(getName(), ConfigParties.RANK_SET_DEFAULT);
			
			updateParty();
			callChange();
			ret = true;
		}
		lock.unlock();
		return ret;
	}
	
	@Override
	public void removeMember(@NonNull PartyPlayer partyPlayer) {
		lock.lock();
		members.remove(partyPlayer.getPlayerUUID());
		onlineMembers.remove(partyPlayer);
		
		((PartyPlayerImpl) partyPlayer).removeFromParty(true);
		
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@NonNull
	@Override
	public Set<PartyPlayer> getOnlineMembers(boolean bypassVanish) {
		HashSet<PartyPlayer> ret = new HashSet<>();
		lock.lock();
		try {
			for (PartyPlayerImpl player : onlineMembers) {
				if (bypassVanish || !player.isVanished())
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
		lock.lock();
		UUID oldLeader = this.leader;
		this.leader = leaderPartyPlayer.getPlayerUUID();
		
		plugin.getPlayerManager().getPlayer(oldLeader).setRank(leaderPartyPlayer.getRank());
		leaderPartyPlayer.setRank(ConfigParties.RANK_SET_HIGHER);
		
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setFixed(boolean fixed, @Nullable PartyPlayer newLeader) {
		if (fixed != isFixed()) {
			lock.lock();
			if (fixed) {
				UUID oldLeader = this.leader;
				this.leader = UUID.fromString(PartiesConstants.FIXED_VALUE_UUID);
				this.fixed = true;
				
				plugin.getPlayerManager().getPlayer(oldLeader).setRank(ConfigParties.RANK_SET_DEFAULT);
			} else if (newLeader != null) {
				this.leader = newLeader.getPlayerUUID();
				this.fixed = false;
				
				newLeader.setRank(ConfigParties.RANK_SET_HIGHER);
			}
			updateParty();
			callChange();
			lock.unlock();
		}
	}
	
	@Override
	public void setDescription(@NonNull String description) {
		lock.lock();
		this.description = description;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setMotd(@NonNull String motd) {
		lock.lock();
		this.motd = motd;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setHome(@Nullable HomeLocation home) {
		lock.lock();
		this.home = home;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setColor(@Nullable Color color) {
		lock.lock();
		this.color = color;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setKills(int kills) {
		lock.lock();
		this.kills = kills;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setPassword(@NonNull String password) {
		lock.lock();
		this.password = password;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setProtection(boolean protection) {
		lock.lock();
		this.protection = protection;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public void setExperience(double experience) {
		lock.lock();
		this.experience = experience;
		updateParty();
		callChange();
		lock.unlock();
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
		lock.lock();
		this.followEnabled = follow;
		updateParty();
		callChange();
		lock.unlock();
	}
	
	@Override
	public int getLevel() {
		return expResult.getLevel();
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
		if (message == null || message.isEmpty())
			return;
		
		String formattedMessage = ConfigParties.GENERAL_CHAT_FORMAT_BROADCAST
				.replace("%message%", message);
		if (partyPlayer != null)
			formattedMessage = plugin.getMessageUtils().convertAllPlaceholders(formattedMessage, this, (PartyPlayerImpl) partyPlayer);
		formattedMessage = plugin.getColorUtils().convertColors(formattedMessage);
		
		broadcastDirectMessage(formattedMessage, true);
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
			((PartyPlayerImpl) player).sendMessage(message, this);
		}
		
		plugin.getSpyManager().sendSpyMessage(new SpyMessage(plugin)
				.setType(SpyMessage.SpyType.BROADCAST)
				.setMessage(message)
				.setParty(this)
				.setPlayer(null));
	}
	
	public void dispatchChatMessage(PartyPlayerImpl sender, String message, boolean dispatchBetweenServers) {
		if (message == null || message.isEmpty())
			return;
		
		for (PartyPlayer player : getOnlineMembers(true)) {
			((PartyPlayerImpl) player).sendDirect(message);
		}
	}
	
	/**
	 * This method is called when something related to Parties placeholders is changed
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
	
	/**
	 * Get current color
	 *
	 * @return Returns the currently used color
	 */
	public Color getCurrentColor() {
		if (getColor() != null)
			return getColor();
		return getDynamicColor();
	}
	
	/**
	 * Give some experience to the party
	 *
	 * @param exp The amount of exp to give
	 */
	public void giveExperience(int exp) {
		lock.lock();
		experience = experience + exp;
		// Update party level directly after got experience
		callChange();
		lock.unlock();
	}
	
	/**
	 * Invite a player
	 */
	public void invitePlayer(PartyPlayerImpl invitedByPartyPlayer, PartyPlayerImpl invitedPartyPlayer) {
		invitedPartyPlayer.getPartyInvites().put(this, invitedByPartyPlayer.getPlayerUUID());
		
		CancellableTask task = plugin.getScheduler().scheduleAsyncLater(
				new InviteTask(plugin, this, invitedPartyPlayer.getPlayerUUID()),
				ConfigParties.GENERAL_INVITE_TIMEOUT,
				TimeUnit.SECONDS);
		inviteList.put(
				invitedPartyPlayer.getPlayerUUID(),
				new PartyInvite(invitedPartyPlayer.getPlayerUUID(), invitedByPartyPlayer.getPlayerUUID(), task)
		);
		
		invitedByPartyPlayer.sendMessage(Messages.MAINCMD_INVITE_SENT, invitedPartyPlayer, this);
		invitedPartyPlayer.sendMessage(Messages.MAINCMD_INVITE_PLAYERINVITED, invitedByPartyPlayer, this);
		
		if (ConfigParties.GENERAL_INVITE_COOLDOWN_ENABLE) {
			if (ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL > 0) {
				new InviteCooldown(plugin, invitedByPartyPlayer.getPlayerUUID())
						.createTask(ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL);
			}
			if (ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL > 0) {
				new InviteCooldown(plugin, invitedByPartyPlayer.getPlayerUUID(), invitedPartyPlayer.getPlayerUUID())
						.createTask(ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL);
			}
		}
	}
	
	/**
	 * Cancel an invite that was sent
	 */
	public void cancelInvite(UUID invitedPlayer) {
		PartyInvite invite = inviteList.get(invitedPlayer);
		if (invite != null) {
			PartyPlayerImpl invitedByPartyPlayer = plugin.getPlayerManager().getPlayer(invite.getInvitedBy());
			PartyPlayerImpl invitedPartyPlayer = plugin.getPlayerManager().getPlayer(invitedPlayer);
			
			invitedByPartyPlayer.sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_NORESPONSE, invitedPartyPlayer, this);
			if (invitedPartyPlayer.getPartyInvites().containsKey(this)) {
				// If the player accept another party, it will receive a timeout messages for each other one
				// This will avoid that
				invitedPartyPlayer.sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_TIMEOUT, invitedByPartyPlayer, this);
				
				invitedPartyPlayer.getPartyInvites().remove(this);
			}
			
			inviteList.remove(invitedPlayer);
		}
	}
	
	/**
	 * Revoke an invite that was sent
	 */
	public void revokeInvite(UUID invitedPlayer) {
		PartyInvite invite = inviteList.get(invitedPlayer);
		if (invite != null) {
			invite.getTask().cancel();
			
			PartyPlayerImpl invitedByPartyPlayer = plugin.getPlayerManager().getPlayer(invite.getInvitedBy());
			PartyPlayerImpl invitedPartyPlayer = plugin.getPlayerManager().getPlayer(invitedPlayer);
			
			invitedByPartyPlayer.sendMessage(Messages.MAINCMD_INVITE_REVOKE_SENT, invitedPartyPlayer, this);
			invitedPartyPlayer.sendMessage(Messages.MAINCMD_INVITE_REVOKE_REVOKED, invitedByPartyPlayer, this);
			
			invitedPartyPlayer.getPartyInvites().remove(this);
			
			inviteList.remove(invitedPlayer);
		}
	}
	
	/**
	 * Accept the invite
	 */
	public void acceptInvite(UUID invitedPlayer) {
		PartyInvite invite = inviteList.get(invitedPlayer);
		if (invite != null) {
			invite.getTask().cancel();
			
			PartyPlayerImpl invitedByPartyPlayer = plugin.getPlayerManager().getPlayer(invite.getInvitedBy());
			PartyPlayerImpl invitedPartyPlayer = plugin.getPlayerManager().getPlayer(invitedPlayer);
			
			// Calling API Event
			IPlayerPreJoinEvent partiesPreJoinEvent = plugin.getEventManager().preparePlayerPreJoinEvent(invitedPartyPlayer, this, true, invite.getInvitedBy());
			plugin.getEventManager().callEvent(partiesPreJoinEvent);
			
			if (!partiesPreJoinEvent.isCancelled()) {
				//Send accepted
				invitedByPartyPlayer.sendMessage(Messages.MAINCMD_ACCEPT_ACCEPTRECEIPT, invitedPartyPlayer, this);
				
				//Send you accepted
				invitedPartyPlayer.sendMessage(Messages.MAINCMD_ACCEPT_ACCEPTED, invitedByPartyPlayer, this);
				
				inviteList.remove(invitedPlayer);
				
				invitedPartyPlayer.getPartyInvites().remove(this);
				
				broadcastMessage(Messages.MAINCMD_ACCEPT_BROADCAST, invitedPartyPlayer);
				
				boolean success = addMember(invitedPartyPlayer);
				if (success) {
					IPlayerPostJoinEvent partiesPostJoinEvent = plugin.getEventManager().preparePlayerPostJoinEvent(invitedPartyPlayer, this, true, invite.getInvitedBy());
					plugin.getEventManager().callEvent(partiesPostJoinEvent);
				} else {
					invitedPartyPlayer.sendMessage(Messages.PARTIES_COMMON_PARTYFULL, this);
				}
			} else
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_JOINEVENT_DENY
						.replace("{player}", invitedPartyPlayer.getName())
						.replace("{party}", getName()), true);
		}
	}
	
	/**
	 * Deny the invite
	 */
	public void denyInvite(UUID invitedPlayer) {
		PartyInvite invite = inviteList.get(invitedPlayer);
		if (invite != null) {
			invite.getTask().cancel();
			
			PartyPlayerImpl invitedByPartyPlayer = plugin.getPlayerManager().getPlayer(invite.getInvitedBy());
			PartyPlayerImpl invitedPartyPlayer = plugin.getPlayerManager().getPlayer(invitedPlayer);
			
			invitedByPartyPlayer.sendMessage(Messages.MAINCMD_DENY_DENYRECEIPT, invitedPartyPlayer, this);
			invitedPartyPlayer.sendMessage(Messages.MAINCMD_DENY_DENIED, invitedByPartyPlayer, this);
			
			invitedPartyPlayer.getPartyInvites().remove(this);
			
			inviteList.remove(invitedPlayer);
		}
	}
}
