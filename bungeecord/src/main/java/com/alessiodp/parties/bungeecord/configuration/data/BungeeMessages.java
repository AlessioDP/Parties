package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.Getter;

public class BungeeMessages extends Messages {
	@Getter private final String fileName = "messages.yml";
	@Getter private final String resourceName = "bungee/messages.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUNGEE_MESSAGES;
	
	//Additional
	public static String OTHER_FOLLOW_SERVER;
	
	public BungeeMessages(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bungee configurations
		// Additional
		OTHER_FOLLOW_SERVER = "&7Following %player% in %server%";
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bungee configuration
		// Additional
		OTHER_FOLLOW_SERVER = confAdapter.getString("other.follow.following-server", OTHER_FOLLOW_SERVER);
	}
}
