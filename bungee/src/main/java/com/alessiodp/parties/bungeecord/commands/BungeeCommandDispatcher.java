package com.alessiodp.parties.bungeecord.commands;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.CommandDispatcher;
import com.alessiodp.parties.common.user.User;

public class BungeeCommandDispatcher extends CommandDispatcher {
	
	public BungeeCommandDispatcher(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		return super.onCommand(sender, command, args);
	}
}