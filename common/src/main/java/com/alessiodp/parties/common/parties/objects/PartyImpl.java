package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.InviteTask;
import com.alessiodp.parties.api.interfaces.Color;
import com.alessiodp.parties.api.interfaces.HomeLocation;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.common.user.User;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class PartyImpl implements Party {
	protected PartiesPlugin plugin;
	
	// Interface fields
	@Getter @Setter private String name;
	@Getter @Setter private List<UUID> members;
	@Getter @Setter private UUID leader;
	@Getter @Setter private boolean fixed;
	@Getter @Setter private String description;
	@Getter @Setter private String motd;
	@Getter @Setter private HomeLocation home;
	@Getter @Setter private Color color;
	@Getter @Setter private int kills;
	@Getter @Setter private String password;
	@Setter private boolean protection;
	@Getter @Setter private double experience;
	@Getter @Setter private ExpResult expResult;
	
	@Getter @Setter private Color dynamicColor;
	@Getter private Set<PartyPlayerImpl> onlinePlayers;
	@Getter private Map<UUID, UUID> inviteMap;
	@Getter private Map<UUID, Integer> inviteTasks;
	
	
	protected PartyImpl(PartiesPlugin instance, String name) {
		plugin = instance;
		
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
		expResult = new ExpResult();
		
		onlinePlayers = new HashSet<>();
		inviteMap = new HashMap<>();
		inviteTasks = new HashMap<>();
		
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_INIT
				.replace("{party}", getName()), true);
	}
	
	public void createParty(PartyPlayerImpl leader) {
		if (leader == null) {
			// Fixed
			setLeader(UUID.fromString(Constants.FIXED_VALUE_UUID));
			setFixed(fixed);
		} else {
			// Normal
			setLeader(leader.getPlayerUUID());
			getMembers().add(leader.getPlayerUUID());
			if (plugin.getOfflinePlayer(leader.getPlayerUUID()).isOnline())
				getOnlinePlayers().add(leader);
		}
		
	}
	
	public void updateParty() {
		plugin.getDatabaseManager().updateParty(this);
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_UPDATED
				.replace("{party}", getName()), true);
	}
	
	public void renameParty(String newName) {
		plugin.getPartyManager().getListParties().remove(getName());
		
		plugin.getDatabaseManager().renameParty(getName(), newName);
		for (PartyPlayer partyPlayer : getOnlinePlayers()) {
			partyPlayer.setPartyName(newName);
		}
		
		setName(newName);
		plugin.getPartyManager().getListParties().put(newName.toLowerCase(), this);
		callChange();
	}
	
	public void removeParty() {
		plugin.getPartyManager().getListParties().remove(getName().toLowerCase());
		plugin.getDatabaseManager().removeParty(this);
		
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_REMOVED
				.replace("{party}", getName()), true);
		
		for (UUID uuid : getMembers()) {
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(uuid);
			pp.cleanupPlayer(true);
		}
	}
	public void refreshPlayers() {
		reloadOnlinePlayers();
		plugin.getColorManager().loadDynamicColor(this);
		
		callChange();
	}
	
	public void callChange() {
		// This method is called when something related to Parties placeholders is changed
		// Used by Bukkit
	}
	
	
	/*
	 * Invite
	 */
	public void invitePlayer(UUID invitedBy, UUID invitedPlayer) {
		int taskId = plugin.getPartiesScheduler().scheduleTaskLater(
				new InviteTask(this, invitedPlayer), ConfigParties.GENERAL_INVITE_TIMEOUT);
		
		inviteMap.put(invitedPlayer, invitedBy);
		inviteTasks.put(invitedPlayer, taskId);
	}
	
	public void cancelInvite(UUID invitedPlayer) {
		UUID invitedBy = inviteMap.get(invitedPlayer);
		
		PartyPlayerImpl invitedByPp = plugin.getPlayerManager().getPlayer(invitedBy);
		PartyPlayerImpl invitedPlayerPp = plugin.getPlayerManager().getPlayer(invitedPlayer);
		
		invitedByPp.sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_NORESPONSE, invitedPlayerPp);
		invitedPlayerPp.sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_TIMEOUT, invitedByPp);
		
		invitedPlayerPp.setLastInvite("");
		
		inviteMap.remove(invitedPlayer);
		inviteTasks.remove(invitedPlayer);
	}
	
	public void acceptInvite(UUID invitedPlayer) {
		plugin.getPartiesScheduler().cancelTask(inviteTasks.get(invitedPlayer));
		
		UUID invitedBy = inviteMap.get(invitedPlayer);
		PartyPlayerImpl invitedByPp = plugin.getPlayerManager().getPlayer(invitedBy);
		PartyPlayerImpl invitedPlayerPp = plugin.getPlayerManager().getPlayer(invitedPlayer);
		
		// Calling API Event
		IPlayerPreJoinEvent partiesPreJoinEvent = plugin.getEventManager().preparePlayerPreJoinEvent(invitedPlayerPp, this, true, invitedBy);
		plugin.getEventManager().callEvent(partiesPreJoinEvent);
		
		if (!partiesPreJoinEvent.isCancelled()) {
			//Send accepted
			invitedByPp.sendMessage(Messages.MAINCMD_ACCEPT_ACCEPTRECEIPT, invitedPlayerPp);
			
			//Send you accepted
			invitedPlayerPp.sendMessage(Messages.MAINCMD_ACCEPT_ACCEPTED, invitedByPp);
			
			inviteMap.remove(invitedBy);
			inviteTasks.remove(invitedBy);
			
			invitedPlayerPp.setLastInvite("");
	
			sendBroadcast(invitedPlayerPp, Messages.MAINCMD_ACCEPT_BROADCAST);
			
			getMembers().add(invitedPlayer);
			onlinePlayers.add(invitedPlayerPp);
			
			invitedPlayerPp.setPartyName(getName());
			invitedPlayerPp.setRank(ConfigParties.RANK_SET_DEFAULT);
			
			updateParty();
			invitedPlayerPp.updatePlayer();

			callChange();
			
			IPlayerPostJoinEvent partiesPostJoinEvent = plugin.getEventManager().preparePlayerPostJoinEvent(invitedPlayerPp, this, true, invitedBy);
			plugin.getEventManager().callEvent(partiesPostJoinEvent);
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_JOINEVENT_DENY
					.replace("{player}", invitedPlayerPp.getName())
					.replace("{party}", getName()), true);
	}
	
	public void denyInvite(UUID invitedPlayer) {
		plugin.getPartiesScheduler().cancelTask(inviteTasks.get(invitedPlayer));
		
		UUID invitedBy = inviteMap.get(invitedPlayer);
		PartyPlayerImpl invitedByPp = plugin.getPlayerManager().getPlayer(invitedBy);
		PartyPlayerImpl invitedPlayerPp = plugin.getPlayerManager().getPlayer(invitedPlayer);
		
		invitedByPp.sendMessage(Messages.MAINCMD_DENY_DENYRECEIPT, invitedPlayerPp);
		invitedPlayerPp.sendMessage(Messages.MAINCMD_DENY_DENIED, invitedByPp);
		
		invitedPlayerPp.setLastInvite("");
		
		inviteMap.remove(invitedPlayer);
		inviteTasks.remove(invitedPlayer);
	}
	
	/**
	 * Send a message into the party chat
	 * @param sender Player that is sending the message
	 * @param playerMessage Message to send
	 */
	public void sendChatMessage(PartyPlayerImpl sender, String playerMessage) {
		if (playerMessage == null || playerMessage.isEmpty())
			return;
		
		String formattedMessage = plugin.getMessageUtils().convertAllPlaceholders(ConfigParties.GENERAL_CHAT_FORMAT_PARTY, this, sender);
		String msg = playerMessage;
		
		if (ConfigParties.GENERAL_CHAT_ALLOWCOLORS) {
			User user = plugin.getPlayer(sender.getPlayerUUID());
			if (user != null
					&& user.hasPermission(PartiesPermission.CHAT_COLOR.toString())
					&& plugin.getRankManager().checkPlayerRank(sender, PartiesPermission.PRIVATE_SENDMESSAGE_COLOR)) {
				msg = plugin.getMessageUtils().convertColors(msg);
			}
		}
		
		formattedMessage = plugin.getMessageUtils().convertColors(formattedMessage).replace("%message%", msg);
		
		sendDirectChatMessage(sender, formattedMessage, true);
	}
	
	public void sendDirectChatMessage(PartyPlayerImpl sender, String formattedMessage, boolean dispatchBetweenServers) {
		for (PartyPlayerImpl player : onlinePlayers) {
			player.sendDirect(formattedMessage);
		}
		
		String messageFormat =  plugin.getMessageUtils().convertAllPlaceholders(ConfigParties.GENERAL_CHAT_FORMAT_SPY, this, sender);
		messageFormat = plugin.getMessageUtils().convertColors(messageFormat).replace("%message%", formattedMessage);
		
		plugin.getSpyManager().sendMessageToSpies(messageFormat, this, sender);
	}
	
	/**
	 * Send a broadcast message to the party
	 * @param sender Player that is sending the message
	 * @param message Message to send
	 */
	public void sendBroadcast(PartyPlayerImpl sender, String message) {
		if (message == null || message.isEmpty())
			return;
		
		String formattedMessage = ConfigParties.GENERAL_CHAT_FORMAT_BROADCAST
				.replace("%message%", message);
		formattedMessage = plugin.getMessageUtils().convertAllPlaceholders(formattedMessage, this, sender);
		formattedMessage = plugin.getMessageUtils().convertColors(formattedMessage);
		
		sendDirectBroadcast(formattedMessage, true);
	}
	
	/**
	 * Send without format it a broadcast message to the party
	 * @param formattedMessage Formatted message to send
	 */
	public void sendDirectBroadcast(String formattedMessage, boolean dispatchBetweenServers) {
		if (formattedMessage == null || formattedMessage.isEmpty())
			return;
		
		for (PartyPlayerImpl player : onlinePlayers) {
			player.sendDirect(formattedMessage);
		}
	}
	
	/**
	 * Reload the online players list
	 */
	public void reloadOnlinePlayers() {
		onlinePlayers = new HashSet<>();
		for (UUID u : getMembers()) {
			if (plugin.getOfflinePlayer(u).isOnline()) {
				onlinePlayers.add(plugin.getPlayerManager().getPlayer(u));
			}
		}
	}
	
	/**
	 * Get the number of online players
	 * @return Returns the number of online players, not vanished
	 */
	public int getNumberOnlinePlayers() {
		int c = 0;
		try {
			for (PartyPlayerImpl player : onlinePlayers) {
				if (!player.isVanished())
					c++;
			}
		} catch (ConcurrentModificationException ex) {
			// Avoiding ConcurrentModificationException if something edit the hashmap
			c = getNumberOnlinePlayers();
		}
		return c;
	}
	
	/**
	 * Get current color
	 * @return Returns the currently used color
	 */
	@Nullable
	public Color getCurrentColor() {
		if (getColor() != null)
			return getColor();
		return getDynamicColor();
	}
	
	/**
	 * Give some experience to the party
	 * @param exp The amount of exp to give
	 */
	public void giveExperience(int exp) {
		experience = experience + exp;
		// Update party level directly after got experience
		callChange();
	}
	
	@Override
	public int getLevel() {
		return expResult.getLevel();
	}
	
	@Override
	public boolean isFriendlyFireProtected() {
		return getProtection();
	}
	
	@Override
	public boolean getProtection() {return protection;}
	
}
