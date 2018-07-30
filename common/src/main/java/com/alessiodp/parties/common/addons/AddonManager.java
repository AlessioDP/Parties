package com.alessiodp.parties.common.addons;

import com.alessiodp.parties.common.PartiesPlugin;

public abstract class AddonManager {
	protected PartiesPlugin plugin;
	
	protected AddonManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public abstract void loadAddons();
}
