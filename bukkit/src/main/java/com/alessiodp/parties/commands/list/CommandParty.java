package com.alessiodp.parties.commands.list;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandParty implements ICommand {
	private Parties plugin;
	 
	public CommandParty(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.HELP.toString())) {
			pp.sendNoPermission(PartiesPermission.HELP);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		/*
		 * Command starts
		 */
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
