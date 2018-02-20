package com.alessiodp.parties;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.alessiodp.parties.addons.AddonManager;
import com.alessiodp.parties.addons.external.MetricsHandler;
import com.alessiodp.parties.addons.internal.ADPUpdater;
import com.alessiodp.parties.addons.libraries.LibraryManager;
import com.alessiodp.parties.api.ApiHandler;
import com.alessiodp.parties.commands.CommandManager;
import com.alessiodp.parties.configuration.ConfigurationManager;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.events.ChatListener;
import com.alessiodp.parties.events.FightListener;
import com.alessiodp.parties.events.FollowListener;
import com.alessiodp.parties.events.JoinLeaveListener;
import com.alessiodp.parties.events.MoveListener;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.ColorManager;
import com.alessiodp.parties.parties.PartyManager;
import com.alessiodp.parties.parties.TagManager;
import com.alessiodp.parties.players.PlayerManager;
import com.alessiodp.parties.players.RankManager;
import com.alessiodp.parties.players.SpyManager;
import com.alessiodp.parties.storage.DatabaseManager;
import com.alessiodp.parties.utils.ConsoleColor;
import com.alessiodp.parties.utils.DebugUtils;
import com.alessiodp.parties.utils.PartiesScheduler;
import com.alessiodp.parties.utils.PartiesUtils;

import lombok.Getter;
import lombok.Setter;

public class Parties extends JavaPlugin {
	private static Parties instance;
	
	@Getter private PartiesScheduler			partiesScheduler;
	@Getter private ConfigurationManager		configManager;
	
	@Getter private AddonManager				addonManager;
	@Getter private PlayerManager				playerManager;
	@Getter private PartyManager				partyManager;
	@Getter private RankManager					rankManager;
	@Getter private SpyManager					spyManager;
	@Getter private ColorManager				colorManager;
	@Getter private TagManager					tagManager;
	@Getter private LibraryManager 				libraryManager;
	
	@Getter private CommandManager				commandManager;
	
	@Getter @Setter private DatabaseManager		databaseManager;
	
	
	public static Parties getInstance() {return instance;}
	
	@Override
	public void onEnable() {
		/* init */
		instance = this;
		long nsTime = System.nanoTime();
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_ENABLING
				.replace("{version}", getDescription().getVersion()));
		
		handle();
		
		if (!getDatabaseManager().isShutdownPlugin()) {
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_PARTIES_ENABLED
					.replace("{version}", getDescription().getVersion()), true, ConsoleColor.CYAN);
			DebugUtils.debugLog(String.format("Parties loaded in %.2fms", (System.nanoTime() - nsTime) / 1000000.0));
		}
	}
	
	@Override
	public void onDisable() {
		// Disable bukkit scheduler
		partiesScheduler.setUseBukkitScheduler(false);
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_DISABLING);
		
		if (databaseManager != null && !databaseManager.isShutdownPlugin()) {
			// This is not a force close
			getPartyManager().resetPendingPartyTask();
			getDatabaseManager().stop();
		}
		
		LoggerManager.log(LogLevel.BASE, Constants.DEBUG_PARTIES_DISABLED_LOG, false);
		partiesScheduler.shutdown();
		
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_DISABLED);
	}
	
	private void handle() {
		// Pre
		new LoggerManager(this);
		partiesScheduler = new PartiesScheduler(this);
		databaseManager = new DatabaseManager(this);
		com.alessiodp.partiesapi.Parties.setApi(new ApiHandler(this));
		
		
		// Peri
		new ADPUpdater(this);
		new PartiesUtils();
		configManager = new ConfigurationManager(this);
		libraryManager = new LibraryManager(this);
		
		getConfigManager().reload();
		LoggerManager.reload();
		getDatabaseManager().reload();
		if (getDatabaseManager().isShutdownPlugin()) {
			// Storage error, shutdown plugin
			LoggerManager.printError(Constants.DEBUG_DB_INIT_FAILED_STOP);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		rankManager = new RankManager();
		colorManager = new ColorManager();
		tagManager = new TagManager();
		spyManager = new SpyManager(this);
		
		partyManager = new PartyManager(this);
		playerManager = new PlayerManager(this);
		addonManager = new AddonManager(this);
		
		getPartyManager().reload();
		getPlayerManager().reload();
		getAddonManager().loadAddons();
		new MetricsHandler(this);
		
		
		// Post
		registerListeners();
		commandManager = new CommandManager(this);
		getCommandManager().registerCommands();
		ADPUpdater.asyncTaskCheckUpdates();
		
		DebugUtils.startDebugTask(this);
	}
	
	private void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ChatListener(this), this);
		pm.registerEvents(new FightListener(this), this);
		pm.registerEvents(new FollowListener(this), this);
		pm.registerEvents(new JoinLeaveListener(this), this);
		pm.registerEvents(new MoveListener(this), this);
	}
	
	public void reloadConfiguration() {
		// Delete pending parties
		getPartyManager().resetPendingPartyTask();
		
		
		getConfigManager().reload();
		LoggerManager.reload();
		getDatabaseManager().reload();
		
		getRankManager().reload();
		getColorManager().reload();
		
		getPartyManager().reload();
		getPlayerManager().reload();
		getAddonManager().loadAddons();
		getCommandManager().registerCommands();
		
		ADPUpdater.asyncCheckUpdates();
	}
	
	
	/*
	 * Log
	 */
	public void log(String msg) {
		getServer().getLogger().log(Level.INFO, "[" + ConsoleColor.CYAN.getCode() + "Parties" + ConsoleColor.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)) + ConsoleColor.RESET.getCode());
	}
	
	public void log(Level level, String msg) {
		getServer().getLogger().log(level, "[" + ConsoleColor.CYAN.getCode() + "Parties" + ConsoleColor.RESET.getCode() + "] " + ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', msg)) + ConsoleColor.RESET.getCode());
	}
}
