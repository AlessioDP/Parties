package com.alessiodp.parties.common.configuration;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.ConfigurationManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;

public abstract class PartiesConfigurationManager extends ConfigurationManager {
	
	public PartiesConfigurationManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void performChanges() {
		plugin.getDatabaseManager().setDatabaseType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_DATABASE));
		PartiesPlaceholder.setupFormats((PartiesPlugin) plugin);
		plugin.getLoginAlertsManager().setPermission(PartiesPermission.ADMIN_ALERTS);
		checkOutdatedConfigs(Messages.PARTIES_CONFIGURATION_OUTDATED);
	}
}
