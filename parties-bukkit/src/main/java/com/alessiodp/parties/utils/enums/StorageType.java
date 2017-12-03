package com.alessiodp.parties.utils.enums;

public enum StorageType {
	NONE("None"), YAML("YAML"), MYSQL("MySQL");
	
	private String formattedName;
	StorageType(String fn) {
		formattedName = fn;
	}
	
	public static StorageType getEnum(String str) {
		StorageType ret = YAML;
		switch (str.toLowerCase()) {
		case "none":
			ret = NONE;
			break;
		case "mysql":
			ret = MYSQL;
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
	
	public String getFormattedName() {
		return formattedName;
	}
}
