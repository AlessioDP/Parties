package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

public class BungeeConfigMain extends ConfigMain {
	
	
	public BungeeConfigMain(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bungee configuration
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bungee configuration
	}
	
	@Override
	public String getFileName() {
		return "config.yml";
	}
	
	@Override
	public String getResourceName() {
		return "bungee/config.yml";
	}
}
