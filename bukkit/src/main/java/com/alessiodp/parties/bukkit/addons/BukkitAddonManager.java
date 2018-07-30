package com.alessiodp.parties.bukkit.addons;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.addons.external.BanManagerHandler;
import com.alessiodp.parties.bukkit.addons.external.CrackShotHandler;
import com.alessiodp.parties.bukkit.addons.external.DynmapHandler;
import com.alessiodp.parties.bukkit.addons.external.EssentialsChatHandler;
import com.alessiodp.parties.bukkit.addons.external.GriefPreventionHandler;
import com.alessiodp.parties.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.bukkit.addons.external.VaultHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.AddonManager;

public class BukkitAddonManager extends AddonManager {
	
	public BukkitAddonManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadAddons() {
		new BanManagerHandler(plugin);
		new CrackShotHandler((BukkitPartiesPlugin) plugin);
		new DynmapHandler((BukkitPartiesPlugin) plugin);
		new EssentialsChatHandler((BukkitPartiesPlugin) plugin);
		new GriefPreventionHandler();
		new PlaceholderAPIHandler(plugin);
		new VaultHandler((BukkitPartiesPlugin) plugin);
	}
}
