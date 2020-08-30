package com.alessiodp.parties.bukkit;

import com.alessiodp.core.bukkit.addons.internal.json.BukkitJsonHandler;
import com.alessiodp.core.bukkit.addons.internal.json.SpigotJsonHandler;
import com.alessiodp.core.bukkit.addons.internal.title.BukkitTitleHandler;
import com.alessiodp.core.bukkit.scheduling.ADPBukkitScheduler;
import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.addons.BukkitPartiesAddonManager;
import com.alessiodp.parties.bukkit.addons.external.BukkitMetricsHandler;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.commands.BukkitPartiesCommandManager;
import com.alessiodp.parties.bukkit.configuration.BukkitPartiesConfigurationManager;
import com.alessiodp.parties.bukkit.events.BukkitEventManager;
import com.alessiodp.parties.bukkit.listeners.BukkitExpListener;
import com.alessiodp.parties.bukkit.listeners.BukkitFightListener;
import com.alessiodp.parties.bukkit.messaging.BukkitPartiesMessenger;
import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
import com.alessiodp.parties.bukkit.parties.BukkitPartyManager;
import com.alessiodp.parties.bukkit.players.BukkitPlayerManager;
import com.alessiodp.parties.bukkit.players.ExpManager;
import com.alessiodp.parties.bukkit.utils.BukkitEconomyManager;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.listeners.BukkitChatListener;
import com.alessiodp.parties.bukkit.listeners.BukkitFollowListener;
import com.alessiodp.parties.bukkit.listeners.BukkitJoinLeaveListener;
import com.alessiodp.parties.bukkit.utils.BukkitMessageUtils;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class BukkitPartiesPlugin extends PartiesPlugin {
	@Getter private final int bstatsId = PartiesConstants.PLUGIN_BSTATS_BUKKIT_ID;
	@Getter private ExpManager expManager;
	
	public BukkitPartiesPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	protected void initializeCore() {
		scheduler = new ADPBukkitScheduler(this);
		configurationManager = new BukkitPartiesConfigurationManager(this);
		messageUtils = new BukkitMessageUtils(this);
		messenger = new BukkitPartiesMessenger(this);
		
		super.initializeCore();
	}
	
	@Override
	protected void loadCore() {
		partyManager = new BukkitPartyManager(this);
		playerManager = new BukkitPlayerManager(this);
		commandManager = new BukkitPartiesCommandManager(this);
		
		super.loadCore();
	}
	
	@Override
	protected void postHandle() {
		addonManager = new BukkitPartiesAddonManager(this);
		cooldownManager = new BukkitCooldownManager(this);
		economyManager = new BukkitEconomyManager(this);
		expManager = new ExpManager(this);
		eventManager = new BukkitEventManager(this);
		
		super.postHandle();
		getExpManager().reload();
		
		new BukkitMetricsHandler(this);
	}
	
	@Override
	protected  void initializeJsonHandler() {
		if (((BukkitPartiesBootstrap) getBootstrap()).isSpigot())
			jsonHandler = new SpigotJsonHandler();
		else
			jsonHandler = new BukkitJsonHandler();
	}
	
	@Override
	protected  void initializeTitleHandler() {
		titleHandler = new BukkitTitleHandler();
	}
	
	@Override
	protected void registerListeners() {
		getLoggerManager().logDebug(Constants.DEBUG_PLUGIN_REGISTERING, true);
		PluginManager pm = ((Plugin) getBootstrap()).getServer().getPluginManager();
		pm.registerEvents(new BukkitChatListener(this), ((Plugin) getBootstrap()));
		pm.registerEvents(new BukkitExpListener(this), ((Plugin) getBootstrap()));
		pm.registerEvents(new BukkitFightListener(this), ((Plugin) getBootstrap()));
		pm.registerEvents(new BukkitFollowListener(this), ((Plugin) getBootstrap()));
		pm.registerEvents(new BukkitJoinLeaveListener(this), ((Plugin) getBootstrap()));
	}
	
	@Override
	public void reloadConfiguration() {
		super.reloadConfiguration();
		getExpManager().reload();
	}
	
	@Override
	public boolean isBungeeCordEnabled() {
		return BukkitConfigMain.PARTIES_BUNGEECORDSYNC_ENABLE;
	}
	
	@Override
	public String getServerName() {
		return BukkitConfigMain.PARTIES_BUNGEECORD_SERVER_NAME;
	}
	
	@Override
	public String getServerId() {
		return BukkitConfigMain.PARTIES_BUNGEECORD_SERVER_ID;
	}
}
