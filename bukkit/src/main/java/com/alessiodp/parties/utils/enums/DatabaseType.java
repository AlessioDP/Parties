package com.alessiodp.parties.utils.enums;

public enum DatabaseType {
	NONE, FILE, SQL;
	
	public static DatabaseType getEnum(String str) {
		DatabaseType ret = FILE;
		switch (str.toLowerCase()) {
		case "none":
			ret = NONE;
			break;
		case "sql":
			ret = SQL;
		}
		return ret;
	}
	
	public boolean isNone() {
		return this == NONE;
	}
	public boolean isFile() {
		return this == FILE;
	}
	public boolean isSQL() {
		return this == SQL;
	}
}
