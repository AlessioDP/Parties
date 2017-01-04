package com.alessiodp.parties.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandMigrate implements CommandInterface{
	private Parties plugin;
	 
    public CommandMigrate(Parties parties) {
		plugin = parties;
	}
    
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			if(Variables.database_file_name.equalsIgnoreCase("none")){
				plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.database_none);
				return true;
			}
			if(Variables.database_migrate_console){
				plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.invalidcommand);
				return true;
			}
			if(sender.hasPermission(PartiesPermissions.ADMIN_MIGRATE.toString())){
				plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_MIGRATE.toString()));
				return true;
			}
		}
		if(!Variables.database_sql_enable || plugin.getSQLDatabase() == null){
			if(sender instanceof Player)
				plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.database_offlinesql);
			plugin.log(ConsoleColors.CYAN.getCode() + Messages.database_offlinesql);
			return true;
		}
		if(args.length > 1){
			switch(args[1]){
			case "sql":
				yaml2sql();
				if(sender instanceof Player)
					plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.database_tosql);
				plugin.log(ConsoleColors.CYAN.getCode() + Messages.database_tosql);
				LogHandler.log(1, "Database system migrated to SQL by " + sender.getName());
				break;
			case "file":
				sql2yaml();
				if(sender instanceof Player)
					plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.database_tofile);
				plugin.log(ConsoleColors.CYAN.getCode() + Messages.database_tofile);
				LogHandler.log(1, "Database system migrated to YAML by " + sender.getName());
				break;
			default:
				if(sender instanceof Player)
					plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.database_wrongcmd);
				else
				    plugin.log(ConsoleColors.CYAN.getCode() + Messages.database_wrongcmd);
			}
		} else {
			if(sender instanceof Player)
				plugin.getPlayerHandler().getThePlayer((Player)sender).sendMessage(Messages.database_wrongcmd);
			else
				plugin.log(ConsoleColors.CYAN.getCode() + Messages.database_wrongcmd);
		}
		return true;
	}
    
	private void yaml2sql(){
		File dataFile = new File(plugin.getDataFolder(), "data.yml");
		if(!dataFile.exists()){
			return;
		}
	    YamlConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
	    List<String> spies = data.getStringList("spies");
	    ConfigurationSection secParties = data.getConfigurationSection("parties");
	    for(String key : secParties.getKeys(false)){
	    	System.out.println(">>" + key);
	    	Party par = new Party(key, plugin);
	    	par.setDescription(secParties.getString(key+".desc") != null ? secParties.getString(key+".desc") : "");
	    	par.setMOTD(secParties.getString(key+".motd") != null ? secParties.getString(key+".motd") : "");
	    	par.setPrefix(secParties.getString(key+".prefix") != null ? secParties.getString(key+".prefix") : "");
	    	par.setSuffix(secParties.getString(key+".suffix") != null ? secParties.getString(key+".suffix") : "");
	    	par.setPassword(secParties.getString(key+".password") != null ? secParties.getString(key+".password") : "");
	    	par.setKills(secParties.getInt(key+".kills"));
	    	par.setHome(calcolateHome(secParties.getString(key+".home") != null ? secParties.getString(key+".home") : ""));
	    	String ldr = secParties.getString(key+".leader");
	    	if(ldr != null){
	    		if(ldr.equalsIgnoreCase("fixed")){
	    			par.setLeader(UUID.fromString("00000000-0000-0000-0000-000000000000"));
		    		par.setFixed(true);
		    	} else
		    		par.setLeader(UUID.fromString(ldr));
	    	}
	    	
	    	ArrayList<UUID> ar = new ArrayList<UUID>();
	    	for(String uuid : secParties.getStringList(key+".members"))
	    		ar.add(UUID.fromString(uuid));
	    	par.setMembers(ar);
	    	plugin.getSQLDatabase().updateParty(par);
	    	
	    	for(UUID uuid : ar){
	    		ThePlayer tp = new ThePlayer(uuid, plugin);
	    		tp.setHaveParty(true);
	    		tp.setPartyName(key);
	    		ConfigurationSection sec = data.getConfigurationSection("players");
	    		if(sec != null)
	    			tp.setRank(sec.getInt(uuid.toString()+".rank"));
	    		else
	    			tp.setRank(Variables.rank_default);
	    		plugin.getSQLDatabase().updatePlayer(tp);
	    	}
	    }
	    for(String key : spies){
	    	plugin.getSQLDatabase().setSpy(UUID.fromString(key), true);
	    }
	}
	private void sql2yaml(){
		for(String name : plugin.getSQLDatabase().getAllParties()){
			Party party = plugin.getSQLDatabase().getParty(name);
			plugin.getConfigHandler().getData().bp_updateParty(party);
		}
		for(String name : plugin.getSQLDatabase().getAllPlayers()){
			ThePlayer tp = new ThePlayer(UUID.fromString(name), plugin);
			tp.setHaveParty(true);
			tp.setPartyName(plugin.getSQLDatabase().getPlayerPartyName(UUID.fromString(name)));
			tp.setRank(plugin.getSQLDatabase().getRank(UUID.fromString(name)));
			plugin.getConfigHandler().getData().bp_updatePlayer(tp);
		}
		for(UUID spy : plugin.getSQLDatabase().getAllSpies()){
			plugin.getConfigHandler().getData().bp_setSpy(spy);
		}
	}
	private Location calcolateHome(String str){
		String[] split = str.split(",");
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
}