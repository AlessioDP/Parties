package com.alessiodp.parties.common.storage.sql;

import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.storage.StorageType;
import com.alessiodp.parties.common.storage.dispatchers.SQLDispatcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class SQLUpgradeManager {
	
	public static void checkUpgrades(SQLDispatcher dispatcher, Connection connection, SQLTable table, StorageType databaseType) {
		String checkQueryMigrate = Constants.QUERY_CHECKVERSION_SET_MYSQL;
		if (databaseType.isSQLite())
			checkQueryMigrate = Constants.QUERY_CHECKVERSION_SET_SQLITE;
		
		int version = -1;
		// New check system
		try (PreparedStatement statement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_CHECKVERSION))) {
			statement.setString(1, table.getTableName());
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					version = rs.getInt("version");
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
		
		// Old system, only MySQL have that
		if (databaseType.isMySQL() && version == -1) {
			try (PreparedStatement statement = connection.prepareStatement(Constants.QUERY_CHECKVERSION_OLD)) {
				statement.setString(1, connection.getCatalog());
				statement.setString(2, table.getTableName());
				try (ResultSet rs = statement.executeQuery()) {
					if (rs.next()) {
						String cmnt = rs.getString("table_comment");
						if (!cmnt.isEmpty()) {
							version = Integer.valueOf(cmnt.split(":")[1]);
							
							if (version >= 3) {
								// Only needed from new databases
								// Send it to the version table
								try (PreparedStatement subStatement = connection.prepareStatement(SQLTable.formatQuery(checkQueryMigrate))) {
									subStatement.setString(1, table.getTableName());
									subStatement.setInt(2, version);
									subStatement.executeUpdate();
								}
							}
						}
					}
				}
			} catch (SQLException ex) {
				LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
			}
		}
		
		int currentVersion = Constants.VERSION_DATABASE_MYSQL;
		if (databaseType.isSQLite())
			currentVersion = Constants.VERSION_DATABASE_SQLITE;
		
		if (version < currentVersion) {
			// Upgrade from old upgrade system
			upgradeTable(dispatcher, databaseType, version, connection, table);
		}
	}
	
	private static void upgradeTable(SQLDispatcher dispatcher, StorageType databaseType, int version, Connection connection, SQLTable table) {
		try (Statement statement = connection.createStatement()) {
			connection.setAutoCommit(false);
			String renameQuery = Constants.QUERY_GENERIC_MYSQL_RENAME;
			if (databaseType.isSQLite())
				renameQuery = Constants.QUERY_GENERIC_SQLITE_RENAME;
			
			statement.executeUpdate(renameQuery
					.replace("{table}", table.getTableName())
					.replace("{newtable}", table.getTableName() + "_temp"));
			dispatcher.createTable(connection, table);
			
			try (ResultSet rs = statement.executeQuery(Constants.QUERY_GENERIC_SELECTALL
					.replace("{table}", table.getTableName() + "_temp"))) {
				switch (table) {
				case PARTIES:
					// Parties
					upgradeTableParties(rs, version, connection, databaseType);
					break;
				case PLAYERS:
					// Players
					upgradeTablePlayers(rs, version, connection, databaseType);
					break;
				case LOG:
					// Spies
					upgradeTableLog(rs, version, connection, databaseType);
					break;
				case VERSIONS:
					// Versions
					upgradeTableVersions(rs, version, connection, databaseType);
					break;
				}
			}
			
			statement.executeUpdate(Constants.QUERY_GENERIC_DROP
					.replace("{table}", table.getTableName() + "_temp"));
			connection.commit();
		} catch (SQLException ex) {
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException ignored) {}
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR_TABLE
					.replace("{table}", table + "[version " + version + "]"), ex));
			ex.printStackTrace();
		}
	}
	
	private static void upgradeTableParties(ResultSet rs, int version, Connection connection, StorageType databaseType) throws SQLException {
		if (databaseType.isMySQL()) {
			// MySQL
			switch (version) {
			case 0:
			case 1:
				// Upgrading from 1.7/2.0
				while (rs.next()) {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_INSERT_MYSQL))) {
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
			case 2:
				// Upgrading from 2.1
				while (rs.next()) {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PARTY_INSERT_MYSQL))) {
						preStatement.setString(1, rs.getString("name"));
						preStatement.setString(2, rs.getString("leader"));
						preStatement.setString(3, rs.getString("descr"));
						preStatement.setString(4, rs.getString("motd"));
						preStatement.setString(5, rs.getString("prefix"));
						preStatement.setString(6, rs.getString("suffix"));
						preStatement.setString(7, rs.getString("color"));
						preStatement.setInt(8, rs.getInt("kills"));
						preStatement.setString(9, rs.getString("password"));
						preStatement.setString(10, rs.getString("home"));
						preStatement.executeUpdate();
					}
				}
				break;
			}
		}
	}
	
	private static void upgradeTablePlayers(ResultSet rs, int version, Connection connection, StorageType databaseType) throws SQLException {
		if (databaseType.isMySQL()) {
			// MySQL
			switch (version) {
			case 0:
				// Upgrading from 1.7
				while (rs.next()) {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_INSERT_MYSQL))) {
						preStatement.setString(1, rs.getString("nickname"));
						preStatement.setString(2, rs.getString("party"));
						preStatement.setInt(3, rs.getInt("rank"));
						preStatement.setString(4, "");
						preStatement.setInt(5, 0);
						preStatement.setBoolean(6, false);
						preStatement.setBoolean(7, false);
						preStatement.executeUpdate();
					}
				}
				break;
			case 1:
			case 2:
				// Upgrading from 2.0/2.1
				while (rs.next()) {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_PLAYER_INSERT_MYSQL))) {
						preStatement.setString(1, rs.getString("uuid"));
						preStatement.setString(2, rs.getString("party"));
						preStatement.setInt(3, rs.getInt("rank"));
						preStatement.setString(4, rs.getString("name"));
						preStatement.setInt(5, rs.getInt("timestamp"));
						preStatement.setBoolean(6, false);
						preStatement.setBoolean(7, false);
						preStatement.executeUpdate();
					}
				}
			}
		}
	}
	
	private static void upgradeTableLog(ResultSet rs, int version, Connection connection, StorageType databaseType) throws SQLException {
		if (databaseType.isMySQL()) {
			// MySQL
			switch (version) {
			case 0:
				// Upgrading from 1.7
				while (rs.next()) {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_LOG_MIGRATE))) {
						preStatement.setInt(1, rs.getInt("line"));
						preStatement.setString(2, rs.getString("date") + " " + rs.getString("time"));
						preStatement.setInt(3, rs.getInt("level"));
						preStatement.setString(4, "");
						preStatement.setString(5, rs.getString("message"));
						preStatement.executeUpdate();
					}
				}
				break;
			case 1:
			case 2:
				// Upgrading from 2.0/2.1
				// Same table
				while (rs.next()) {
					try (PreparedStatement preStatement = connection.prepareStatement(SQLTable.formatQuery(Constants.QUERY_LOG_MIGRATE))) {
						preStatement.setInt(1, rs.getInt("line"));
						preStatement.setString(2, rs.getString("date"));
						preStatement.setInt(3, rs.getInt("level"));
						preStatement.setString(4, rs.getString("position"));
						preStatement.setString(5, rs.getString("message"));
						preStatement.executeUpdate();
					}
				}
			}
		}
	}
	
	private static void upgradeTableVersions(ResultSet rs, int version, Connection connection, StorageType databaseType) {
		// Future usage
	}
	
}
