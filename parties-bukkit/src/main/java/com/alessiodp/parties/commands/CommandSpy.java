package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandSpy implements CommandInterface {
	private Parties plugin;
	 
	public CommandSpy(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		
		if (!p.hasPermission(PartiesPermissions.ADMIN_SPY.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_SPY.toString()));
			return true;
		}
		boolean isSpy = plugin.getPlayerHandler().isSpy(p.getUniqueId());
		
		plugin.getPlayerHandler().setSpy(p.getUniqueId(), !isSpy);
		
		if (isSpy) {
			tp.sendMessage(Messages.spy_disable);
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] isn't a spy anymore", true);
		} else {
			tp.sendMessage(Messages.spy_active);
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] now is a spy", true);
		}
		return true;
	}
}
