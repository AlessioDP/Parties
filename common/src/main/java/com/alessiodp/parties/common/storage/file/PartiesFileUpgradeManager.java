package com.alessiodp.parties.common.storage.file;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.file.FileUpgradeManager;
import com.alessiodp.core.common.storage.interfaces.IDatabaseFile;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import lombok.NonNull;
import ninja.leaping.configurate.ConfigurationNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PartiesFileUpgradeManager extends FileUpgradeManager {
	
	public PartiesFileUpgradeManager(@NonNull ADPPlugin plugin, @NonNull IDatabaseFile databaseFile, @NonNull StorageType storageType) {
		super(plugin, databaseFile, storageType);
	}
	
	@Override
	protected void upgradeDatabase(int currentVersion) {
		if (storageType == StorageType.YAML && currentVersion == 1) {
			// Upgrade from 2.6.X
			plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_MIGRATE_YAML, currentVersion), true);
			
			HashMap<String, String> idParties = new HashMap<>();
			databaseFile.getRootNode().getNode("parties-old").setValue(databaseFile.getRootNode().getNode("parties").copy());
			databaseFile.getRootNode().getNode("parties").setValue(null);
			
			databaseFile.getRootNode().getNode("parties-old").getChildrenMap().forEach((name, data) -> {
				String newUuid = UUID.randomUUID().toString();
				idParties.put(name.toString(), newUuid);
				
				ConfigurationNode cn = databaseFile.getRootNode().getNode("parties", newUuid);
				cn.getNode("name").setValue(name.toString());
				cn.getNode("leader").setValue(data.getNode("leader").getValue());
				cn.getNode("description").setValue(data.getNode("desc").getValue());
				cn.getNode("motd").setValue(data.getNode("motd").getValue());
				cn.getNode("color").setValue(data.getNode("color").getValue());
				cn.getNode("kills").setValue(data.getNode("kills").getValue());
				cn.getNode("password").setValue(data.getNode("password").getValue());
				cn.getNode("home").setValue(data.getNode("home").getValue() != null ? "default," + data.getNode("home").getValue() + "," : null); // Add name + server
				cn.getNode("protection").setValue(data.getNode("protection").getValue());
				cn.getNode("experience").setValue(data.getNode("experience").getValue());
				cn.getNode("follow").setValue(data.getNode("follow").getValue());
				cn.getNode("members").setValue(data.getNode("members").getValue());
			});
			
			databaseFile.getRootNode().getNode("players-old").setValue(databaseFile.getRootNode().getNode("players"));
			databaseFile.getRootNode().getNode("players").setValue(null);
			databaseFile.getRootNode().getNode("players-old").getChildrenMap().forEach((name, data) -> {
				Object party = data.getNode("party").getValue();
				ConfigurationNode cn = databaseFile.getRootNode().getNode("players", name.toString());
				cn.getNode("party").setValue(party != null && !party.toString().isEmpty() ? idParties.get(party.toString()) : null);
				cn.getNode("rank").setValue(data.getNode("rank").getValue());
				cn.getNode("spy").setValue(data.getNode("spy").getValue());
				cn.getNode("mute").setValue(data.getNode("mute").getValue());
			});
			
			ConfigurationNode cn = databaseFile.getRootNode().getNode("map-parties-by-name");
			idParties.forEach((name, id) -> cn.getNode(CommonUtils.toLowerCase(name)).setValue(id));
			
			databaseFile.getRootNode().getNode("parties-old").setValue(null);
			databaseFile.getRootNode().getNode("players-old").setValue(null);
			
			databaseFile.getRootNode().getNode("database-version").setValue(PartiesConstants.VERSION_DATABASE_YAML);
			
			try {
				databaseFile.saveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
