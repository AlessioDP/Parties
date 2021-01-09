package com.alessiodp.parties.common.commands;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.CommandManager;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

import java.util.LinkedList;

public abstract class PartiesCommandManager extends CommandManager {
	protected PartiesCommandManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void prepareCommands() {
		commandOrder = new LinkedList<>();
		commandOrder.addAll(ConfigMain.COMMANDS_ORDER);
	}
	
	@Override
	public CommandData initializeCommandData() {
		return new PartiesCommandData();
	}
}
