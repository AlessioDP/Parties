package com.alessiodp.parties.commands;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.utils.ConsoleColor;

public class CommandDispatcher implements CommandExecutor {
	private static HashMap<String, ICommand> commands = new HashMap<String, ICommand>();
	private Parties plugin;
	
	public CommandDispatcher(Parties parties) {
		plugin = parties;
	}
	
	public void register(String name, ICommand cmd) {
		commands.put(name, cmd);
	}
	
	public boolean exists(String name) {
		return commands.containsKey(name);
	}
	
	public ICommand getExecutor(String name) {
		return commands.get(name);
	}
	
	public int getCommandsNumber() {
		return commands.size();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			// Player
			if (commandLabel.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_PARTY)) {
				if (args.length > 0) {
					if (exists(args[0])) {
						prepareCommand(args[0], sender, cmd, commandLabel, args);
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.PARTIES_COMMON_INVALIDCMD));
					}
				} else {
					prepareCommand(commandLabel, sender, cmd, commandLabel, args);
				}
			} else if (commandLabel.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_P)) {
				prepareCommand(commandLabel, sender, cmd, commandLabel, args);
			}
		} else {
			// Sender
			if (commandLabel.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_PARTY)) {
				if (args.length > 0
						&& (args[0].equalsIgnoreCase(ConfigMain.COMMANDS_CMD_RELOAD)
								|| args[0].equalsIgnoreCase(ConfigMain.COMMANDS_CMD_MIGRATE))) {
					prepareCommand(args[0], sender, cmd, commandLabel, args);
				} else {
					printHelp();
				}
			} else if (commandLabel.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_P)) {
				plugin.log(ConsoleColor.RED.getCode() + Constants.ONLY_PLAYERS);
			}
		}
		return true;
	}
	
	private void printHelp() {
		for (String str : Messages.HELP_CONSOLEHELP) {
			plugin.log(str);
		}
	}
	
	private void prepareCommand(String partiesCommand, CommandSender sender, Command cmd, String commandLabel, String[] args) {
		CommandData cd = new CommandData();
		cd.setSender(sender);
		cd.setCommandLabel(commandLabel);
		cd.setArgs(args);
		
		if (getExecutor(partiesCommand).preRequisites(cd)) {
			CompletableFuture.supplyAsync(() -> {
				getExecutor(partiesCommand).onCommand(cd);
				return true;
			}, plugin.getPartiesScheduler().getCommandsExecutor())
			.exceptionally(ex -> {
				ex.printStackTrace();
				return null;
			});
		}
	}
	/*
	private void executeAsync(Runnable runnable) {
		CompletableFuture.supplyAsync(() -> runnable, plugin.getPartiesScheduler().getCommandsExecutor())
		.exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});
	}
	private void executeAsync(String partiesCommand, CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// cmd is not used
		CompletableFuture.supplyAsync(() -> {
			getExecutor(partiesCommand).onCommand(sender, commandLabel, args);
			return true;
		}, plugin.getPartiesScheduler().getCommandsExecutor())
		.exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});
	}*/
}