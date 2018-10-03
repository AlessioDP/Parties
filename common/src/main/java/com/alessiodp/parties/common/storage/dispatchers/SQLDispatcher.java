package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.logging.LogLine;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.HomeLocationImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.DatabaseData;
import com.alessiodp.parties.common.storage.StorageType;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseSQL;
import com.alessiodp.parties.common.storage.sql.MySQLDao;
import com.alessiodp.parties.common.storage.sql.SQLTable;
import com.alessiodp.parties.common.storage.sql.SQLUpgradeManager;
import com.alessiodp.parties.common.storage.sql.SQLiteDao;

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
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class SQLDispatcher implements IDatabaseDispatcher {
	private PartiesPlugin plugin;
	
	private StorageType databaseType;
	private IDatabaseSQL database;
	
	private HashMap<SQLTable, String> schemaTables;
	
	public SQLDispatcher(PartiesPlugin instance) {
		plugin = instance;
		schemaTables = new HashMap<>();
	}
	
	@Override
	public void init(StorageType type) {
		SQLTable.setupTables();
		
		switch (type) {
		case MYSQL:
			database = new MySQLDao(plugin);
			database.initSQL();
			break;
		case SQLITE:
			database = new SQLiteDao(plugin);
			database.initSQL();
			break;
		case YAML:
		case NONE:
			// Pass
		}
		
		if (database != null && !database.isFailed()) {
			databaseType = type;
			database.handleSchema(schemaTables);
			initTables(getConnection());
		}
	}
	@Override
	public void stop() {
		if (database != null) {
			database.stopSQL();
		}
	}
	
	@Override
	public DatabaseData loadEntireData() {
		// Players
		Map<UUID, PartyPlayerImpl> players = new HashMap<>();
		for (PartyPlayerImpl pp : getAllPlayers())
			players.put(pp.getPlayerUUID(), pp);
		
		// Parties
		Map<String, PartyImpl> parties = new HashMap<>();
		for (PartyImpl p : getAllParties())
			parties.put(p.getName(), p);
		
		return new DatabaseData(players, parties);
	}
	@Override
	public boolean saveEntireData(DatabaseData data) {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);
			
			// Players
			for (Entry<UUID, PartyPlayerImpl> entry : data.getPlayers().entrySet())
				updatePlayer(entry.getValue(), connection);
			
			// Parties
			for (Entry<String, PartyImpl> entry : data.getParties().entrySet()) {
				updateParty(entry.getValue(), connection);
			}
			
			connection.commit();
			ret = true;
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	@Override
	public boolean prepareNewOutput() {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);
			
			// Players
			renameTable(connection, SQLTable.PLAYERS.getTableName(), ConfigMain.STORAGE_MIGRATE_SUFFIX);
			createTable(connection, SQLTable.PLAYERS);
			
			// Parties
			renameTable(connection, SQLTable.PARTIES.getTableName(), ConfigMain.STORAGE_MIGRATE_SUFFIX);
			createTable(connection, SQLTable.PARTIES);
			
			connection.commit();
			ret = true;
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	
	public String renameTable(Connection connection, String table, String tableSuffix) throws SQLException {
		String ret = "";
		// Load existing tables
		List<String> listTables = new ArrayList<>();
		DatabaseMetaData metadata = connection.getMetaData();
		try (ResultSet rs = metadata.getTables(null, null, "%", null)) {
			while (rs.next()) {
				listTables.add(rs.getString(3));
			}
		}
		
		String newTable = table + tableSuffix;
		int count = 1;
		while (listTables.contains(newTable)) {
			newTable = table + tableSuffix + Integer.toString(count);
			count++;
		}
		
		try (Statement statement = connection.createStatement()) {
			String query = Constants.QUERY_GENERIC_MYSQL_RENAME;
			if (databaseType == StorageType.SQLITE)
				query = Constants.QUERY_GENERIC_SQLITE_RENAME;
			
			statement.executeUpdate(query
					.replace("{table}", table)
					.replace("{newtable}", newTable));
			ret = newTable;
		}
		return ret;
	}
	
	private Connection getConnection() {
		return database.getConnection();
	}
	public boolean isFailed() {
		return database.isFailed();
	}
	
	public void createTable(Connection connection, SQLTable table) {
		try (Statement statement = connection.createStatement()) {
			String versionQuery = Constants.QUERY_CHECKVERSION_SET_MYSQL;
			if (databaseType.isSQLite())
				versionQuery = Constants.QUERY_CHECKVERSION_SET_SQLITE;
			int versionValue = Constants.VERSION_DATABASE_MYSQL;
			if (databaseType.isSQLite())
				versionValue = Constants.VERSION_DATABASE_SQLITE;
			
			// Create table
			statement.executeUpdate(SQLTable.formatSchema(schemaTables.get(table), Constants.VERSION_DATABASE_MYSQL));
			
			// Change version into the versions table
			if (!table.equals(SQLTable.VERSIONS)) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(versionQuery))) {
					preStatement.setString(1, table.getTableName());
					preStatement.setInt(2, versionValue);
					preStatement.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR_TABLE
					.replace("{table}", table.name()), ex));
		}
	}
	private void initTables(Connection connection) {
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			for (SQLTable table : SQLTable.values()) {
				try (ResultSet rs = metadata.getTables(null, null, table.getTableName(), null)) {
					if (rs.next()) {
						SQLUpgradeManager.checkUpgrades(this, connection, table, databaseType); // Checking for porting
					} else {
						createTable(connection, table); // Create table
					}
				} catch (SQLException ex) {
					LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
				}
			}
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	
	/* 
	 * Players
	 */
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updatePlayer(player, connection);
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	private void updatePlayer(PartyPlayerImpl player, Connection connection) {
		try {
			boolean existData = false;
			if (!player.getPartyName().isEmpty() || player.isSpy() || player.isMuted())
				existData = true;
			
			if (existData) {
				// Save the data
				String query = Constants.QUERY_PLAYER_INSERT_MYSQL;
				if (databaseType == StorageType.SQLITE)
					query = Constants.QUERY_PLAYER_INSERT_SQLITE;
				
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(query))) {
					preStatement.setString(1, player.getPlayerUUID().toString());
					if (player.getPartyName().isEmpty()) {
						preStatement.setString(2, "");
						preStatement.setInt(3, 0);
					} else {
						preStatement.setString(2, player.getPartyName());
						preStatement.setInt(3, player.getRank());
					}
					preStatement.setString(4, player.getName());
					preStatement.setInt(5, (int) player.getNameTimestamp());
					preStatement.setBoolean(6, player.isSpy());
					preStatement.setBoolean(7, player.isMuted());
					
					preStatement.executeUpdate();
				}
			} else {
				// Remove the data
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_DELETE))) {
					preStatement.setString(1, player.getPlayerUUID().toString());
					
					preStatement.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		PartyPlayerImpl ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_GET))) {
					preStatement.setString(1, uuid.toString());
					
					try (ResultSet rs = preStatement.executeQuery()) {
						if (rs.next())
							ret = getPlayerFromResultSet(rs);
					}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	
	@Override
	public List<PartyPlayerImpl> getPartyPlayersByName(String name) {
		List<PartyPlayerImpl> ret = new ArrayList<>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_GETBYNAME))) {
					preStatement.setString(1, name);
					
					try (ResultSet rs = preStatement.executeQuery()) {
						while (rs.next()) {
							ret.add(getPlayerFromResultSet(rs));
						}
					}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	
	
	/*
	 *	Parties
	 */
	@Override
	public void updateParty(PartyImpl party) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updateParty(party, connection);
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	private void updateParty(PartyImpl party, Connection connection) {
		String query = Constants.QUERY_PARTY_INSERT_MYSQL;
		if (databaseType.isSQLite())
			query = Constants.QUERY_PARTY_INSERT_SQLITE;
		
		try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(query))) {
			preStatement.setString(1, party.getName());
			preStatement.setString(2, party.isFixed() ? Constants.FIXED_VALUE_TEXT : party.getLeader().toString());
			preStatement.setString(3, party.getDescription());
			preStatement.setString(4, party.getMotd());
			preStatement.setString(5, party.getColor() != null ? party.getColor().getName() : "");
			preStatement.setInt(6, party.getKills());
			preStatement.setString(7, party.getPassword());
			preStatement.setString(8, party.getHome() != null ? party.getHome().toString() : "");
			preStatement.setBoolean(9, party.getProtection());
			preStatement.setDouble(10, party.getExperience());
			preStatement.executeUpdate();
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	
	@Override
	public PartyImpl getParty(String party) {
		PartyImpl ret = null;
		String query = Constants.QUERY_PARTY_GET_MYSQL;
		if (databaseType.isSQLite())
			query = Constants.QUERY_PARTY_GET_SQLITE;
		
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(query))) {
					preStatement.setString(1, party);
					
					try (ResultSet rs = preStatement.executeQuery()) {
						if (rs.next())
							ret = getPartyFromResultSet(connection, rs);
					}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	
	@Override
	public void renameParty(String prev, String next) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				try {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_RENAME_PARTIES))) {
						preStatement.setString(1, next);
						preStatement.setString(2, prev);
						
						preStatement.executeUpdate();
					}
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_RENAME_PLAYERS))) {
						preStatement.setString(1, next);
						preStatement.setString(2, prev);
						
						preStatement.executeUpdate();
						connection.commit();
					}
				} catch (Exception ex) {
					connection.rollback();
					LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				try {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_REMOVE_PARTIES))) {
						preStatement.setString(1, party.getName());
						
						preStatement.executeUpdate();
					}
				} catch (Exception ex) {
					connection.rollback();
					LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	
	@Override
	public boolean existParty(String party) {
		boolean ret = false;
		String query = Constants.QUERY_PARTY_GET_MYSQL;
		if (databaseType.isSQLite())
			query = Constants.QUERY_PARTY_GET_SQLITE;
		
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(query))) {
					preStatement.setString(1, party);
					
					try (ResultSet rs = preStatement.executeQuery()) {
						if (rs.next())
							ret = true;
					}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	
	@Override
	public List<PartyImpl> getAllFixed() {
		List<PartyImpl> list = new ArrayList<>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery(SQLTable.formatQuery(Constants.QUERY_PARTY_GETALLFIXED))
					) {
						while (rs.next()) {
							list.add(getPartyFromResultSet(connection, rs));
						}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return list;
	}
	
	@Override
	public List<PartyImpl> getAllParties() {
		List<PartyImpl> list = new ArrayList<>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery(SQLTable.formatQuery(Constants.QUERY_PARTY_GETALL))
					) {
						while (rs.next()) {
							list.add(getPartyFromResultSet(connection, rs));
						}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return list;
	}
	
	@Override
	public List<PartyPlayerImpl> getAllPlayers() {
		List<PartyPlayerImpl> list = new ArrayList<>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery(SQLTable.formatQuery(Constants.QUERY_PLAYER_GETALL))
					) {
						while (rs.next()) {
							list.add(getPlayerFromResultSet(rs));
						}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
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
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_LOG_INSERT))) {
					preStatement.setTimestamp(1, new Timestamp(line.getDate().getTime()));
					preStatement.setInt(2, Integer.valueOf(line.getLevel()));
					preStatement.setString(3, line.getPosition());
					preStatement.setString(4, line.getMessage());
					
					preStatement.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	/*
	 * Private MySQL queries
	 */
	private PartyPlayerImpl getPlayerFromResultSet(ResultSet rs) {
		PartyPlayerImpl ret = null;
		try {
			ret = plugin.getPlayerManager().initializePlayer(UUID.fromString(rs.getString("uuid")));
			ret.setName(rs.getString("name"));
			ret.setNameTimestamp(rs.getInt("timestamp"));
			ret.setPartyName(rs.getString("party"));
			ret.setRank(rs.getInt("rank"));
			ret.setSpy(rs.getBoolean("spy"));
			ret.setMuted(rs.getBoolean("mute"));
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	private PartyImpl getPartyFromResultSet(Connection connection, ResultSet rs) {
		PartyImpl ret = null;
		try {
			ret = plugin.getPartyManager().initializeParty(rs.getString("name"));
			ret.setDescription(rs.getString("description"));
			ret.setMotd(rs.getString("motd"));
			ret.setColor(plugin.getColorManager().searchColorByName(rs.getString("color")));
			ret.setKills(rs.getInt("kills"));
			ret.setPassword(rs.getString("password"));
			ret.setHome(HomeLocationImpl.deserialize(rs.getString("home")));
			ret.setProtection(rs.getBoolean("protection"));
			ret.setExperience(rs.getDouble("experience"));
			String leader = rs.getString("leader");
			if (leader != null) {
				if (leader.equalsIgnoreCase(Constants.FIXED_VALUE_TEXT)) {
					ret.setLeader(UUID.fromString(Constants.FIXED_VALUE_UUID));
					ret.setFixed(true);
				} else
					ret.setLeader(UUID.fromString(leader));
			}
			ret.setMembers(getMembersParty(connection, rs.getString("name")));
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	private List<UUID> getMembersParty(Connection connection, String party) {
		List<UUID> list = new ArrayList<>();
		
		try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_GETBYPARTY))) {
			preStatement.setString(1, party);
			
			try (ResultSet rs = preStatement.executeQuery()) {
				while (rs.next()) {
					try {
						list.add(UUID.fromString(rs.getString("uuid")));
					} catch (IllegalArgumentException ignored) {}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return list;
	}
}
