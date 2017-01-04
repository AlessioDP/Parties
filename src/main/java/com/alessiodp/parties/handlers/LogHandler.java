package com.alessiodp.parties.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.utils.ConsoleColors;

public class LogHandler {
	static Parties plugin;
	static String username;
	static String password;
	static String url;
	static String log_table;
	static Connection connection;
	
	boolean toSQL = false;
	
	public LogHandler(Parties instance){
		plugin = instance;
		if(Variables.log_enable && Variables.log_type.equalsIgnoreCase("sql")){
			username = Variables.log_sql_username;
			password = Variables.log_sql_password;
			url = Variables.log_sql_url;
			log_table = Variables.log_sql_logtable;
			if(!startLogSQL()){
				Variables.log_type = "file";
				plugin.log(ConsoleColors.CYAN.getCode() + "Failed open connection with Server SQL. Log changed to File");
				LogHandler.log(1, "Failed open connection with Server SQL. Log changed to File");
			}
			toSQL = true;
		}
	}
	public static void log(int level, String txt){
		if(!Variables.log_enable)
			return;
		if(level > Variables.log_mode)
			return;
		if(Variables.log_type.equalsIgnoreCase("sql")){
			sqllog(level, txt);
			return;
		}
		try {
			File file = new File(Parties.datafolder, Variables.log_file_name);
			if(!file.exists())
				file.createNewFile();
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Parties.datafolder+"/" + Variables.log_file_name, true)));
		    SimpleDateFormat sdf;
		    txt = Variables.log_prefix + txt;
		    if(txt.contains("%time%")){
		    	sdf = new SimpleDateFormat("HH:mm:ss");
		        txt = txt.replace("%time%", sdf.format(Calendar.getInstance().getTime()));
		    }
	        if(txt.contains("%date%")){
	        	sdf = new SimpleDateFormat("yyyy-MM-dd");
		        txt = txt.replace("%date%", sdf.format(Calendar.getInstance().getTime()));
	        }
	        if(txt.contains("%position%"))
	        	txt = txt.replace("%position%", Thread.currentThread().getStackTrace()[1].getClassName());
		    out.println(txt);
		    out.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	private static void sqllog(int level, String txt){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	        String time = sdf.format(Calendar.getInstance().getTime());
	        sdf = new SimpleDateFormat("dd-MM-yyyy");
	        String date = sdf.format(Calendar.getInstance().getTime());
			
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO "+log_table+" (date, time, level, message) VALUES ('"+date+"', '"+time+"', "+level+", '"+txt+"');");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't log: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't log: " + ex.getMessage());
		}
	}
	private boolean startLogSQL(){
		connection = getConnection();
		if(connection==null)
			return false;
		initTable();
		return true;
	}
	
	/* SQL Base */
	private void initTable() {
		if(checkTable())
			return;
		try {
			connection = getConnection();

			if (connection == null) {
				plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
						+ "Can't connect to the database SQL");
				LogHandler.log(1, "Can't connect to the database SQL");
				return;
			}
			Statement statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE " + log_table + " (line INT NOT NULL AUTO_INCREMENT, date VARCHAR(10), time VARCHAR(10), level TINYINT, message VARCHAR(255), PRIMARY KEY (line));");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Table Creation: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Table Creation: " + ex.getMessage());
		}
	}
	private boolean checkTable() {
		try {
			connection = getConnection();

			if (connection == null) {
				return false;
			}
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM " + log_table);
			if(result!=null)
				return true;
		} catch (SQLException ex) {}
		return false;
	}
	private static boolean haveDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return true;
		} catch (ClassNotFoundException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "MySQL Driver missing: " + ex.getMessage());
			LogHandler.log(1, "MySQL Driver missing: " + ex.getMessage());
		}
		return false;
	}
	private static Connection getConnection() {
		try {
			if(!haveDriver())
			    return null;
			if (connection == null)
				return DriverManager.getConnection(url, username, password);
			if (connection.isValid(3)) {
				return connection;
			}
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Can't connect to the server SQL: " + ex.getMessage());
			LogHandler.log(1, "Can't connect to the server SQL: " + ex.getMessage());
		}
		return null;
	}
}
