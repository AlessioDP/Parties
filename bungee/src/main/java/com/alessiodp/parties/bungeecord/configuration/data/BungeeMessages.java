package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.Messages;

public class BungeeMessages extends Messages {
	
	public BungeeMessages(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bungee configurations
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bungee configuration
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
