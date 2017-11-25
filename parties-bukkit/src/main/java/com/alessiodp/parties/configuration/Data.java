package com.alessiodp.parties.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class Data {
	private Parties plugin;
	private File dataFile;
	private FileConfiguration data;
	private boolean mysqlEnabled;
	private boolean mysqlUse;

	public Data(Parties instance, boolean ismysql) {
		plugin = instance;
		mysqlEnabled = ismysql;
		reloadDatabase();
	}
	private void reloadDatabase() {
		mysqlUse = (mysqlEnabled && plugin.getDatabaseType().isSQL()) ? true : false;
		
		dataFile = createDataFile();
		// Upcoming database version checker
		data = YamlConfiguration.loadConfiguration(dataFile);
	}
	private File createDataFile() {
		File ret = new File(plugin.getDataFolder(), Variables.database_file_name);
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
	
	/*
	 * Spies based
	 */
	public void updateSpies(List<UUID> list, boolean bypassSql) {
		Parties.debugLog("Data call: updateSpies()");
		long msTime = System.currentTimeMillis();
		if (mysqlUse && !bypassSql)
			plugin.getSQLDatabase().updateSpies(list);
		else {
			List<String> spies = new ArrayList<String>();
			for (UUID u : list) {
				spies.add(u.toString());
			}
			data.set("spies", spies);
			
			try {
				data.save(dataFile);
			} catch (IOException ex) {
				reloadDatabase();
				LogHandler.printError("Error in Data updateSpies(): " + ex.getMessage());
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}
	public List<UUID> getSpies(boolean bypassSql) {
		Parties.debugLog("Data call: getSpies()");
		long msTime = System.currentTimeMillis();
		List<UUID> ret;
		if (mysqlUse && !bypassSql)
			ret = plugin.getSQLDatabase().getSpies();
		else {
			ret = new ArrayList<UUID>();
			List<String> lst = data.getStringList("spies");
			for (String spy : lst) {
				try {
					ret.add(UUID.fromString(spy));
				} catch (Exception ex) {
					LogHandler.printError("Error in Data getSpies(): " + ex.getMessage());
				}
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	/*
	 * Player based
	 */
	public ThePlayer getPlayer(UUID uuid, boolean bypassSql) {
		Parties.debugLog("Data call: getPlayer()");
		long msTime = System.currentTimeMillis();
		ThePlayer ret = null;
		if (mysqlUse && !bypassSql)
			ret = plugin.getSQLDatabase().getPlayer(uuid);
		else {
			ret = new ThePlayer(uuid, plugin);
			if (data.get("players." + uuid.toString()) != null) {
				ret.setPartyName(data.getString("players." + uuid.toString() + ".party", ""));
				ret.setRank(data.getInt("players." + uuid.toString() + ".rank", 0));
				
				// Last - call updatePlayer if name is different
				ret.compareName(data.getString("players." + uuid.toString() + ".name.name", ""));
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	public void updatePlayer(ThePlayer tp, boolean bypassSql) {
		Parties.debugLog("Data call: updatePlayer()");
		long msTime = System.currentTimeMillis();
		if (mysqlUse && !bypassSql)
			plugin.getSQLDatabase().updatePlayer(tp);
		else {
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
				reloadDatabase();
				LogHandler.printError("Error in Data updatePlayer(): " + ex.getMessage());
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	public void removePlayer(UUID uuid) {
		Parties.debugLog("Data call: removePlayer()");
		long msTime = System.currentTimeMillis();
		if (mysqlUse)
			plugin.getSQLDatabase().removePlayer(uuid);
		else {
			data.set("players." + uuid.toString(), null);
			
			try {
				data.save(dataFile);
			} catch (IOException ex) {
				reloadDatabase();
				LogHandler.printError("Error in Data removePlayer(): " + ex.getMessage());
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}
	
	public String getPlayerPartyName(UUID uuid) {
		Parties.debugLog("Data call: getPlayerPartyName()");
		long msTime = System.currentTimeMillis();
		String ret;
		if (mysqlUse)
			ret = plugin.getSQLDatabase().getPlayerPartyName(uuid);
		else
			ret = data.getString("players." + uuid.toString() + ".party", "");
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	public int getRank(UUID uuid) {
		Parties.debugLog("Data call: getRank()");
		long msTime = System.currentTimeMillis();
		int ret;
		if (mysqlUse)
			ret = plugin.getSQLDatabase().getRank(uuid);
		else
			ret = data.getInt("players." + uuid.toString() + ".rank");
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	public HashMap<UUID, Object[]> getPlayersRank(String party) {
		Parties.debugLog("Data call: getPlayersRank()");
		long msTime = System.currentTimeMillis();
		HashMap<UUID, Object[]> ret;
		if (mysqlUse)
			ret = plugin.getSQLDatabase().getPlayersRank(party);
		else {
			ret = new HashMap<UUID, Object[]>();
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
						LogHandler.printError("Error in Data getPlayersRank(): " + ex.getMessage());
					}
				}
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	public HashMap<UUID, Long> getPlayersFromName(String name) {
		Parties.debugLog("Data call: getPlayersFromName()");
		long msTime = System.currentTimeMillis();
		HashMap<UUID, Long> ret;
		if (mysqlUse)
			ret = plugin.getSQLDatabase().getPlayersFromName(name);
		else {
			ret = new HashMap<UUID, Long>();
			ConfigurationSection cs = data.getConfigurationSection("players");
			Set<String> keys = cs.getKeys(false);
			for (String uuid : keys) {
				if (name.equalsIgnoreCase(cs.getString(uuid + ".name.name"))) {
					try {
						long ts = cs.getLong(uuid + ".name.timestamp");
						ret.put(UUID.fromString(uuid), ts);
					} catch (Exception ex) {
						LogHandler.printError("Error in Data getPlayersFromName(): " + ex.getMessage());
					}
				}
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	public String getOldPlayerName(UUID uuid) {
		Parties.debugLog("Data call: getOldPlayerName()");
		long msTime = System.currentTimeMillis();
		String ret;
		if (mysqlUse)
			ret = plugin.getSQLDatabase().getOldPlayerName(uuid);
		else
			ret = data.getString("players." + uuid.toString() + ".name.name", "");
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	/*
	 * Party Based
	 */
	public Party getParty(String name, boolean bypassSql) {
		Parties.debugLog("Data call: getParty()");
		long msTime = System.currentTimeMillis();
		Party ret;
		if (mysqlUse && !bypassSql)
			ret = plugin.getSQLDatabase().getParty(name);
		else {
			ret = new Party(name, plugin);
			
			ret.setDescription(data.getString("parties." + name + ".desc", ""));
			ret.setMOTD(data.getString("parties." + name + ".motd", ""));
			ret.setPrefix(data.getString("parties." + name + ".prefix", ""));
			ret.setSuffix(data.getString("parties." + name + ".suffix", ""));
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
				LogHandler.printError("Error in Data getParty(1): " + ex.getMessage());
			}
			List<UUID> list = new ArrayList<UUID>();
			for (String id : data.getStringList("parties." + name + ".members")) {
				try {
					list.add(UUID.fromString(id));
				} catch (Exception ex) {
					LogHandler.printError("Error in Data getParty(2): " + ex.getMessage());
				}
			}
			ret.setMembers(list);
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	public void renameParty(String before, String now) {
		Parties.debugLog("Data call: renameParty()");
		long msTime = System.currentTimeMillis();
		if (mysqlUse)
			plugin.getSQLDatabase().renameParty(before, now);
		else {
			ConfigurationSection cs = data.getConfigurationSection("parties." + before);
			data.set("parties." + now, cs);
			data.set("parties." + before, null);
			
			List<String> playerList = data.getStringList("parties." + now + ".members");
			for (String uuid : playerList)
				data.set("players." + uuid + ".party", now);
			try {
				data.save(dataFile);
			} catch (IOException ex) {
				reloadDatabase();
				LogHandler.printError("Error in Data renameParty(): " + ex.getMessage());
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}
	public void updateParty(Party party, boolean bypassSql) {
		Parties.debugLog("Data call: updateParty()");
		long msTime = System.currentTimeMillis();
		if (mysqlUse && !bypassSql)
			plugin.getSQLDatabase().updateParty(party);
		else {
			String str;
			
			str = party.getDescription();
			data.set("parties." + party.getName() + ".desc", !str.isEmpty() ? str : null);
			
			str = party.getMOTD();
			data.set("parties." + party.getName() + ".motd", !str.isEmpty() ? str : null);
			
			str = party.getPrefix();
			data.set("parties." + party.getName() + ".prefix", !str.isEmpty() ? str : null);
			
			str = party.getSuffix();
			data.set("parties." + party.getName() + ".suffix", !str.isEmpty() ? str : null);
			
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
				reloadDatabase();
				LogHandler.printError("Error in Data updateParty(): " + ex.getMessage());
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	public void removeParty(Party party) {
		Parties.debugLog("Data call: removeParty()");
		long msTime = System.currentTimeMillis();
		if (mysqlUse)
			plugin.getSQLDatabase().removeParty(party.getName());
		else {
			for (UUID uuid : party.getMembers()) {
				removePlayer(uuid);
			}

			data.set("parties." + party.getName(), null);
			for (UUID uuid : party.getMembers())
				data.set("players." + uuid.toString(), null);
			try {
				data.save(dataFile);
			} catch (IOException ex) {
				reloadDatabase();
				LogHandler.printError("Error in Data removeParty(): " + ex.getMessage());
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	public boolean existParty(String name) {
		Parties.debugLog("Data call: existParty()");
		long msTime = System.currentTimeMillis();
		boolean ret = false;
		if (mysqlUse)
			ret = plugin.getSQLDatabase().existParty(name);
		else {
			String leader = data.getString("parties." + name + ".leader");
			if (leader != null && !leader.isEmpty()) {
				if (leader.equals("fixed"))
					ret = true;
				else {
					try {
						String partyname = getPlayerPartyName(UUID.fromString(leader));
						if (partyname != null && !partyname.isEmpty())
							if (partyname.equalsIgnoreCase(name))
								ret = true;
					} catch (Exception ex) {
						LogHandler.printError("Error in Data existParty(): " + ex.getMessage());
					}
				}
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	
	public List<Party> getAllParties() {
		Parties.debugLog("Data call: getAllParties()");
		long msTime = System.currentTimeMillis();
		List<Party> ret;
		if (mysqlUse)
			ret = plugin.getSQLDatabase().getAllParties();
		else {
			ret = new ArrayList<Party>();
			
			ConfigurationSection cs = data.getConfigurationSection("parties");
			for (String name : cs.getKeys(false)) {
				// Annotation: The party is not loaded into the server, for example: it doesn't know how many players are online
				ret.add(getParty(name, true));
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	
	public List<String> getAllFixed() {
		Parties.debugLog("Data call: getAllFixed()");
		long msTime = System.currentTimeMillis();
		List<String> ret;
		if (mysqlUse)
			ret =  plugin.getSQLDatabase().getAllFixed();
		else {
			ret = new ArrayList<String>();
			
			ConfigurationSection cs = data.getConfigurationSection("parties");
			for (String name : cs.getKeys(false)) {
				try {
					if (cs.getString(name + ".leader").equalsIgnoreCase("fixed"))
						ret.add(name);
				} catch (Exception ex) {}
			}
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	
	/*
	 * Migration
	 */
	public boolean migrateSQLtoYAML() {
		Parties.debugLog("Data call: migrateSQLtoYAML()");
		long msTime = System.currentTimeMillis();
		boolean completed = false;
		if (mysqlEnabled) {
			String fileName = Variables.database_file_name + Variables.database_migrate_suffix;
			File backupFile = new File(plugin.getDataFolder(), fileName);
			int count = 1;
			while (backupFile.exists()) {
				fileName = Variables.database_file_name + Variables.database_migrate_suffix + count;
				backupFile = new File(plugin.getDataFolder(), fileName);
				count++;
			}
			
			// Rename old data with the new name
			dataFile.renameTo(backupFile);
			
			// Generate new data file
			dataFile = createDataFile();
			data = YamlConfiguration.loadConfiguration(dataFile);
			
			// Start migration
			completed = plugin.getSQLDatabase().migrateSQLtoYAML();
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return completed;
	}
	public boolean migrateYAMLtoSQL() {
		Parties.debugLog("Data call: migrateYAMLtoSQL()");
		long msTime = System.currentTimeMillis();
		boolean completed = false;
		if (mysqlEnabled) {
			// Spies
			List<UUID> spies = new ArrayList<UUID>();
			List<String> list = data.getStringList("spies");
			for (String str : list) {
				try {
					spies.add(UUID.fromString(str));
				} catch (Exception ex) {}
			}
			
			// Players
			List<UUID> players = new ArrayList<UUID>();
			for (String key : data.getConfigurationSection("players").getKeys(false)) {
				try {
					players.add(UUID.fromString(key));
				} catch (Exception ex) {}
			}
			
			// Parties
			List<String> parties = new ArrayList<String>();
			for (String key : data.getConfigurationSection("parties").getKeys(false)) {
				parties.add(key);
			}
			
			completed = plugin.getSQLDatabase().migrateYAMLtoSQL(spies, players, parties);
		}
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return completed;
	}
	
	public boolean isSQLEnabled() {return mysqlEnabled;}
}