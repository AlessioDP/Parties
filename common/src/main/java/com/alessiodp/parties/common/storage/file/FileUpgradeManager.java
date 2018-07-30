package com.alessiodp.parties.common.storage.file;

import com.alessiodp.parties.common.storage.interfaces.IDatabaseFile;
import ninja.leaping.configurate.ConfigurationNode;

public abstract class FileUpgradeManager {
	
	public static void checkUpgrades(IDatabaseFile database) {
		ConfigurationNode node = database.getRootNode().getNode("database-version");
		if (node.getValue() == null) {
			// < 2.3.0
			try {
				node.setValue(1);
				database.saveFile();
			} catch (Exception ignored) {}
		}/* else {
			// Updated
		}*/
	}
}
