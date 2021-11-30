package com.alessiodp.parties.bungeecord.addons;

import com.alessiodp.parties.bungeecord.addons.external.BungeeAdvancedBanHandler;
import com.alessiodp.parties.bungeecord.addons.external.BungeeChatHandler;
import com.alessiodp.parties.bungeecord.addons.external.PremiumVanishHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.PartiesAddonManager;

public class BungeePartiesAddonManager extends PartiesAddonManager {
	private final BungeeAdvancedBanHandler advancedBanHandler;
	private final BungeeChatHandler bungeeChat;
	private final PremiumVanishHandler premiumVanish;
	
	public BungeePartiesAddonManager(PartiesPlugin plugin) {
		super(plugin);
		
		advancedBanHandler = new BungeeAdvancedBanHandler(plugin);
		bungeeChat = new BungeeChatHandler(plugin);
		premiumVanish = new PremiumVanishHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		super.loadAddons();
		
		advancedBanHandler.init();
		bungeeChat.init();
		premiumVanish.init();
	}
}
