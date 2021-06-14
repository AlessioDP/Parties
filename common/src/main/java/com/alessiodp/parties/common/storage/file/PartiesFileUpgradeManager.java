package com.alessiodp.parties.common.storage.file;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.file.FileUpgradeManager;
import com.alessiodp.core.common.storage.interfaces.IDatabaseFile;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import lombok.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

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
			try {
				plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_MIGRATE_YAML, currentVersion), true);
				
				HashMap<String, String> idParties = new HashMap<>();
				databaseFile.getRootNode().node("parties-old").set(databaseFile.getRootNode().node("parties").copy());
				databaseFile.getRootNode().node("parties").set(null);
				
				databaseFile.getRootNode().node("parties-old").childrenMap().forEach((name, data) -> {
					String newUuid = UUID.randomUUID().toString();
					idParties.put(name.toString(), newUuid);
					
					ConfigurationNode cn = databaseFile.getRootNode().node("parties", newUuid);
					try {
						cn.node("name").set(name.toString());
						cn.node("leader").set(data.node("leader").get(Object.class));
						cn.node("description").set(data.node("desc").get(Object.class));
						cn.node("motd").set(data.node("motd").get(Object.class));
						cn.node("color").set(data.node("color").get(Object.class));
						cn.node("kills").set(data.node("kills").get(Object.class));
						cn.node("password").set(data.node("password").get(Object.class));
						cn.node("home").set(data.node("home").get(Object.class) != null ? "default," + data.node("home").get(Object.class) + "," : null); // Add name + server
						cn.node("protection").set(data.node("protection").get(Object.class));
						cn.node("experience").set(data.node("experience").get(Object.class));
						cn.node("follow").set(data.node("follow").get(Object.class));
						cn.node("members").set(data.node("members").get(Object.class));
					} catch (SerializationException e) {
						e.printStackTrace();
					}
				});
				
				databaseFile.getRootNode().node("players-old").set(databaseFile.getRootNode().node("players"));
				databaseFile.getRootNode().node("players").set(null);
				databaseFile.getRootNode().node("players-old").childrenMap().forEach((name, data) -> {
					try {
						Object party = data.node("party").get(Object.class);
						ConfigurationNode cn = databaseFile.getRootNode().node("players", name.toString());
						cn.node("party").set(party != null && !party.toString().isEmpty() ? idParties.get(party.toString()) : null);
						cn.node("rank").set(data.node("rank").get(Object.class));
						cn.node("spy").set(data.node("spy").get(Object.class));
						cn.node("mute").set(data.node("mute").get(Object.class));
					} catch (SerializationException e) {
						e.printStackTrace();
					}
				});
				
				ConfigurationNode cn = databaseFile.getRootNode().node("map-parties-by-name");
				idParties.forEach((name, id) -> {
					try {
						cn.node(CommonUtils.toLowerCase(name)).set(id);
					} catch (SerializationException e) {
						e.printStackTrace();
					}
				});
				
				databaseFile.getRootNode().node("parties-old").set(null);
				databaseFile.getRootNode().node("players-old").set(null);
				
				databaseFile.getRootNode().node("database-version").set(PartiesConstants.VERSION_DATABASE_YAML);
				
				databaseFile.saveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
