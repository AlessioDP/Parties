package com.alessiodp.parties.commands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandHelp implements CommandInterface {
	private Parties plugin;
	 
	public CommandHelp(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		
		if (!p.hasPermission(PartiesPermissions.HELP.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.HELP.toString()));
			return true;
		}
		
		List<String> list = new ArrayList<String>();
		LinkedHashMap<String, String> commands = tp.getAllowedCommands();
		for(Entry<String, String> entry : commands.entrySet()) {
			list.add(entry.getValue());
		}
		
		int page = 1;
		int maxpages = (list.size() % Variables.commandsperpage) == 0 ? list.size() / Variables.commandsperpage : (list.size() / Variables.commandsperpage) + 1;
		if (args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException ex) {
				page = 1;
			}
			if (page > maxpages || page < 1)
				page = maxpages;
		}
		if (list.size() == 0)
			maxpages=1;
		int c=0;
		
		String message = Messages.help_header
				.replace("%page%", Integer.toString(page))
				.replace("%maxpages%", Integer.toString(maxpages)) + "\n";
		for (String string : list) {
			if (c >= (page-1)*Variables.commandsperpage && c < (page-1)*Variables.commandsperpage+Variables.commandsperpage) {
				message += string + "\n";
			}
			c++;
		}
		tp.sendMessage(message);
		return true;
	}
}
