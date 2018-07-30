package com.alessiodp.parties.common.storage;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LogLine;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.dispatchers.FileDispatcher;
import com.alessiodp.parties.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.parties.common.storage.special.NoneDao;
import com.alessiodp.parties.common.utils.DebugUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DatabaseManager {
	private PartiesPlugin plugin;
	
	// Single instance for each storage type
	private NoneDao none;
	private FileDispatcher yaml;
	private SQLDispatcher mysql;
	private SQLDispatcher sqlite;
	
	// Active storage
	private IDatabaseDispatcher database;
	private IDatabaseDispatcher log;
	
	// Storage type
	@Getter @Setter private StorageType databaseType;
	@Getter @Setter private StorageType logType = StorageType.NONE;
	
	@Getter private boolean shutdownPlugin;
	
	public DatabaseManager(PartiesPlugin instance) {
		plugin = instance;
		shutdownPlugin = false;
	}
	
	public void reload() {
		LoggerManager.log(LogLevel.DEBUG,
				Constants.DEBUG_DB_INIT
						.replace("{class}", getClass().getSimpleName())
						.replace("{db}", getDatabaseType().getFormattedName())
						.replace("{log}", getLogType().getFormattedName()),
				true);
		
		// Stop if initialized
		stop();
		
		// Database init
		switch (getDatabaseType()) {
		case NONE:
			database = initNone();
			break;
		case YAML:
			database = initYAML();
			break;
		case MYSQL:
			database = initMySQL();
			break;
		case SQLITE:
			database = initSQLite();
			break;
		}
		
		// Log init
		switch (getLogType()) {
		case NONE:
			log = initNone();
			break;
		case YAML:
			log = initYAML();
			break;
		case MYSQL:
			log = initMySQL();
			break;
		case SQLITE:
			log = initSQLite();
			break;
		}
		
		// Force inits
		forceInits();
		
		// If something gone wrong stop the plugin
		if (database == null || log == null) {
			shutdownPlugin = true;
			//stopServer();
		}
	}
	
	private IDatabaseDispatcher initNone() {
		if (none == null) {
			none = new NoneDao();
			none.init(StorageType.NONE);
		}
		return none;
	}
	
	private IDatabaseDispatcher initYAML() {
		IDatabaseDispatcher ret = null;
		if (yaml == null) {
			yaml = new FileDispatcher(plugin);
			yaml.init(StorageType.YAML);
		}
		if (!yaml.isFailed())
			ret = yaml;
		return ret;
	}
	
	private IDatabaseDispatcher initMySQL() {
		IDatabaseDispatcher ret = null;
		if (mysql == null) {
			mysql = new SQLDispatcher(plugin);
			mysql.init(StorageType.MYSQL);
		}
		if (!mysql.isFailed())
			ret = mysql;
		return ret;
	}
	
	private IDatabaseDispatcher initSQLite() {
		IDatabaseDispatcher ret = null;
		if (sqlite == null) {
			sqlite = new SQLDispatcher(plugin);
			sqlite.init(StorageType.SQLITE);
		}
		if (!sqlite.isFailed())
			ret = sqlite;
		return ret;
	}
	
	private void forceInits() {
		if (ConfigMain.STORAGE_MIGRATE_INIT_YAML) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_DB_FORCEINIT
					.replace("{value}", StorageType.YAML.getFormattedName()), true);
			initYAML();
		}
		if (ConfigMain.STORAGE_MIGRATE_INIT_MYSQL) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_DB_FORCEINIT
					.replace("{value}", StorageType.MYSQL.getFormattedName()), true);
			initMySQL();
		}
		if (ConfigMain.STORAGE_MIGRATE_INIT_SQLITE) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_DB_FORCEINIT
					.replace("{value}", StorageType.SQLITE.getFormattedName()), true);
			initSQLite();
		}
	}
	
	public boolean isStorageOnline(StorageType st) {
		boolean ret = false;
		switch (st) {
		case YAML:
			ret = (yaml != null);
			break;
		case MYSQL:
			ret = (mysql != null);
			break;
		case SQLITE:
			ret = (sqlite != null);
			break;
		}
		return ret;
	}
	
	public void stop() {
		// Stop each storage instance
		if (yaml != null) {
			yaml.stop();
			yaml = null;
		}
		if (mysql != null) {
			mysql.stop();
			mysql = null;
		}
		if (sqlite != null) {
			sqlite.stop();
			sqlite = null;
		}
	}
	
	private <T> CompletableFuture<T> runSupplyAsync(Supplier<T> supplier) {
		return CompletableFuture.supplyAsync(supplier, plugin.getPartiesScheduler().getDatabaseExecutor())
				.exceptionally(exp -> {
					exp.printStackTrace();
					return null;
				});
	}
	
	private CompletableFuture<Void> runAsync(Runnable runnable) {
		return CompletableFuture.runAsync(runnable, plugin.getPartiesScheduler().getDatabaseExecutor())
				.exceptionally(exp -> {
					exp.printStackTrace();
					return null;
				});
	}
	
	
	/*
	 * Players
	 */
	public CompletableFuture<Void> updatePlayer(PartyPlayerImpl player) {
		return runAsync(() -> {
			DebugUtils.debugLog("Data call: updatePlayer()");
			long nsTime = System.nanoTime();
			
			database.updatePlayer(player);
			
			DebugUtils.debugDataTiming(nsTime);
		});
	}
	
	public CompletableFuture<PartyPlayerImpl> getPlayer(UUID uuid) {
		return runSupplyAsync(() -> getPlayerSync(uuid));
	}
	private PartyPlayerImpl getPlayerSync(UUID uuid) {
		DebugUtils.debugLog("Data call: getPlayer()");
		long nsTime = System.nanoTime();
		
		PartyPlayerImpl futureRet = database.getPlayer(uuid);
		
		DebugUtils.debugDataTiming(nsTime);
		return futureRet;
	}
	
	public CompletableFuture<List<PartyPlayerImpl>> getPartyPlayersByName(String name) {
		return runSupplyAsync(() -> {
			DebugUtils.debugLog("Data call: getPartyPlayersByName()");
			long nsTime = System.nanoTime();
			
			List<PartyPlayerImpl> futureRet = database.getPartyPlayersByName(name);
			
			DebugUtils.debugDataTiming(nsTime);
			return futureRet;
		});
	}
	
	/*
	 * Parties
	 */
	public CompletableFuture<Void> updateParty(PartyImpl party) {
		return runAsync(() -> {
			DebugUtils.debugLog("Data call: updateParty()");
			long nsTime = System.nanoTime();
			
			database.updateParty(party);
			
			DebugUtils.debugDataTiming(nsTime);
		});
	}
	
	public CompletableFuture<PartyImpl> getParty(String party) {
		return runSupplyAsync(() -> getPartySync(party));
	}
	private PartyImpl getPartySync(String party) {
		DebugUtils.debugLog("Data call: getParty()");
		long nsTime = System.nanoTime();
		
		PartyImpl futureRet = database.getParty(party);
		
		DebugUtils.debugDataTiming(nsTime);
		return futureRet;
	}
	
	public CompletableFuture<Void> renameParty(String prev, String next) {
		return runAsync(() -> {
			DebugUtils.debugLog("Data call: renameParty()");
			long nsTime = System.nanoTime();
			
			database.renameParty(prev, next);
			
			DebugUtils.debugDataTiming(nsTime);
		});
	}
	
	public CompletableFuture<Void> removeParty(PartyImpl party) {
		return runAsync(() -> {
			DebugUtils.debugLog("Data call: removeParty()");
			long nsTime = System.nanoTime();
			
			database.removeParty(party);
			
			DebugUtils.debugDataTiming(nsTime);
		});
	}
	
	public CompletableFuture<Boolean> existParty(String party) {
		return runSupplyAsync(() -> {
			DebugUtils.debugLog("Data call: existParty()");
			long nsTime = System.nanoTime();
			
			boolean futureRet = database.existParty(party);
			
			DebugUtils.debugDataTiming(nsTime);
			return futureRet;
		});
	}
	
	public CompletableFuture<List<PartyImpl>> getAllFixed() {
		return runSupplyAsync(() -> {
			DebugUtils.debugLog("Data call: getAllFixed()");
			long nsTime = System.nanoTime();
			
			List<PartyImpl> futureRet = database.getAllFixed();
			
			DebugUtils.debugDataTiming(nsTime);
			return futureRet;
		});
	}
	
	public CompletableFuture<List<PartyImpl>> getAllParties() {
		return runSupplyAsync(() -> {
			DebugUtils.debugLog("Data call: getAllParties()");
			long nsTime = System.nanoTime();
			
			List<PartyImpl> futureRet = database.getAllParties();
			
			DebugUtils.debugDataTiming(nsTime);
			return futureRet;
		});
	}
	
	public CompletableFuture<List<PartyPlayerImpl>> getAllPlayers() {
		return runSupplyAsync(() -> {
			DebugUtils.debugLog("Data call: getAllParties()");
			long nsTime = System.nanoTime();
			
			List<PartyPlayerImpl> futureRet = database.getAllPlayers();
			
			DebugUtils.debugDataTiming(nsTime);
			return futureRet;
		});
	}
	
	public CompletableFuture<Void> insertLog(LogLine line) {
		return CompletableFuture.runAsync(() -> {
			if (log != null)
				log.insertLog(line);
		}, plugin.getPartiesScheduler().getLogExecutor())
				.exceptionally(exp -> {
					exp.printStackTrace();
					return null;
				});
	}
	
	/*
	 * Migration
	 */
	public boolean migrate(StorageType from, StorageType to) {
		boolean ret = false;
		DatabaseData fromData = getDatabaseFromType(from).loadEntireData();
		IDatabaseDispatcher toDatabase = getDatabaseFromType(to);
		
		if (fromData != null && toDatabase != null) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_DB_MIGRATION
					.replace("{from}", from.getFormattedName())
					.replace("{to}", to.getFormattedName()), true);
			if (toDatabase.prepareNewOutput()) {
				toDatabase.saveEntireData(fromData);
				ret = true;
			}
		}
		return ret;
	}
	private IDatabaseDispatcher getDatabaseFromType(StorageType type) {
		IDatabaseDispatcher ret;
		switch (type) {
		case MYSQL:
			ret = mysql;
			break;
		case SQLITE:
			ret = sqlite;
			break;
		default:
			ret = yaml;
		}
		return ret;
	}
}
