package com.alessiodp.parties.configuration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class SQLData {
	private Parties plugin;

	private String username;
	private String password;
	private String url;
	private boolean failed;
	
	private int tablePartiesVersion = 1;
	private int tablePlayersVersion = 1;
	private int tableSpiesVersion = 1;
	
	private String varcharSize;

	public SQLData(Parties instance, String un, String pw, String u, int varchar) {
		plugin = instance;
		username = un;
		password = pw;
		url = u;
		varcharSize = Integer.toString(varchar);
		
		Connection connection = getConnection();
		if (connection != null) {
			initTables(connection);
			failed = false;
		} else
			failed = true;
	}
	
	/*
	 * Spies
	 */
	public void updateSpies(List<UUID> list) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updateSpies(list, connection);
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query updateSpies(1): " + ex.getMessage());
		}
	}
	public void updateSpies(List<UUID> list, Connection connection) {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate("DELETE FROM " + Variables.database_sql_tables_spies);
			
			for (UUID uuid : list) {
				statement.executeUpdate("INSERT INTO " + Variables.database_sql_tables_spies
						+ " (uuid) VALUES ('" + uuid.toString() + "');");
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query updateSpies(2): " + ex.getMessage());
		}
	}
	public List<UUID> getSpies() {
		List<UUID> ret = new ArrayList<UUID>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_spies + ";")
					) {
					while (rs.next()) {
						try {
							ret.add(UUID.fromString(rs.getString("uuid")));
						} catch (Exception ex) {}
					}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getSpies(): " + ex.getMessage());
		}
		return ret;
	}
	
	
	/* 
	 * Players
	 */
	public void updatePlayer(ThePlayer tp) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updatePlayer(tp, connection);
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query updatePlayer(1): " + ex.getMessage());
		}
	}
	public void updatePlayer(ThePlayer tp, Connection connection) {
		try {
			if (!tp.getPartyName().isEmpty()) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + Variables.database_sql_tables_players
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
					statement.executeUpdate("DELETE FROM " + Variables.database_sql_tables_players
							+ " WHERE uuid='" + tp.getUUID().toString() + "';");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query updatePlayer(2): " + ex.getMessage());
		}
	}
	public ThePlayer getPlayer(UUID uuid) {
		ThePlayer ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_players
								+ " WHERE uuid='" + uuid.toString() + "';");
					) {
					if (rs.next())
						ret = this.getPlayerFromResultSet(connection, rs);
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getPlayer(): " + ex.getMessage());
		}
		return ret;
	}
	public void removePlayer(UUID uuid) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate("DELETE FROM " + Variables.database_sql_tables_players
							+ " WHERE uuid='" + uuid.toString() + "';");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query removePlayer(): " + ex.getMessage());
		}
	}
	public String getPlayerPartyName(UUID uuid) {
		String ret = "";
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_players
								+ " WHERE uuid='" + uuid.toString() + "';")
					) {
					if (rs.next())
						ret = rs.getString("party");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getPlayerPartyName(): " + ex.getMessage());
		}
		return ret;
	}
	public int getRank(UUID uuid) {
		int ret = -1;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_players
								+ " WHERE uuid='" + uuid.toString() + "';")
					) {
					if (rs.next())
						ret = rs.getInt("rank");
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getRank(): " + ex.getMessage());
		}
		return ret;
	}
	public HashMap<UUID, Object[]> getPlayersRank(String party) {
		// Object: [String, Integer] -> [name, rank]
		HashMap<UUID, Object[]> ret = new HashMap<UUID, Object[]>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM " + Variables.database_sql_tables_players
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
			LogHandler.printError("Error in SQL Query getPlayersRank(): " + ex.getMessage());
		}
		return ret;
	}
	public HashMap<UUID, Long> getPlayersFromName(String name) {
		// Object: [UUID, Long] -> [UUID, timestamp]
		HashMap<UUID, Long> hash = new HashMap<UUID, Long>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM " + Variables.database_sql_tables_players
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
			LogHandler.printError("Error in SQL Query getOldPlayerName(): " + ex.getMessage());
		}
		return hash;
	}
	public String getOldPlayerName(UUID uuid) {
		String ret = "";
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_players
								+ " WHERE uuid='" + uuid.toString() + "';")
					) {
					if (rs.next())
						ret = rs.getString("name");
					
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getOldPlayerName(): " + ex.getMessage());
		}
		return ret;
	}
	
	
	/*
	 *	Parties
	 */
	public void updateParty(Party party) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updateParty(party, connection);
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query updateParty(1): " + ex.getMessage());
		}
	}
	public void updateParty(Party party, Connection connection) {
		try (
				PreparedStatement preStatement = connection.prepareStatement(
						"INSERT INTO " + Variables.database_sql_tables_parties + " (name, leader, descr, motd, prefix, suffix, kills, password, home)"
						+ " VALUES (?,?,?,?,?,?,?,?,?)"
						+ " ON DUPLICATE KEY UPDATE leader=VALUES(leader), descr=VALUES(descr), motd=VALUES(motd), prefix=VALUES(prefix), suffix=VALUES(suffix), kills=VALUES(kills), password=VALUES(password), home=VALUES(home);")
			) {
			preStatement.setString(1, party.getName());
			preStatement.setString(2, party.isFixed() ? "fixed" : party.getLeader().toString());
			preStatement.setString(3, party.getDescription());
			preStatement.setString(4, party.getMOTD());
			preStatement.setString(5, party.getPrefix());
			preStatement.setString(6, party.getSuffix());
			preStatement.setInt(7, party.getKills());
			preStatement.setString(8, party.getPassword());
			preStatement.setString(9, party.getHomeRaw());
			
			preStatement.executeUpdate();
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query updateParty(2): " + ex.getMessage());
		}
	}
	public Party getParty(String party) {
		Party ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_parties
								+ " WHERE name='" + party + "';")
					) {
						if (rs.next())
							ret = getPartyFromResultSet(connection, rs);
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getParty(): " + ex.getMessage());
		}
		return ret;
	}
	public void renameParty(String prev, String next) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate("UPDATE " + Variables.database_sql_tables_parties
							+ " SET name='" + next + "'"
							+ " WHERE name='" + prev + "';");
					statement.executeUpdate("UPDATE " + Variables.database_sql_tables_players
							+ " SET party='" + next + "'"
							+ " WHERE party='" + prev + "';");
				}
				
				connection.commit();
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query removeParty(): " + ex.getMessage());
		}
	}
	public void removeParty(String party) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate("DELETE FROM " + Variables.database_sql_tables_parties
							+ " WHERE name='" + party + "';");
					statement.executeUpdate("DELETE FROM " + Variables.database_sql_tables_players
							+ " WHERE party='" + party + "';");
				}
				
				connection.commit();
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query removeParty(): " + ex.getMessage());
		}
	}
	public boolean existParty(String party) {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_parties + " WHERE name='" + party + "';")
					) {
						if (rs.next())
							ret = true;
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query existParty(): " + ex.getMessage());
		}
		return ret;
	}
	public List<Party> getAllParties() {
		List<Party> list = new ArrayList<Party>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_parties + ";")
					) {
						while (rs.next()) {
							list.add(getPartyFromResultSet(connection, rs));
						}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getAllParties(): " + ex.getMessage());
		}
		return list;
	}
	public List<String> getAllFixed() {
		List<String> list = new ArrayList<String>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery("SELECT name FROM " + Variables.database_sql_tables_parties + " WHERE leader='fixed';")
					) {
						while (rs.next()) {
							list.add(rs.getString("name"));
						}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getAllFixed(): " + ex.getMessage());
		}
		return list;
	}
	
	/*
	 * Private SQL query
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
			LogHandler.printError("Error in SQL Query getPlayerFromResultSet(): " + ex.getMessage());
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
			LogHandler.printError("Error in SQL Query getPartyFromResultSet(): " + ex.getMessage());
		}
		return ret;
	}
	private List<UUID> getMembersParty(Connection connection, String party) {
		List<UUID> list = new ArrayList<UUID>();
		try (
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("SELECT uuid FROM " + Variables.database_sql_tables_players + " WHERE party='" + party + "';")
			) {
				while (rs.next()) {
					try {
						list.add(UUID.fromString(rs.getString("uuid")));
					} catch (IllegalArgumentException ex) {}
				}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Query getPartyMembers(): " + ex.getMessage());
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
			try (ResultSet rs = metadata.getTables(null, null, Variables.database_sql_tables_parties, null)) {
				if (rs.next())
					checkUpgrades(connection, 1); // Checking for porting
				else
					createTable(connection, 1); // Create table
			} catch (SQLException ex) {
				LogHandler.printError("Error in SQL init tables (parties): " + ex.getMessage());
			}
			// Players
			try (ResultSet rs = metadata.getTables(null, null, Variables.database_sql_tables_players, null)) {
				if (rs.next())
					checkUpgrades(connection, 2); // Checking for porting
				else
					createTable(connection, 2); // Create table
			} catch (SQLException ex) {
				LogHandler.printError("Error in SQL init tables (players): " + ex.getMessage());
			}
			// Spies
			try (ResultSet rs = metadata.getTables(null, null, Variables.database_sql_tables_spies, null)) {
				if (rs.next())
					checkUpgrades(connection, 3); // Checking for porting
				else
					createTable(connection, 3); // Create table
			} catch (SQLException ex) {
				LogHandler.printError("Error in SQL init tables (spies): " + ex.getMessage());
			}
		} catch (Exception ex) {
			LogHandler.printError("Error in SQL init tables: " + ex.getMessage());
		}
	}
	public void checkUpgrades(Connection connection, int type) {
		String table;
		switch (type) {
		case 3:
			table = Variables.database_sql_tables_spies;
			break;
		case 2:
			table = Variables.database_sql_tables_players;
			break;
		default:
			table = Variables.database_sql_tables_parties;
		}
		
		try (PreparedStatement statement = connection.prepareStatement("SELECT table_comment FROM INFORMATION_SCHEMA.tables WHERE table_schema=? AND table_name=?;")) {
			statement.setString(1, connection.getCatalog());
			statement.setString(2, table);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					if (rs.getString("table_comment").isEmpty()) {
						// 1.7 > 1.8
						upgradeLogTable17(connection, type, table);
					}
				}
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL check upgrades for " + table + ": " + ex.getMessage());
		}
	}
	private void upgradeLogTable17(Connection connection, int type, String table) {
		try (Statement statement = connection.createStatement()) {
			
			statement.execute("RENAME TABLE " + table + " TO " + table + "_temp;");
			createTable(connection, type);
			
			try (ResultSet rs = statement.executeQuery("SELECT * FROM " + table + "_temp;");) {
				while (rs.next()) {
					switch (type) {
					case 3:
						// Spies
						try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + table + " (uuid) VALUES (?);")) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.executeUpdate();
						}
						break;
					case 2:
						// Players
						try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + table + " (uuid,party,rank,name,timestamp) VALUES (?,?,?,'',NULL);")) {
							preStatement.setString(1, rs.getString("nickname"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.executeUpdate();
						}
						break;
					default:
						// Parties
						try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + table + " (name,leader,descr,motd,prefix,suffix,kills,password,home) VALUES (?,?,?,?,?,?,?,?,?);")) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("descr"));
							preStatement.setString(4, rs.getString("motd"));
							preStatement.setString(5, rs.getString("prefix"));
							preStatement.setString(6, rs.getString("suffix"));
							preStatement.setInt(7, rs.getInt("kills"));
							preStatement.setString(8, rs.getString("password"));
							preStatement.setString(9, rs.getString("home"));
							preStatement.executeUpdate();
						}
					}
				}
			} catch (Exception ex) {
				LogHandler.printError("Error in SQL Table (" + table + ") upgrade (Insert Into): " + ex.getMessage());
			}
			
			statement.execute("DROP TABLE " + table + "_temp;");
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Table upgrade 1.7: " + ex.getMessage());
		}
	}
	public void createTable(Connection connection, int type) {
		try (Statement statement = connection.createStatement()) {
			switch (type) {
			case 1:
				// Parties
				statement.execute("CREATE TABLE " + Variables.database_sql_tables_parties
						+ " (name VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "leader VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "descr VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "motd VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "prefix VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "suffix VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "kills INT DEFAULT 0,"
							+ "password VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "home VARCHAR(" + varcharSize + ") DEFAULT '',"
							+ "PRIMARY KEY (name))"
						+ "COMMENT='Database version (do not edit):"+tablePartiesVersion+"';");
				break;
			case 2:
				// Players
				statement.execute("CREATE TABLE " + Variables.database_sql_tables_players
						+ " (uuid VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "party VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "rank INT DEFAULT 0,"
							+ "name VARCHAR(" + varcharSize + "),"
							+ "timestamp INT,"
							+ "PRIMARY KEY (uuid))"
						+ "COMMENT='Database version (do not edit):"+tablePlayersVersion+"';");
				break;
			case 3:
				// Spies
				statement.execute("CREATE TABLE " + Variables.database_sql_tables_spies
						+ " (uuid VARCHAR(" + varcharSize + ") NOT NULL,"
							+ "PRIMARY KEY (uuid))"
						+ "COMMENT='Database version (do not edit):"+tableSpiesVersion+"';");
			}
		} catch (SQLException ex) {
			LogHandler.printError("Error in SQL Table (" + type + ") Creation: " + ex.getMessage());
		}
	}
	
	/*
	 * Migration
	 */
	public boolean migrateSQLtoYAML() {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			try (Statement statement = connection.createStatement()) {
				// Spies
				try (ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_spies + ";")) {
					List<UUID> list = new ArrayList<UUID>();
					while (rs.next()) {
						try {
							list.add(UUID.fromString(rs.getString("uuid")));
						} catch (Exception ex) {}
					}
					plugin.getDataHandler().updateSpies(list, true);
				}
				
				// Players
				try (ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_players + ";")) {
					while (rs.next()) {
						ThePlayer tp = getPlayerFromResultSet(connection, rs);
						plugin.getDataHandler().updatePlayer(tp, true);
					}
				}
				
				// Parties
				try (ResultSet rs = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_parties + ";")) {
					while (rs.next()) {
						Party party = getPartyFromResultSet(connection, rs);
						plugin.getDataHandler().updateParty(party, true);
					}
				}
				
				ret = true;
			}
		} catch (Exception ex) {
			LogHandler.printError("Error in SQL Query migrateSQLtoYAML(): " + ex.getMessage());
		}
		return ret;
	}
	
	public boolean migrateYAMLtoSQL(List<UUID> spies, List<UUID> players, List<String> parties) {
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
			migrateYAMLtoSQLrenaming(connection, tables, Variables.database_sql_tables_spies);
			createTable(connection, 3);
			updateSpies(plugin.getDataHandler().getSpies(true), connection);
			
			// Players
			migrateYAMLtoSQLrenaming(connection, tables, Variables.database_sql_tables_players);
			createTable(connection, 2);
			for (UUID uuid : players) {
				ThePlayer tp = plugin.getDataHandler().getPlayer(uuid, true);
				updatePlayer(tp, connection);
			}
			
			// Parties
			migrateYAMLtoSQLrenaming(connection, tables, Variables.database_sql_tables_parties);
			createTable(connection, 1);
			for (String name : parties) {
				Party party = plugin.getDataHandler().getParty(name, true);
				updateParty(party, connection);
			}
			
			connection.commit();
			ret = true;
		} catch (Exception ex) {
			LogHandler.printError("Error in SQL Query migrateYAMLtoSQL(): " + ex.getMessage());
		}
		return ret;
	}
	private void migrateYAMLtoSQLrenaming(Connection connection, List<String> existingTables, String table) throws SQLException {
		String newTable = table + Variables.database_migrate_suffix;
		int count = 1;
		while (existingTables.contains(newTable)) {
			newTable = table + Variables.database_migrate_suffix + Integer.toString(count);
			count++;
		}
		
		try (Statement statement = connection.createStatement()) {
			statement.execute("RENAME TABLE " + table + " TO " + newTable + ";");
		}
	}
	
	/*
	 * Connection
	 */
	
	/* Upcoming db
	public void initDatabase() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		
		testDS = new HikariDataSource(config);
	}
	HikariDataSource testDS;
	public Connection getConnection2() throws SQLException {
		return testDS.getConnection();
	}
	*/
	public boolean isFailed() {
		return failed;
	}
	private boolean haveDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return true;
		} catch (ClassNotFoundException ex) {
			LogHandler.printError("MySQL Driver missing: " + ex.getMessage());
		}
		return false;
	}
	private Connection getConnection() {
		Connection ret = null;
		try {
			if (haveDriver())
				ret = DriverManager.getConnection(url, username, password);
		} catch (SQLException ex) {
			LogHandler.printError("Can't connect to the server SQL: " + ex.getMessage());
		}
		return ret;
	}
}
