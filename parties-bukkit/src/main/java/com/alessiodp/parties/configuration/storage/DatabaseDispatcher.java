package com.alessiodp.parties.configuration.storage;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.DatabaseInterface;
import com.alessiodp.parties.utils.LogLine;
import com.alessiodp.parties.utils.enums.StorageType;

public class DatabaseDispatcher implements DatabaseInterface {
	private Parties plugin;
	
	// Single instance for each storage type
	private NoneData none;
	private YAMLData yaml;
	private MySQLData mysql;
	
	private DatabaseInterface database;
	private DatabaseInterface log;
	
	public DatabaseDispatcher(Parties instance) {
		plugin = instance;
		init();
	}
	
	@Override
	public void init() {
		// Database init
		switch (plugin.getDatabaseType()) {
		case NONE:
			database = initNone();
		case YAML:
			database = initYAML();
			break;
		case MYSQL:
			database = initMySQL();
			break;
		}
		
		// Log init
		switch (plugin.getLogType()) {
		case NONE:
			log = initNone();
		case YAML:
			log = initYAML();
			break;
		case MYSQL:
			log = initMySQL();
			break;
		}
		
		// Force MySQL
		if (Variables.storage_migrate_forcemysql)
			initMySQL();
		
		// If something gone wrong stop the plugin
		if (database == null || log == null)
			stopServer();
	}
	private DatabaseInterface initNone() {
		if (none == null) {
			none = new NoneData();
			none.init();
		}
		return (DatabaseInterface) none;
	}
	private DatabaseInterface initYAML() {
		DatabaseInterface ret = null;
		if (yaml == null) {
			yaml = new YAMLData(plugin);
			yaml.init();
		}
		if (!yaml.isFailed())
			ret = (DatabaseInterface) yaml;
		return ret;
	}
	private DatabaseInterface initMySQL() {
		DatabaseInterface ret = null;
		if (mysql == null) {
			mysql = new MySQLData(plugin);
			mysql.init();
		}
		if (!mysql.isFailed())
			ret = (DatabaseInterface) mysql;
		return ret;
	}
	public boolean isStorageOnline(StorageType st) {
		boolean ret = false;
		switch (st) {
		case NONE:
			break;
		case YAML:
			ret = (yaml != null);
			break;
		case MYSQL:
			ret = (mysql != null);
			break;
		}
		return ret;
	}
	private void stopServer() {
		LogHandler.printError("Failed to initialize the storage, stopping Parties!");
		Bukkit.getPluginManager().disablePlugin(plugin);
		throw new RuntimeException("Disabling plugin");
	}
	
	@Override
	public void stop() {
		// Stop each storage instance
		if (yaml != null)
			yaml.stop();
		if (mysql != null)
			mysql.stop();
	}
	
