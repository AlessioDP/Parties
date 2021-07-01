package com.alessiodp.parties.common.storage.file;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.addons.external.simpleyaml.configuration.ConfigurationSection;
import com.alessiodp.core.common.storage.file.YAMLDao;
import com.alessiodp.core.common.storage.file.YAMLUpgradeManager;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import lombok.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PartiesYAMLUpgradeManager extends YAMLUpgradeManager {
	
	public PartiesYAMLUpgradeManager(@NonNull ADPPlugin plugin, @NonNull YAMLDao databaseFile) {
		super(plugin, databaseFile);
	}
	
	@Override
	protected void upgradeDatabase(int currentVersion) {
		if (currentVersion == 1) {
			// Upgrade from 2.6.X
			try {
				plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_MIGRATE_YAML, currentVersion), true);
				
				HashMap<String, String> idParties = new HashMap<>();
				databaseFile.getYaml().set("parties-old", databaseFile.getYaml().get("parties"));
				databaseFile.getYaml().set("parties", null);
				
				ConfigurationSection oldPartiesNode = databaseFile.getYaml().getConfigurationSection("parties-old");
				oldPartiesNode.getKeys(false).forEach(key -> {
					String newUuid = UUID.randomUUID().toString();
					idParties.put(key, newUuid);
					ConfigurationSection newPartiesNode = databaseFile.getYaml().createSection("parties." + newUuid);
					newPartiesNode.set("name", key);
					newPartiesNode.set("leader", oldPartiesNode.get(key + ".leader"));
					newPartiesNode.set("description", oldPartiesNode.get(key + ".desc"));
					newPartiesNode.set("motd", oldPartiesNode.get(key + ".motd"));
					newPartiesNode.set("color", oldPartiesNode.get(key + ".color"));
					newPartiesNode.set("kills", oldPartiesNode.get(key + ".kills"));
					newPartiesNode.set("password", oldPartiesNode.get(key + ".password"));
					newPartiesNode.set("home", oldPartiesNode.get(key + ".home") != null ? ("default," + oldPartiesNode.get(key + ".home") + ",") : null);
					newPartiesNode.set("protection", oldPartiesNode.get(key + ".protection"));
					newPartiesNode.set("experience", oldPartiesNode.get(key + ".experience"));
					newPartiesNode.set("follow", oldPartiesNode.get(key + ".follow"));
					newPartiesNode.set("members", oldPartiesNode.get(key + ".members"));
				});
				
				databaseFile.getYaml().set("players-old", databaseFile.getYaml().get("players"));
				databaseFile.getYaml().set("players", null);
				
				ConfigurationSection oldPlayersNode = databaseFile.getYaml().getConfigurationSection("players-old");
				oldPlayersNode.getKeys(false).forEach(key -> {
					String party = oldPlayersNode.getString(key + ".party");
					ConfigurationSection newPlayersNode = databaseFile.getYaml().createSection("players." + key);
					newPlayersNode.set("party", party != null && !party.isEmpty() ? idParties.get(party) : null);
					newPlayersNode.set("rank", oldPlayersNode.get(key + ".rank"));
					newPlayersNode.set("spy", oldPlayersNode.get(key + ".spy"));
					newPlayersNode.set("mute", oldPlayersNode.get(key + ".mute"));
				});
				
				idParties.forEach((name, id) -> {
					databaseFile.getYaml().set("map-parties-by-name." + CommonUtils.toLowerCase(name), id);
				});
				
				databaseFile.getYaml().remove("parties-old");
				databaseFile.getYaml().remove("players-old");
				
				databaseFile.getYaml().set("database-version", PartiesConstants.VERSION_DATABASE_YAML);
				
				databaseFile.saveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
