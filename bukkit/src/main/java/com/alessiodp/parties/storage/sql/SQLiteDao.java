package com.alessiodp.parties.storage.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.libraries.ILibrary;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.storage.StorageType;
import com.alessiodp.parties.storage.interfaces.IDatabaseSQL;

public class SQLiteDao implements IDatabaseSQL {
	private Parties plugin;
	
	private Connection connection;
	private boolean failed;
	
	private HashMap<SQLTable, String> schemaTables;
	
	
	public SQLiteDao(Parties instance) {
		plugin = instance;
		
		schemaTables = new HashMap<SQLTable, String>();
	}
	
	/*
	 * Connection
	 */
	@Override
	public void initSQL() {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		failed = false;
		if (plugin.getLibraryManager().initLibrary(ILibrary.SQLITE_JDBC)) {
			if (getConnection() == null) {
				failed = true;
			}
		} else
			failed = true;
	}
	
	@Override
	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName("org.sqlite.JDBC");
				
				connection = DriverManager.getConnection("jdbc:sqlite:"
						+ plugin.getDataFolder().getAbsolutePath()
						+ File.separator + ConfigMain.STORAGE_SETTINGS_SQL_SQLITE_DBNAME);
			}
		} catch (Exception ex) {
			LoggerManager.printError(Constants.DEBUG_SQL_CONNECTIONERROR
					.replace("{storage}", StorageType.SQLITE.getFormattedName())
					.replace("{message}", ex.getMessage()));
		}
		return connection;
	}
	
	@Override
	public void stopSQL() {
		try {
			if (connection != null && !connection.isClosed())
				connection.close();
		} catch (Exception ex) {}
	}
	
	@Override
	public boolean isFailed() {
		return failed;
	}
	
	/*
	 * Tables
	 */
	@Override
	public void handleSchema() {
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_SQL_SCHEMA_INIT.replace("{class}", getClass().getSimpleName()), true);
		BufferedReader buff = new BufferedReader(new InputStreamReader(
				plugin.getResource("schemas/" + Constants.DATABASE_SQLITE_SCHEMA),
				StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = buff.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Matcher m = Pattern
				.compile(Constants.DATABASE_SCHEMA_DIVIDER, Pattern.CASE_INSENSITIVE)
				.matcher(sb.toString());
		while (m.find()) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_SQL_SCHEMA_FOUND.replace("{schema}", m.group(1)), true);
			SQLTable table = SQLTable.getExactEnum(m.group(1));
			if (table != null) {
				schemaTables.put(table, m.group(2));
			}
		}
	}
	
	@Override
	public void initTables(Connection connection) {
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			for (SQLTable table : SQLTable.values()) {
				try (ResultSet rs = metadata.getTables(null, null, table.getTableName(), null)) {
					if (rs.next())
						checkUpgrades(connection, table); // Checking for porting
					else
						createTable(connection, table); // Create table
				} catch (SQLException ex) {
					LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
				}
			}
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	
	@Override
	public void checkUpgrades(Connection connection, SQLTable table) {
		try (PreparedStatement statement = connection.prepareStatement(Constants.QUERY_SQLITE_CHECKVERSION)) {
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					int version = rs.getInt(1);
					
					if (version < Constants.VERSION_DATABASE_SQLITE)
						SQLUpgradeManager.upgradeTable(this, StorageType.SQLITE, version, connection, table);
				}
			}
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR, ex));
		}
	}
	
	@Override
	public void createTable(Connection connection, SQLTable table) {
		try (Statement statement = connection.createStatement()) {
			
			statement.execute(SQLTable.formatSchema(schemaTables.get(table), Constants.VERSION_DATABASE_SQLITE));
			
		} catch (SQLException ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_SQL_ERROR_TABLE
					.replace("{table}", table.name()), ex));
		}
	}
}
