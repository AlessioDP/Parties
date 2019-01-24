package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp extends AbstractCommand {
	
	public CommandHelp(PartiesPlugin instance) {
		super(instance);
	}

	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.HELP.toString())) {
			pp.sendNoPermission(PartiesPermission.HELP);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command starts
		 */
		// Get all allowed commands
		List<String> list = new ArrayList<>();
		List<PartiesCommand> allowedCommands = pp.getAllowedCommands();
		for(PartiesCommand pc : plugin.getCommandManager().getOrderedCommands()) {
			if (allowedCommands.contains(pc)) {
				list.add(pc.getHelp());
			}
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
		
		if (commandData.getArgs().length > 1) {
			try {
				page = Integer.parseInt(commandData.getArgs()[1]);
			} catch(NumberFormatException ignored) {}
			
			if (page > maxpages || page < 1)
				page = maxpages;
		}
		
		// Start printing
		int commandNumber = 0;
		pp.sendMessage(Messages.HELP_HEADER
				.replace("%page%", Integer.toString(page))
				.replace("%maxpages%", Integer.toString(maxpages)));
		for (String string : list) {
			int currentChoosenPage = (page-1) * ConfigMain.COMMANDS_HELP_PERPAGE;
			if (commandNumber >= currentChoosenPage
					&& commandNumber < currentChoosenPage + ConfigMain.COMMANDS_HELP_PERPAGE) {
				pp.sendMessage(string);
			}
			commandNumber++;
		}
		pp.sendMessage(Messages.HELP_FOOTER
				.replace("%page%", Integer.toString(page))
				.replace("%maxpages%", Integer.toString(maxpages)));
	}
}
