package com.alessiodp.parties.common.commands;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class CommandDispatcher {
	private PartiesPlugin plugin;
	private HashMap<String, AbstractCommand> commands;
	@Getter private List<PartiesCommand> enabledCommands;
	
	protected CommandDispatcher(PartiesPlugin instance) {
		plugin = instance;
		commands = new HashMap<>();
		enabledCommands = new ArrayList<>();
	}
	
	public void register(PartiesCommand command, AbstractCommand commandExecutor) {
		commands.put(command.getCommand().toLowerCase(), commandExecutor);
		enabledCommands.add(command);
	}
	
	public int getCommandsNumber() {
		return commands.size();
	}
	
	private boolean exists(String name) {
		return commands.containsKey(name.toLowerCase());
	}
	
	private AbstractCommand getExecutor(String name) {
		return commands.get(name);
	}
	
	public boolean onCommand(User sender, String command, String[] args) {
		if (sender.isPlayer()) {
			// Player
			if (command.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_PARTY)) {
				if (args.length > 0) {
					if (exists(args[0])) {
						prepareCommand(sender, command, args[0], args);
					} else {
						sender.sendMessage(Messages.PARTIES_COMMON_INVALIDCMD, true);
					}
				} else {
					prepareCommand(sender, command, command, args);
				}
			} else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_P)) {
				prepareCommand(sender, command, command, args);
			}
		} else {
			// Console
			if (command.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_PARTY)) {
				if (args.length > 0
						&& (args[0].equalsIgnoreCase(ConfigMain.COMMANDS_CMD_RELOAD)
						|| args[0].equalsIgnoreCase(ConfigMain.COMMANDS_CMD_MIGRATE))) {
					prepareCommand(sender, command, args[0], args);
				} else {
					printConsoleHelp();
				}
			} else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_P)) {
				sender.sendMessage(Constants.ONLY_PLAYERS, false);
			}
		}
		return true;
	}
	
	private void prepareCommand(User sender, String command, String subCommand, String[] args) {
		CommandData cd = new CommandData();
		cd.setSender(sender);
		cd.setCommandLabel(command);
		cd.setArgs(args);
		if (getExecutor(subCommand.toLowerCase()).preRequisites(cd)) {
			CompletableFuture.supplyAsync(() -> {
				getExecutor(subCommand.toLowerCase()).onCommand(cd);
				return true;
			}, plugin.getPartiesScheduler().getCommandsExecutor())
					.exceptionally(ex -> {
						ex.printStackTrace();
						return false;
					});
		}
	}
	
	private void printConsoleHelp() {
		for (String str : Messages.HELP_CONSOLEHELP) {
			plugin.log(str);
		}
	}
}
