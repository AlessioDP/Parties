package com.alessiodp.parties.bungeecord.commands;

import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.CommandManager;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;


public class BungeeCommandManager extends CommandManager {
	
	public BungeeCommandManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	protected void prepareCommands() {
		super.prepareCommands();
	}
	
	@Override
	protected void registerCommands() {
		dispatcher = new BungeeCommandDispatcher(plugin);
		super.registerCommands();
	}
	
	@Override
	protected void setupCommands() {
		try {
			BungeePartiesBootstrap bungeePlugin = ((BungeePartiesPlugin) plugin).getBootstrap();
			
			BungeeCommandImpl cmdParty = new BungeeCommandImpl(ConfigMain.COMMANDS_CMD_PARTY);
			BungeeCommandImpl cmdP = new BungeeCommandImpl(ConfigMain.COMMANDS_CMD_P);
			
			bungeePlugin.getProxy().getPluginManager().registerCommand(bungeePlugin, cmdParty);
			bungeePlugin.getProxy().getPluginManager().registerCommand(bungeePlugin, cmdP);
			
			cmdParty.setExecutor(dispatcher);
			cmdP.setExecutor(dispatcher);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_COMMANDS_REGISTER_POST
					.replace("{value}", Integer.toString(dispatcher.getCommandsNumber())), true);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
