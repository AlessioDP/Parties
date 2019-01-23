package com.alessiodp.parties.common.commands.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.user.User;

import java.util.List;

public abstract class PartiesCommandExecutor {
	protected PartiesPlugin plugin;
	
	protected PartiesCommandExecutor(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public abstract boolean onCommand(User sender, String command, String[] args);
	
	public abstract List<String> onTabComplete(User sender, String[] args);
}
