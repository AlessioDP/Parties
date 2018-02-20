package com.alessiodp.parties.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PartiesCommand extends Command {
	private CommandExecutor exe = null;
	private TabCompleter tabCompleter;
	 
	public PartiesCommand(String name) {
		super(name);
	}
	
	public boolean execute(CommandSender sender, String commandLabel,String[] args) {
		if (exe != null) {
			exe.onCommand(sender, this, commandLabel, args);
		}
		return false;
	}
	
	public void setExecutor(CommandExecutor exe) {
		this.exe = exe;
	}
	
	public void setTabCompleter(TabCompleter tc) {
		tabCompleter = tc;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
		if (tabCompleter != null) {
			List<String> res = tabCompleter.onTabComplete(sender, this, alias, args);
			if (res != null)
				return res;
		}
		return super.tabComplete(sender, alias, args);
	}
}
