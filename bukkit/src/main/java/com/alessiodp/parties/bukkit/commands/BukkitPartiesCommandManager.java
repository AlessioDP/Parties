package com.alessiodp.parties.bukkit.commands;

import java.util.ArrayList;

import com.alessiodp.core.bukkit.commands.utils.BukkitCommandUtils;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.bukkit.commands.main.BukkitCommandP;
import com.alessiodp.parties.bukkit.commands.main.BukkitCommandParty;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.PartiesCommandManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

public class BukkitPartiesCommandManager extends PartiesCommandManager {
	
	public BukkitPartiesCommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void prepareCommands() {
		super.prepareCommands();
		commandUtils = new BukkitCommandUtils(plugin, ConfigMain.COMMANDS_SUB_ON, ConfigMain.COMMANDS_SUB_OFF);
	}
	
	@Override
	public void registerCommands() {
		mainCommands = new ArrayList<>();
		mainCommands.add(new BukkitCommandParty((PartiesPlugin) plugin));
		if (!((PartiesPlugin) plugin).isBungeeCordEnabled()) {
			mainCommands.add(new BukkitCommandP((PartiesPlugin) plugin));
		}
	}
}
