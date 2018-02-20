package com.alessiodp.parties.logging;

import java.util.logging.Level;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.storage.StorageType;
import com.alessiodp.parties.utils.ConsoleColor;

public class LoggerManager {
	private static Parties plugin;
	
	private static LogLevel logLevel;
	private static boolean logToFile;
	
	public LoggerManager(Parties instance) {
		plugin = instance;
		logToFile = false;
	}
	
	public static void reload() {
		logLevel = LogLevel.getEnum(ConfigMain.STORAGE_LOG_LEVEL);
		logToFile = plugin.getDatabaseManager().getLogType() != StorageType.NONE;
	}
	
	public static void printError(String message) {
		plugin.log(Level.WARNING, ConsoleColor.RED.getCode() + message);
		log(LogLevel.BASE, message, false, null);
	}
	
	public static void log(LogLevel level, String message, boolean printConsole) {
		log(level, message, printConsole, null);
	}
	
	public static void log(LogLevel level, String message, boolean printConsole, ConsoleColor color) {
		if (printConsole) {
			if (level.equals(LogLevel.BASE) || (ConfigMain.STORAGE_LOG_PRINTCONSOLE
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
		
		if (logToFile && level.getLevel() <= logLevel.getLevel()) {
			LogLine ll = new LogLine(ConfigMain.STORAGE_LOG_FORMAT, level, getCallTrace(3), message);
			plugin.getDatabaseManager().insertLog(ll);
		}
	}
	
	public static String formatErrorCallTrace(String base, Exception ex) {
		StackTraceElement st = Thread.currentThread().getStackTrace()[2];
		String[] clss = st.getClassName().split("\\.");
		return base.replace("{class}", clss[clss.length - 1]).replace("{method}", st.getMethodName())
				.replace("{line}", Integer.toString(st.getLineNumber())).replace("{message}", ex.getMessage());
	}
	
	private static String getCallTrace(int index) {
		String[] clss = Thread.currentThread().getStackTrace()[index].getClassName().split("\\.");
		String str = clss[clss.length - 1] + "." + Thread.currentThread().getStackTrace()[index].getMethodName() + "["
				+ Thread.currentThread().getStackTrace()[index].getLineNumber() + "]";
		return str;
	}
}
