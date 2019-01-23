package com.alessiodp.parties.bungeecord.commands.utils;

import com.alessiodp.parties.bungeecord.user.BungeeUser;
import com.alessiodp.parties.common.commands.utils.PartiesCommandExecutor;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class BungeeCommandImpl extends Command implements TabExecutor {
	private PartiesCommandExecutor executor;
	
	public BungeeCommandImpl(String name, PartiesCommandExecutor executor) {
		super(name);
		this.executor = executor;
		
	}
	
	@Override
	public void execute(CommandSender commandSender, String[] args) {
		executor.onCommand(new BungeeUser(commandSender), this.getName(), args);
	}
	
	@Override
	public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
		if (ConfigMain.COMMANDS_TABSUPPORT) {
			return executor.onTabComplete(new BungeeUser(commandSender), args);
		}
		return new ArrayList<>();
	}
}
