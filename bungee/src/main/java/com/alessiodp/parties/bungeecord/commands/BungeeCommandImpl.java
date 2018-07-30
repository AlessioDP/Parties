package com.alessiodp.parties.bungeecord.commands;

import com.alessiodp.parties.bungeecord.user.BungeeUser;
import com.alessiodp.parties.common.commands.CommandDispatcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

class BungeeCommandImpl extends Command {
	private CommandDispatcher exe = null;
	//private TabCompleter tabCompleter;
	
	BungeeCommandImpl(String name) {
		super(name);
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] strings) {
		if (exe != null) {
			exe.onCommand(new BungeeUser(commandSender), super.getName(), strings);
		}
	}
	
	void setExecutor(CommandDispatcher exe) {
		this.exe = exe;
	}
}
