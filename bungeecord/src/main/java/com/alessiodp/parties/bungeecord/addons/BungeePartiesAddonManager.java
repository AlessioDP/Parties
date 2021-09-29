package com.alessiodp.parties.bungeecord.addons;

import com.alessiodp.parties.bungeecord.addons.external.BungeeChatHandler;
import com.alessiodp.parties.bungeecord.addons.external.PremiumVanishHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.PartiesAddonManager;

public class BungeePartiesAddonManager extends PartiesAddonManager {
	private final BungeeChatHandler bungeeChat;
	private final PremiumVanishHandler premiumVanish;
	
	public BungeePartiesAddonManager(PartiesPlugin plugin) {
		super(plugin);
		
		bungeeChat = new BungeeChatHandler(plugin);
		premiumVanish = new PremiumVanishHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		super.loadAddons();
		
		bungeeChat.init();
		premiumVanish.init();
	}
}
