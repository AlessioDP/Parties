package com.alessiodp.parties.storage.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.storage.interfaces.IDatabaseFile;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class YAMLDao implements IDatabaseFile {
	private Parties plugin;
	private File dataFile;
	private FileConfiguration data;

	public YAMLDao(Parties instance) {
		plugin = instance;
	}
	
	
	@Override
	public void initFile() {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		dataFile = createDataFile();
		// Upcoming database version checker
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	@Override
	public void stopFile() {
		// Nothing to do
	}
	
	
	@Override
	public File createDataFile() {
		File ret = new File(plugin.getDataFolder(), ConfigMain.STORAGE_SETTINGS_FILE_YAML_DBNAME);
		if (!ret.exists()) {
			// Create data file
			try {
				YamlConfiguration file = new YamlConfiguration();
				file.createSection("players");
				file.createSection("parties");
				file.save(ret);
			} catch (Exception ex) {
				LoggerManager.printError(Constants.DEBUG_FILE_CREATEFAIL
						.replace("{message}", ex.getMessage()));
			}
		}
		return ret;
	}
	@Override
	public boolean prepareNewOutput() {
		boolean ret = false;
		try {
			String baseName = ConfigMain.STORAGE_SETTINGS_FILE_YAML_DBNAME + ConfigMain.STORAGE_MIGRATE_SUFFIX;
			String fileName = baseName;
			File backupFile = new File(plugin.getDataFolder(), fileName);
			int count = 1;
			while (backupFile.exists()) {
				fileName = baseName + count;
				backupFile = new File(plugin.getDataFolder(), fileName);
				count++;
			}
			
			// Rename old data with the new name
			dataFile.renameTo(backupFile);
			createDataFile();
			ret = true;
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
		return ret;
	}
	
	
	@Override
	public boolean existData(String path) {
		return data.get(path) != null ? true : false;
	}
	@Override
	public void saveData(Map<String, Object> map) {
		for (Entry<String, Object> entry : map.entrySet()) {
			data.set(entry.getKey(), entry.getValue());
		}
		
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
	@Override
	public void saveData(String path, Object obj) {
		data.set(path, obj);
		
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
	@Override
	public Object loadData(String path) {
		return data.get(path);
	}
	@Override
	public PartyPlayer loadPartyPlayerData(UUID uuid) {
		PartyPlayer ret = new PartyPlayer(uuid, ConfigParties.RANK_SET_DEFAULT);
		String node = "players." + uuid.toString();
		
		ret.setName(data.getString(node + ".name.name", ""));
		ret.setNameTimestamp(data.getLong(node + ".name.timestamp"));
		ret.setRank(data.getInt(node + ".rank"));
		ret.setPartyName(data.getString(node + ".party", ""));
		ret.setSpy(data.getBoolean(node + ".options.spy", false));
		ret.setPreventNotify(data.getBoolean(node + ".options.notify", false));
		
		return ret;
	}
	@Override
	public Party loadPartyData(String party) {
		Party ret = new Party(party);
		String node = "parties." + party;
		
		ret.setDescription(data.getString(node + ".desc", ""));
		ret.setMotd(data.getString(node + ".motd", ""));
		ret.setPrefix(data.getString(node + ".prefix", ""));
		ret.setSuffix(data.getString(node + ".suffix", ""));
		ret.setColor(plugin.getColorManager().searchColorByName(data.getString(node + ".color", "")));
		ret.setKills(data.getInt(node + ".kills", 0));
		ret.setPassword(data.getString(node + ".password", ""));
		ret.setHome(PartiesUtils.formatHome(data.getString(node + ".home", "")));
		
		String lead = data.getString(node + ".leader", "");
		try {
			if (!lead.isEmpty()) {
				if (lead.equalsIgnoreCase(Constants.FIXED_VALUE_TEXT)) {
					ret.setLeader(UUID.fromString(Constants.FIXED_VALUE_UUID));
					ret.setFixed(true);
				} else
					ret.setLeader(UUID.fromString(lead));
			}
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
		List<UUID> list = new ArrayList<UUID>();
		for (String id : data.getStringList(node + ".members")) {
			try {
				list.add(UUID.fromString(id));
			} catch (Exception ex) {
				LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
			}
		}
		ret.setMembers(list);
		
		return ret;
	}
	
	
	@Override
	public void renameParty(String before, String now) {
		ConfigurationSection cs = data.getConfigurationSection("parties." + before);
		data.set("parties." + now, cs);
		data.set("parties." + before, null);
		
		List<String> playerList = data.getStringList("parties." + now + ".members");
		for (String uuid : playerList)
			data.set("players." + uuid + ".party", now);
		
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
	}
	
	@Override
	public List<Party> getAllParties() {
		List<Party> ret = new ArrayList<Party>();
		for (String key : data.getConfigurationSection("parties").getKeys(false)) {
			String node = "parties." + key;
			try {
				Party p = new Party(key);
				p.setDescription(data.getString(node + ".desc", ""));
				p.setMotd(data.getString(node + "..motd", ""));
				p.setPrefix(data.getString(node + ".prefix", ""));
				p.setSuffix(data.getString(node + ".suffix", ""));
				p.setColor(plugin.getColorManager().searchColorByName(data.getString(node + ".color", "")));
				p.setKills(data.getInt(node + ".kills", 0));
				p.setHome(PartiesUtils.formatHome(data.getString(node + ".home", "")));
				
				String lead = data.getString(node + ".leader", "");
				if (!lead.isEmpty()) {
					if (lead.equalsIgnoreCase(Constants.FIXED_VALUE_TEXT)) {
						p.setLeader(UUID.fromString(Constants.FIXED_VALUE_UUID));
						p.setFixed(true);
					} else
						p.setLeader(UUID.fromString(lead));
				}
				
				List<UUID> mem = new ArrayList<UUID>();
				for (String id : data.getStringList(node + ".members")) {
					try {
						mem.add(UUID.fromString(id));
					} catch (Exception ex) {}
				}
				p.setMembers(mem);
				
				ret.add(p);
			} catch (Exception ex) {}
		}
		return ret;
	}
	
	@Override
	public List<PartyPlayer> getAllPlayers() {
		List<PartyPlayer> ret = new ArrayList<PartyPlayer>();
		for (String key : data.getConfigurationSection("players").getKeys(false)) {
			try {
				ret.add(loadPartyPlayerData(UUID.fromString(key)));
			} catch (Exception ex) {}
		}
		return ret;
	}
}