package com.alessiodp.parties.bukkit.configuration;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;

public class BukkitPartiesConfigurationManager extends PartiesConfigurationManager {
	
	public BukkitPartiesConfigurationManager(PartiesPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new BukkitMessages(plugin));
		getConfigs().add(new BukkitConfigMain(plugin));
		getConfigs().add(new BukkitConfigParties(plugin));
	}
}
