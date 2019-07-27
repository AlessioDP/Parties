package com.alessiodp.parties.bukkit.configuration;

import com.alessiodp.core.bukkit.configuration.adapter.BukkitConfigurationAdapter;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;

import java.nio.file.Path;

public class BukkitPartiesConfigurationManager extends PartiesConfigurationManager {
	
	public BukkitPartiesConfigurationManager(PartiesPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new BukkitConfigMain(plugin));
		getConfigs().add(new BukkitConfigParties(plugin));
		getConfigs().add(new BukkitMessages(plugin));
	}
	
	@Override
	protected ConfigurationAdapter initializeConfigurationAdapter(Path configurationFile) {
		return new BukkitConfigurationAdapter(configurationFile);
	}
}
