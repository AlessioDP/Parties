package com.alessiodp.parties.utils.enums;

public enum LogLevel {
	BASE(0),
	BASIC(1),
	MEDIUM(2),
	DEBUG(3);
	
	private final int level;
	LogLevel(int l) {
		level = l;
	}
	
	public static LogLevel getEnum(int mode) {
		LogLevel ret = BASE;
		switch (mode) {
		case 1:
			ret = BASIC;
			break;
		case 2:
			ret = MEDIUM;
			break;
		case 3:
			ret = DEBUG;
		}
		return ret;
	}
	
	public int getLevel() {
		return level;
	}
}
