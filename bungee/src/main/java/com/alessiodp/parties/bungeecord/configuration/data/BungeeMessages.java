package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.Messages;

public class BungeeMessages extends Messages {
	//Additional
	public static String OTHER_FOLLOW_SERVER;
	
	public BungeeMessages(PartiesPlugin instance) {
		super(instance);
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
	
	@Override
	public String getFileName() {
		return "messages.yml";
	}
	
	@Override
	public String getResourceName() {
		return "bungee/messages.yml";
	}
}
