package com.alessiodp.parties.common.storage.sql;


import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.libraries.ILibrary;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.storage.StorageType;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseSQL;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLiteDao implements IDatabaseSQL {
	private PartiesPlugin plugin;
	
	private Connection connection;
	private boolean failed;
	
	
	public SQLiteDao(PartiesPlugin instance) {
		plugin = instance;
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
						+ plugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_SQL_SQLITE_DBNAME));
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
		} catch (Exception ignored) {}
	}
	
	@Override
	public boolean isFailed() {
		return failed;
	}
	
	/*
	 * Tables
	 */
	@Override
	public void handleSchema(HashMap<SQLTable, String> schema) {
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_SQL_SCHEMA_INIT.replace("{class}", getClass().getSimpleName()), true);
		try {
			byte[] data = ByteStreams.toByteArray(plugin.getResource("schemas/" + Constants.DATABASE_SQLITE_SCHEMA));
			String dataString = new String(data, StandardCharsets.UTF_8);
			
			Matcher m = Pattern
					.compile(Constants.DATABASE_SCHEMA_DIVIDER, Pattern.CASE_INSENSITIVE)
					.matcher(dataString);
			while (m.find()) {
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_SQL_SCHEMA_FOUND.replace("{schema}", m.group(1)), true);
				SQLTable table = SQLTable.getExactEnum(m.group(1));
				if (table != null) {
					schema.put(table, m.group(2));
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
