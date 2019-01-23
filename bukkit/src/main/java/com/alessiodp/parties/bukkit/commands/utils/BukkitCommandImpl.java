package com.alessiodp.parties.bukkit.commands.utils;

import com.alessiodp.parties.bukkit.user.BukkitUser;
import com.alessiodp.parties.common.commands.utils.PartiesCommandExecutor;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandImpl extends Command implements CommandExecutor {
	@Setter private PartiesCommandExecutor executor;
	
	public BukkitCommandImpl(String name, PartiesCommandExecutor executor) {
		super(name);
		this.executor = executor;
	}
	
	@Override
	public boolean execute(CommandSender commandSender, String cmd, String[] args) {
		return this.onCommand(commandSender,this, cmd, args);
	}
	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String cmd, String[] args) {
		return executor.onCommand(new BukkitUser(commandSender), cmd, args);
	}
	
	@Override
	public List<String> tabComplete(CommandSender commandSender, String alias, String[] args) {
		if (ConfigMain.COMMANDS_TABSUPPORT) {
			return executor.onTabComplete(new BukkitUser(commandSender), args);
		}
		return new ArrayList<>();
	}
}
