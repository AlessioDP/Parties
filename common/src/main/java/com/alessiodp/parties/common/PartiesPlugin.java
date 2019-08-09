package com.alessiodp.parties.common;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.logging.ConsoleColor;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import com.alessiodp.parties.common.api.ApiHandler;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.events.EventManager;
import com.alessiodp.parties.common.parties.ColorManager;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.RankManager;
import com.alessiodp.parties.common.players.spy.SpyManager;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.utils.CensorUtils;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.common.utils.MessageUtils;
import com.alessiodp.parties.common.utils.PartiesPlayerUtils;
import lombok.Getter;

import java.util.ArrayList;

public abstract class PartiesPlugin extends ADPPlugin {
	// Plugin fields
	@Getter private final String pluginName = PartiesConstants.PLUGIN_NAME;
	@Getter private final String pluginFallbackName = PartiesConstants.PLUGIN_FALLBACK;
	@Getter private final ConsoleColor consoleColor = PartiesConstants.PLUGIN_CONSOLECOLOR;
	
	// Parties fields
	@Getter protected PartiesAPI api;
	@Getter protected ColorManager colorManager;
	@Getter protected PartyManager partyManager;
	@Getter protected PlayerManager playerManager;
	@Getter protected RankManager rankManager;
	@Getter protected SpyManager spyManager;
	
	@Getter protected CensorUtils censorUtils;
	@Getter protected CooldownManager cooldownManager;
	@Getter protected EventManager eventManager;
	@Getter protected EconomyManager economyManager;
	@Getter protected MessageUtils messageUtils;
	
	@Getter private ArrayList<String> loginAlerts;
	
	public PartiesPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	public void onDisabling() {
		if (databaseManager != null) {
			getPartyManager().resetPendingPartyTask();
		}
	}
	
	@Override
	protected void initializeCore() {
		loginAlerts = new ArrayList<>();
		databaseManager = new PartiesDatabaseManager(this);
	}
	
	@Override
	protected void loadCore() {
		getConfigurationManager().reload();
		reloadLoggerManager();
		getDatabaseManager().reload();
	}
	
	@Override
	protected void postHandle() {
		api = new ApiHandler(this);
		censorUtils = new CensorUtils(this);
		colorManager = new ColorManager();
		cooldownManager = new CooldownManager();
		playerUtils = new PartiesPlayerUtils(this);
		rankManager = new RankManager(this);
		spyManager = new SpyManager(this);
		
		getPartyManager().reload();
		getPlayerManager().reload();
		getCommandManager().setup();
		getMessenger().reload();
		registerListeners();
		
		reloadAdpUpdater();
		getAddonManager().loadAddons();
		Parties.setApi(api);
	}
	
	protected abstract void registerListeners();
	
	@Override
	public void reloadConfiguration() {
		getLoggerManager().logDebug(PartiesConstants.DEBUG_PLUGIN_RELOADING, true);
		loginAlerts.clear();
		getConfigurationManager().reload();
		reloadLoggerManager();
		getDatabaseManager().reload();
		
		getRankManager().reload();
		getColorManager().reload();
		getPartyManager().reload();
		getPlayerManager().reload();
		getAddonManager().loadAddons();
		getCommandManager().setup();
		getMessenger().reload();
		
		reloadAdpUpdater();
	}
	
	@Override
	public PartiesDatabaseManager getDatabaseManager() {
		return (PartiesDatabaseManager) databaseManager;
	}
	
	private void reloadLoggerManager() {
		getLoggerManager().reload(
				ConfigMain.PARTIES_LOGGING_DEBUG,
				ConfigMain.PARTIES_LOGGING_SAVE_ENABLE,
				ConfigMain.PARTIES_LOGGING_SAVE_FILE,
				ConfigMain.PARTIES_LOGGING_SAVE_FORMAT
		);
	}
	
	private void reloadAdpUpdater() {
		getAdpUpdater().reload(
				getPluginFallbackName(),
				PartiesConstants.PLUGIN_SPIGOTCODE,
				ConfigMain.PARTIES_UPDATES_CHECK,
				ConfigMain.PARTIES_UPDATES_WARN,
				PartiesPermission.ADMIN_ALERTS.toString(),
				Messages.PARTIES_UPDATEAVAILABLE
		);
		getAdpUpdater().asyncTaskCheckUpdates();
	}
	
	public abstract boolean isBungeeCordEnabled();
}
