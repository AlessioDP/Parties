package com.alessiodp.parties.storage.dispatchers;

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

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLine;
import com.alessiodp.parties.storage.DatabaseData;
import com.alessiodp.parties.storage.StorageType;
import com.alessiodp.parties.storage.interfaces.IDatabaseDispatcher;
import com.alessiodp.parties.storage.interfaces.IDatabaseSQL;
import com.alessiodp.parties.storage.sql.SQLTable;
import com.alessiodp.parties.storage.sql.SQLUpgradeManager;
import com.alessiodp.parties.storage.sql.MySQLDao;
import com.alessiodp.parties.storage.sql.SQLiteDao;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class SQLDispatcher implements IDatabaseDispatcher {
	private Parties plugin;
	
	private StorageType databaseType;
	private IDatabaseSQL database;
	
	private HashMap<SQLTable, String> schemaTables;
	
	public SQLDispatcher(Parties instance) {
		plugin = instance;
		schemaTables = new HashMap<SQLTable, String>();
	}
	
	@Override
	public void init() {}
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
		Map<UUID, PartyPlayer> players = new HashMap<UUID, PartyPlayer>();
		for (PartyPlayer pp : getAllPlayers())
			players.put(pp.getPlayerUUID(), pp);
		
		// Parties
		Map<String, Party> parties = new HashMap<String, Party>();
		for (Party p : getAllParties())
			parties.put(p.getName(), p);
		
		return new DatabaseData(players, parties);
	}
	@Override
	public boolean saveEntireData(DatabaseData data) {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);
			
			// Players
			for (Entry<UUID, PartyPlayer> entry : data.getPlayers().entrySet())
				updatePlayer(entry.getValue(), connection);
			
			// Parties
			for (Entry<String, Party> entry : data.getParties().entrySet()) {
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
			//Renaming tables
			List<String> tables = new ArrayList<String>();
			DatabaseMetaData metadata = connection.getMetaData();
			try (ResultSet rs = metadata.getTables(null, null, "%", null)) {
				while (rs.next()) {
					tables.add(rs.getString(3));
				}
			}
			
			// Players
			renameTable(connection, tables, SQLTable.PLAYERS.getTableName());
			createTable(connection, SQLTable.PLAYERS);
			
			// Parties
			renameTable(connection, tables, SQLTable.PARTIES.getTableName());
			createTable(connection, SQLTable.PARTIES);
			
			connection.commit();
			ret = true;
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	private void renameTable(Connection connection, List<String> existingTables, String table) throws SQLException {
		String newTable = table + ConfigMain.STORAGE_MIGRATE_SUFFIX;
		int count = 1;
		while (existingTables.contains(newTable)) {
			newTable = table + ConfigMain.STORAGE_MIGRATE_SUFFIX + Integer.toString(count);
			count++;
		}
		
		try (Statement statement = connection.createStatement()) {
			String query = Constants.QUERY_GENERIC_MYSQL_RENAME;
			if (databaseType == StorageType.SQLITE)
				query = Constants.QUERY_GENERIC_SQLITE_RENAME;
			
			statement.executeUpdate(query
					.replace("{table}", table)
					.replace("{newtable}", newTable));
		}
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
			try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(versionQuery))) {
				preStatement.setString(1, table.getTableName());
				preStatement.setInt(2, versionValue);
				preStatement.executeUpdate();
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
	public void updatePlayer(PartyPlayer player) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updatePlayer(player, connection);
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	private void updatePlayer(PartyPlayer player, Connection connection) {
		try {
			boolean existData = false;
			if (!player.getPartyName().isEmpty() || player.isSpy() || player.isPreventNotify())
				existData = true;
			
			if (existData) {
				// Save the data
				String query = Constants.QUERY_PLAYER_INSERT_MYSQL;
				if (databaseType == StorageType.SQLITE)
					query = Constants.QUERY_PLAYER_INSERT_SQLITE;
				
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(query))) {
					preStatement.setString(1, player.getPlayerUUID().toString());
					preStatement.setString(2, !player.getPartyName().isEmpty() ? player.getPartyName() : "");
					preStatement.setInt(3, !player.getPartyName().isEmpty() ? player.getRank() : 0);
					preStatement.setString(4, !player.getPartyName().isEmpty() ? player.getName() : "");
					preStatement.setInt(5, !player.getPartyName().isEmpty() ? ((int) player.getNameTimestamp()) : null);
					preStatement.setBoolean(6, player.isSpy());
					preStatement.setBoolean(7, player.isPreventNotify());
					
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
	public PartyPlayer getPlayer(UUID uuid) {
		PartyPlayer ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_GET))) {
					preStatement.setString(1, uuid.toString());
					
					try (ResultSet rs = preStatement.executeQuery()) {
						if (rs.next())
							ret = getPlayerFromResultSet(connection, rs);
					}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	
	@Override
	public List<PartyPlayer> getPartyPlayersByName(String name) {
		List<PartyPlayer> ret = new ArrayList<PartyPlayer>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_GETBYNAME))) {
					preStatement.setString(1, name);
					
					try (ResultSet rs = preStatement.executeQuery()) {
						while (rs.next()) {
							ret.add(getPlayerFromResultSet(connection, rs));
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
	public void updateParty(Party party) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updateParty(party, connection);
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	private void updateParty(Party party, Connection connection) {
		String query = Constants.QUERY_PARTY_INSERT_MYSQL;
		if (databaseType.isSQLite())
			query = Constants.QUERY_PARTY_INSERT_SQLITE;
		
		try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(query))) {
			preStatement.setString(1, party.getName());
			preStatement.setString(2, party.isFixed() ? Constants.FIXED_VALUE_TEXT : party.getLeader().toString());
			preStatement.setString(3, party.getDescription());
			preStatement.setString(4, party.getMotd());
			preStatement.setString(5, party.getPrefix());
			preStatement.setString(6, party.getSuffix());
			preStatement.setString(7, party.getColor() != null ? party.getColor().getName() : "");
			preStatement.setInt(8, party.getKills());
			preStatement.setString(9, party.getPassword());
			preStatement.setString(10, PartiesUtils.formatHome(party.getHome()));
			preStatement.executeUpdate();
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	@Override
	public Party getParty(String party) {
		Party ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_GET))) {
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
	public void removeParty(Party party) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				try {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_REMOVE_PARTIES))) {
						preStatement.setString(1, party.getName());
						
						preStatement.executeUpdate();
					}
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_REMOVE_PLAYERS))) {
						preStatement.setString(1, party.getName());
						
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
	public boolean existParty(String party) {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_GET))) {
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
	public List<Party> getAllFixed() {
		List<Party> list = new ArrayList<Party>();
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
	public List<Party> getAllParties() {
		List<Party> list = new ArrayList<Party>();
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
	public List<PartyPlayer> getAllPlayers() {
		List<PartyPlayer> list = new ArrayList<PartyPlayer>();
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (
						Statement statement = connection.createStatement();
						ResultSet rs = statement.executeQuery(SQLTable.formatQuery(Constants.QUERY_PLAYER_GETALL))
					) {
						while (rs.next()) {
							list.add(getPlayerFromResultSet(connection, rs));
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
					preStatement.setTimestamp(1, new Timestamp(line.getFullDate().getTime()));
					preStatement.setInt(2, Integer.valueOf(line.getLevel()));
					preStatement.setString(3, line.getPosition());
					preStatement.setString(3, line.getMessage());
					
					preStatement.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	
	
	/*
	 * Private MySQL queries
	 */
	private PartyPlayer getPlayerFromResultSet(Connection connection, ResultSet rs) {
		PartyPlayer ret = null;
		try {
			ret = new PartyPlayer(UUID.fromString(rs.getString("uuid")), ConfigParties.RANK_SET_DEFAULT);
			ret.setName(rs.getString("name"));
			ret.setNameTimestamp(rs.getInt("timestamp"));
			ret.setPartyName(rs.getString("party"));
			ret.setRank(rs.getInt("rank"));
			ret.setSpy(rs.getBoolean("spy"));
			ret.setPreventNotify(rs.getBoolean("notify"));
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return ret;
	}
	private Party getPartyFromResultSet(Connection connection, ResultSet rs) {
		Party ret = null;
		try {
			ret = new Party(rs.getString("name"));
			ret.setDescription(rs.getString("description"));
			ret.setMotd(rs.getString("motd"));
			ret.setPrefix(rs.getString("prefix"));
			ret.setSuffix(rs.getString("suffix"));
			ret.setColor(plugin.getColorManager().searchColorByName(rs.getString("color")));
			ret.setKills(rs.getInt("kills"));
			ret.setPassword(rs.getString("password"));
			ret.setHome(PartiesUtils.formatHome(rs.getString("home")));
			
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
		List<UUID> list = new ArrayList<UUID>();
		
		try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_GETBYPARTY))) {
			preStatement.setString(1, party);
			
			try (ResultSet rs = preStatement.executeQuery()) {
				while (rs.next()) {
					try {
						list.add(UUID.fromString(rs.getString("uuid")));
					} catch (IllegalArgumentException ex) {}
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		return list;
	}
}
