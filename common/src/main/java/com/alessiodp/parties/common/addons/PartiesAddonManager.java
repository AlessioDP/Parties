package com.alessiodp.parties.common.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.AddonManager;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import com.alessiodp.parties.common.addons.external.LuckPermsHandler;
import org.jetbrains.annotations.NotNull;

public abstract class PartiesAddonManager extends AddonManager {
	private final LLAPIHandler llapiHandler;
	private final LuckPermsHandler luckPermsHandler;
	
	public PartiesAddonManager(@NotNull ADPPlugin plugin) {
		super(plugin);
		llapiHandler = new LLAPIHandler(plugin);
		luckPermsHandler = new LuckPermsHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		plugin.getLoggerManager().logDebug(Constants.DEBUG_ADDON_INIT, true);
		
		llapiHandler.init();
		
		// Schedule sync later (post load plugins)
		plugin.getScheduler().getSyncExecutor().execute(this::postLoadAddons);
	}
	
	public void postLoadAddons() {
		luckPermsHandler.init();
	}
}
