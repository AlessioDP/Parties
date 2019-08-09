package com.alessiodp.parties.common.configuration;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.NonNull;

public abstract class PartiesConfigurationFile extends ConfigurationFile {
	public PartiesConfigurationFile(@NonNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public boolean checkVersion(@NonNull ConfigurationAdapter confAdapter) {
		boolean ret = super.checkVersion(confAdapter);
		if (ret) {
			((PartiesPlugin) plugin).getLoginAlerts().add(Messages.PARTIES_CONFIGURATION_OUTDATED
					.replace("%config%", getFileName()));
		}
		return ret;
	}
}
