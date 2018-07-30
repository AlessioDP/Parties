package com.alessiodp.parties.common.logging;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.storage.StorageType;
import com.alessiodp.parties.common.utils.ConsoleColor;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LoggerManager {
	private static PartiesPlugin plugin;
	
	private static LogLevel logLevel;
	private static boolean logToFile;
	
	public LoggerManager(PartiesPlugin instance) {
		plugin = instance;
		logToFile = false;
	}
	
	public static void reload() {
		logLevel = LogLevel.getEnum(ConfigMain.STORAGE_LOG_LEVEL);
		logToFile = plugin.getDatabaseManager().getLogType() != StorageType.NONE;
	}
	
	public static void printError(String message) {
		plugin.logError(ConsoleColor.RED.getCode() + message);
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
			LogLine logLine = new LogLine(ConfigMain.STORAGE_LOG_FORMAT, level, getCallTrace(3 + (color == null ? 1 : 0)), message);
			plugin.getDatabaseManager().insertLog(logLine);
		}
	}
	
	public static String formatErrorCallTrace(String base, Exception ex) {
		StackTraceElement st = Thread.currentThread().getStackTrace()[2];
		String[] clss = st.getClassName().split("\\.");
		return base
				.replace("{class}", clss[clss.length - 1])
				.replace("{method}", st.getMethodName())
				.replace("{line}", Integer.toString(st.getLineNumber()))
				.replace("{type}", ex.getClass().getSimpleName())
				.replace("{message}", ex.getMessage() != null ? ex.getMessage() : ex.toString())
				.replace("{stacktrace}", Arrays.stream(ex.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
	}
	
	private static String getCallTrace(int index) {
		String[] clss = Thread.currentThread().getStackTrace()[index].getClassName().split("\\.");
		return clss[clss.length - 1] + "." + Thread.currentThread().getStackTrace()[index].getMethodName() + "["
				+ Thread.currentThread().getStackTrace()[index].getLineNumber() + "]";
	}
}
