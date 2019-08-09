package com.alessiodp.parties.common.storage.file;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.file.FileUpgradeManager;
import com.alessiodp.core.common.storage.interfaces.IDatabaseFile;
import lombok.NonNull;

public class PartiesFileUpgradeManager extends FileUpgradeManager {
	
	public PartiesFileUpgradeManager(@NonNull ADPPlugin plugin, @NonNull IDatabaseFile databaseFile, @NonNull StorageType storageType) {
		super(plugin, databaseFile, storageType);
	}
	
	@Override
	protected void upgradeDatabase(int currentVersion) {
		// Nothing to upgrade
	}
}
