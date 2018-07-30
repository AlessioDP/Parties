package com.alessiodp.parties.bungeecord.configuration;

import com.alessiodp.parties.bungeecord.configuration.adapter.BungeeConfigurationAdapter;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.configuration.ConfigurationManager;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.storage.StorageType;

import java.nio.file.Files;
import java.nio.file.Path;

public class BungeeConfigurationManager extends ConfigurationManager {
	
	public BungeeConfigurationManager(PartiesPlugin instance, ConfigMain configMain, ConfigParties configParties, Messages messages) {
		super(instance, configMain, configParties, messages);
	}
	
	@Override
	public void reload() {
		super.reload();
		
		// Initialize configs
		Path configMainPath = configMain.saveConfiguration(plugin.getFolder());
		Path configPartiesPath = configParties.saveConfiguration(plugin.getFolder());
		Path messagesPath = messages.saveConfiguration(plugin.getFolder());
		
		// Initialize ConfigurationAdapter
		ConfigurationAdapter configMainCA = new BungeeConfigurationAdapter(configMainPath);
		ConfigurationAdapter configPartiesCA = new BungeeConfigurationAdapter(configPartiesPath);
		ConfigurationAdapter messagesCA = new BungeeConfigurationAdapter(messagesPath);
		
		// Check versions
		configMain.checkVersion(configMainCA, Constants.VERSION_BUNGEE_CONFIG_MAIN);
		configParties.checkVersion(configPartiesCA, Constants.VERSION_BUNGEE_CONFIG_PARTIES);
		messages.checkVersion(messagesCA, Constants.VERSION_BUNGEE_MESSAGES);
		
		// Load configuration
		if (Files.exists(configMainPath))
			configMain.loadConfiguration(configMainCA);
		if (Files.exists(configPartiesPath))
			configParties.loadConfiguration(configPartiesCA);
		if (Files.exists(messagesPath))
			messages.loadConfiguration(messagesCA);
		
		// Set changes
		plugin.getDatabaseManager().setDatabaseType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_DATABASE));
		plugin.getDatabaseManager().setLogType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_LOG));
		PartiesPlaceholder.setupFormats(plugin);
	}
}
