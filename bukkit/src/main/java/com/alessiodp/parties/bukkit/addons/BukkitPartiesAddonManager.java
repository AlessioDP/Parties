package com.alessiodp.parties.bukkit.addons;

import com.alessiodp.parties.bukkit.addons.external.BanManagerHandler;
import com.alessiodp.parties.bukkit.addons.external.DynmapHandler;
import com.alessiodp.parties.bukkit.addons.external.EssentialsChatHandler;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.addons.external.GriefPreventionHandler;
import com.alessiodp.parties.bukkit.addons.external.LevelPointsHandler;
import com.alessiodp.parties.bukkit.addons.external.MMOCoreHandler;
import com.alessiodp.parties.bukkit.addons.external.MythicMobsHandler;
import com.alessiodp.parties.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.bukkit.addons.external.SkillAPIHandler;
import com.alessiodp.parties.bukkit.addons.external.SkriptHandler;
import com.alessiodp.parties.bukkit.addons.external.VaultHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.PartiesAddonManager;

public class BukkitPartiesAddonManager extends PartiesAddonManager {
	private final BanManagerHandler banManager;
	private final DynmapHandler dynmap;
	private final EssentialsHandler essentials;
	private final EssentialsChatHandler essentialsChat;
	private final GriefPreventionHandler griefPrevention;
	private final LevelPointsHandler levelPoints;
	private final MMOCoreHandler mmoCore;
	private final MythicMobsHandler mythicMobs;
	private final PlaceholderAPIHandler placeholderAPI;
	private final SkillAPIHandler skillAPI;
	private final SkriptHandler skriptHandler;
	private final VaultHandler vault;
	
	public BukkitPartiesAddonManager(PartiesPlugin plugin) {
		super(plugin);
		
		banManager = new BanManagerHandler(plugin);
		dynmap = new DynmapHandler(plugin);
		essentials = new EssentialsHandler(plugin);
		essentialsChat = new EssentialsChatHandler(plugin);
		griefPrevention = new GriefPreventionHandler(plugin);
		levelPoints = new LevelPointsHandler(plugin);
		mmoCore = new MMOCoreHandler(plugin);
		mythicMobs = new MythicMobsHandler(plugin);
		placeholderAPI = new PlaceholderAPIHandler(plugin);
		skillAPI = new SkillAPIHandler(plugin);
		skriptHandler = new SkriptHandler(plugin);
		vault = new VaultHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		super.loadAddons();
		
		banManager.init();
		dynmap.init();
		essentials.init();
		essentialsChat.init();
		griefPrevention.init();
		levelPoints.init();
		mmoCore.init();
		mythicMobs.init();
		placeholderAPI.init();
		skillAPI.init();
		skriptHandler.init();
		vault.init();
	}
}
