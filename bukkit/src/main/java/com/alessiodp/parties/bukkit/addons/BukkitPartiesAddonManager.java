package com.alessiodp.parties.bukkit.addons;

import com.alessiodp.parties.bukkit.addons.external.BanManagerHandler;
import com.alessiodp.parties.bukkit.addons.external.BukkitAdvancedBanHandler;
import com.alessiodp.parties.bukkit.addons.external.ClaimHandler;
import com.alessiodp.parties.bukkit.addons.external.DynmapHandler;
import com.alessiodp.parties.bukkit.addons.external.EssentialsChatHandler;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.addons.external.MagicHandler;
import com.alessiodp.parties.bukkit.addons.external.PlaceholderAPIHandler;
import com.alessiodp.parties.bukkit.addons.external.SkriptHandler;
import com.alessiodp.parties.bukkit.addons.external.VaultHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.PartiesAddonManager;

public class BukkitPartiesAddonManager extends PartiesAddonManager {
	private final BukkitAdvancedBanHandler advancedBanHandler;
	private final BanManagerHandler banManager;
	private final ClaimHandler claimHandler;
	private final DynmapHandler dynmap;
	private final EssentialsHandler essentials;
	private final EssentialsChatHandler essentialsChat;
	private final MagicHandler magic;
	private final PlaceholderAPIHandler placeholderAPI;
	private final SkriptHandler skriptHandler;
	private final VaultHandler vault;
	
	public BukkitPartiesAddonManager(PartiesPlugin plugin) {
		super(plugin);
		
		advancedBanHandler = new BukkitAdvancedBanHandler(plugin);
		banManager = new BanManagerHandler(plugin);
		claimHandler = new ClaimHandler(plugin);
		dynmap = new DynmapHandler(plugin);
		essentials = new EssentialsHandler(plugin);
		essentialsChat = new EssentialsChatHandler(plugin);
		magic = new MagicHandler(plugin);
		placeholderAPI = new PlaceholderAPIHandler(plugin);
		skriptHandler = new SkriptHandler(plugin);
		vault = new VaultHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		super.loadAddons();
		
		dynmap.init();
		placeholderAPI.init();
		skriptHandler.init();
	}
	
	@Override
	public void postLoadAddons() {
		super.postLoadAddons();
		
		advancedBanHandler.init();
		banManager.init();
		claimHandler.init();
		essentials.init();
		essentialsChat.init();
		magic.init();
		vault.init();
	}
}
