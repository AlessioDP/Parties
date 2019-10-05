package com.alessiodp.parties.common.storage.dispatchers;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.sql.ISQLTable;
import com.alessiodp.core.common.storage.sql.mysql.MySQLDao;
import com.alessiodp.core.common.storage.sql.mysql.MySQLHikariConfiguration;
import com.alessiodp.core.common.storage.sql.sqlite.SQLiteDao;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.HomeLocationImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.storage.interfaces.IPartiesDatabaseDispatcher;
import com.alessiodp.parties.common.storage.sql.PartiesSQLUpgradeManager;
import com.alessiodp.parties.common.storage.sql.SQLTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PartiesSQLDispatcher extends SQLDispatcher implements IPartiesDatabaseDispatcher {
	
	public PartiesSQLDispatcher(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void init(StorageType type) {
		upgradeManager = new PartiesSQLUpgradeManager(plugin, this, type);
		
		switch (type) {
			case MYSQL:
				SQLTable.setupTables(
						PartiesConstants.VERSION_DATABASE_MYSQL,
						plugin.getResource("schemas/" + type.name().toLowerCase(Locale.ENGLISH) + ".sql")
				);
				MySQLHikariConfiguration hikari = new MySQLHikariConfiguration(
						plugin.getPluginFallbackName(),
						ConfigMain.STORAGE_SETTINGS_MYSQL_ADDRESS,
						ConfigMain.STORAGE_SETTINGS_MYSQL_PORT,
						ConfigMain.STORAGE_SETTINGS_MYSQL_DATABASE,
						ConfigMain.STORAGE_SETTINGS_MYSQL_USERNAME,
						ConfigMain.STORAGE_SETTINGS_MYSQL_PASSWORD
				);
				hikari.setMaximumPoolSize(ConfigMain.STORAGE_SETTINGS_MYSQL_POOLSIZE);
				hikari.setMaxLifetime(ConfigMain.STORAGE_SETTINGS_MYSQL_CONNLIFETIME);
				hikari.setCharacterEncoding(ConfigMain.STORAGE_SETTINGS_MYSQL_CHARSET);
				hikari.setUseSSL(ConfigMain.STORAGE_SETTINGS_MYSQL_USESSL);
				database = new MySQLDao(plugin, hikari);
				database.initSQL();
				break;
			case SQLITE:
				SQLTable.setupTables(
						PartiesConstants.VERSION_DATABASE_SQLITE,
						plugin.getResource("schemas/" + type.name().toLowerCase(Locale.ENGLISH) + ".sql")
				);
				database = new SQLiteDao(plugin, ConfigMain.STORAGE_SETTINGS_SQLITE_DBFILE);
				database.initSQL();
				break;
			default:
				// Unsupported storage type
		}
		
		if (database != null && !database.isFailed()) {
			databaseType = type;
			
			// Prepare tables list
			LinkedList<ISQLTable> tables = new LinkedList<>();
			tables.add(SQLTable.VERSIONS); // Version must be first
			tables.add(SQLTable.PLAYERS);
			tables.add(SQLTable.PARTIES);
			
			try (Connection connection = getConnection()) {
				initTables(connection, tables);
			} catch (Exception ex) {
				plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
			}
		}
	}
	
	@Override
	public void updatePlayer(PartyPlayerImpl player) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updatePlayer(player, connection);
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	private void updatePlayer(PartyPlayerImpl player, Connection connection) {
		try {
			boolean existData = false;
			if (!player.getPartyName().isEmpty() || player.isSpy() || player.isMuted())
				existData = true;
			
			if (existData) {
				// Save the data
				String query = PartiesConstants.QUERY_PLAYER_INSERT_MYSQL;
				if (databaseType == StorageType.SQLITE)
					query = PartiesConstants.QUERY_PLAYER_INSERT_SQLITE;
				
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(query))) {
					preStatement.setString(1, player.getPlayerUUID().toString());
					if (player.getPartyName().isEmpty()) {
						// Out party
						preStatement.setString(2, "");
						preStatement.setInt(3, 0);
					} else {
						// In party
						preStatement.setString(2, player.getPartyName());
						preStatement.setInt(3, player.getRank());
					}
					preStatement.setBoolean(4, player.isSpy());
					preStatement.setBoolean(5, player.isMuted());
					
					preStatement.executeUpdate();
				}
			} else {
				// Remove the data
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PLAYER_DELETE))) {
					preStatement.setString(1, player.getPlayerUUID().toString());
					
					preStatement.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	@Override
	public PartyPlayerImpl getPlayer(UUID uuid) {
		PartyPlayerImpl ret = null;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PLAYER_GET))) {
					preStatement.setString(1, uuid.toString());
					
					try (ResultSet rs = preStatement.executeQuery()) {
						if (rs.next())
							ret = getPlayerFromResultSet(rs);
					}
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	
	@Override
	public void updateParty(PartyImpl party) {
		try (Connection connection = getConnection()) {
			if (connection != null)
				updateParty(party, connection);
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	private void updateParty(PartyImpl party, Connection connection) {
		String query = PartiesConstants.QUERY_PARTY_INSERT_MYSQL;
		if (databaseType == StorageType.SQLITE)
			query = PartiesConstants.QUERY_PARTY_INSERT_SQLITE;
		
		try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(query))) {
			preStatement.setString(1, party.getName());
			preStatement.setString(2, party.isFixed() ? PartiesConstants.FIXED_VALUE_TEXT : party.getLeader().toString());
			preStatement.setString(3, party.getDescription());
			preStatement.setString(4, party.getMotd());
			preStatement.setString(5, party.getColor() != null ? party.getColor().getName() : "");
			preStatement.setInt(6, party.getKills());
			preStatement.setString(7, party.getPassword());
			preStatement.setString(8, party.getHome() != null ? party.getHome().toString() : "");
			preStatement.setBoolean(9, party.getProtection());
			preStatement.setDouble(10, party.getExperience());
			preStatement.setBoolean(11, party.isFollowEnabled());
			preStatement.executeUpdate();
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	
	@Override
	public PartyImpl getParty(String party) {
		PartyImpl ret = null;
		String query = PartiesConstants.QUERY_PARTY_GET_MYSQL;
		if (databaseType == StorageType.SQLITE)
			query = PartiesConstants.QUERY_PARTY_GET_SQLITE;
		
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(query))) {
					preStatement.setString(1, party);
					
					try (ResultSet rs = preStatement.executeQuery()) {
						if (rs.next())
							ret = getPartyFromResultSet(connection, rs);
					}
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	
	@Override
	public void renameParty(String prev, String next) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PARTY_RENAME_PARTIES))) {
					preStatement.setString(1, next);
					preStatement.setString(2, prev);
					
					preStatement.executeUpdate();
				}
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PARTY_RENAME_PLAYERS))) {
					preStatement.setString(1, next);
					preStatement.setString(2, prev);
					
					preStatement.executeUpdate();
					connection.commit();
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	
	@Override
	public void removeParty(PartyImpl party) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				try {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PARTY_REMOVE_PARTIES))) {
						preStatement.setString(1, party.getName());
						
						preStatement.executeUpdate();
						connection.commit();
					}
				} catch (Exception ex) {
					connection.rollback();
					plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
	}
	
	@Override
	public boolean existParty(String party) {
		boolean ret = false;
		String query = PartiesConstants.QUERY_PARTY_GET_MYSQL;
		if (databaseType == StorageType.SQLITE)
			query = PartiesConstants.QUERY_PARTY_GET_SQLITE;
		
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(query))) {
					preStatement.setString(1, party);
					
					try (ResultSet rs = preStatement.executeQuery()) {
						if (rs.next())
							ret = true;
					}
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
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
						ResultSet rs = statement.executeQuery(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PARTY_GETALLFIXED))
					) {
						while (rs.next()) {
							list.add(getPartyFromResultSet(connection, rs));
						}
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
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
						ResultSet rs = statement.executeQuery(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PARTY_GETALL))
					) {
						while (rs.next()) {
							list.add(getPartyFromResultSet(connection, rs));
						}
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
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
						ResultSet rs = statement.executeQuery(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PLAYER_GETALL))
					) {
						while (rs.next()) {
							list.add(getPlayerFromResultSet(rs));
						}
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return list;
	}
	
	private PartyPlayerImpl getPlayerFromResultSet(ResultSet rs) {
		PartyPlayerImpl ret = null;
		try {
			ret = ((PartiesPlugin) plugin).getPlayerManager().initializePlayer(UUID.fromString(rs.getString("uuid")));
			ret.fromDatabase(
					rs.getString("party"),
					rs.getInt("rank"),
					rs.getBoolean("spy"),
					rs.getBoolean("mute")
			);
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	private PartyImpl getPartyFromResultSet(Connection connection, ResultSet rs) {
		PartyImpl ret = null;
		try {
			ret = ((PartiesPlugin) plugin).getPartyManager().initializeParty(rs.getString("name"));
			
			// Leader check
			UUID leader = null;
			boolean fixed = false;
			String leaderStr = rs.getString("leader");
			if (leaderStr != null) {
				if (leaderStr.equalsIgnoreCase(PartiesConstants.FIXED_VALUE_TEXT)) {
					leader = UUID.fromString(PartiesConstants.FIXED_VALUE_UUID);
					fixed = true;
				} else
					leader = UUID.fromString(leaderStr);
			}
			
			// Members check
			List<UUID> members = getMembersParty(connection, rs.getString("name"));
			
			ret.fromDatabase(
					leader,
					members,
					fixed,
					rs.getString("description"),
					rs.getString("motd"),
					((PartiesPlugin) plugin).getColorManager().searchColorByName(rs.getString("color")),
					rs.getInt("kills"),
					rs.getString("password"),
					HomeLocationImpl.deserialize(rs.getString("home")),
					rs.getBoolean("protection"),
					rs.getDouble("experience"),
					rs.getBoolean("follow")
			);
		} catch (Exception ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return ret;
	}
	private List<UUID> getMembersParty(Connection connection, String party) {
		List<UUID> list = new ArrayList<>();
		
		try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatGenericQuery(PartiesConstants.QUERY_PLAYER_GETBYPARTY))) {
			preStatement.setString(1, party);
			
			try (ResultSet rs = preStatement.executeQuery()) {
				while (rs.next()) {
					try {
						list.add(UUID.fromString(rs.getString("uuid")));
					} catch (IllegalArgumentException ignored) {}
				}
			}
		} catch (SQLException ex) {
			plugin.getLoggerManager().printErrorStacktrace(Constants.DEBUG_SQL_ERROR, ex);
		}
		return list;
	}
}
