package com.alessiodp.parties.bungeecord;

import com.alessiodp.parties.bungeecord.addons.BungeeAddonManager;
import com.alessiodp.parties.bungeecord.addons.external.MetricsHandler;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.commands.BungeeCommandManager;
import com.alessiodp.parties.bungeecord.configuration.BungeeConfigurationManager;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.parties.bungeecord.events.BungeeEventManager;
import com.alessiodp.parties.bungeecord.listeners.BungeeChatListener;
import com.alessiodp.parties.bungeecord.listeners.BungeeFollowListener;
import com.alessiodp.parties.bungeecord.listeners.BungeeJoinLeaveListener;
import com.alessiodp.parties.bungeecord.parties.BungeePartyManager;
import com.alessiodp.parties.bungeecord.players.BungeePlayerManager;
import com.alessiodp.parties.bungeecord.scheduling.BungeePartiesScheduler;
import com.alessiodp.parties.bungeecord.user.BungeeOfflineUser;
import com.alessiodp.parties.bungeecord.user.BungeeUser;
import com.alessiodp.parties.bungeecord.utils.BungeeEconomyManager;
import com.alessiodp.parties.bungeecord.utils.BungeeMessageUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.bootstrap.PartiesBootstrap;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.storage.DatabaseManager;
import com.alessiodp.parties.common.user.OfflineUser;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.ConsoleColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class BungeePartiesPlugin extends PartiesPlugin {
	
	public BungeePartiesPlugin(PartiesBootstrap instance) {
		super(instance);
	}
	
	@Override
	public void disabling() {
		// Disable bungee scheduler
		((BungeePartiesScheduler) partiesScheduler).setUseBungeeScheduler(false);
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_DISABLING);
		
		super.disabling();
	}
	
	@Override
	protected void preHandle() {
		super.preHandle();
		partiesScheduler = new BungeePartiesScheduler(this);
		databaseManager = new DatabaseManager(this);
		configManager = new BungeeConfigurationManager(this,
				new BungeeConfigMain(this),
				new BungeeConfigParties(this),
				new BungeeMessages(this));
		messageUtils = new BungeeMessageUtils(this);
	}
	
	@Override
	protected void handle() {
		super.handle();
		partyManager = new BungeePartyManager(this);
		playerManager = new BungeePlayerManager(this);
		addonManager = new BungeeAddonManager(this);
	}
	
	@Override
	protected void postHandle() {
		super.postHandle();
		commandManager = new BungeeCommandManager(this);
		getCommandManager().setup();
		eventManager = new BungeeEventManager(this);
		economyManager = new BungeeEconomyManager(this);
		
		new MetricsHandler(this);
	}
	
	@Override
	protected void registerListeners() {
		PluginManager pm = getBootstrap().getProxy().getPluginManager();
		pm.registerListener(getBootstrap(), new BungeeChatListener(this));
		pm.registerListener(getBootstrap(), new BungeeFollowListener(this));
		pm.registerListener(getBootstrap(), new BungeeJoinLeaveListener(this));
	}
	
	@Override
	public List<User> getOnlinePlayers() {
		List<User> ret = new ArrayList<>();
		for (ProxiedPlayer player : getBootstrap().getProxy().getPlayers()) {
			ret.add(new BungeeUser(player));
		}
		return ret;
	}
	
	@Override
	public User getPlayer(UUID uuid) {
		ProxiedPlayer player = getBootstrap().getProxy().getPlayer(uuid);
		return player != null ? new BungeeUser(player) : null;
	}
	
	@Override
	public User getPlayerByName(String name) {
		ProxiedPlayer player = getBootstrap().getProxy().getPlayer(name);
		return player != null ? new BungeeUser(player) : null;
	}
	
	@Override
	public OfflineUser getOfflinePlayer(UUID uuid) {
		ProxiedPlayer player = getBootstrap().getProxy().getPlayer(uuid);
		return new BungeeOfflineUser(player, uuid);
	}
	
	@Override
	public BungeePartiesBootstrap getBootstrap() {
		return (BungeePartiesBootstrap) super.getBootstrap();
	}
	
	@Override
	public void log(String message) {
		getBootstrap().getProxy().getLogger().log(Level.INFO, "[" + ConsoleColor.CYAN.getCode() + "Parties" + ConsoleColor.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)) + ConsoleColor.RESET.getCode());
	}
	
	@Override
	public void logError(String message) {
		getBootstrap().getProxy().getLogger().log(Level.WARNING, "[" + ConsoleColor.CYAN.getCode() + "Parties" + ConsoleColor.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)) + ConsoleColor.RESET.getCode());
	}
}
