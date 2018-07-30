package com.alessiodp.parties.bukkit.addons.internal;

import java.util.ArrayList;
import java.util.List;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCompleterHandler implements TabCompleter {
	private PartiesPlugin plugin;
	
	public TabCompleterHandler(PartiesPlugin instance) {
		plugin = instance;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender instanceof Player) {
			if (cmd.getName().equalsIgnoreCase(ConfigMain.COMMANDS_CMD_PARTY)) {
				if (args.length == 1) {
					PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(((Player) sender).getUniqueId());
					String tabWord = args[0].toLowerCase();
					
					List<String> list = new ArrayList<>();
					for(PartiesCommand pc : pp.getAllowedCommands()) {
						if (!pc.getType().equals("P") && pc.getCommand().toLowerCase().startsWith(tabWord)) {
							list.add(pc.getCommand());
						}
					}
					return list;
				}
			}
		}
		return null;
	}

}
