package com.alessiodp.parties.bungeecord.configuration;

import com.alessiodp.core.bungeecord.configuration.adapter.BungeeConfigurationAdapter;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;

import java.nio.file.Path;

public class BungeePartiesConfigurationManager extends PartiesConfigurationManager {
	
	public BungeePartiesConfigurationManager(PartiesPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new BungeeConfigMain(plugin));
		getConfigs().add(new BungeeConfigParties(plugin));
		getConfigs().add(new BungeeMessages(plugin));
	}
	
	@Override
	protected ConfigurationAdapter initializeConfigurationAdapter(Path configurationFile) {
		return new BungeeConfigurationAdapter(configurationFile);
	}
}
