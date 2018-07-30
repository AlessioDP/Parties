package com.alessiodp.parties.common;

import com.alessiodp.parties.common.addons.AddonManager;
import com.alessiodp.parties.common.addons.internal.ADPUpdater;
import com.alessiodp.parties.common.addons.libraries.LibraryManager;
import com.alessiodp.parties.common.api.ApiHandler;
import com.alessiodp.parties.common.bootstrap.AbstractPartiesBootstrap;
import com.alessiodp.parties.common.bootstrap.PartiesBootstrap;
import com.alessiodp.parties.common.commands.CommandManager;
import com.alessiodp.parties.common.configuration.ConfigurationManager;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.ColorManager;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.RankManager;
import com.alessiodp.parties.common.players.SpyManager;
import com.alessiodp.parties.common.scheduling.PartiesScheduler;
import com.alessiodp.parties.common.user.OfflineUser;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.storage.DatabaseManager;
import com.alessiodp.parties.common.utils.ConsoleColor;
import com.alessiodp.parties.common.utils.DebugUtils;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.MessageUtils;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

public abstract class PartiesPlugin extends AbstractPartiesBootstrap {
	// Plugin fields
	@Getter private static PartiesPlugin instance;
	@Getter private PartiesAPI api;
	
	// Common fields
	@Getter protected ConfigurationManager configManager;
	@Getter @Setter protected DatabaseManager databaseManager;
	@Getter protected LibraryManager libraryManager;
	@Getter protected MessageUtils messageUtils;
	@Getter protected PartiesScheduler partiesScheduler;
	
	@Getter protected AddonManager addonManager;
	@Getter protected ColorManager colorManager;
	@Getter protected PartyManager partyManager;
	@Getter protected PlayerManager playerManager;
	@Getter protected RankManager rankManager;
	@Getter protected SpyManager spyManager;
	
	@Getter protected CommandManager commandManager;
	@Getter protected EventManager eventManager;
	@Getter protected EconomyManager economyManager;
	@Getter protected CooldownManager cooldownManager;
	
	
	protected PartiesPlugin(PartiesBootstrap instance) {
		super(instance);
	}
	
	public void enabling() {
		// Init
		instance = this;
		long nsTime = System.nanoTime();
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_ENABLING
				.replace("{version}", this.getVersion()));
		
		preHandle();
		handle();
		
		if (!getDatabaseManager().isShutdownPlugin()) {
			postHandle();
			
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_PARTIES_ENABLED
					.replace("{version}", this.getVersion()), true, ConsoleColor.CYAN);
			DebugUtils.debugLog(String.format("Parties loaded in %.2fms", (System.nanoTime() - nsTime) / 1000000.0));
		}
	}
	public void disabling() {
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
	
	/* Handle methods */
	protected void preHandle() {
		new LoggerManager(this);
		libraryManager = new LibraryManager(this);
	}
	protected void handle() {
		getConfigManager().reload();
		LoggerManager.reload();
		getDatabaseManager().reload();
		if (getDatabaseManager().isShutdownPlugin()) {
			// Storage error, shutdown plugin
			LoggerManager.printError(Constants.DEBUG_DB_INIT_FAILED_STOP);
			super.getBootstrap().stopPlugin();
			return;
		}
		
		rankManager = new RankManager(this);
		colorManager = new ColorManager();
		spyManager = new SpyManager(this);
	}
	protected void postHandle() {
		getPartyManager().reload();
		getPlayerManager().reload();
		getAddonManager().loadAddons();
		registerListeners();
		
		api = new ApiHandler(this);
		Parties.setApi(api);
		new ADPUpdater(this);
		ADPUpdater.asyncTaskCheckUpdates();
		DebugUtils.startDebugTask(this);
	}
	
	/* Loading methods */
	protected abstract void registerListeners();
	
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
		getCommandManager().setup();
		
		ADPUpdater.asyncCheckUpdates();
	}
	
	/* Player methods */
	public abstract List<User> getOnlinePlayers();
	public abstract User getPlayer(UUID uuid);
	public abstract User getPlayerByName(String name);
	public abstract OfflineUser getOfflinePlayer(UUID uuid);
	
	/* Log methods */
	public abstract void log(String message);
	public abstract void logError(String message);
}
