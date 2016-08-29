package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandReload implements CommandInterface{
	private Parties plugin;
	 
    public CommandReload(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			Player p = (Player)sender;
			if(!p.hasPermission(PartiesPermissions.ADMIN_RELOAD.toString())){
				plugin.getPlayerHandler().getThePlayer(p).sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_RELOAD.toString()));
				return true;
			}

			plugin.reloadConfiguration();
			plugin.getPlayerHandler().getThePlayer(p).sendMessage(Messages.configurationreloaded);
			LogHandler.log(1, "Configuration reloaded by " + p.getName() + "["+p.getUniqueId()+"]");
			return true;
		}
		
		plugin.reloadConfiguration();
		plugin.log(ConsoleColors.GREEN.getCode() + Messages.configurationreloaded);
		
		LogHandler.log(1, "Configuration reloaded by console");
		return true;
	}
}
