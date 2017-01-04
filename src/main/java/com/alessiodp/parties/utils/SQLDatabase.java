package com.alessiodp.parties.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class SQLDatabase {
	private Parties plugin;
	private Connection connection;
	private boolean failed;

	private String username;
	private String password;
	private String url;

	public SQLDatabase(Parties instance, String un, String pw, String url) {
		plugin = instance;
		username = un;
		password = pw;
		this.url = url;
		failed = false;
		connection = getConnection();
		if(connection==null)
			failed = true;
		else
			initTables();
	}
	public boolean isFailed(){
		return failed;
	}
	private boolean haveDriver() {
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
	public Connection getConnection() {
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
	/*
	 *  Migration
	 */
	public ArrayList<String> getAllParties(){
		try {
			connection = getConnection();
			if (connection == null)
				return null;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT name FROM "+Variables.database_sql_tables_parties+";");
			ArrayList<String> list = new ArrayList<String>();
			while (res.next()) {
				list.add(res.getString("name"));
			}
			return list;
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Player get all parties: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Player get all parties: " + ex.getMessage());
		}
		return null;
	}
	public ArrayList<String> getAllPlayers(){
		try {
			connection = getConnection();
			if (connection == null)
				return null;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT nickname FROM "+Variables.database_sql_tables_players+";");
			ArrayList<String> list = new ArrayList<String>();
			while (res.next()) {
				list.add(res.getString("nickname"));
			}
			return list;
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Player get all players: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Player get all players: " + ex.getMessage());
		}
		return null;
	}
	public ArrayList<UUID> getAllSpies(){
		try{
			connection = getConnection();
			if(connection == null)
				return null;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT name FROM "+Variables.database_sql_tables_spies+";");
			ArrayList<UUID> list = new ArrayList<UUID>();
			while(res.next()){
				list.add(UUID.fromString(res.getString("name")));
			}
			return list;
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Player get all spies: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Player get all spies: " + ex.getMessage());
		}
		return null;
	}
	
	/*
	 * Spies
	 */

	public boolean isSpy(UUID uuid){
		try {
			connection = getConnection();
			if (connection == null)
				return false;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+Variables.database_sql_tables_spies+" WHERE name='"+uuid.toString()+"';");
			if(res.next())
				return true;
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't check if is a spy: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't check if is a spy: " + ex.getMessage());
		}
		return false;
	}
	public void setSpy(UUID uuid, boolean value){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			Statement statement = connection.createStatement();
			if(value)
				statement.executeUpdate("INSERT IGNORE INTO "+Variables.database_sql_tables_spies+" (name) VALUES ('"+uuid.toString()+"');");
			else
				statement.executeUpdate("DELETE FROM "+Variables.database_sql_tables_spies+" WHERE name='"+uuid.toString()+"';");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't set player spy: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't set player spy: " + ex.getMessage());
		}
	}
	
	/*
	 * Player based
	 */
	public void updatePlayer(ThePlayer tp){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			
			Statement statement = connection.createStatement();
			if(!tp.haveParty())
				statement.executeUpdate("INSERT INTO "+Variables.database_sql_tables_players+" (nickname, party, rank) VALUES ('"+tp.getUUID().toString()+"', NULL, "+tp.getRank()+") ON DUPLICATE KEY UPDATE party=VALUES(party), rank=VALUES(rank);");
			else
				statement.executeUpdate("INSERT INTO "+Variables.database_sql_tables_players+" (nickname, party, rank) VALUES ('"+tp.getUUID().toString()+"', '"+tp.getPartyName()+"', "+tp.getRank()+") ON DUPLICATE KEY UPDATE party=VALUES(party), rank=VALUES(rank);");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't update player: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't update player: " + ex.getMessage());
		}
	}
	public void removePlayer(UUID uuid){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM "+Variables.database_sql_tables_players+" WHERE nickname='"+uuid.toString()+"';");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't remove player: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't remove player: " + ex.getMessage());
		}
	}
	public void setRank(UUID uuid, int rank){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE "+Variables.database_sql_tables_players+" SET rank="+rank+" WHERE nickname='"+uuid.toString()+"';");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't set player rank: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't set player rank: " + ex.getMessage());
		}
	}
	public int getRank(UUID uuid){
		try {
			connection = getConnection();
			if (connection == null)
				return -1;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+Variables.database_sql_tables_players+" WHERE nickname='"+uuid.toString()+"';");
			if(res.next())
				return res.getInt("rank");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get player rank: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get player rank: " + ex.getMessage());
		}
		return -1;
	}
	
	public String getPlayerPartyName(UUID uuid){
		try {
			connection = getConnection();
			if (connection == null)
				return "";
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+Variables.database_sql_tables_players+" WHERE nickname='"+uuid.toString()+"';");
			if(res.next())
				return res.getString("party");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get player party: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get player party: " + ex.getMessage());
		}
		return "";
	}
	public void setPartyName(UUID uuid, String name){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE "+Variables.database_sql_tables_players+" SET party='"+name+"' WHERE nickname='"+uuid.toString()+"';");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't set player party name: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't set player party name: " + ex.getMessage());
		}
	}
	
	/*
	 *  Party Based
	 */
	public Party getParty(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return null;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next()){
				Party pt = new Party(res.getString("name"), plugin);
				pt.setDescription(res.getString("descr"));
				pt.setMOTD(res.getString("motd"));
				pt.setPrefix(res.getString("prefix"));
				pt.setSuffix(res.getString("suffix"));
				pt.setKills(res.getInt("kills"));
				pt.setPassword(res.getString("password"));
				if(res.getString("home") != null){
					String[] split = res.getString("home").split(",");
					World world;
					int x,y,z;
					float yaw,pitch;
					try{
						world = Bukkit.getWorld(split[0]);
						x = Integer.parseInt(split[1]);
						y = Integer.parseInt(split[2]);
						z = Integer.parseInt(split[3]);
						yaw = Float.parseFloat(split[4]);
						pitch = Float.parseFloat(split[5]);
						pt.setHome(new Location(world, x, y, z, yaw, pitch));
					} catch(Exception ex){
						pt.setHome(null);
					}
				}
				
				String str = res.getString("leader");
				if(str != null){
					if(str.equalsIgnoreCase("fixed")){
						pt.setLeader(UUID.fromString("00000000-0000-0000-0000-000000000000"));
						pt.setFixed(true);
					} else
						pt.setLeader(UUID.fromString(str));
				}
				pt.setMembers(getMembersParty(party));
				return pt;
			}
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't check if exist party: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't check if exist party: " + ex.getMessage());
		}
		return null;
	}
	public void updateParty(Party party){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			String home = "";
			if(party.getHome() != null)
				home = party.getHome().getWorld().getName() + "," + party.getHome().getBlockX() + "," + party.getHome().getBlockY() + "," + party.getHome().getBlockZ() + "," + party.getHome().getYaw() + "," + party.getHome().getPitch();
			
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO "+Variables.database_sql_tables_parties+" (name, leader, descr, motd, prefix, suffix, kills, password, home) VALUES ('"+party.getName()+"', '"+(party.isFixed() ? "fixed" : party.getLeader().toString())+"', '"+party.getDescription()+"', '"+party.getMOTD()+"', '"+party.getPrefix()+"', '"+party.getSuffix()+"', "+party.getKills()+", '"+party.getPassword()+"', '"+home+"') ON DUPLICATE KEY UPDATE leader=VALUES(leader), descr=VALUES(descr), motd=VALUES(motd), prefix=VALUES(prefix), suffix=VALUES(suffix), kills=VALUES(kills), password=VALUES(password), home=VALUES(home);");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't update party: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't update party: " + ex.getMessage());
		}
	}
	public void renameParty(String prev, String next){
		Party old = new Party(getParty(prev));
		old.setName(next);
		updateParty(old);
		removeParty(prev);
	}
	public void removeParty(String party){
		try {
			connection = getConnection();
			if (connection == null)
				return;
			
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't remove party: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't remove party: " + ex.getMessage());
		}
	}
	public boolean existParty(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return false;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return true;
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't check if exist party: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't check if exist party: " + ex.getMessage());
		}
		return false;
	}
	public ArrayList<UUID> getMembersParty(String party){
		ArrayList<UUID> list = new ArrayList<UUID>();
		try {
			connection = getConnection();
			if (connection == null)
				return list;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT nickname FROM "+Variables.database_sql_tables_players+" WHERE party='"+party+"';");
			while (res.next()) {
				list.add(UUID.fromString(res.getString("nickname")));
			}
			return list;
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Player get all players in party: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Player get all players in party: " + ex.getMessage());
		}
		return list;
	}
	/* 
	 * Deprecated
	 * 
	public String getPartyLeader(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return "";
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT leader FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return res.getString("leader");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party leader: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party leader: " + ex.getMessage());
		}
		return "";
	}
	public String getPartyDesc(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return "";
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT descr FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return res.getString("descr");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party description: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party description: " + ex.getMessage());
		}
		return "";
	}
	public String getPartyMotd(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return "";
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT motd FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return res.getString("motd");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party motd: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party motd: " + ex.getMessage());
		}
		return "";
	}
	public String getPartyPrefix(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return "";
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT prefix FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return res.getString("prefix");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party prefix: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party prefix: " + ex.getMessage());
		}
		return "";
	}
	public String getPartySuffix(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return "";
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT suffix FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return res.getString("suffix");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party suffix: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party suffix: " + ex.getMessage());
		}
		return "";
	}
	public int getPartyKills(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return 0;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT kills FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return res.getInt("kills");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party kills: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party kills: " + ex.getMessage());
		}
		return 0;
	}
	public String getPartyPassword(String party){
		try{
			connection = getConnection();
			if(connection == null)
				return "";
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT password FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next())
				return res.getString("password");
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party kills: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party kills: " + ex.getMessage());
		}
		return "";
	}
	public Location getPartyHome(String party) {
		try{
			connection = getConnection();
			if(connection == null)
				return null;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT home FROM "+Variables.database_sql_tables_parties+" WHERE name='"+party+"';");
			if(res.next()){
				String[] split = res.getString("home").split(",");
				World world;
				int x,y,z;
				float yaw,pitch;
				try{
					world = Bukkit.getWorld(split[0]);
					x = Integer.parseInt(split[1]);
					y = Integer.parseInt(split[2]);
					z = Integer.parseInt(split[3]);
					yaw = Float.parseFloat(split[4]);
					pitch = Float.parseFloat(split[5]);
				} catch(Exception ex){
					return null;
				}
				return new Location(world, x, y, z, yaw, pitch);
			}
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query: Can't get party home: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query: Can't get party home: " + ex.getMessage());
		}
		return null;
	}
	*/
	public ArrayList<String> getAllFixed(){
		ArrayList<String> list = new ArrayList<String>();
		try {
			connection = getConnection();
			if (connection == null)
				return list;
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT name FROM "+Variables.database_sql_tables_parties+" WHERE leader='fixed';");
			while (res.next()) {
				list.add(res.getString("name"));
			}
			return list;
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Query get all fixed parties: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Query get all fixed parties: " + ex.getMessage());
		}
		return list;
	}
	/*
	 * TABLES
	 */

	public void initTables() {
		if (!checkTable(Variables.database_sql_tables_parties))
			createTable(Variables.database_sql_tables_parties, 1);
		else
			convertTableParties(Variables.database_sql_tables_parties);

		if (!checkTable(Variables.database_sql_tables_players))
			createTable(Variables.database_sql_tables_players, 2);
		else
			convertTablePlayers(Variables.database_sql_tables_players);
		
		if(!checkTable(Variables.database_sql_tables_spies))
			createTable(Variables.database_sql_tables_spies, 3);
		checkConvertedLeaders();
	}

	public boolean checkTable(String name) {
		try {
			connection = getConnection();

			if (connection == null) {
				return false;
			}
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM " + name + ";");
			
			if (result != null)
				return true;
		} catch (SQLException ex) {
		}
		return false;
	}
	public void convertTableParties(String name){
		try {
			connection = getConnection();

			if (connection == null) {
				return;
			}
			Statement statement = connection.createStatement();
			statement.executeQuery("SELECT leader FROM " + name);
			return;
		} catch (SQLException ex) {}
		plugin.log(ConsoleColors.CYAN.getCode() + "Converting old parties table (MySQL)");
		LogHandler.log(1, "Converting old parties table (MySQL)");
		try {
			connection = getConnection();

			if (connection == null) {
				return;
			}
			Statement statement = connection.createStatement();
			statement.executeUpdate("RENAME TABLE "+name+" TO "+name+"_temp;");
			createTable(Variables.database_sql_tables_parties, 1);
			ResultSet rs = statement.executeQuery("SELECT * FROM "+name+"_temp;");
			while(rs.next()){
				/*
				 * Search first old leader
				 */
				String leader = "";
				try {
					Statement statementsub = connection.createStatement();
					ResultSet res = statementsub.executeQuery("SELECT nickname FROM "+Variables.database_sql_tables_players+" WHERE party='"+rs.getString("name")+"' AND isLeader=1;");
					while (res.next()) {
						leader = res.getString("nickname");
					}
				} catch (SQLException ex) {
					plugin.log(Level.WARNING, ConsoleColors.RED.getCode() + "Error searching old leader in party: " + ex.getMessage());
					LogHandler.log(1, "Error searching old leader in party: " + ex.getMessage());
				}
				/*
				 * 
				 */
				Statement statement2 = connection.createStatement();
				statement2.executeUpdate("INSERT INTO "+name+" (name, leader, descr, motd, prefix, suffix, kills, password, home) VALUES ('"+rs.getString("name")+"', '" + leader + "', '"+rs.getString("descr")+"', '"+rs.getString("motd")+"', '"+rs.getString("prefix")+"', '"+rs.getString("suffix")+"', "+rs.getInt("kills")+", '', '"+rs.getString("home")+"') ON DUPLICATE KEY UPDATE leader=VALUES(leader), descr=VALUES(descr), motd=VALUES(motd), prefix=VALUES(prefix), suffix=VALUES(suffix), kills=VALUES(kills), password=VALUES(password), home=VALUES(home);");
			}
			statement.executeUpdate("DROP TABLE "+name+"_temp");
			return;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	public void convertTablePlayers(String name){
		try {
			connection = getConnection();

			if (connection == null) {
				return;
			}
			Statement statement = connection.createStatement();
			statement.executeQuery("SELECT isLeader FROM " + name);
			/* Here give error if isLeader doesn't exist (so it doesnt is old) */
			plugin.log(ConsoleColors.CYAN.getCode() + "Converting old players table (MySQL)");
			LogHandler.log(1, "Converting old players table (MySQL)");
			Statement substatement = connection.createStatement();
			substatement.executeUpdate("RENAME TABLE "+name+" TO "+name+"_temp;");
			createTable(name, 2);
			ResultSet rs = substatement.executeQuery("SELECT * FROM "+name+"_temp;");
			while(rs.next()){
				Statement statement3 = connection.createStatement();
				statement3.executeUpdate("INSERT INTO "+name+" (nickname, party, rank) VALUES ('"+rs.getString("nickname")+"', '"+rs.getString("party")+ "', '"+Variables.rank_default+"') ON DUPLICATE KEY UPDATE party=VALUES(party), rank=VALUES(rank);");
			}
			statement.executeUpdate("DROP TABLE "+Variables.database_sql_tables_players+"_temp" + ";");
		} catch (SQLException ex) {}
	}
	public void checkConvertedLeaders(){
		try {
			connection = getConnection();
			if (connection == null) {
				return;
			}
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM " + Variables.database_sql_tables_parties + ";");
			while (res.next()) {
				String leader = res.getString("leader");
				if(leader == null || leader.isEmpty())
					removeParty(res.getString("name"));
				else if(!leader.equalsIgnoreCase("fixed"))
					setRank(UUID.fromString(leader), Variables.rank_last);
			}
		} catch (SQLException ex) {}
	}
	public void createTable(String name, int type) {
		try {
			connection = getConnection();

			if (connection == null) {
				return;
			}
			Statement statement = connection.createStatement();
			switch (type) {
			case 1:
				statement.executeUpdate("CREATE TABLE " + name + " (name VARCHAR(40) NOT NULL, leader VARCHAR(40) DEFAULT '', descr VARCHAR(50) DEFAULT '', motd VARCHAR(255) DEFAULT '', prefix VARCHAR(25) DEFAULT '', suffix VARCHAR(25) DEFAULT '', kills INT DEFAULT 0, password VARCHAR(64) DEFAULT '', home VARCHAR(128) DEFAULT '', PRIMARY KEY (name));");
				break;
			case 2:
				statement.executeUpdate("CREATE TABLE " + name + " (nickname VARCHAR(40) NOT NULL, party VARCHAR(25) DEFAULT '',rank INT DEFAULT 0, PRIMARY KEY (nickname));");
				break;
			case 3:
				statement.executeUpdate("CREATE TABLE " + name + " (name VARCHAR(40) NOT NULL, PRIMARY KEY (name));");
			}
		} catch (SQLException ex) {
			plugin.log(Level.WARNING, ConsoleColors.RED.getCode()
					+ "Error in SQL Table Creation: " + ex.getMessage());
			LogHandler.log(1, "Error in SQL Table Creation: " + ex.getMessage());
		}
	}
}
