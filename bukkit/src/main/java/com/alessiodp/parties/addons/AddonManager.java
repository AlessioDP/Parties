package com.alessiodp.parties.addons;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.BanManagerHandler;
import com.alessiodp.parties.addons.external.CrackShotHandler;
import com.alessiodp.parties.addons.external.DynmapHandler;
import com.alessiodp.parties.addons.external.EssentialsChatHandler;
import com.alessiodp.parties.addons.external.GriefPreventionHandler;
import com.alessiodp.parties.addons.external.MythicMobsHandler;
import com.alessiodp.parties.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.addons.external.ProtocolLibHandler;
import com.alessiodp.parties.addons.external.SkillAPIHandler;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.addons.internal.BungeeService;

public class AddonManager {
	private Parties plugin;
	
	public AddonManager(Parties instance) {
		plugin = instance;
	}
	
	public void loadAddons() {
		new BanManagerHandler(plugin);
		new BungeeService(plugin);
		new CrackShotHandler(plugin);
		new DynmapHandler(plugin);
		new EssentialsChatHandler(plugin);
		new GriefPreventionHandler();
		new MythicMobsHandler(plugin);
		new PlaceholderAPIHandler(plugin);
		new ProtocolLibHandler(plugin);
		new SkillAPIHandler();
		new VaultHandler(plugin);
	}
}
