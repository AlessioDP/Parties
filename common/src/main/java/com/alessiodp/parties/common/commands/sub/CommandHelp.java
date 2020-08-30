package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPExecutableCommand;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandHelp extends PartiesSubCommand {
	
	public CommandHelp(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.HELP,
				PartiesPermission.USER_HELP,
				ConfigMain.COMMANDS_CMD_HELP,
				false
		);
		
		syntax = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PAGE
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_HELP;
		help = Messages.HELP_MAIN_COMMANDS_HELP;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command starts
		// Get all allowed commands
		LinkedList<String> list = new LinkedList<>();
		
		List<ADPCommand> allowedCommands = partyPlayer.getAllowedCommands();
		for(Map.Entry<ADPCommand, ADPExecutableCommand> e : plugin.getCommandManager().getOrderedCommands().entrySet()) {
			if (allowedCommands.contains(e.getKey()) && e.getValue().isListedInHelp()) {
				list.add(e.getValue().getHelp()
						.replace("%syntax%", e.getValue().getSyntaxForUser(sender))
						.replace("%description%", e.getValue().getDescription())
						.replace("%run_command%", e.getValue().getRunCommand())
						.replace("%perform_command%", Messages.HELP_PERFORM_COMMAND));
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
		sendMessage(sender, partyPlayer, Messages.HELP_HEADER
				.replace("%page%", Integer.toString(page))
				.replace("%maxpages%", Integer.toString(maxpages)));
		
		for (String string : list) {
			int currentChoosenPage = (page-1) * ConfigMain.COMMANDS_HELP_PERPAGE;
			if (commandNumber >= currentChoosenPage
					&& commandNumber < currentChoosenPage + ConfigMain.COMMANDS_HELP_PERPAGE) {
				sendMessage(sender, partyPlayer, string);
			}
			commandNumber++;
		}
		
		sendMessage(sender, partyPlayer, Messages.HELP_FOOTER
				.replace("%page%", Integer.toString(page))
				.replace("%maxpages%", Integer.toString(maxpages)));
	}
}
