package com.alessiodp.parties.configuration.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.DatabaseInterface;
import com.alessiodp.parties.utils.LogLine;
import com.alessiodp.parties.utils.enums.LogLevel;

public class YAMLData implements DatabaseInterface {
	private Parties plugin;
	private File dataFile;
	private FileConfiguration data;

	public YAMLData(Parties instance) {
		plugin = instance;
	}
	
	@Override
	public void init() {
		LogHandler.log(LogLevel.DEBUG, "Initializing YAMLData", true);
		dataFile = createDataFile();
		// Upcoming database version checker
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	private File createDataFile() {
		File ret = new File(plugin.getDataFolder(), Variables.storage_settings_yaml_name_database);
		if (!ret.exists()) {
			// Create data file
			try {
				InputStream in = plugin.getResource("data.yml");
				OutputStream out = new FileOutputStream(ret);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
			} catch (Exception ex) {
				LogHandler.printError("Failed to create data file:\n" + ex.getMessage());
			}
		}
		return ret;
	}
	
	@Override
	public void stop() {}
	
	@Override
	public boolean isFailed() {
		return data == null ? true : false;
	}
	
	
	/*
	 * Spies
	 */
	@Override
	public void updateSpies(List<UUID> list) {
		List<String> spies = new ArrayList<String>();
		for (UUID u : list) {
			spies.add(u.toString());
		}
		data.set("spies", spies);
		
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LogHandler.printError("Error in YAMLData updateSpies(): " + ex.getMessage());
		}
	}
	@Override
	public List<UUID> getSpies() {
		List<UUID> ret = new ArrayList<UUID>();
		List<String> lst = data.getStringList("spies");
		for (String spy : lst) {
			try {
				ret.add(UUID.fromString(spy));
			} catch (Exception ex) {
				LogHandler.printError("Error in YAMLData getSpies(): " + ex.getMessage());
			}
		}
		return ret;
	}
	
	
	/* 
	 * Players
	 */
	@Override
	public void updatePlayer(ThePlayer tp) {
		if (tp.getPartyName().isEmpty())
			data.set("players." + tp.getUUID().toString(), null);
		else {
			data.set("players." + tp.getUUID().toString() + ".party", tp.getPartyName());
			data.set("players." + tp.getUUID().toString() + ".rank", tp.getRank());
			data.set("players." + tp.getUUID().toString() + ".name.name", tp.getName());
			data.set("players." + tp.getUUID().toString() + ".name.timestamp", System.currentTimeMillis() / 1000L);
		}
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LogHandler.printError("Error in YAMLData updatePlayer(): " + ex.getMessage());
		}
	}
	@Override
	public ThePlayer getPlayer(UUID uuid) {
		ThePlayer ret = null;
		if (data.get("players." + uuid.toString()) != null) {
			ret = new ThePlayer(uuid, plugin);
			ret.setPartyName(data.getString("players." + uuid.toString() + ".party", ""));
			ret.setRank(data.getInt("players." + uuid.toString() + ".rank", 0));
			
			// Need to be the last one - because call updatePlayer if name is different
			ret.compareName(data.getString("players." + uuid.toString() + ".name.name", ""));
		}
		return ret;
	}
	@Override
	public void removePlayer(UUID uuid) {
		data.set("players." + uuid.toString(), null);
		
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LogHandler.printError("Error in YAMLData removePlayer(): " + ex.getMessage());
		}
	}
	@Override
	public String getPlayerPartyName(UUID uuid) {
		return data.getString("players." + uuid.toString() + ".party", "");
	}
	@Override
	public int getRank(UUID uuid) {
		return data.getInt("players." + uuid.toString() + ".rank", -1);
	}
	@Override
	public HashMap<UUID, Object[]> getPlayersRank(String party) {
		// Object: [String, Integer] -> [name, rank]
		HashMap<UUID, Object[]> ret = new HashMap<UUID, Object[]>();
		ConfigurationSection cs = data.getConfigurationSection("players");
		Set<String> keys = cs.getKeys(false);
		for (String uuid : keys) {
			if (party.equalsIgnoreCase(cs.getString(uuid + ".party"))) {
				try {
					Object[] ob = new Object[] {
							cs.getString(uuid + ".name.name"),
							cs.getInt(uuid + ".rank")};
					ret.put(UUID.fromString(uuid), ob);
				} catch (Exception ex) {
					LogHandler.printError("Error in YAMLData getPlayersRank(): " + ex.getMessage());
				}
			}
		}
		return ret;
	}
	@Override
	public HashMap<UUID, Long> getPlayersFromName(String name) {
		HashMap<UUID, Long> ret = new HashMap<UUID, Long>();
		ConfigurationSection cs = data.getConfigurationSection("players");
		Set<String> keys = cs.getKeys(false);
		for (String uuid : keys) {
			if (name.equalsIgnoreCase(cs.getString(uuid + ".name.name"))) {
				try {
					long ts = cs.getLong(uuid + ".name.timestamp");
					ret.put(UUID.fromString(uuid), ts);
				} catch (Exception ex) {
					LogHandler.printError("Error in YAMLData getPlayersFromName(): " + ex.getMessage());
				}
			}
		}
		return ret;
	}
	@Override
	public String getOldPlayerName(UUID uuid) {
		return data.getString("players." + uuid.toString() + ".name.name", "");
	}
	
	
	/*
	 *	Parties
	 */
	@Override
	public void updateParty(Party party) {
		String str;
		
		str = party.getDescription();
		data.set("parties." + party.getName() + ".desc", !str.isEmpty() ? str : null);
		
		str = party.getMOTD();
		data.set("parties." + party.getName() + ".motd", !str.isEmpty() ? str : null);
		
		str = party.getPrefix();
		data.set("parties." + party.getName() + ".prefix", !str.isEmpty() ? str : null);
		
		str = party.getSuffix();
		data.set("parties." + party.getName() + ".suffix", !str.isEmpty() ? str : null);
		
		if (Variables.color_enable) {
			str = party.getColorRaw();
			data.set("parties." + party.getName() + ".color", !str.isEmpty() ? str : null);
		}
		
		if (Variables.kill_enable)
			data.set("parties." + party.getName() + ".kills", party.getKills());
		
		if (Variables.password_enable) {
			str = party.getPassword();
			data.set("parties." + party.getName() + ".password", !str.isEmpty() ? str : null);
		}
		
		str = party.getHomeRaw();
		data.set("parties." + party.getName() + ".home", !str.isEmpty() ? str : null);
		
		
		data.set("parties." + party.getName() + ".leader", party.isFixed() ? "fixed" : party.getLeader().toString());
		
		List<String> lt = new ArrayList<String>();
		for (UUID uuid : party.getMembers())
			lt.add(uuid.toString());
		data.set("parties." + party.getName() + ".members", lt);
		
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LogHandler.printError("Error in YAMLData updateParty(): " + ex.getMessage());
		}
	}
	@Override
	public Party getParty(String name) {
		Party ret = new Party(name, plugin);
		
		ret.setDescription(data.getString("parties." + name + ".desc", ""));
		ret.setMOTD(data.getString("parties." + name + ".motd", ""));
		ret.setPrefix(data.getString("parties." + name + ".prefix", ""));
		ret.setSuffix(data.getString("parties." + name + ".suffix", ""));
		ret.setColorRaw(data.getString("parties." + name + ".color", ""));
		ret.setKills(data.getInt("parties." + name + ".kills", 0));
		ret.setPassword(data.getString("parties." + name + ".password", ""));
		ret.setHomeRaw(data.getString("parties." + name + ".home", ""));
		
		String lead = data.getString("parties." + name + ".leader", "");
		try {
			if (!lead.isEmpty()) {
				if (lead.equalsIgnoreCase("fixed")) {
					ret.setLeader(UUID.fromString("00000000-0000-0000-0000-000000000000"));
					ret.setFixed(true);
				} else
					ret.setLeader(UUID.fromString(lead));
			}
		} catch (Exception ex) {
			LogHandler.printError("Error in YAMLData getParty(1): " + ex.getMessage());
		}
		List<UUID> list = new ArrayList<UUID>();
		for (String id : data.getStringList("parties." + name + ".members")) {
			try {
				list.add(UUID.fromString(id));
			} catch (Exception ex) {
				LogHandler.printError("Error in YAMLData getParty(2): " + ex.getMessage());
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
			LogHandler.printError("Error in YAMLData renameParty(): " + ex.getMessage());
		}
	}
	@Override
	public void removeParty(Party party) {
		for (UUID uuid : party.getMembers()) {
			removePlayer(uuid);
		}

		data.set("parties." + party.getName(), null);
		for (UUID uuid : party.getMembers())
			data.set("players." + uuid.toString(), null);
		try {
			data.save(dataFile);
		} catch (IOException ex) {
			LogHandler.printError("Error in YAMLData removeParty(): " + ex.getMessage());
		}
	}
	@Override
	public boolean existParty(String name) {
		boolean ret = false;
		String leader = data.getString("parties." + name + ".leader", "");
		if (!leader.isEmpty()) {
			ret = true;
		}
		return ret;
	}
	@Override
	public List<Party> getAllParties() {
		List<Party> ret = new ArrayList<Party>();
		ConfigurationSection cs = data.getConfigurationSection("parties");
		for (String name : cs.getKeys(false)) {
			// Annotation: The party is not loaded into the server, for example: it doesn't know how many players are online
			ret.add(getParty(name));
		}
		return ret;
	}
	@Override
	public List<String> getAllFixed() {
		List<String> ret = new ArrayList<String>();
		ConfigurationSection cs = data.getConfigurationSection("parties");
		for (String name : cs.getKeys(false)) {
			try {
				if (cs.getString(name + ".leader").equalsIgnoreCase("fixed"))
					ret.add(name);
			} catch (Exception ex) {}
		}
		return ret;
	}
	
	/*
	 * Log
	 */
	@Override
	public void insertLog(LogLine line) {
		try {
			File file = new File(plugin.getDataFolder(), Variables.storage_settings_yaml_name_log);
			if (!file.exists())
				file.createNewFile();
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			out.println(line.getFormattedMessage());
			out.close();
		} catch (IOException ex) {
			LogHandler.printError("Error in YAMLData insertLog(): " + ex.getMessage());
		}
	}
	
	
	/*
	 * Migration
	 */
	public List<?>[] migration_getYAML() {
		List<?>[] ret = new ArrayList<?>[3];
		// Spies
		List<UUID> spies = new ArrayList<UUID>();
		List<String> list = data.getStringList("spies");
		for (String str : list) {
			try {
				spies.add(UUID.fromString(str));
			} catch (Exception ex) {}
		}
		ret[0] = spies;
		
		// Players
		List<UUID> players = new ArrayList<UUID>();
		for (String key : data.getConfigurationSection("players").getKeys(false)) {
			try {
				players.add(UUID.fromString(key));
			} catch (Exception ex) {}
		}
		ret[1] = players;
		
		// Parties
		List<String> parties = new ArrayList<String>();
		for (String key : data.getConfigurationSection("parties").getKeys(false)) {
			parties.add(key);
		}
		ret[2] = parties;
		
		return ret;
	}
	public void migration_cleanData() {
		String baseName = Variables.storage_settings_yaml_name_database + Variables.storage_migrate_suffix;
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
		
		// Generate new data file
		dataFile = createDataFile();
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
}