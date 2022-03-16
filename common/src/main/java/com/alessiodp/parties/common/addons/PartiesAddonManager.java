package com.alessiodp.parties.common.addons;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.AddonManager;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import org.jetbrains.annotations.NotNull;

public abstract class PartiesAddonManager extends AddonManager {
	private final LLAPIHandler llapiHandler;
	
	public PartiesAddonManager(@NotNull ADPPlugin plugin) {
		super(plugin);
		llapiHandler = new LLAPIHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		plugin.getLoggerManager().logDebug(Constants.DEBUG_ADDON_INIT, true);
		
		llapiHandler.init();
	}
}
