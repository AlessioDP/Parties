package com.alessiodp.parties.configuration.storage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.DatabaseInterface;
import com.alessiodp.parties.utils.LogLine;
import com.alessiodp.parties.utils.enums.Library;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MySQLData implements DatabaseInterface {
	private Parties plugin;
	
	private HikariDataSource hikariDataSource;
	private boolean failed;
	
	private String tableParties;
	private String tablePlayers;
	private String tableSpies;
	private String tableLog;
	private String varcharSize;
	
	
	// Tables settings
	private final int tablePartiesVersion = 2;
	private final int tablePlayersVersion = 1;
	private final int tableSpiesVersion = 1;
	private final int tableLogVersion = 1;

	public MySQLData(Parties instance) {
		plugin = instance;
		varcharSize = String.valueOf(Variables.storage_settings_mysql_varcharsize);
		tableParties = Variables.storage_settings_mysql_tables_parties;
		tablePlayers = Variables.storage_settings_mysql_tables_players;
		tableSpies = Variables.storage_settings_mysql_tables_spies;
		tableLog = Variables.storage_settings_mysql_tables_log;
	}
	
	/*
	 * Connection
	 */
	@Override
	public void init() {
		LogHandler.log(LogLevel.DEBUG, "Initializing MySQLData", true);
		failed = false;
		if (plugin.getLibraryHandler().initLibrary(Library.HIKARI)
				&& plugin.getLibraryHandler().initLibrary(Library.SLF4J_API)
				&& plugin.getLibraryHandler().initLibrary(Library.SLF4J_SIMPLE)) {
			try {
				initConnection();
				initTables(getConnection());
			} catch (Exception ex) {
				failed = true;
			}
		} else
			failed = true;
	}
	private void initConnection() throws Exception {
		HikariConfig config = new HikariConfig();
		config.setPoolName("Parties");
		config.setJdbcUrl(Variables.storage_settings_mysql_url);
		config.setUsername(Variables.storage_settings_mysql_username);
		config.setPassword(Variables.storage_settings_mysql_password);
		config.setMaximumPoolSize(Variables.storage_settings_mysql_poolsize);
		config.setMinimumIdle(Variables.storage_settings_mysql_poolsize);
		config.setMaxLifetime(Variables.storage_settings_mysql_connlifetime);
		config.setIdleTimeout(Variables.storage_settings_mysql_conntimeout);
		
		// Properties: https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html
		config.addDataSourceProperty("cachePreStmts", "true"); // Enable Prepared Statement caching
		config.addDataSourceProperty("prepStmtCacheSize", "25"); // How many PS cache, default: 25
		config.addDataSourceProperty("useServerPrepStmts", "true"); // If supported use PS server-side
		config.addDataSourceProperty("useLocalSessionState", "true"); // Enable setAutoCommit
		config.addDataSourceProperty("useLocalTransactionState", "true"); // Enable commit/rollbacks
		
		hikariDataSource = new HikariDataSource(config);
	}
	private Connection getConnection() {
		Connection ret = null;
		try {
			ret = hikariDataSource.getConnection();
		} catch (Exception ex) {
			LogHandler.printError("Can't connect to the MySQL server: " + ex.getMessage());
		}
		return ret;
	}
	
	@Override
	public void stop() {
		if (hikariDataSource != null) {
			hikariDataSource.close();
		}
	}
	
	@Override
	public boolean isFailed() {
		return failed;
	}
	
	
	/*
	 * Spies
	 */
	@Override
	public void updateSpies(List<UUID> list) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updateSpies(list, connection);
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData updateSpies(1): " + ex.getMessage());
		}
	}
	private void updateSpies(List<UUID> list, Connection connection) {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate("DELETE FROM " + tableSpies);
			
			for (UUID uuid : list) {
				statement.executeUpdate("INSERT INTO " + tableSpies
						+ " (uuid) VALUES ('" + uuid.toString() + "');");
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData updateSpies(2): " + ex.getMessage());
		}
	}
	@Override
	public List<UUID> getSpies() {
		List<UUID> ret = new ArrayList<UUID>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tableSpies + ";")
					) {
					while (rs.next()) {
						try {
							ret.add(UUID.fromString(rs.getString("uuid")));
						} catch (Exception ex) {}
					}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getSpies(): " + ex.getMessage());
		}
		return ret;
	}
	
	
	/* 
	 * Players
	 */
	@Override
	public void updatePlayer(ThePlayer tp) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updatePlayer(tp, connection);
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData updatePlayer(1): " + ex.getMessage());
		}
	}
	private void updatePlayer(ThePlayer tp, Connection connection) {
		try {
			if (!tp.getPartyName().isEmpty()) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + tablePlayers
						+ " (uuid, party, rank, name, timestamp)"
						+ " VALUES (?,?,?,?,?)"
						+ " ON DUPLICATE KEY UPDATE party=VALUES(party), rank=VALUES(rank), name=VALUES(name), timestamp=VALUES(timestamp);")) {
					preStatement.setString(1, tp.getUUID().toString());
					preStatement.setString(2, tp.getPartyName());
					preStatement.setInt(3, tp.getRank());
					preStatement.setString(4, tp.getName());
					preStatement.setInt(5, (int)(System.currentTimeMillis() / 1000L));
					
					preStatement.executeUpdate();
				}
			} else {
				try (Statement statement = connection.createStatement()) {
					// Delete players that doesn't have a party from DB
					statement.executeUpdate("DELETE FROM " + tablePlayers
							+ " WHERE uuid='" + tp.getUUID().toString() + "';");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData updatePlayer(2): " + ex.getMessage());
		}
	}
	@Override
	public ThePlayer getPlayer(UUID uuid) {
		ThePlayer ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tablePlayers
								+ " WHERE uuid='" + uuid.toString() + "';");
					) {
					if (rs.next())
						ret = getPlayerFromResultSet(connection, rs);
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getPlayer(): " + ex.getMessage());
		}
		return ret;
	}
	@Override
	public void removePlayer(UUID uuid) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate("DELETE FROM " + tablePlayers
							+ " WHERE uuid='" + uuid.toString() + "';");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData removePlayer(): " + ex.getMessage());
		}
	}
	@Override
	public String getPlayerPartyName(UUID uuid) {
		String ret = "";
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tablePlayers
								+ " WHERE uuid='" + uuid.toString() + "';")
					) {
					if (rs.next())
						ret = rs.getString("party");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getPlayerPartyName(): " + ex.getMessage());
		}
		return ret;
	}
	@Override
	public int getRank(UUID uuid) {
		int ret = -1;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tablePlayers
								+ " WHERE uuid='" + uuid.toString() + "';")
					) {
					if (rs.next())
						ret = rs.getInt("rank");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getRank(): " + ex.getMessage());
		}
		return ret;
	}
	@Override
	public HashMap<UUID, Object[]> getPlayersRank(String party) {
		// Object: [String, Integer] -> [name, rank]
		HashMap<UUID, Object[]> ret = new HashMap<UUID, Object[]>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM " + tablePlayers
						+ " WHERE party=?;")) {
					preStatement.setString(1, party);
					try (ResultSet rs = preStatement.executeQuery()) {
						while (rs.next()) {
							try {
								Object[] ob = new Object[] {
										rs.getString("name"),
										rs.getInt("rank")};
								ret.put(UUID.fromString(rs.getString("uuid")), ob);
							} catch (IllegalArgumentException ex) {}
						}
					}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getPlayersRank(): " + ex.getMessage());
		}
		return ret;
	}
	@Override
	public HashMap<UUID, Long> getPlayersFromName(String name) {
		// Object: [UUID, Long] -> [UUID, timestamp]
		HashMap<UUID, Long> hash = new HashMap<UUID, Long>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM " + tablePlayers
						+ " WHERE name=?;")) {
					preStatement.setString(1, name);
					try (ResultSet rs = preStatement.executeQuery()) {
						while (rs.next()) {
							UUID u = UUID.fromString(rs.getString("uuid"));
							Long t = (long) rs.getInt("timestamp");
							hash.put(u, t);
						}
					}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getPlayersFromName(): " + ex.getMessage());
		}
		return hash;
	}
	@Override
	public String getOldPlayerName(UUID uuid) {
		String ret = "";
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tablePlayers
								+ " WHERE uuid='" + uuid.toString() + "';")
					) {
					if (rs.next())
						ret = rs.getString("name");
					
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getOldPlayerName(): " + ex.getMessage());
		}
		return ret;
	}
	
	
	/*
	 *	Parties
	 */
	@Override
	public void updateParty(Party party) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updateParty(party, connection);
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData updateParty(1): " + ex.getMessage());
		}
	}
	private void updateParty(Party party, Connection connection) {
		try (
				PreparedStatement preStatement = connection.prepareStatement(
						"INSERT INTO " + tableParties + " (name, leader, descr, motd, prefix, suffix, color, kills, password, home)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?)"
						+ " ON DUPLICATE KEY UPDATE leader=VALUES(leader), descr=VALUES(descr), motd=VALUES(motd), prefix=VALUES(prefix), suffix=VALUES(suffix), color=VALUES(color), kills=VALUES(kills), password=VALUES(password), home=VALUES(home);")
			) {
			preStatement.setString(1, party.getName());
			preStatement.setString(2, party.isFixed() ? "fixed" : party.getLeader().toString());
			preStatement.setString(3, party.getDescription());
			preStatement.setString(4, party.getMOTD());
			preStatement.setString(5, party.getPrefix());
			preStatement.setString(6, party.getSuffix());
			preStatement.setString(7, party.getColorRaw());
			preStatement.setInt(8, party.getKills());
			preStatement.setString(9, party.getPassword());
			preStatement.setString(10, party.getHomeRaw());
			
			preStatement.executeUpdate();
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData updateParty(2): " + ex.getMessage());
		}
	}
	@Override
	public Party getParty(String party) {
		Party ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tableParties
								+ " WHERE name='" + party + "';")
					) {
						if (rs.next())
							ret = getPartyFromResultSet(connection, rs);
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getParty(): " + ex.getMessage());
		}
		return ret;
	}
	@Override
	public void renameParty(String prev, String next) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate("UPDATE " + tableParties
							+ " SET name='" + next + "'"
							+ " WHERE name='" + prev + "';");
					statement.executeUpdate("UPDATE " + tablePlayers
							+ " SET party='" + next + "'"
							+ " WHERE party='" + prev + "';");
				}
				
				connection.commit();
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData removeParty(): " + ex.getMessage());
		}
	}
	@Override
	public void removeParty(Party party) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate("DELETE FROM " + tableParties
							+ " WHERE name='" + party.getName() + "';");
					statement.executeUpdate("DELETE FROM " + tablePlayers
							+ " WHERE party='" + party.getName() + "';");
				}
				
				connection.commit();
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData removeParty(): " + ex.getMessage());
		}
	}
	@Override
	public boolean existParty(String party) {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tableParties
								+ " WHERE name='" + party + "';")
					) {
						if (rs.next())
							ret = true;
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData existParty(): " + ex.getMessage());
		}
		return ret;
	}
	@Override
	public List<Party> getAllParties() {
		List<Party> list = new ArrayList<Party>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + tableParties + ";")
					) {
						while (rs.next()) {
							list.add(getPartyFromResultSet(connection, rs));
						}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getAllParties(): " + ex.getMessage());
		}
		return list;
	}
	@Override
	public List<String> getAllFixed() {
		List<String> list = new ArrayList<String>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT name FROM " + tableParties
								+ " WHERE leader='fixed';")
					) {
						while (rs.next()) {
							list.add(rs.getString("name"));
						}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getAllFixed(): " + ex.getMessage());
		}
		return list;
	}
	
	
	/*
	 * Log
	 */
	@Override
	public void insertLog(LogLine line) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + tableLog + " (date, level, position, message) VALUES (?,?,?,?);")) {
					preStatement.setTimestamp(1, new Timestamp(line.getFullDate().getTime()));
					preStatement.setInt(2, Integer.valueOf(line.getLevel()));
					preStatement.setString(3, line.getPosition());
					preStatement.setString(3, line.getMessage());
					preStatement.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData insertLog(): " + ex.getMessage());
		}
	}
	
	
	/*
	 * Private MySQL queries
	 */
	private ThePlayer getPlayerFromResultSet(Connection connection, ResultSet rs) {
		ThePlayer ret = null;
		try {
			ret = new ThePlayer(UUID.fromString(rs.getString("uuid")), plugin);
			ret.setPartyName(rs.getString("party"));
			ret.setRank(rs.getInt("rank"));
			
			// Last - call updatePlayer if name is different
			ret.compareName(rs.getString("name"));
		} catch (Exception ex) {
			LogHandler.printError("Error in MySQLData getPlayerFromResultSet(): " + ex.getMessage());
		}
		return ret;
	}
	private Party getPartyFromResultSet(Connection connection, ResultSet rs) {
		Party ret = null;
		try {
			ret = new Party(rs.getString("name"), plugin);
			ret.setDescription(rs.getString("descr"));
			ret.setMOTD(rs.getString("motd"));
			ret.setPrefix(rs.getString("prefix"));
			ret.setSuffix(rs.getString("suffix"));
			ret.setColorRaw(rs.getString("color"));
			ret.setKills(rs.getInt("kills"));
			ret.setPassword(rs.getString("password"));
			ret.setHomeRaw(rs.getString("home"));
			
			String leader = rs.getString("leader");
			if (leader != null) {
				if (leader.equalsIgnoreCase("fixed")) {
					ret.setLeader(UUID.fromString("00000000-0000-0000-0000-000000000000"));
					ret.setFixed(true);
				} else
					ret.setLeader(UUID.fromString(leader));
			}
			ret.setMembers(getMembersParty(connection, rs.getString("name")));
		} catch (Exception ex) {
			LogHandler.printError("Error in MySQLData getPartyFromResultSet(): " + ex.getMessage());
		}
		return ret;
	}
	private List<UUID> getMembersParty(Connection connection, String party) {
		List<UUID> list = new ArrayList<UUID>();
		try (
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("SELECT uuid FROM " + tablePlayers + " WHERE party='" + party + "';")
			) {
				while (rs.next()) {
					try {
						list.add(UUID.fromString(rs.getString("uuid")));
					} catch (IllegalArgumentException ex) {}
				}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData getPartyMembers(): " + ex.getMessage());
		}
		return list;
	}
	
	
	/*
	 * TABLES
	 */
	public void initTables(Connection connection) {
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			// Parties
			try (ResultSet rs = metadata.getTables(null, null, tableParties, null)) {
				if (rs.next())
					checkUpgrades(connection, MySQLTable.PARTIES); // Checking for porting
				else
					createTable(connection, MySQLTable.PARTIES); // Create table
			} catch (SQLException ex) {
				LogHandler.printError("Error in MySQLData initTables(parties): " + ex.getMessage());
			}
			// Players
			try (ResultSet rs = metadata.getTables(null, null, tablePlayers, null)) {
				if (rs.next())
					checkUpgrades(connection, MySQLTable.PLAYERS); // Checking for porting
				else
					createTable(connection, MySQLTable.PLAYERS); // Create table
			} catch (SQLException ex) {
				LogHandler.printError("Error in MySQLData initTables(players): " + ex.getMessage());
			}
			// Spies
			try (ResultSet rs = metadata.getTables(null, null, tableSpies, null)) {
				if (rs.next())
					checkUpgrades(connection, MySQLTable.SPIES); // Checking for porting
				else
					createTable(connection, MySQLTable.SPIES); // Create table
			} catch (SQLException ex) {
				LogHandler.printError("Error inMySQLData initTables(spies): " + ex.getMessage());
			}
		} catch (Exception ex) {
			LogHandler.printError("Error in MySQLData initTables(): " + ex.getMessage());
		}
	}
	public void checkUpgrades(Connection connection, MySQLTable type) {
		String table = null;
		switch (type) {
		case PARTIES:
			table = tableParties;
			break;
		case PLAYERS:
			table = tablePlayers;
			break;
		case SPIES:
			table = tableSpies;
		case LOG:
			table = tableLog;
		}
		
		try (PreparedStatement statement = connection.prepareStatement("SELECT table_comment FROM INFORMATION_SCHEMA.tables WHERE table_schema=? AND table_name=?;")) {
			statement.setString(1, connection.getCatalog());
			statement.setString(2, table);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					String cmnt = rs.getString("table_comment");
					int version = 0;
					if (!cmnt.isEmpty())
						version = Integer.valueOf(cmnt.split(":")[1]);
					upgradeTable(version, connection, type, table);
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData checkUpgrades(" + table + "): " + ex.getMessage());
		}
	}
	public void createTable(Connection connection, MySQLTable type) {
		try (Statement statement = connection.createStatement()) {
			switch (type) {
			case PARTIES:
				// Parties
				statement.execute("CREATE TABLE " + tableParties
						+ " (name VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "leader VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "descr VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "motd VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "prefix VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "suffix VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "color VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "kills INT DEFAULT 0,"
							+ "password VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "home VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "PRIMARY KEY (name))"
						+ "COMMENT='Database version (do not edit):"+tablePartiesVersion+"';");
				break;
			case PLAYERS:
				// Players
				statement.execute("CREATE TABLE " + tablePlayers
						+ " (uuid VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "party VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "rank INT DEFAULT 0,"
							+ "name VARCHAR(" + varcharSize + "),"
							+ "timestamp INT,"
							+ "PRIMARY KEY (uuid))"
						+ "COMMENT='Database version (do not edit):"+tablePlayersVersion+"';");
				break;
			case SPIES:
				// Spies
				statement.execute("CREATE TABLE " + tableSpies
						+ " (uuid VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "PRIMARY KEY (uuid))"
						+ "COMMENT='Database version (do not edit):"+tableSpiesVersion+"';");
				break;
			case LOG:
				// Log
				statement.execute("CREATE TABLE " + tableLog
						+ " (id INT NOT NULL AUTO_INCREMENT,"
							+ "date DATETIME,"
							+ "level TINYINT,"
							+ "position VARCHAR(" + varcharSize + "),"
							+ "message VARCHAR(" + varcharSize + "),"
							+ "PRIMARY KEY (id))"
						+ "COMMENT='Database version (do not edit):"+tableLogVersion+"';");
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in MySQLData createTable(" + type + "): " + ex.getMessage());
		}
	}
	
	
	/*
	 * Migration
	 */
	public boolean migration_migrateData(DatabaseInterface to) {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				// Spies
				try (ResultSet rs = statement.executeQuery("SELECT * FROM " + tableSpies + ";")) {
					List<UUID> list = new ArrayList<UUID>();
					while (rs.next()) {
						try {
							list.add(UUID.fromString(rs.getString("uuid")));
						} catch (Exception ex) {}
					}
					to.updateSpies(list);
				}
				
				// Players
				try (ResultSet rs = statement.executeQuery("SELECT * FROM " + tablePlayers + ";")) {
					while (rs.next()) {
						ThePlayer tp = getPlayerFromResultSet(connection, rs);
						to.updatePlayer(tp);
					}
				}
				
				// Parties
				try (ResultSet rs = statement.executeQuery("SELECT * FROM " + tableParties + ";")) {
					while (rs.next()) {
						Party party = getPartyFromResultSet(connection, rs);
						to.updateParty(party);
					}
				}
				
				ret = true;
			}
		} catch (Exception ex) {
			LogHandler.printError("Error in MySQLData migration_migrateData(): " + ex.getMessage());
		}
		return ret;
	}
	@SuppressWarnings("unchecked")
	public boolean migration_storeYAML(List<?>[] yaml, DatabaseInterface from) {
		// yaml = [List<UUID>, List<UUID>, List<String>]
		List<UUID> listSpies = (List<UUID>) yaml[0];
		List<UUID> listPlayers = (List<UUID>) yaml[1];
		List<String> listParties = (List<String>) yaml[2];
		boolean ret = false;
		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);
			//Renaming tables
			List<String> tables = new ArrayList<String>();
			DatabaseMetaData metadata = connection.getMetaData();
			try (ResultSet rs = metadata.getTables(null, null, "%", null)) {
				while (rs.next()) {
					tables.add(rs.getString(3));
				}
			}
			// Spies
			migration_renameTable(connection, tables, tableSpies);
			createTable(connection, MySQLTable.SPIES);
			updateSpies(listSpies, connection);
			
			// Players
			migration_renameTable(connection, tables, tablePlayers);
			createTable(connection, MySQLTable.PLAYERS);
			for (UUID uuid : listPlayers) {
				ThePlayer tp = from.getPlayer(uuid);
				updatePlayer(tp, connection);
			}
			
			// Parties
			migration_renameTable(connection, tables, tableParties);
			createTable(connection, MySQLTable.PARTIES);
			for (String name : listParties) {
				Party party = from.getParty(name);
				updateParty(party, connection);
			}
			
			connection.commit();
			ret = true;
		} catch (Exception ex) {
			LogHandler.printError("Error in MySQLData migration_storeYAML(): " + ex.getMessage());
		}
		return ret;
	}
	private void migration_renameTable(Connection connection, List<String> existingTables, String table) throws SQLException {
		String newTable = table + Variables.storage_migrate_suffix;
		int count = 1;
		while (existingTables.contains(newTable)) {
			newTable = table + Variables.storage_migrate_suffix + Integer.toString(count);
			count++;
		}
		
		try (Statement statement = connection.createStatement()) {
			statement.execute("RENAME TABLE " + table + " TO " + newTable + ";");
		}
	}
	
	/*
	 * Upgrading rules
	 */
	private void upgradeTable(int version, Connection connection, MySQLTable type, String table) {
		switch (type) {
		case PARTIES:
			if (version < tablePartiesVersion) {
				upgradeTablePreparation(version, connection, type, table);
			}
			break;
		case PLAYERS:
			if (version < tablePlayersVersion) {
				upgradeTablePreparation(version, connection, type, table);
			}
			break;
		case SPIES:
			if (version < tableSpiesVersion) {
				upgradeTablePreparation(version, connection, type, table);
			}
			break;
		case LOG:
			if (version < tableSpiesVersion) {
				upgradeTablePreparation(version, connection, type, table);
			}
			break;
		}
	}
	private void upgradeTablePreparation(int version, Connection connection, MySQLTable type, String table) {
		try (Statement statement = connection.createStatement()) {
			
			statement.execute("RENAME TABLE " + table + " TO " + table + "_temp;");
			createTable(connection, type);
			
			try (ResultSet rs = statement.executeQuery("SELECT * FROM " + table + "_temp;");) {
				switch (type) {
				case PARTIES:
					// Parties
					upgradeTableParties(rs, version, connection, table);
				case PLAYERS:
					// Players
					upgradeTablePlayers(rs, version, connection, table);
					break;
				case SPIES:
					// Spies
					upgradeTableSpies(rs, version, connection, table);
					break;
				case LOG:
					// Spies
					upgradeTableLog(rs, version, connection, table);
					break;
				}
			} catch (Exception ex) {
				LogHandler.printError("Error in MySQLData upgradeTablePreparation(" + table + ") upgrade from " + version + " (Insert Into): " + ex.getMessage());
			}
			
			//statement.execute("DROP TABLE " + table + "_temp;");
		} catch (SQLException ex) {
			LogHandler.printError("Error in  MySQLData upgradeTablePreparation(" + table + "): " + ex.getMessage());
		}
	}
	private void upgradeTableParties(ResultSet rs, int version, Connection connection, String table) throws SQLException {
		switch (version) {
		case 0:
		case 1:
			// Upgrading from 1.7/2.0
			while (rs.next()) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + table + " (name,leader,descr,motd,prefix,suffix,color,kills,password,home) VALUES (?,?,?,?,?,?,?,?,?,?);")) {
					preStatement.setString(1, rs.getString("name"));
					preStatement.setString(2, rs.getString("leader"));
					preStatement.setString(3, rs.getString("descr"));
					preStatement.setString(4, rs.getString("motd"));
					preStatement.setString(5, rs.getString("prefix"));
					preStatement.setString(6, rs.getString("suffix"));
					preStatement.setString(7, "");
					preStatement.setInt(8, rs.getInt("kills"));
					preStatement.setString(9, rs.getString("password"));
					preStatement.setString(10, rs.getString("home"));
					preStatement.executeUpdate();
				}
			}
			break;
		}
	}
	private void upgradeTablePlayers(ResultSet rs, int version, Connection connection, String table) throws SQLException {
		switch (version) {
		case 0:
			// Upgrading from 1.7
			while (rs.next()) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + table + " (uuid,party,rank,name,timestamp) VALUES (?,?,?,'',NULL);")) {
					preStatement.setString(1, rs.getString("nickname"));
					preStatement.setString(2, rs.getString("party"));
					preStatement.setInt(3, rs.getInt("rank"));
					preStatement.executeUpdate();
				}
			}
		}
	}
	private void upgradeTableSpies(ResultSet rs, int version, Connection connection, String table) throws SQLException {
		switch (version) {
		case 0:
			// Upgrading from 1.7
			while (rs.next()) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + table + " (uuid) VALUES (?);")) {
					preStatement.setString(1, rs.getString("name"));
					preStatement.executeUpdate();
				}
			}
		}
	}
	private void upgradeTableLog(ResultSet rs, int version, Connection connection, String table) throws SQLException {
		switch (version) {
		case 0:
			// Upgrading from 1.7
			while (rs.next()) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + table + " (id, date, level, position, message) VALUES (?,?,?,'',?);")) {
					preStatement.setInt(1, rs.getInt("line"));
					preStatement.setString(2, rs.getString("date") + " " + rs.getString("time"));
					preStatement.setInt(3, rs.getInt("level"));
					preStatement.setString(4, rs.getString("message"));
					preStatement.executeUpdate();
				}
			}
		}
	}
	
	
	public enum MySQLTable {
		PARTIES, PLAYERS, SPIES, LOG;
	}
}
