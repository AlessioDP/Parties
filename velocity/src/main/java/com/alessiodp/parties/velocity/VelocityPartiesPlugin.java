package com.alessiodp.parties.velocity;

import com.alessiodp.core.common.bootstrap.ADPBootstrap;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.velocity.addons.internal.json.VelocityJsonHandler;
import com.alessiodp.core.velocity.addons.internal.title.VelocityTitleHandler;
import com.alessiodp.core.velocity.bootstrap.ADPVelocityBootstrap;
import com.alessiodp.core.velocity.scheduling.ADPVelocityScheduler;
import com.alessiodp.core.velocity.user.VelocityUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.addons.VelocityPartiesAddonManager;
import com.alessiodp.parties.velocity.addons.external.VelocityMetricsHandler;
import com.alessiodp.parties.velocity.commands.VelocityPartiesCommandManager;
import com.alessiodp.parties.velocity.configuration.VelocityPartiesConfigurationManager;
import com.alessiodp.parties.velocity.events.VelocityEventManager;
import com.alessiodp.parties.velocity.listeners.VelocityChatListener;
import com.alessiodp.parties.velocity.listeners.VelocityFollowListener;
import com.alessiodp.parties.velocity.listeners.VelocityJoinLeaveListener;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessenger;
import com.alessiodp.parties.velocity.parties.VelocityPartyManager;
import com.alessiodp.parties.velocity.players.VelocityPlayerManager;
import com.alessiodp.parties.velocity.utils.VelocityEconomyManager;
import com.alessiodp.parties.velocity.utils.VelocityMessageUtils;
import com.velocitypowered.api.event.EventManager;
import lombok.Getter;

public class VelocityPartiesPlugin extends PartiesPlugin {
	@Getter private final int bstatsId = PartiesConstants.PLUGIN_BSTATS_VELOCITY_ID;
	
	public VelocityPartiesPlugin(ADPBootstrap bootstrap) {
		super(bootstrap);
	}
	
	@Override
	protected void initializeCore() {
		scheduler = new ADPVelocityScheduler(this);
		configurationManager = new VelocityPartiesConfigurationManager(this);
		messageUtils = new VelocityMessageUtils(this);
		messenger = new VelocityPartiesMessenger(this);
		
		super.initializeCore();
	}
	
	@Override
	protected void loadCore() {
		partyManager = new VelocityPartyManager(this);
		playerManager = new VelocityPlayerManager(this);
		commandManager = new VelocityPartiesCommandManager(this);
		
		super.loadCore();
	}
	
	@Override
	protected void postHandle() {
		addonManager = new VelocityPartiesAddonManager(this);
		economyManager = new VelocityEconomyManager(this);
		eventManager = new VelocityEventManager(this);
		
		super.postHandle();
		
		new VelocityMetricsHandler(this);
		((VelocityPartiesConfigurationManager) getConfigurationManager()).makeConfigsSync();
	}
	
	@Override
	protected  void initializeJsonHandler() {
		jsonHandler = new VelocityJsonHandler(this);
	}
	
	@Override
	protected  void initializeTitleHandler() {
		titleHandler = new VelocityTitleHandler(this);
	}
	
	@Override
	protected void registerListeners() {
		getLoggerManager().logDebug(Constants.DEBUG_PLUGIN_REGISTERING, true);
		ADPVelocityBootstrap plugin = (ADPVelocityBootstrap) getBootstrap();
		EventManager em = plugin.getServer().getEventManager();
		em.register(plugin, new VelocityChatListener(this));
		em.register(plugin, new VelocityFollowListener(this));
		em.register(plugin, new VelocityJoinLeaveListener(this));
	}
	
	@Override
	public void reloadConfiguration() {
		super.reloadConfiguration();
		
		((VelocityPartiesConfigurationManager) getConfigurationManager()).makeConfigsSync();
	}
	
	@Override
	public boolean isBungeeCordEnabled() {
		return false;
	}
	
	@Override
	public String getServerName(PartyPlayerImpl player) {
		if (player != null) {
			VelocityUser user = (VelocityUser) getPlayer(player.getPlayerUUID());
			if (user != null)
				return user.getServerName();
		}
		return "";
	}
	
	@Override
	public String getServerId(PartyPlayerImpl player) {
		return getServerName(player);
	}
}
