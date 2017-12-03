package com.alessiodp.parties.handlers;

import java.util.logging.Level;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.utils.LogLine;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.StorageType;
import com.alessiodp.parties.utils.enums.LogLevel;

public class LogHandler {
	private static Parties plugin;
	
	private static LogLevel logLevel;
	
	
	public LogHandler(Parties instance) {
		plugin = instance;
		logLevel = LogLevel.getEnum(Variables.storage_log_level);
	}
	public static void printError(String message) {
		plugin.log(Level.WARNING, ConsoleColors.RED.getCode() + message);
		log(LogLevel.BASE, message, false, null);
	}
	public static void log(LogLevel level, String message, boolean printConsole) {
		log(level, message, printConsole, null);
	}
	public static void log(LogLevel level, String message, boolean printConsole, ConsoleColors color) {
		if (printConsole) {
			if (level.equals(LogLevel.BASE)
					|| (plugin.getLogType() != StorageType.NONE
							&& Variables.storage_log_printconsole
							&& level.getLevel() <= logLevel.getLevel())) {
				// Preparing message to print
				String print = message;
				if (color != null)
					print = color.getCode() + print;
				if (level.equals(LogLevel.DEBUG))
					print = "[" + level.getLevel() + "] " + print;
				
				// Print it
				plugin.log(print);
			}
		}
		
		if (!plugin.getLogType().equals(StorageType.NONE) && level.getLevel() <= logLevel.getLevel()) {
			LogLine ll = new LogLine(Variables.storage_log_format, level, getCallTrace(3), message);
			if (plugin.getDatabaseDispatcher() != null)
				plugin.getDatabaseDispatcher().insertLog(ll);
		}
	}
	
	private static String getCallTrace(int index) {
		String[] clss = Thread.currentThread().getStackTrace()[index].getClassName().split("\\.");
		String str = clss[clss.length-1]
				+ "." + Thread.currentThread().getStackTrace()[index].getMethodName()
				+ "[" + Thread.currentThread().getStackTrace()[index].getLineNumber() + "]";
		return str;
	}
}
