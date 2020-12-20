package com.alessiodp.parties.bungeecord.addons;

import com.alessiodp.parties.bungeecord.addons.external.BungeeChatHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.PartiesAddonManager;

public class BungeePartiesAddonManager extends PartiesAddonManager {
	private final BungeeChatHandler bungeeChat;
	
	public BungeePartiesAddonManager(PartiesPlugin plugin) {
		super(plugin);
		
		bungeeChat = new BungeeChatHandler(plugin);
	}
	
	@Override
	public void loadAddons() {
		super.loadAddons();
		
		bungeeChat.init();
	}
}
