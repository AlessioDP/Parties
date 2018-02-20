package com.alessiodp.parties.storage.sql;

import com.alessiodp.parties.configuration.data.ConfigMain;

import lombok.Getter;

public enum SQLTable {
	PLAYERS, PARTIES, LOG;
	
	@Getter
	private String tableName;
	private static String varcharSize;
	private static String charset;
	
	SQLTable() {
	}
	
	public static void setupTables() {
		PARTIES.tableName = ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_PARTIES;
		PLAYERS.tableName = ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_PLAYERS;
		LOG.tableName = ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_TABLES_LOG;
		
		varcharSize = Integer.toString(ConfigMain.STORAGE_SETTINGS_SQL_GENERAL_VARCHARSIZE);
		charset = ConfigMain.STORAGE_SETTINGS_SQL_MYSQL_CHARSET;
	}
	
	public static SQLTable getExactEnum(String str) {
		SQLTable ret = null;
		if (str != null && !str.isEmpty()) {
			switch (str.toLowerCase()) {
			case "parties":
				ret = PARTIES;
				break;
			case "players":
				ret = PLAYERS;
				break;
			case "log":
				ret = LOG;
				break;
			}
		}
		return ret;
	}
	
	public static String formatQuery(String base) {
		return formatSchema(base, -1);
	}
	
	public static String formatSchema(String base, int version) {
		return base
				.replace("{table_parties}", PARTIES.tableName)
				.replace("{table_players}", PLAYERS.tableName)
				.replace("{table_log}", LOG.tableName)
				.replace("{charset}", charset)
				.replace("{version}", Integer.toString(version))
				.replace("{varcharsize}", varcharSize);
	}
}
