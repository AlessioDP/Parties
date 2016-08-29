package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandSpy implements CommandInterface{
	private Parties plugin;
	 
    public CommandSpy(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		
		if(!p.hasPermission(PartiesPermissions.ADMIN_SPY.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_SPY.toString()));
			return true;
		}
		if (!plugin.getPlayerHandler().isSpy(p.getUniqueId())) {
			if(Variables.database_type.equalsIgnoreCase("none")){
				plugin.getPlayerHandler().forceSpy(p.getUniqueId());
			} else {
				plugin.getConfigHandler().getData().setSpy(p.getUniqueId(), true);
				plugin.getPlayerHandler().initSpy(p.getUniqueId());
			}
			tp.sendMessage(Messages.spy_active);
			LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] isn't anymore a spy");
		} else {
			if(Variables.database_type.equalsIgnoreCase("none")){
				plugin.getPlayerHandler().removeSpy(p.getUniqueId());
			} else {
				plugin.getConfigHandler().getData().setSpy(p.getUniqueId(), false);
				plugin.getPlayerHandler().initSpy(p.getUniqueId());
			}
			tp.sendMessage(Messages.spy_disable);
			LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] now is a spy");
		}
		return true;
	}
}
