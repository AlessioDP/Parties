package com.alessiodp.parties.common.commands;

import com.alessiodp.parties.common.PartiesPlugin;

public abstract class AbstractCommand {
	protected PartiesPlugin plugin;
	
	protected AbstractCommand(PartiesPlugin instance) {
		plugin = instance;
	}
	/**
	 * Used to check player before async execution
	 */
	public abstract boolean preRequisites(CommandData commandData);
	/**
	 * Async command
	 */
	public abstract void onCommand(CommandData commandData);
}