package com.alessiodp.parties.bungeecord;

import com.alessiodp.core.bungeecord.addons.internal.json.BungeeJsonHandler;
import com.alessiodp.core.bungeecord.addons.internal.title.BungeeTitleHandler;
import com.alessiodp.core.bungeecord.scheduling.ADPBungeeScheduler;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bungeecord.addons.BungeePartiesAddonManager;
import com.alessiodp.parties.bungeecord.addons.external.BungeeMetricsHandler;
import com.alessiodp.parties.bungeecord.commands.BungeePartiesCommandManager;
import com.alessiodp.parties.bungeecord.configuration.BungeePartiesConfigurationManager;
import com.alessiodp.parties.bungeecord.events.BungeeEventManager;
import com.alessiodp.parties.bungeecord.listeners.BungeeChatListener;
import com.alessiodp.parties.bungeecord.listeners.BungeeFollowListener;
import com.alessiodp.parties.bungeecord.listeners.BungeeJoinLeaveListener;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessenger;
import com.alessiodp.parties.bungeecord.parties.BungeePartyManager;
import com.alessiodp.parties.bungeecord.players.BungeePlayerManager;
import com.alessiodp.parties.bungeecord.utils.BungeeEconomyManager;
import com.alessiodp.parties.bungeecord.utils.BungeeMessageUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.ExpManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeePartiesPlugin extends PartiesPlugin {
	@Getter private final int bstatsId = PartiesConstants.PLUGIN_BSTATS_BUNGEE_ID;
	
	public BungeePartiesPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	protected void initializeCore() {
		scheduler = new ADPBungeeScheduler(this);
		configurationManager = new BungeePartiesConfigurationManager(this);
		messageUtils = new BungeeMessageUtils(this);
		messenger = new BungeePartiesMessenger(this);
		
		super.initializeCore();
	}
	
	@Override
	protected void loadCore() {
		partyManager = new BungeePartyManager(this);
		playerManager = new BungeePlayerManager(this);
		commandManager = new BungeePartiesCommandManager(this);
		
		super.loadCore();
	}
	
	@Override
	protected void postHandle() {
		addonManager = new BungeePartiesAddonManager(this);
		cooldownManager = new CooldownManager(this);
		economyManager = new BungeeEconomyManager(this);
		expManager = new ExpManager(this);
		eventManager = new BungeeEventManager(this);
		
		super.postHandle();
		
		new BungeeMetricsHandler(this);
		((BungeePartiesConfigurationManager) getConfigurationManager()).makeConfigsSync();
	}
	
	@Override
	protected  void initializeJsonHandler() {
		jsonHandler = new BungeeJsonHandler();
	}
	
	@Override
	protected  void initializeTitleHandler() {
		titleHandler = new BungeeTitleHandler();
	}
	
	@Override
	protected void registerListeners() {
		getLoggerManager().logDebug(Constants.DEBUG_PLUGIN_REGISTERING, true);
		Plugin plugin = (Plugin) getBootstrap();
		PluginManager pm = plugin.getProxy().getPluginManager();
		pm.registerListener(plugin, new BungeeChatListener(this));
		pm.registerListener(plugin, new BungeeFollowListener(this));
		pm.registerListener(plugin, new BungeeJoinLeaveListener(this));
	}
	
	@Override
	public void reloadConfiguration() {
		super.reloadConfiguration();
		
		((BungeePartiesConfigurationManager) getConfigurationManager()).makeConfigsSync();
	}
	
	@Override
	public boolean isBungeeCordEnabled() {
		return false;
	}
	
	@Override
	public String getServerName() {
		return "";
	}
	
	@Override
	public String getServerId() {
		return "";
	}
}
