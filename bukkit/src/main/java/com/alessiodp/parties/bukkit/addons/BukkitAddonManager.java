package com.alessiodp.parties.bukkit.addons;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
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
import com.alessiodp.parties.common.addons.AddonManager;

public class BukkitAddonManager extends AddonManager {
	
	private BanManagerHandler banManager;
	private CrackShotHandler crackShot;
	private DynmapHandler dynmap;
	private EssentialsChatHandler essentialsChat;
	private GriefPreventionHandler griefPrevention;
	private MythicMobsHandler mythicMobs;
	private PlaceholderAPIHandler placeholderAPI;
	private SkillAPIHandler skillAPI;
	private VaultHandler vault;
	
	public BukkitAddonManager(PartiesPlugin instance) {
		super(instance);
		
		banManager = new BanManagerHandler(plugin);
		crackShot = new CrackShotHandler((BukkitPartiesPlugin) plugin);
		dynmap = new DynmapHandler((BukkitPartiesPlugin) plugin);
		essentialsChat = new EssentialsChatHandler((BukkitPartiesPlugin) plugin);
		griefPrevention = new GriefPreventionHandler();
		mythicMobs = new MythicMobsHandler((BukkitPartiesPlugin) plugin);
		placeholderAPI = new PlaceholderAPIHandler(plugin);
		skillAPI = new SkillAPIHandler();
		vault = new VaultHandler((BukkitPartiesPlugin) plugin);
	}
	
	@Override
	public void loadAddons() {
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
