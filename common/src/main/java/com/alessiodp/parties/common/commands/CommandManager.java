package com.alessiodp.parties.common.commands;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.commands.main.CommandP;
import com.alessiodp.parties.common.commands.main.CommandParty;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import lombok.Getter;

import java.util.LinkedList;

public abstract class CommandManager {
	protected PartiesPlugin plugin;
	protected CommandParty commandParty;
	protected CommandP commandP;
	@Getter protected LinkedList<PartiesCommand> orderedCommands;
	
	protected CommandManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public final void setup() {
		prepareCommands();
		registerCommands();
		setupCommands();
		orderCommands();
	}
	
	protected void prepareCommands() {
		CommonCommands.setup();
	}
	
	protected abstract void registerCommands();
	protected abstract void setupCommands();
	
	private void orderCommands() {
		orderedCommands = new LinkedList<>();
		for (String command : ConfigMain.COMMANDS_ORDER) {
			// P command
			if (command.equalsIgnoreCase(CommonCommands.P.getType()))
				orderedCommands.add(CommonCommands.P);
			// Party sub-commands
			for (PartiesCommand pc : commandParty.getEnabledSubCommands()) {
				if (pc.getType().equalsIgnoreCase(command)) {
					orderedCommands.add(pc);
					break;
				}
			}
		}
	}
}
