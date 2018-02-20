package com.alessiodp.parties.parties.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.BanManagerHandler;
import com.alessiodp.parties.addons.external.DynmapHandler;
import com.alessiodp.parties.addons.internal.JSONHandler;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.tasks.InviteTask;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.events.PartiesPlayerJoinEvent;
import com.alessiodp.partiesapi.interfaces.Color;
import com.alessiodp.partiesapi.objects.Party;

import lombok.Getter;
import lombok.Setter;

public class PartyEntity extends Party {
	private Parties plugin;
	
	@Getter @Setter private Color dynamicColor;
	@Getter private HashSet<Player> onlinePlayers;
	@Getter private HashMap<UUID, UUID> whoInvite;
	@Getter private HashMap<UUID, Integer> invited;
	
	
	public PartyEntity(String name, Parties instance) {
		super(name);
		plugin = instance;
		onlinePlayers = new HashSet<Player>();
		whoInvite = new HashMap<UUID, UUID>();
		invited = new HashMap<UUID, Integer>();
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_INIT
				.replace("{party}", getName()), true);
	}
	public PartyEntity(Party copy, Parties instance) {
		super(copy);
		plugin = instance;
		onlinePlayers = new HashSet<Player>();
		whoInvite = new HashMap<UUID, UUID>();
		invited = new HashMap<UUID, Integer>();
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_INIT
				.replace("{party}", getName()), true);
	}
	public PartyEntity(PartyEntity copy) {
		super(copy);
		plugin = copy.plugin;
		onlinePlayers = copy.onlinePlayers;
		whoInvite = copy.whoInvite;
		invited = copy.invited;
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_INIT_COPY
				.replace("{party}", getName()), true);
	}
	
	
	public void updateParty() {
		DynmapHandler.updatePartyMarker(this);
		plugin.getDatabaseManager().updateParty(this);
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_UPDATED
				.replace("{party}", getName()), true);
	}
	public void renamingParty() {
		DynmapHandler.removeMarker(getName());
		plugin.getTagManager().deleteTag(this);
	}
	public void removeParty() {
		plugin.getPartyManager().getListParties().remove(getName().toLowerCase());
		DynmapHandler.removeMarker(getName());
		plugin.getDatabaseManager().removeParty(this);
		plugin.getTagManager().deleteTag(this);
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_PARTY_REMOVED
				.replace("{party}", getName()), true);
		
		for (UUID uuid : getMembers()) {
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(uuid);
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
		plugin.getTagManager().refreshTag(this);
	}
	
	
	/*
	 * Invite
	 */
	public void invitePlayer(UUID from, UUID to) {
		BukkitTask it = new InviteTask(this, to).runTaskLater(plugin, ConfigParties.GENERAL_INVITE_TIMEOUT * 20);
		whoInvite.put(to, from);
		invited.put(to, it.getTaskId());
	}
	
	public void cancelInvite(UUID to) {
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		UUID from = whoInvite.get(to);
		
		PartyPlayerEntity fromPp = plugin.getPlayerManager().getPlayer(from);
		PartyPlayerEntity toPp = plugin.getPlayerManager().getPlayer(to);
		
		fromPp.sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_NORESPONSE, toPp);
		toPp.sendMessage(Messages.MAINCMD_INVITE_TIMEOUT_TIMEOUT, fromPp);
		
		toPp.setLastInvite("");
		
		whoInvite.remove(to);
		invited.remove(to);
	}
	
	public void acceptInvite(UUID to) {
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		
		UUID from = whoInvite.get(to);
		PartyPlayerEntity fromPp = plugin.getPlayerManager().getPlayer(from);
		PartyPlayerEntity toPp = plugin.getPlayerManager().getPlayer(to);
		
		// Calling API Event
		PartiesPlayerJoinEvent partiesJoinEvent = new PartiesPlayerJoinEvent(toPp, this, true, from);
		Bukkit.getServer().getPluginManager().callEvent(partiesJoinEvent);
		if (!partiesJoinEvent.isCancelled()) {
			//Send accepted
			fromPp.sendMessage(Messages.MAINCMD_ACCEPT_ACCEPTRECEIPT, toPp);
			
			//Send you accepted
			toPp.sendMessage(Messages.MAINCMD_ACCEPT_ACCEPTED, fromPp);
			
			whoInvite.remove(to);
			invited.remove(to);
	
			toPp.setLastInvite("");
	
			sendBroadcast(toPp, Messages.MAINCMD_ACCEPT_BROADCAST);
			
			getMembers().add(to);
			onlinePlayers.add(toPp.getPlayer());
	
			toPp.setPartyName(getName());
			toPp.setRank(ConfigParties.RANK_SET_DEFAULT);
			
			updateParty();
			toPp.updatePlayer();

			callChange();
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_JOINEVENT_DENY
					.replace("{player}", toPp.getName())
					.replace("{party}", getName()), true);
	}
	
	public void denyInvite(UUID to) {
		plugin.getServer().getScheduler().cancelTask(invited.get(to));
		
		UUID from = whoInvite.get(to);
		PartyPlayerEntity fromTP = plugin.getPlayerManager().getPlayer(from);
		PartyPlayerEntity toTP = plugin.getPlayerManager().getPlayer(to);
		
		fromTP.sendMessage(Messages.MAINCMD_DENY_DENYRECEIPT, toTP);
		toTP.sendMessage(Messages.MAINCMD_DENY_DENIED, fromTP);
		
		toTP.setLastInvite("");
		
		whoInvite.remove(to);
		invited.remove(to);
	}
	
	
	/*
	 * Send Message
	 */
	public void sendPlayerMessage(PartyPlayerEntity sender, String playerMessage) {
		if (playerMessage == null || playerMessage.isEmpty())
			return;
		if (ConfigMain.ADDONS_BANMANAGER_ENABLE && ConfigMain.ADDONS_BANMANAGER_PREVENTCHAT
				&& BanManagerHandler.isMuted(sender.getPlayerUUID())) {
			return;
		}
		
		String formattedMessage = PartiesUtils.convertAllPlaceholders(ConfigParties.GENERAL_CHAT_FORMAT_PARTY, this, sender);
		String msg = playerMessage;
		if (ConfigParties.GENERAL_CHAT_ALLOWCOLORS)
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
		
		String messageFormat =  PartiesUtils.convertAllPlaceholders(ConfigParties.GENERAL_CHAT_FORMAT_SPY, this, sender);
		plugin.getSpyManager().sendMessageToSpies(
				ChatColor.translateAlternateColorCodes('&', messageFormat)
					.replace("%message%", playerMessage), getName());
	}
	
	public void sendBroadcast(PartyPlayerEntity sender, String message) {
		if (message == null || message.isEmpty())
			return;
		
		String formattedMessage = ConfigParties.GENERAL_CHAT_FORMAT_BROADCAST
				.replace("%message%", message);
		formattedMessage = PartiesUtils.convertAllPlaceholders(formattedMessage, this, sender);
		
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
	
	public void sendFriendlyFireWarn(PartyPlayerEntity victim, PartyPlayerEntity attacker) {
		if (ConfigParties.FRIENDLYFIRE_WARNONFIGHT) {
			String message = Messages.OTHER_FRIENDLYFIRE_WARN
					.replace(Constants.PLACEHOLDER_PLAYER_PLAYER, attacker.getName())
					.replace(Constants.PLACEHOLDER_PLAYER_VICTIM, victim.getName());
			
			for (Player onlineP : getOnlinePlayers()) {
				if (!onlineP.getUniqueId().equals(attacker.getPlayerUUID())) {
					PartyPlayerEntity onlinePp = plugin.getPlayerManager().getPlayer(onlineP.getUniqueId());
					if (!PartiesUtils.checkPlayerRank(onlinePp, PartiesPermission.PRIVATE_WARNONDAMAGE)) {
						
						onlinePp.sendMessage(message, this);
					}
				}
			}
		}
	}
	
	
	/*
	 * Online players
	 */
	public void reloadOnlinePlayers() {
		onlinePlayers = new HashSet<Player>();
		for (UUID u : getMembers()) {
			OfflinePlayer op = Bukkit.getOfflinePlayer(u);
			if (op != null && op.isOnline())
				onlinePlayers.add(op.getPlayer());
		}
	}
	public int getNumberOnlinePlayers() {
		int c = 0;
		for (Player p : onlinePlayers) {
			if (!PartiesUtils.isVanished(p))
				c++;
		}
		return c;
	}
	
	/*
	 * Current color
	 */
	public Color getCurrentColor() {
		if (getColor() != null)
			return getColor();
		return getDynamicColor();
	}
	
}
