package com.alessiodp.parties.commands.list;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandHelp implements ICommand {
	private Parties plugin;
	 
	public CommandHelp(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.HELP.toString())) {
			pp.sendNoPermission(PartiesPermission.HELP);
			return;
		}
		
		/*
		 * Command starts
		 */
		// Get all allowed commands
		List<String> list = new ArrayList<String>();
		LinkedHashMap<String, String> commands = pp.getAllowedCommands();
		for(Entry<String, String> entry : commands.entrySet()) {
			list.add(entry.getValue());
		}
		
		// Split commands per page
		int page = 1;
		int maxpages;
		if (list.size() == 0)
			maxpages = 1;
		else if ((list.size() % ConfigMain.COMMANDS_HELP_PERPAGE) == 0)
			maxpages = list.size() / ConfigMain.COMMANDS_HELP_PERPAGE;
		else
			maxpages = (list.size() / ConfigMain.COMMANDS_HELP_PERPAGE) + 1;
		
		if (args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException ex) {}
			
			if (page > maxpages || page < 1)
				page = maxpages;
		}
		
		// Start priting
		int commandNumber = 0;
		String message = Messages.HELP_HEADER
				.replace("%page%", Integer.toString(page))
				.replace("%maxpages%", Integer.toString(maxpages)) + "\n";
		for (String string : list) {
			int currentChoosenPage = (page-1) * ConfigMain.COMMANDS_HELP_PERPAGE;
			if (commandNumber >= currentChoosenPage
					&& commandNumber < currentChoosenPage + ConfigMain.COMMANDS_HELP_PERPAGE) {
				message += string + "\n";
			}
			commandNumber++;
		}
		message += Messages.HELP_FOOTER
				.replace("%page%", Integer.toString(page))
				.replace("%maxpages%", Integer.toString(maxpages));
		
		pp.sendMessage(message);
	}
}
