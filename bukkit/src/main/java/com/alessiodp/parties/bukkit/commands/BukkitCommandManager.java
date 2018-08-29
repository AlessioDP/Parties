package com.alessiodp.parties.bukkit.commands;

import java.lang.reflect.Field;

import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.commands.executors.CommandProtection;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.CommandManager;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import com.alessiodp.parties.bukkit.addons.internal.TabCompleterHandler;
import com.alessiodp.parties.bukkit.commands.executors.CommandClaim;
import com.alessiodp.parties.bukkit.commands.executors.CommandConfirm;
import com.alessiodp.parties.bukkit.commands.executors.CommandHome;
import com.alessiodp.parties.bukkit.commands.executors.CommandSetHome;
import com.alessiodp.parties.bukkit.commands.executors.CommandTeleport;
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
		dispatcher = new BukkitCommandDispatcher(plugin);
		super.registerCommands();
		
		// Claim
		if (BukkitConfigMain.ADDONS_GRIEFPREVENTION_ENABLE)
			dispatcher.register(BukkitCommands.CLAIM, new CommandClaim(plugin));
		
		// Confirm
		if (BukkitConfigMain.ADDONS_VAULT_ENABLE && BukkitConfigMain.ADDONS_VAULT_CONFIRM_ENABLE)
			dispatcher.register(BukkitCommands.CONFIRM, new CommandConfirm(plugin));
		
		// Home
		if (BukkitConfigParties.HOME_ENABLE) {
			dispatcher.register(BukkitCommands.HOME, new CommandHome(plugin));
			dispatcher.register(BukkitCommands.SETHOME, new CommandSetHome(plugin));
		}
		
		// Protection
		if (BukkitConfigParties.FRIENDLYFIRE_TYPE.equalsIgnoreCase("command"))
			dispatcher.register(BukkitCommands.PROTECTION, new CommandProtection(plugin));
		
		// Teleport
		if (BukkitConfigParties.TELEPORT_ENABLE)
			dispatcher.register(BukkitCommands.TELEPORT, new CommandTeleport(plugin));
	}
	
	@Override
	protected void setupCommands() {
		try {
			BukkitCommandImpl cmdParty = new BukkitCommandImpl(ConfigMain.COMMANDS_CMD_PARTY);
			BukkitCommandImpl cmdP = new BukkitCommandImpl(ConfigMain.COMMANDS_CMD_P);
			cmdParty.setDescription(BukkitConfigMain.COMMANDS_DESC_PARTY);
			cmdP.setDescription(BukkitConfigMain.COMMANDS_DESC_P);
			
			// Register commands
			final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			
			cmdParty.setExecutor((BukkitCommandDispatcher) dispatcher);
			if (BukkitConfigMain.COMMANDS_TABSUPPORT)
				cmdParty.setTabCompleter(new TabCompleterHandler(plugin));
			cmdP.setExecutor((BukkitCommandDispatcher) dispatcher);
			
			commandMap.register(ConfigMain.COMMANDS_CMD_PARTY, cmdParty);
			commandMap.register(ConfigMain.COMMANDS_CMD_P, cmdP);
			bukkitCommandMap.setAccessible(false);
			
			
			// Force Parties executors instead of other plugins
			PluginCommand commandParties = ((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_PARTY);
			PluginCommand commandP = ((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_P);
			
			// Register party command
			if (commandParties != null && commandParties.getPlugin() != null && !commandParties.getPlugin().getName().equals("Parties")) {
				commandParties.setExecutor((BukkitCommandDispatcher) dispatcher);
				if (BukkitConfigMain.COMMANDS_TABSUPPORT)
					commandParties.setTabCompleter(new TabCompleterHandler(plugin));
			}
			// Register p command
			if (commandP != null && commandP.getPlugin() != null && !commandP.getPlugin().getName().equals("Parties"))
				commandP.setExecutor((BukkitCommandDispatcher) dispatcher);
			
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_COMMANDS_REGISTER_POST
					.replace("{value}", Integer.toString(dispatcher.getCommandsNumber())), true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
