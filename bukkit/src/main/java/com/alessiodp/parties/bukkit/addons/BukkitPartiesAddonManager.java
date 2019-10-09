package com.alessiodp.parties.bukkit.addons;

import com.alessiodp.parties.bukkit.addons.external.BanManagerHandler;
import com.alessiodp.parties.bukkit.addons.external.CrackShotHandler;
import com.alessiodp.parties.bukkit.addons.external.DynmapHandler;
import com.alessiodp.parties.bukkit.addons.external.EssentialsChatHandler;
import com.alessiodp.parties.bukkit.addons.external.GriefPreventionHandler;
import com.alessiodp.parties.bukkit.addons.external.MythicMobsHandler;
import com.alessiodp.parties.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.bukkit.addons.external.SkillAPIHandler;
import com.alessiodp.parties.bukkit.addons.external.VaultHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.PartiesAddonManager;

public class BukkitPartiesAddonManager extends PartiesAddonManager {
	private final BanManagerHandler banManager;
	private final CrackShotHandler crackShot;
	private final DynmapHandler dynmap;
	private final EssentialsChatHandler essentialsChat;
	private final GriefPreventionHandler griefPrevention;
	private final MythicMobsHandler mythicMobs;
	private final PlaceholderAPIHandler placeholderAPI;
	private final SkillAPIHandler skillAPI;
	private final VaultHandler vault;
	
	public BukkitPartiesAddonManager(PartiesPlugin plugin) {
		super(plugin);
		
		banManager = new BanManagerHandler(plugin);
		crackShot = new CrackShotHandler(plugin);
		dynmap = new DynmapHandler(plugin);
		essentialsChat = new EssentialsChatHandler(plugin);
		griefPrevention = new GriefPreventionHandler(plugin);
		mythicMobs = new MythicMobsHandler(plugin);
		placeholderAPI = new PlaceholderAPIHandler(plugin);
		skillAPI = new SkillAPIHandler(plugin);
		vault = new VaultHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		super.loadAddons();
		
		banManager.init();
		crackShot.init();
		dynmap.init();
		essentialsChat.init();
		griefPrevention.init();
		mythicMobs.init();
		placeholderAPI.init();
		skillAPI.init();
		vault.init();
	}
}
