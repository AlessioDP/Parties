package com.alessiodp.parties.velocity.configuration;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigMain;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigParties;
import com.alessiodp.parties.velocity.configuration.data.VelocityMessages;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;

public class VelocityPartiesConfigurationManager extends PartiesConfigurationManager {
	
	public VelocityPartiesConfigurationManager(PartiesPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new VelocityMessages(plugin));
		getConfigs().add(new VelocityConfigMain(plugin));
		getConfigs().add(new VelocityConfigParties(plugin));
	}
	
	public void makeConfigsSync() {
		((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendConfigs();
	}
}
