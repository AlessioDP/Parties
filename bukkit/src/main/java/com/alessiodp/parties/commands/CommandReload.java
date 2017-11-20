package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandReload implements CommandInterface {
	private Parties plugin;
	 
	public CommandReload(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player)sender;
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
			if (!p.hasPermission(PartiesPermissions.ADMIN_RELOAD.toString())) {
				tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_RELOAD.toString()));
				return true;
			}

			plugin.reloadConfiguration();
			tp.sendMessage(Messages.configurationreloaded);
			LogHandler.log(LogLevel.BASIC, "Configuration reloaded by " + p.getName() + "["+p.getUniqueId()+"]", true);
			return true;
		}
		
		plugin.reloadConfiguration();
		
		LogHandler.log(LogLevel.BASIC, "Configuration reloaded by console", true, ConsoleColors.GREEN);
		return true;
	}
}
