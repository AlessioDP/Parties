package com.alessiodp.parties.velocity.commands;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.velocity.commands.utils.VelocityCommandUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.PartiesCommandManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.velocity.commands.main.VelocityCommandP;
import com.alessiodp.parties.velocity.commands.main.VelocityCommandParty;

import java.util.ArrayList;

public class VelocityPartiesCommandManager extends PartiesCommandManager {
	public VelocityPartiesCommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void prepareCommands() {
		commandUtils = new VelocityCommandUtils(plugin, ConfigMain.COMMANDS_MISC_ON, ConfigMain.COMMANDS_MISC_OFF);
		
		super.prepareCommands();
	}
	
	@Override
	public void registerCommands() {
		mainCommands = new ArrayList<>();
		mainCommands.add(new VelocityCommandParty((PartiesPlugin) plugin));
		mainCommands.add(new VelocityCommandP((PartiesPlugin) plugin));
	}
}
