package com.alessiodp.parties.common.configuration;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;

public abstract class ConfigurationManager {
	protected PartiesPlugin plugin;
	protected ConfigMain configMain;
	protected ConfigParties configParties;
	protected Messages messages;
	
	protected ConfigurationManager(PartiesPlugin instance, ConfigMain configMain, ConfigParties configParties, Messages messages) {
		plugin = instance;
		this.configMain = configMain;
		this.configParties = configParties;
		this.messages = messages;
	}
	
	public void reload() {
		configMain.loadDefaults();
		configParties.loadDefaults();
		messages.loadDefaults();
		// Continue into the sub class
	}
}
