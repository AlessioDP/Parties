package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;

public class CommandReload implements ICommand {
	private Parties plugin;
	 
	public CommandReload(Parties parties) {
		plugin = parties;
	}
	
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		boolean isPlayer = sender instanceof Player;
		PartyPlayerEntity pp = null;
		
		if (isPlayer) {
			Player p = (Player) sender;
			pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
			
			if (!p.hasPermission(PartiesPermission.ADMIN_RELOAD.toString())) {
				pp.sendNoPermission(PartiesPermission.ADMIN_RELOAD);
				return;
			}
		}
		
		plugin.reloadConfiguration();
		
		if (isPlayer) {
			pp.sendMessage(Messages.PARTIES_COMMON_CONFIGRELOAD);
			
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_RELOAD
					.replace("{player}", pp.getName()), true);
		} else {
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_RELOAD_CONSOLE, true, ConsoleColor.GREEN);
		}
	}
}
