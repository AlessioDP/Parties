package com.alessiodp.parties.common;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.ExternalLibraries;
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
import com.alessiodp.parties.common.parties.ExpManager;
import com.alessiodp.parties.common.parties.PartyManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.RankManager;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.Parties;
import com.alessiodp.parties.common.utils.MessageUtils;
import com.alessiodp.parties.common.utils.PartiesPlayerUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public abstract class PartiesPlugin extends ADPPlugin {
	// Plugin fields
	@Getter private final String pluginName = PartiesConstants.PLUGIN_NAME;
	@Getter private final String pluginFallbackName = PartiesConstants.PLUGIN_FALLBACK;
	@Getter private final ConsoleColor consoleColor = PartiesConstants.PLUGIN_CONSOLECOLOR;
	@Getter private final String packageName = PartiesConstants.PLUGIN_PACKAGENAME;
	
	// Parties fields
	@Getter protected PartiesAPI api;
	@Getter protected ColorManager colorManager;
	@Getter protected PartyManager partyManager;
	@Getter protected PlayerManager playerManager;
	@Getter protected RankManager rankManager;
	
	@Getter protected CooldownManager cooldownManager;
	@Getter protected EventManager eventManager;
	@Getter protected ExpManager expManager;
	@Getter protected EconomyManager economyManager;
	@Getter protected MessageUtils messageUtils;
	
	public PartiesPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	public void onDisabling() {
		if (getPartyManager() != null) // Can be null if plugin failed to load
			getPartyManager().disbandLoadedParties();
	}
	
	@Override
	protected void initializeCore() {
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
		colorManager = new ColorManager();
		cooldownManager = new CooldownManager(this);
		playerUtils = new PartiesPlayerUtils(this);
		rankManager = new RankManager(this);
		
		getPartyManager().reload();
		getPlayerManager().reload();
		getCommandManager().setup();
		getMessenger().reload();
		getExpManager().reload();
		registerListeners();
		
		reloadAdpUpdater();
		getAddonManager().loadAddons();
		Parties.setApi(api);
	}
	
	protected abstract void registerListeners();
	
	@Override
	public void reloadConfiguration() {
		getLoggerManager().logDebug(PartiesConstants.DEBUG_PLUGIN_RELOADING, true);
		getLoginAlertsManager().reload();
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
		getExpManager().reload();
		
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
				PartiesPermission.ADMIN_ALERTS,
				Messages.PARTIES_UPDATEAVAILABLE
		);
		getAdpUpdater().asyncTaskCheckUpdates();
	}
	
	public abstract boolean isBungeeCordEnabled();
	
	public abstract String getServerName(PartyPlayerImpl player);
	public abstract String getServerId(PartyPlayerImpl player);
	
	@Override
	public List<ExternalLibraries.Usage> getLibrariesUsages() {
		return Arrays.asList(
				ExternalLibraries.Usage.H2,
				ExternalLibraries.Usage.MYSQL,
				ExternalLibraries.Usage.MARIADB,
				ExternalLibraries.Usage.POSTGRESQL,
				ExternalLibraries.Usage.SQLITE,
				ExternalLibraries.Usage.SCRIPT
		);
	}
}
