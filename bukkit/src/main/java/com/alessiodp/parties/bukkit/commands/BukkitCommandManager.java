package com.alessiodp.parties.bukkit.commands;

import java.lang.reflect.Field;

import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.commands.main.BukkitCommandP;
import com.alessiodp.parties.bukkit.commands.main.BukkitCommandParty;
import com.alessiodp.parties.bukkit.commands.utils.BukkitCommandImpl;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.CommandManager;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;

public class BukkitCommandManager extends CommandManager {
	
	public BukkitCommandManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	protected void prepareCommands() {
		super.prepareCommands();
		BukkitCommands.setup();
	}

	@Override
	protected void registerCommands() {
		commandParty = new BukkitCommandParty(plugin);
		commandP = new BukkitCommandP(plugin);
	}
	
	@Override
	protected void setupCommands() {
		try {
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			
			// Party command
			if (commandMap.getCommand(ConfigMain.COMMANDS_CMD_PARTY) == null) {
				// New command
				BukkitCommandImpl cmdParty = new BukkitCommandImpl(ConfigMain.COMMANDS_CMD_PARTY, commandParty);
				commandMap.register("parties", cmdParty);
			} else {
				// Update the executor and description
				Command cmd = commandMap.getCommand(ConfigMain.COMMANDS_CMD_PARTY);
				cmd.setDescription(BukkitConfigMain.COMMANDS_DESC_PARTY);
				if (cmd instanceof BukkitCommandImpl) {
					// Parties command
					((BukkitCommandImpl) cmd).setExecutor(commandParty);
				} else {
					// External command - overwrite it
					LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_COMMANDS_REGISTER_PARTY_EXIST, true);
					
					if (cmd instanceof PluginCommand){
						BukkitCommandImpl cmdParty = new BukkitCommandImpl(ConfigMain.COMMANDS_CMD_PARTY, commandParty);
						((PluginCommand) cmd).setExecutor(cmdParty);
					}
				}
			}
			
			// P command
			if (commandMap.getCommand(ConfigMain.COMMANDS_CMD_P) == null) {
				// New command
				BukkitCommandImpl cmdParty = new BukkitCommandImpl(ConfigMain.COMMANDS_CMD_P, commandP);
				commandMap.register("parties", cmdParty);
			} else {
				// Update the executor and description
				Command cmd = commandMap.getCommand(ConfigMain.COMMANDS_CMD_P);
				cmd.setDescription(BukkitConfigMain.COMMANDS_DESC_P);
				if (cmd instanceof BukkitCommandImpl) {
					// Parties command
					((BukkitCommandImpl) cmd).setExecutor(commandP);
				} else {
					// External command - overwrite it
					LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_COMMANDS_REGISTER_P_EXIST, true);
					
					if (cmd instanceof PluginCommand){
						BukkitCommandImpl cmdP = new BukkitCommandImpl(ConfigMain.COMMANDS_CMD_P, commandP);
						((PluginCommand) cmd).setExecutor(cmdP);
					}
				}
			}
			bukkitCommandMap.setAccessible(false);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
