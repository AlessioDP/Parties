package com.alessiodp.parties.storage.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.storage.StorageType;
import com.alessiodp.parties.storage.interfaces.IDatabaseSQL;

public abstract class SQLUpgradeManager {
	
	public static void upgradeTable(IDatabaseSQL database, StorageType databaseType, int version, Connection connection, SQLTable table) {
		try (Statement statement = connection.createStatement()) {
			connection.setAutoCommit(false);
			String renameQuery = Constants.QUERY_GENERIC_MYSQL_RENAME;
			if (databaseType == StorageType.SQLITE)
				renameQuery = Constants.QUERY_SQLITE_CHECKVERSION;
			
			statement.execute(renameQuery
					.replace("{table}", table.getTableName())
					.replace("{newtable}", table.getTableName() + "_temp"));
			database.createTable(connection, table);
			
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
				}
			}
			
			statement.execute(Constants.QUERY_GENERIC_DROP
					.replace("{table}", table.getTableName() + "_temp"));
			connection.commit();
		} catch (SQLException ex) {
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {}
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
}
