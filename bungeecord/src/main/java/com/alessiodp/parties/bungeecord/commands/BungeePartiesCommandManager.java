package com.alessiodp.parties.bungeecord.commands;

import com.alessiodp.core.bungeecord.commands.utils.BungeeCommandUtils;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.bungeecord.commands.main.BungeeCommandP;
import com.alessiodp.parties.bungeecord.commands.main.BungeeCommandParty;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.PartiesCommandManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

import java.util.ArrayList;

public class BungeePartiesCommandManager extends PartiesCommandManager {
	public BungeePartiesCommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void prepareCommands() {
		commandUtils = new BungeeCommandUtils(plugin, ConfigMain.COMMANDS_SUB_ON, ConfigMain.COMMANDS_SUB_OFF);
		
		super.prepareCommands();
	}
	
	@Override
	public void registerCommands() {
		mainCommands = new ArrayList<>();
		mainCommands.add(new BungeeCommandParty((PartiesPlugin) plugin));
		mainCommands.add(new BungeeCommandP((PartiesPlugin) plugin));
	}
}
