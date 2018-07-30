package com.alessiodp.parties.bukkit.commands;

import com.alessiodp.parties.bukkit.user.BukkitUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.CommandDispatcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

class BukkitCommandDispatcher extends CommandDispatcher implements CommandExecutor {
	
	BukkitCommandDispatcher(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		return super.onCommand(new BukkitUser(commandSender), command.getName(), args);
	}
}