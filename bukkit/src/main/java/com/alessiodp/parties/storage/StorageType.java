package com.alessiodp.parties.storage;

public enum StorageType {
	NONE("None"), YAML("YAML"), MYSQL("MySQL"), SQLITE("SQLite");
	
	private String formattedName;
	StorageType(String fn) {
		formattedName = fn;
	}
	
	public static StorageType getEnum(String str) {
		// Default YAML, TXT = YAML
		StorageType ret = YAML;
		switch (str.toLowerCase()) {
		case "none":
			ret = NONE;
			break;
		case "mysql":
			ret = MYSQL;
			break;
		case "sqlite":
			ret = SQLITE;
			break;
		}
		return ret;
	}
	public static StorageType getExactEnum(String str) {
		StorageType ret = null;
		switch (str.toLowerCase()) {
		case "none":
			ret = NONE;
			break;
		case "yaml":
			ret = YAML;
			break;
		case "mysql":
			ret = MYSQL;
			break;
		case "sqlite":
			ret = SQLITE;
		}
		return ret;
	}
	
	public boolean isNone() {
		return this == NONE;
	}
	public boolean isYAML() {
		return this == YAML;
	}
	public boolean isMySQL() {
		return this == MYSQL;
	}
	public boolean isSQLite() {
		return this == SQLITE;
	}
	
	public String getFormattedName() {
		return formattedName;
	}
}
