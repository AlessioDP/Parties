package com.alessiodp.parties.addons;

import org.bukkit.Bukkit;

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
		if (Bukkit.getPluginManager().isPluginEnabled("BanManager"))
			new BanManagerHandler(plugin);
		new BungeeService(plugin);
		if (Bukkit.getPluginManager().isPluginEnabled("CrackShot"))
			new CrackShotHandler(plugin);
		new DynmapHandler(plugin);
		new EssentialsChatHandler(plugin);
		new GriefPreventionHandler();
		new MythicMobsHandler(plugin);
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
				new PlaceholderAPIHandler(plugin);
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib"))
			new ProtocolLibHandler(plugin);
		if (Bukkit.getPluginManager().isPluginEnabled("SkillAPI"))
			new SkillAPIHandler();
		if (Bukkit.getPluginManager().isPluginEnabled("Vault"))
			new VaultHandler(plugin);
	}
}
