package com.alessiodp.parties.storage.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.libraries.ILibrary;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.storage.StorageType;
import com.alessiodp.parties.storage.interfaces.IDatabaseSQL;

public class SQLiteDao implements IDatabaseSQL {
	private Parties plugin;
	
	private Connection connection;
	private boolean failed;
	
	
	public SQLiteDao(Parties instance) {
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
	public void handleSchema(HashMap<SQLTable, String> schema) {
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
				schema.put(table, m.group(2));
			}
		}
	}
}
