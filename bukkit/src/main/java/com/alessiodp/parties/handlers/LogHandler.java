package com.alessiodp.parties.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;

public class LogHandler {
	private static Parties plugin;
	private static String username;
	private static String password;
	private static String url;
	private static String logTable;
	
	private static LogLevel logLevel;
	
	private int tableLogVersion = 1;
	
	public LogHandler(Parties instance) {
		plugin = instance;
		logLevel = LogLevel.getEnum(Variables.log_mode);
		
		if (Variables.log_enable && Variables.log_type.equalsIgnoreCase("sql")) {
			username = Variables.log_sql_username;
			password = Variables.log_sql_password;
			url = Variables.log_sql_url;
			logTable = Variables.log_sql_logtable;
			if (!startLogSQL()) {
				Variables.log_type = "file";
				printError("Failed to open the connection with Server SQL. Log changed to File");
			}
		}
	}
	public static void printError(String message) {
		plugin.log(Level.WARNING, ConsoleColors.RED.getCode() + message);
		log(LogLevel.BASE, message, false, null);
	}
	public static void log(LogLevel level, String txt, boolean printConsole) {
		log(level, txt, printConsole, null);
	}
	public static void log(LogLevel level, String txt, boolean printConsole, ConsoleColors color) {
		if (level.equals(LogLevel.BASE)
				|| (printConsole
				&& (level.equals(LogLevel.BASIC)
						|| (Variables.log_enable
								&& Variables.log_printconsole
								&& level.getLevel() <= logLevel.getLevel())))) {
			String print = txt;
			if (color != null)
				print = color.getCode() + print;
			if (Variables.log_mode == LogLevel.DEBUG.getLevel() && Variables.log_printconsole)
				print = "[" + level.getLevel() + "] " + print;
			plugin.log(print);
		}
		if (Variables.log_enable && level.getLevel() <= logLevel.getLevel()) {
			if (Variables.log_type.equalsIgnoreCase("sql")) {
				// SQL
				logSQL(level, getCallTrace(3), txt);
				return;
			} else {
				// File
				try {
					File file = new File(plugin.getDataFolder(), Variables.log_file_name);
					if (!file.exists())
						file.createNewFile();
						PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(plugin.getDataFolder() + "/" + Variables.log_file_name, true)));
						out.println(handleMessage(level, txt));
						out.close();
				} catch (IOException ex) {
						printError("Error in Log send message: " + ex.getMessage());
				}
			}
		}
	}
	private static String handleMessage(LogLevel level, String message) {
		SimpleDateFormat sdf;
		String txt = Variables.log_format;
		if (txt.contains("%date%")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			txt = txt.replace("%date%", sdf.format(Calendar.getInstance().getTime()));
		}
		if (txt.contains("%time%")) {
			sdf = new SimpleDateFormat("HH:mm:ss");
			txt = txt.replace("%time%", sdf.format(Calendar.getInstance().getTime()));
		}
		if (txt.contains("%level%"))
			txt = txt.replace("%level%", Integer.toString(level.getLevel()));
		if (txt.contains("%position%"))
			txt = txt.replace("%position%", getCallTrace(6));
		if (txt.contains("%message%"))
			txt = txt.replace("%message%", message);
		return txt;
	}
	private static void logSQL(LogLevel level, String position, String txt) {
		try (Connection connection = getConnection()) {
			if (connection != null) {
				try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + logTable + " (date, level, position, message) VALUES (?,?,?,?);")) {
					preStatement.setTimestamp(1, new Timestamp(new Date().getTime()));
					preStatement.setInt(2, level.getLevel());
					preStatement.setString(3, position);
					preStatement.setString(4, txt);
					preStatement.executeUpdate();
				}
			}
		} catch (SQLException ex) {
			printError("Error in SQL Log send message: " + ex.getMessage());
		}
	}
	private boolean startLogSQL() {
		boolean ret = false;
		try (Connection connection = getConnection()) {
			if (connection != null) {
				connection.setAutoCommit(false);
				initTable(connection);
				ret = true;
			}
		} catch (SQLException ex) {
			printError("Error in SQL Log starting: " + ex.getMessage());
		}
		return ret;
	}
	private static String getCallTrace(int index) {
		String[] clss = Thread.currentThread().getStackTrace()[index].getClassName().split("\\.");
		String str = clss[clss.length-1]
				+ "." + Thread.currentThread().getStackTrace()[index].getMethodName()
				+ "[" + Thread.currentThread().getStackTrace()[index].getLineNumber() + "]";
		return str;
	}
	
	/* SQL Base */
	private void initTable(Connection connection) {
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			
			try (ResultSet rs = metadata.getTables(null, null, logTable, null)) {
				if (rs.next()) {
					// Table exist
					checkUpgrades(connection, metadata);
				} else
					createLogTable(connection);
			}
		} catch (SQLException ex) {
			printError("Error in SQL Log Table checking: " + ex.getMessage());
		}
	}
	private void checkUpgrades(Connection connection, DatabaseMetaData metadata) {
		try (PreparedStatement statement = connection.prepareStatement("SELECT table_comment FROM INFORMATION_SCHEMA.tables WHERE table_schema=? AND table_name=?;")) {
			statement.setString(1, connection.getCatalog());
			statement.setString(2, Variables.log_sql_logtable);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					if (rs.getString("table_comment").isEmpty()) {
						// 1.7 > 1.8
						upgradeLogTable17(connection);
					}
				}
			}
		} catch (SQLException ex) {
			printError("Error in SQL Log check upgrades: " + ex.getMessage());
		}
	}
	// Upgrade table: 1.7 > 1.8
	private void upgradeLogTable17(Connection connection) {
		try (Statement statement = connection.createStatement()) {
			// Renaming table
			statement.execute("RENAME TABLE " + logTable + " TO " + logTable + "_temp;");
			if (createLogTable(connection)) {
				// Moving data
				try (ResultSet rs = statement.executeQuery("SELECT * FROM " + logTable + "_temp;");) {
					while (rs.next()) {
						try (PreparedStatement preStatement = connection.prepareStatement("INSERT INTO " + logTable + " (id, date, level, position, message) VALUES (?,?,?,'',?);")) {
							preStatement.setInt(1, rs.getInt("line"));
							preStatement.setString(2, rs.getString("date") + " " + rs.getString("time"));
							preStatement.setInt(3, rs.getInt("level"));
							preStatement.setString(4, rs.getString("message"));
							preStatement.executeUpdate();
						} catch (Exception ex) {
							printError("Error in SQL Log Table upgrade (Insert Into): " + ex.getMessage());
						}
					}
				} catch (Exception ex) {}
				
				// Delete renamed table
				statement.execute("DROP TABLE " + logTable + "_temp;");
				
				connection.commit();
			}
		} catch (SQLException ex) {
			printError("Error in SQL Log Table upgrade 1.7: " + ex.getMessage());
		}
	}
	private boolean createLogTable(Connection connection) {
		boolean ret = false;
		try (Statement statement = connection.createStatement()) {
			statement.execute("CREATE TABLE " + logTable
					+ " (id INT NOT NULL AUTO_INCREMENT,"
						+ "date DATETIME,"
						+ "level TINYINT,"
						+ "position VARCHAR(50),"
						+ "message VARCHAR(255),"
						+ "PRIMARY KEY (id))"
					+ "COMMENT='Database version (do not edit):"+tableLogVersion+"';");
			ret = true;
		} catch (SQLException ex) {
			printError("Error in SQL Log Table Creation: " + ex.getMessage());
		}
		return ret;
	}
	
	/*
	 * Connection
	 */
	private static boolean haveDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return true;
		} catch (ClassNotFoundException ex) {
			printError("MySQL Driver missing: " + ex.getMessage());
		}
		return false;
	}
	private static Connection getConnection() {
		Connection ret = null;
		try {
			if (haveDriver())
				ret = DriverManager.getConnection(url, username, password);
		} catch (SQLException ex) {
			printError("Can't connect to the server SQL: " + ex.getMessage());
		}
		return ret;
	}
}
