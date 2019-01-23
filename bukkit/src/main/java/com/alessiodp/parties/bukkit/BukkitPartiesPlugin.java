package com.alessiodp.parties.bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import com.alessiodp.parties.bukkit.addons.BukkitAddonManager;
import com.alessiodp.parties.bukkit.addons.external.MetricsHandler;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.commands.BukkitCommandManager;
import com.alessiodp.parties.bukkit.listeners.BukkitExpListener;
import com.alessiodp.parties.bukkit.listeners.BukkitFightListener;
import com.alessiodp.parties.bukkit.messaging.MessageManager;
import com.alessiodp.parties.bukkit.parties.BukkitPartyManager;
import com.alessiodp.parties.bukkit.players.BukkitPlayerManager;
import com.alessiodp.parties.bukkit.players.ExpManager;
import com.alessiodp.parties.bukkit.user.BukkitOfflineUser;
import com.alessiodp.parties.bukkit.user.BukkitUser;
import com.alessiodp.parties.bukkit.utils.BukkitEconomyManager;
import com.alessiodp.parties.common.bootstrap.PartiesBootstrap;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.storage.DatabaseManager;
import com.alessiodp.parties.common.user.OfflineUser;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.ConsoleColor;
import com.alessiodp.parties.bukkit.configuration.BukkitConfigurationManager;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.events.BukkitEventManager;
import com.alessiodp.parties.bukkit.listeners.BukkitChatListener;
import com.alessiodp.parties.bukkit.listeners.BukkitFollowListener;
import com.alessiodp.parties.bukkit.listeners.BukkitJoinLeaveListener;
import com.alessiodp.parties.bukkit.scheduling.BukkitPartiesScheduler;
import com.alessiodp.parties.bukkit.utils.BukkitMessageUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class BukkitPartiesPlugin extends PartiesPlugin {
	@Getter private ExpManager expManager;
	@Getter private MessageManager messageManager;
	
	public BukkitPartiesPlugin(PartiesBootstrap instance) {
		super(instance);
	}
	
	@Override
	public void disabling() {
		// Disable bukkit scheduler
		((BukkitPartiesScheduler) partiesScheduler).setUseBukkitScheduler(false);
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_DISABLING);
		
		super.disabling();
	}
	
	@Override
	protected void preHandle() {
		super.preHandle();
		partiesScheduler = new BukkitPartiesScheduler(this);
		databaseManager = new DatabaseManager(this);
		configManager = new BukkitConfigurationManager(this,
				new BukkitConfigMain(this),
				new BukkitConfigParties(this),
				new BukkitMessages(this));
		messageUtils = new BukkitMessageUtils(this);
	}
	
	@Override
	protected void handle() {
		super.handle();
		partyManager = new BukkitPartyManager(this);
		playerManager = new BukkitPlayerManager(this);
		addonManager = new BukkitAddonManager(this);
		expManager = new ExpManager(this);
		messageManager = new MessageManager(this);
	}
	
	@Override
	protected void postHandle() {
		super.postHandle();
		commandManager = new BukkitCommandManager(this);
		getCommandManager().setup();
		eventManager = new BukkitEventManager(this);
		economyManager = new BukkitEconomyManager(this);
		
		new MetricsHandler(this);
	}
	
	@Override
	protected void registerListeners() {
		PluginManager pm = getBootstrap().getServer().getPluginManager();
		pm.registerEvents(new BukkitChatListener(this), getBootstrap());
		pm.registerEvents(new BukkitExpListener(this), getBootstrap());
		pm.registerEvents(new BukkitFightListener(this), getBootstrap());
		pm.registerEvents(new BukkitFollowListener(this), getBootstrap());
		pm.registerEvents(new BukkitJoinLeaveListener(this), getBootstrap());
	}
	
	@Override
	public void reloadConfiguration() {
		super.reloadConfiguration();
		expManager.reload();
		messageManager.reload();
	}
	
	@Override
	public List<User> getOnlinePlayers() {
		List<User> ret = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			ret.add(new BukkitUser(player));
		}
		return ret;
	}
	
	@Override
	public User getPlayer(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		return player != null ? new BukkitUser(player) : null;
	}
	
	@Override
	public User getPlayerByName(String name) {
		Player player = Bukkit.getPlayer(name);
		return player != null ? new BukkitUser(player) : null;
	}
	
	@Override
	public OfflineUser getOfflinePlayer(UUID uuid) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		return player != null ? new BukkitOfflineUser(player) : null;
	}
	
	@Override
	public BukkitPartiesBootstrap getBootstrap() {
		return (BukkitPartiesBootstrap) super.getBootstrap();
	}
	
	@Override
	public void log(String message) {
		getBootstrap().getServer().getLogger().log(Level.INFO, "[" + ConsoleColor.CYAN.getCode() + "Parties" + ConsoleColor.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)) + ConsoleColor.RESET.getCode());
	}
	@Override
	public void logError(String message) {
		getBootstrap().getServer().getLogger().log(Level.WARNING, "[" + ConsoleColor.CYAN.getCode() + "Parties" + ConsoleColor.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', message)) + ConsoleColor.RESET.getCode());
	}
}
