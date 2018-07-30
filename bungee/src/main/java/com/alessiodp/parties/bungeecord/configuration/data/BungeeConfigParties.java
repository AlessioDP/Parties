package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.ConfigParties;

public class BungeeConfigParties extends ConfigParties {
	
	public BungeeConfigParties(PartiesPlugin instance) {
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
		return "parties.yml";
	}
	
	@Override
	public String getResourceName() {
		return "bungee/parties.yml";
	}
}