	@Override
	public boolean isFailed() {
		// Useless here
		return true;
	}
	
	
	/*
	 * Spies
	 */
	@Override
	public void updateSpies(List<UUID> list) {
		Parties.debugLog("Data call: updateSpies()");
		long msTime = System.currentTimeMillis();
		
		database.updateSpies(list);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	@Override
	public List<UUID> getSpies() {
		Parties.debugLog("Data call: getSpies()");
		long msTime = System.currentTimeMillis();
		
		List<UUID> ret = database.getSpies();
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	
	
	/* 
	 * Players
	 */
	@Override
	public void updatePlayer(ThePlayer tp) {
		Parties.debugLog("Data call: updatePlayer()");
		long msTime = System.currentTimeMillis();
		
		database.updatePlayer(tp);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	@Override
	public ThePlayer getPlayer(UUID uuid) {
		Parties.debugLog("Data call: getPlayer()");
		long msTime = System.currentTimeMillis();
		
		ThePlayer ret = database.getPlayer(uuid);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public void removePlayer(UUID uuid) {
		Parties.debugLog("Data call: removePlayer()");
		long msTime = System.currentTimeMillis();
		
		database.removePlayer(uuid);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	@Override
	public String getPlayerPartyName(UUID uuid) {
		Parties.debugLog("Data call: getPlayerPartyName()");
		long msTime = System.currentTimeMillis();
		
		String ret = database.getPlayerPartyName(uuid);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public int getRank(UUID uuid) {
		Parties.debugLog("Data call: getRank()");
		long msTime = System.currentTimeMillis();
		
		int ret = database.getRank(uuid);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public HashMap<UUID, Object[]> getPlayersRank(String party) {
		Parties.debugLog("Data call: getPlayersRank()");
		long msTime = System.currentTimeMillis();
		
		HashMap<UUID, Object[]> ret = database.getPlayersRank(party);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public HashMap<UUID, Long> getPlayersFromName(String name) {
		Parties.debugLog("Data call: getPlayersFromName()");
		long msTime = System.currentTimeMillis();
		
		HashMap<UUID, Long> ret = database.getPlayersFromName(name);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public String getOldPlayerName(UUID uuid) {
		Parties.debugLog("Data call: getOldPlayerName()");
		long msTime = System.currentTimeMillis();
		
		String ret = database.getOldPlayerName(uuid);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}
	
	
	/*
	 *	Parties
	 */
	@Override
	public void updateParty(Party party) {
		Parties.debugLog("Data call: updateParty()");
		long msTime = System.currentTimeMillis();
		
		database.updateParty(party);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	@Override
	public Party getParty(String party) {
		Parties.debugLog("Data call: getParty()");
		long msTime = System.currentTimeMillis();
		
		Party ret = database.getParty(party);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public void renameParty(String prev, String next) {
		Parties.debugLog("Data call: renameParty()");
		long msTime = System.currentTimeMillis();
		
		database.renameParty(prev, next);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	@Override
	public void removeParty(Party party) {
		Parties.debugLog("Data call: removeParty()");
		long msTime = System.currentTimeMillis();
		
		database.removeParty(party);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
	}

	@Override
	public boolean existParty(String party) {
		Parties.debugLog("Data call: existParty()");
		long msTime = System.currentTimeMillis();
		
		boolean ret = database.existParty(party);
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public List<Party> getAllParties() {
		Parties.debugLog("Data call: getAllParties()");
		long msTime = System.currentTimeMillis();
		
		List<Party> ret = database.getAllParties();
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public List<String> getAllFixed() {
		Parties.debugLog("Data call: getAllFixed()");
		long msTime = System.currentTimeMillis();
		
		List<String> ret = database.getAllFixed();
		
		Parties.debugLog("End data call in " + (System.currentTimeMillis() - msTime) + "ms");
		return ret;
	}

	@Override
	public void insertLog(LogLine line) {
		log.insertLog(line);
	}
	
	/*
	 * Migration
	 */
	
	public boolean migrateStart(StorageType from, StorageType to) {
		boolean ret = false;
		if (from.isYAML() && to.isMySQL()) {
			// YAML to MySQL
			ret = migrateYAMLtoMySQL();
		} else if (from.isMySQL() && to.isYAML()) {
			// MySQL to YAML
			ret = migrateMySQLtoYAML();
		}
		return ret;
	}
	
	public boolean migrateYAMLtoMySQL() {
		boolean ret = false;
		
		initYAML();
		if (mysql != null) {
			/* Starting migration
			 * HOW:	Get all data keys from YAMLData and start a new MySQL table,
			 *		from MySQLData get every value by a call inside YAMLData with
			 *		each key. Once done migration will be completed.
			 */
			ret = mysql.migration_storeYAML(yaml.migration_getYAML(), yaml); 
		}
		return ret;
	}
	public boolean migrateMySQLtoYAML() {
		// Handled by YAMLData
		boolean ret = false;
		
		initYAML();
		if (mysql != null) {
			yaml.migration_cleanData();
			
			ret = mysql.migration_migrateData(yaml);
		}
		return ret;
	}
}
