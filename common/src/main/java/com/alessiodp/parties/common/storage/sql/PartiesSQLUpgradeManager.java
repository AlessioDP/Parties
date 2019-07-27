package com.alessiodp.parties.common.storage.sql;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.sql.ISQLTable;
import com.alessiodp.core.common.storage.sql.mysql.SQLUpgradeManager;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import lombok.NonNull;
import org.checkerframework.checker.units.qual.C;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PartiesSQLUpgradeManager extends SQLUpgradeManager {
	
	public PartiesSQLUpgradeManager(@NonNull ADPPlugin plugin, @NonNull SQLDispatcher dispatcher, @NonNull StorageType storageType) {
		super(plugin, dispatcher, storageType, ConfigMain.STORAGE_SETTINGS_SQLGENERAL_UPGRADE_SAVEOLD, ConfigMain.STORAGE_SETTINGS_SQLGENERAL_UPGRADE_OLDSUFFIX);
	}
	
	@Override
	protected void upgradeTable(Connection connection, ResultSet rs, ISQLTable table, int currentVersion) throws SQLException {
		switch ((SQLTable) table) {
			case PARTIES:
				// Parties
				upgradeTableParties(connection, rs, currentVersion);
				break;
			case PLAYERS:
				// Players
				upgradeTablePlayers(connection, rs, currentVersion);
				break;
			default:
				// Nothing to upgrade
		}
	}
	
	@Override
	protected boolean isVersionTable(ISQLTable table) {
		return table.equals(SQLTable.VERSIONS);
	}
	
	@Override
	protected String formatQuery(String query) {
		return SQLTable.formatGenericQuery(query);
	}
	
	private void upgradeTableParties(Connection connection, ResultSet rs, int currentVersion) throws SQLException {
		if (storageType.equals(StorageType.MYSQL)) {
			// MySQL
			switch (currentVersion) {
				case 0:
				case 1:
					// Upgrading from 1.7/2.0
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("descr"));
							preStatement.setString(4, rs.getString("motd"));
							preStatement.setString(5, "");
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, false);
							preStatement.setDouble(10, 0);
							preStatement.executeUpdate();
						}
					}
					break;
				case 2:
					// Upgrading from 2.1
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("descr")); // Renamed descr to description
							preStatement.setString(4, rs.getString("motd"));
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, false);
							preStatement.setDouble(10, 0);
							preStatement.setBoolean(11, true);
							preStatement.executeUpdate();
						}
					}
					break;
				case 3:
					// Upgrading from 2.3.2
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							// Removed prefix & suffix
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, false); // Set new pvp
							preStatement.setDouble(10, 0); // Set new experience
							preStatement.setBoolean(11, true);
							preStatement.executeUpdate();
						}
					}
					break;
				case 4:
					// Upgrading from 2.3.4
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, rs.getBoolean("pvp")); // Changed pvp into protection
							preStatement.setDouble(10, rs.getDouble("experience"));
							preStatement.setBoolean(11, true);
							preStatement.executeUpdate();
						}
					}
					break;
				case 5:
					// Upgrading from 2.4
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, rs.getBoolean("protection"));
							preStatement.setDouble(10, rs.getDouble("experience"));
							preStatement.setBoolean(11, true); // Set new follow
							preStatement.executeUpdate();
						}
					}
					break;
				case 6:
				case 7:
					// Same table
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, rs.getBoolean("protection"));
							preStatement.setDouble(10, rs.getDouble("experience"));
							preStatement.setBoolean(11, rs.getBoolean("follow"));
							preStatement.executeUpdate();
						}
					}
					break;
			}
		}
		if (storageType.equals(StorageType.SQLITE)) {
			// SQLite
			switch (currentVersion) {
				case 1:
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_SQLITE))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							// Removed prefix & suffix
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, false); // Set new pvp
							preStatement.setDouble(10, 0); // Set new experience
							preStatement.setBoolean(11, true);
							preStatement.executeUpdate();
						}
					}
					break;
				case 2:
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_SQLITE))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							// Removed prefix & suffix
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, rs.getBoolean("pvp")); // Changed pvp into protection
							preStatement.setDouble(10, rs.getDouble("experience"));
							preStatement.setBoolean(11, true);
							preStatement.executeUpdate();
						}
					}
					break;
				case 3:
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_SQLITE))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							// Removed prefix & suffix
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, rs.getBoolean("protection"));
							preStatement.setDouble(10, rs.getDouble("experience"));
							preStatement.setBoolean(11, true); // Set new follow
							preStatement.executeUpdate();
						}
					}
					break;
				case 4:
				case 5:
					// Same table
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PARTY_INSERT_SQLITE))) {
							preStatement.setString(1, rs.getString("name"));
							preStatement.setString(2, rs.getString("leader"));
							preStatement.setString(3, rs.getString("description"));
							preStatement.setString(4, rs.getString("motd"));
							preStatement.setString(5, rs.getString("color"));
							preStatement.setInt(6, rs.getInt("kills"));
							preStatement.setString(7, rs.getString("password"));
							preStatement.setString(8, rs.getString("home"));
							preStatement.setBoolean(9, rs.getBoolean("protection"));
							preStatement.setDouble(10, rs.getDouble("experience"));
							preStatement.setBoolean(11, rs.getBoolean("follow"));
							preStatement.executeUpdate();
						}
					}
					break;
			}
		}
	}
	
	private void upgradeTablePlayers(Connection connection, ResultSet rs, int currentVersion) throws SQLException {
		if (storageType.equals(StorageType.MYSQL)) {
			// MySQL
			switch (currentVersion) {
				case 0:
					// Upgrading from 1.7
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("nickname"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.setBoolean(4, false);
							preStatement.setBoolean(5, false);
							preStatement.executeUpdate();
						}
					}
					break;
				case 1:
				case 2:
					// Upgrading from 2.0/2.1
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("uuid"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.setBoolean(4, false);
							preStatement.setBoolean(5, false);
							preStatement.executeUpdate();
						}
					}
					break;
				case 3:
				case 4:
					// Upgrading from 2.3.4
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("uuid"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.setBoolean(4, rs.getBoolean("spy"));
							preStatement.setBoolean(5, rs.getBoolean("notify")); // Changed notify into mute
							preStatement.executeUpdate();
						}
					}
					break;
				case 5:
				case 6:
					// Upgrading from 2.5
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("uuid"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							// Removed name and timestamp
							preStatement.setBoolean(4, rs.getBoolean("spy"));
							preStatement.setBoolean(5, rs.getBoolean("mute"));
							preStatement.executeUpdate();
						}
					}
				case 7:
					// Same table
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_MYSQL))) {
							preStatement.setString(1, rs.getString("uuid"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.setBoolean(4, rs.getBoolean("spy"));
							preStatement.setBoolean(5, rs.getBoolean("mute"));
							preStatement.executeUpdate();
						}
					}
					break;
			}
		}
		if (storageType.equals(StorageType.SQLITE)) {
			// SQLite
			switch (currentVersion ) {
				case 1:
				case 2:
					// Upgrading from 2.3.4
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_SQLITE))) {
							preStatement.setString(1, rs.getString("uuid"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.setBoolean(4, rs.getBoolean("spy"));
							preStatement.setBoolean(5, rs.getBoolean("notify")); // Changed notify into mute
							preStatement.executeUpdate();
						}
					}
					break;
				case 3:
				case 4:
					// Upgrading from 2.5
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_SQLITE))) {
							preStatement.setString(1, rs.getString("uuid"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.setBoolean(4, rs.getBoolean("spy"));
							preStatement.setBoolean(5, rs.getBoolean("mute"));
							preStatement.executeUpdate();
						}
					}
					break;
				case 5:
					// Same table
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(PartiesConstants.QUERY_PLAYER_INSERT_SQLITE))) {
							preStatement.setString(1, rs.getString("uuid"));
							preStatement.setString(2, rs.getString("party"));
							preStatement.setInt(3, rs.getInt("rank"));
							preStatement.setBoolean(4, rs.getBoolean("spy"));
							preStatement.setBoolean(5, rs.getBoolean("mute"));
							preStatement.executeUpdate();
						}
					}
					break;
			}
		}
	}
}
