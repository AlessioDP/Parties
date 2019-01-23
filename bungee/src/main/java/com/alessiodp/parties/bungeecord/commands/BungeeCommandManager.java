package com.alessiodp.parties.bungeecord.commands;

import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.commands.main.BungeeCommandP;
import com.alessiodp.parties.bungeecord.commands.main.BungeeCommandParty;
import com.alessiodp.parties.bungeecord.commands.utils.BungeeCommandImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.CommandManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;


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
		commandParty = new BungeeCommandParty(plugin);
		commandP = new BungeeCommandP(plugin);
	}
	
	@Override
	protected void setupCommands() {
		try {
			BungeePartiesBootstrap bungeePlugin = ((BungeePartiesPlugin) plugin).getBootstrap();
			
			BungeeCommandImpl cmdParty = new BungeeCommandImpl(ConfigMain.COMMANDS_CMD_PARTY, commandParty);
			bungeePlugin.getProxy().getPluginManager().registerCommand(bungeePlugin, cmdParty);
			
			BungeeCommandImpl cmdP = new BungeeCommandImpl(ConfigMain.COMMANDS_CMD_P, commandP);
			bungeePlugin.getProxy().getPluginManager().registerCommand(bungeePlugin, cmdP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
