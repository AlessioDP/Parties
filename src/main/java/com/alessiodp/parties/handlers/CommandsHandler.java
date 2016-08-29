package com.alessiodp.parties.handlers;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.ConsoleColors;

public class CommandsHandler implements CommandExecutor {
	private static HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();
	private Parties plugin;

	public CommandsHandler(Parties parties) {
		plugin = parties;
	}

	public void register(String name, CommandInterface cmd) {
		commands.put(name, cmd);
	}

	public boolean exists(String name) {
		return commands.containsKey(name);
	}

	public CommandInterface getExecutor(String name) {
		return commands.get(name);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase(Variables.command_reload)) {
				getExecutor(Variables.command_reload).onCommand(sender, cmd,
						commandLabel, args);
				return true;
			} else if (args[0].equalsIgnoreCase(Variables.command_migrate)) {
				getExecutor(Variables.command_migrate).onCommand(sender, cmd, commandLabel, args);
				return true;
			}
		}

		if (sender instanceof Player) {
			if (args.length == 0) {
				if(commandLabel.equalsIgnoreCase(Variables.command_party)){
					getExecutor(Variables.command_party).onCommand(sender, cmd, commandLabel, args);
				} else if(commandLabel.equalsIgnoreCase(Variables.command_p)){
					getExecutor(Variables.command_p).onCommand(sender, cmd, commandLabel, args);
				}
				return true;
			}
			if (args.length > 0) {
				if (commandLabel.equalsIgnoreCase(Variables.command_p)) {
					getExecutor(Variables.command_p).onCommand(sender, cmd,
							commandLabel, args);
					return true;
				} else if (args[0].equalsIgnoreCase(Variables.command_party)
						|| args[0].equalsIgnoreCase(Variables.command_p)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', Messages.invalidcommand));
					return true;
				}
				if (exists(args[0])) {
					getExecutor(args[0]).onCommand(sender, cmd, commandLabel,
							args);
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', Messages.invalidcommand));
					return true;
				}
			}
		} else {
			plugin.log(ConsoleColors.RED.getCode() + "You must be a player to use this command.");
			return true;
		}
		return false;
	}

}