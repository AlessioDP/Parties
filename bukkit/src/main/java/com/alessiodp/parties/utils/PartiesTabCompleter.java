package com.alessiodp.parties.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.ThePlayer;

public class PartiesTabCompleter implements TabCompleter {
	Parties plugin;
	
	public PartiesTabCompleter(Parties instance) {
		plugin = instance;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase(Variables.command_party)) {
				if (args.length == 1) {
					ThePlayer tp = plugin.getPlayerHandler().getPlayer(((Player) sender).getUniqueId());
					LinkedHashMap<String, String> commands = tp.getAllowedCommands();
					List<String> list = new ArrayList<String>();
					for(String str : commands.keySet()) {
						if (str.equalsIgnoreCase(Variables.command_p))
							continue;
						list.add(str);
					}
					for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
						String str = iterator.next();
						if(!str.startsWith(args[0]))
							iterator.remove();
					}
					return list;
				}
			}
		}
		return null;
	}

}
