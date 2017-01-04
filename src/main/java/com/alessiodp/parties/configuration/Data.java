package com.alessiodp.parties.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.alessiodp.parties.handlers.ConfigHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;

public class Data {
	private ConfigHandler ch;
	File dataFile;
	FileConfiguration data;
	boolean mysql = false;

	ArrayList<String> oldSpies;
	ConfigurationSection oldPlayers;
	ConfigurationSection oldParties;

	public Data(ConfigHandler instance, boolean ismysql) {
		ch = instance;
		mysql = ismysql;
		dataFile = new File(ch.getMain().getDataFolder(), "data.yml");
		if (!dataFile.exists()) {
			ch.getMain().saveResource("data.yml", true);
		}
		data = YamlConfiguration.loadConfiguration(dataFile);
	}

	/*
	 * Spies based
	 */
	public boolean isSpy(UUID uuid) {
		if (mysql)
			return ch.getMain().getSQLDatabase().isSpy(uuid);
		if (data.getStringList("spies").contains(uuid.toString()))
			return true;
		return false;
	}

	public void setSpy(UUID uuid, boolean value) {
		if (mysql) {
			ch.getMain().getSQLDatabase().setSpy(uuid, value);
			return;
		}
		List<String> lt = data.getStringList("spies");
		if (!lt.contains(uuid.toString()) && value) {
			lt.add(uuid.toString());
			data.set("spies", lt);

			try {
				data.save(dataFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (lt.contains(uuid.toString()) && !value) {
			lt.remove(uuid.toString());
			data.set("spies", lt);

			try {
				data.save(dataFile);
			} catch (IOException e) {
				ch.reloadData();
				e.printStackTrace();
			}
		}
	}

	/*
	 * Player based
	 */

	public void updatePlayer(ThePlayer tp) {
		if (mysql) {
			ch.getMain()
					.getSQLDatabase()
					.updatePlayer(tp);
			return;
		}
		if (!tp.haveParty())
			data.set("players." + tp.getUUID().toString(), null);
		else{
			data.set("players." + tp.getUUID().toString()+".party", tp.getPartyName());
			data.set("players." + tp.getUUID().toString()+".rank", tp.getRank());
		}
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}

	public void removePlayer(UUID uuid) {
		if (mysql) {
			ch.getMain().getSQLDatabase().removePlayer(uuid);
			return;
		}
		data.set("players." + uuid.toString(), null);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}
	
	public String getPlayerPartyName(UUID uuid) {
		if (mysql)
			return ch.getMain().getSQLDatabase().getPlayerPartyName(uuid);
		String partyname = data.getString("players." + uuid.toString() + ".party");
		if (partyname != null)
			return partyname;
		return "";
	}
	public int getRank(UUID uuid) {
		if (mysql)
			return ch.getMain().getSQLDatabase().getRank(uuid);
		return data.getInt("players." + uuid.toString() + ".rank");
	}
	public void setRank(UUID uuid, int rank){
		if (mysql) {
			ch.getMain().getSQLDatabase().setRank(uuid, rank);
			return;
		}
		data.set("players." + uuid.toString() + ".rank", rank);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}
	public void setPartyName(UUID uuid, String party){
		if (mysql) {
			ch.getMain().getSQLDatabase().setPartyName(uuid, party);
			return;
		}
		data.set("players."+uuid.toString(), party);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}

	/*
	 * Party Based
	 */
	public Party getParty(String name){
		if(mysql)
			return ch.getMain().getSQLDatabase().getParty(name);
		String str;
		Party party = new Party(name, ch.getMain());
		
		str = data.getString("parties." + name + ".desc");
		party.setDescription(str != null ? str : "");
		
		str = data.getString("parties." + name + ".motd");
		party.setMOTD(str != null ? str : "");
		
		str = data.getString("parties." + name + ".prefix");
		party.setPrefix(str != null ? str : "");
		
		str = data.getString("parties." + name + ".suffix");
		party.setSuffix(str != null ? str : "");
		
		str = data.getString("parties." + name + ".password");
		party.setPassword(str != null ? str : "");
		
		str = data.getString("parties." + name + ".leader");
		if(str != null){
			if(str.equalsIgnoreCase("fixed")){
				party.setLeader(UUID.fromString("00000000-0000-0000-0000-000000000000"));
				party.setFixed(true);
			} else
				party.setLeader(UUID.fromString(str));
		}
		
		if(data.get("parties." + name + ".kills")!=null)
			try{party.setKills(data.getInt("parties." + name + ".kills"));}catch(NumberFormatException ex){};
		
		if(data.get("parties."+name+".home") != null){
			String[] split = data.getString("parties."+name+".home").split(",");
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
				party.setHome(new Location(world,x,y,z,yaw,pitch));
			} catch(Exception ex){}
		}
		
		party.setMembers(getMembersParty(name));
		
		return party;
	}
	public void renameParty(String prev, String next){
		if(mysql){
			ch.getMain()
			.getSQLDatabase()
				.renameParty(prev, next);
			return;
		}
		ConfigurationSection cs = data.getConfigurationSection("parties."+prev);
		data.set("parties."+next, cs);
		data.set("parties."+prev, null);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}
	public void updateParty(Party party) {
		if (mysql) {
			ch.getMain()
				.getSQLDatabase()
					.updateParty(party);
			return;
		}
		String str = party.getDescription();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".desc", str);
		else
			data.set("parties." + party.getName() + ".desc", null);
		str = party.getMOTD();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".motd", str);
		else
			data.set("parties." + party.getName() + ".motd", null);
		if(party.getHome() != null){
			try{
				str = party.getHome().getWorld().getName() + "," + party.getHome().getBlockX() + "," + party.getHome().getBlockY() + "," + party.getHome().getBlockZ() + "," + party.getHome().getYaw() + "," + party.getHome().getPitch();
				data.set("parties." + party.getName() + ".home", str);
			}catch(NullPointerException ex){
				data.set("parties." + party.getName() + ".home", null);
			}
		} else
			data.set("parties." + party.getName() + ".home", null);
		str = party.getPrefix();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".prefix", str);
		else
			data.set("parties." + party.getName() + ".prefix", null);
		str = party.getSuffix();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".suffix", str);
		else
			data.set("parties." + party.getName() + ".suffix", null);
		if (Variables.kill_enable)
			data.set("parties." + party.getName() + ".kills", party.getKills());
		if(Variables.password_enable)
			data.set("parties." + party.getName() + ".password", party.getPassword());
		ArrayList<String> lt = new ArrayList<String>();
		for(UUID uuid : party.getMembers()){
			lt.add(uuid.toString());
		}
		if(party.isFixed())
			data.set("parties." + party.getName() + ".leader", "fixed");
		else
			data.set("parties." + party.getName() + ".leader", party.getLeader().toString());
		data.set("parties." + party.getName() + ".members", lt);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
		if(party.isFixed())
			addFixed(party.getName());
	}

	public void removeParty(Party party) {
		if (mysql) {
			for (UUID uuid : ch.getMain().getSQLDatabase().getMembersParty(party.getName())) {
				ch.getMain().getSQLDatabase().removePlayer(uuid);
			}
			ch.getMain().getSQLDatabase().removeParty(party.getName());
		} else {
			for (UUID uuid : party.getMembers()) {
				removePlayer(uuid);
			}

			data.set("parties." + party.getName(), null);
			try {
				data.save(dataFile);
			} catch (IOException e) {
				ch.reloadData();
				e.printStackTrace();
			}
			if(party.isFixed())
				removeFixed(party.getName());
		}
		party.removeAllPlayers();
		ch.getMain().getPartyHandler().listParty.remove(party.getName());
	}

	public boolean existParty(String name) {
		if (mysql)
			return ch.getMain().getSQLDatabase().existParty(name);
		String leader = data.getString("parties." + name + ".leader");
		String partyname;
		if(leader != null && !leader.isEmpty()){
			if(leader.equals("fixed"))
				return true;
			partyname = getPlayerPartyName(UUID.fromString(leader));
			if(partyname != null && !partyname.isEmpty())
				if(partyname.equalsIgnoreCase(name))
					return true;
		}
		return false;
	}
	
	public ArrayList<String> getAllParties() {
		if(mysql)
			return ch.getMain().getSQLDatabase().getAllParties();
		ConfigurationSection cs = data.getConfigurationSection("parties");
		if(cs != null)
			return new ArrayList<String>(data.getConfigurationSection("parties").getKeys(false));
		return new ArrayList<String>();
	}
	
	public ArrayList<UUID> getMembersParty(String name) {
		ArrayList<UUID> list = new ArrayList<UUID>();
		for(String id : data.getStringList("parties." + name + ".members")){
			list.add(UUID.fromString(id));
		}
		return list;
	}
	
	/*
	 * Deprecated
	 * 
	public UUID getPartyLeader(String name) {
		String lead;
		if (mysql)
			lead = ch.getMain().getSQLDatabase().getPartyLeader(name);
		else
			lead = data.getString("parties." + name + ".leader");
		if (lead != null)
			return lead.equals("fixed") ? UUID.fromString("00000000-0000-0000-0000-000000000000") : UUID.fromString(lead);
		return null;
	}
	public String getPartyDesc(String name) {
		String desc;
		if (mysql)
			desc = ch.getMain().getSQLDatabase().getPartyDesc(name);
		else
			desc = data.getString("parties." + name + ".desc");
		if (desc != null)
			return desc;
		return "";
	}
	
	public String getPartyMotd(String name) {
		String motd;
		if (mysql)
			motd = ch.getMain().getSQLDatabase().getPartyMotd(name);
		else
			motd = data.getString("parties." + name + ".motd");
		if (motd != null)
			return motd;
		return "";
	}
	
	public String getPartyPrefix(String name) {
		String prefix;
		if (mysql)
			prefix = ch.getMain().getSQLDatabase().getPartyPrefix(name);
		else
			prefix = data.getString("parties." + name + ".prefix");
		if (prefix != null)
			return prefix;
		return "";
	}

	public String getPartySuffix(String name) {
		String suffix;
		if (mysql)
			suffix = ch.getMain().getSQLDatabase().getPartySuffix(name);
		else
			suffix = data.getString("parties." + name + ".suffix");
		if (suffix != null)
			return suffix;
		return "";
	}

	public int getPartyKills(String name) {
		if (mysql)
			return ch.getMain().getSQLDatabase().getPartyKills(name);
		int kills = 0;
		if (data.get("parties." + name + ".kills") != null) {
			try {
				kills = data.getInt("parties." + name + ".kills");
			} catch (NumberFormatException ex) {
			}
		}
		return kills;
	}
	public String getPartyPassword(String name) {
		if (mysql)
			return ch.getMain().getSQLDatabase().getPartyPassword(name);
		String password = "";
		if (data.get("parties." + name + ".password") != null) {
			try {
				password = data.getString("parties." + name + ".password");
			} catch (NumberFormatException ex) {
			}
		}
		return password;
	}
	public Location getPartyHome(String name) {
		if(mysql)
			return ch.getMain().getSQLDatabase().getPartyHome(name);
		if(data.get("parties."+name+".home") == null)
			return null;
		String[] split = data.getString("parties."+name+".home").split(",");
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
	*/
	/*
	 * Fixed
	 */
	public void addFixed(String name){
		if(mysql)
			return;
		List<String> lst = getAllFixed();
		if(!lst.contains(name))
			lst.add(name);
		data.set("players.fixed", lst);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}
	public void removeFixed(String name){
		if(mysql)
			return;
		List<String> lst = getAllFixed();
		if(lst.contains(name))
			lst.remove(name);
		if(lst.isEmpty())
			data.set("players.fixed", null);
		else
			data.set("players.fixed", lst);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}
	public List<String> getAllFixed(){
		if(mysql)
			return ch.getMain().getSQLDatabase().getAllFixed();
		List<String> lst = new ArrayList<String>();
		if(data.get("players.fixed")!=null)
			lst = data.getStringList("players.fixed");
		return lst;
	}
	/*
	 * Bypass
	 */
	public void bp_updatePlayer(ThePlayer tp) {
		if (!tp.haveParty())
			data.set("players." + tp.getUUID().toString(), null);
		else{
			data.set("players." + tp.getUUID().toString()+".party", tp.getPartyName());
			data.set("players." + tp.getUUID().toString()+".rank", tp.getRank());
		}
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}

	public void bp_updateParty(Party party) {
		String str = party.getDescription();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".desc", str);
		else
			data.set("parties." + party.getName() + ".desc", null);
		str = party.getMOTD();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".motd", str);
		else
			data.set("parties." + party.getName() + ".motd", null);
		if(party.getHome() != null){
			try{
				str = party.getHome().getWorld().getName() + "," + party.getHome().getBlockX() + "," + party.getHome().getBlockY() + "," + party.getHome().getBlockZ() + "," + party.getHome().getYaw() + "," + party.getHome().getPitch();
				data.set("parties." + party.getName() + ".home", str);
			}catch(NullPointerException ex){
				data.set("parties." + party.getName() + ".home", null);
			}
		} else
			data.set("parties." + party.getName() + ".home", null);
		str = party.getPrefix();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".prefix", str);
		else
			data.set("parties." + party.getName() + ".prefix", null);
		str = party.getSuffix();
		if(str != null && !str.isEmpty())
			data.set("parties." + party.getName() + ".suffix", str);
		else
			data.set("parties." + party.getName() + ".suffix", null);
		if (Variables.kill_enable)
			data.set("parties." + party.getName() + ".kills", party.getKills());
		if(Variables.password_enable)
			data.set("parties." + party.getName() + ".password", party.getPassword());
		ArrayList<String> lt = new ArrayList<String>();
		for(UUID uuid : party.getMembers()){
			lt.add(uuid.toString());
		}
		if(party.isFixed())
			data.set("parties." + party.getName() + ".leader", "fixed");
		else
			data.set("parties." + party.getName() + ".leader", party.getLeader().toString());
		data.set("parties." + party.getName() + ".members", lt);
		try {
			data.save(dataFile);
		} catch (IOException e) {
			ch.reloadData();
			e.printStackTrace();
		}
	}

	public void bp_setSpy(UUID uuid) {
		List<String> lt = data.getStringList("spies");
		if (!lt.contains(uuid.toString())) {
			lt.add(uuid.toString());
			data.set("spies", lt);
			try {
				data.save(dataFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}