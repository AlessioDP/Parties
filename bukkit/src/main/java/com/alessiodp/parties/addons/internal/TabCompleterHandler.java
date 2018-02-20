package com.alessiodp.parties.addons.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class TabCompleterHandler implements TabCompleter {
	Parties plugin;
	
	public TabCompleterHandler(Parties instance) {
		plugin = instance;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase(ConfigMain.COMMANDS_CMD_PARTY)) {
				if (args.length == 1) {
					PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(((Player) sender).getUniqueId());
					LinkedHashMap<String, String> commands = pp.getAllowedCommands();
					List<String> list = new ArrayList<String>();
					for(String str : commands.keySet()) {
						if (str.equalsIgnoreCase(ConfigMain.COMMANDS_CMD_P))
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
