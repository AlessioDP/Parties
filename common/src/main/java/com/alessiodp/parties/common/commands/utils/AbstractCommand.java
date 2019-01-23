package com.alessiodp.parties.common.commands.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.user.User;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * Used to tab complete the command
	 */
	public List<String> onTabComplete(User sender, String[] args){
		return new ArrayList<>();
	}
}