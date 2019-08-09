package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandHelp(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.HELP.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.HELP);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_HELP
				.replace("{player}", sender.getName())
				.replace("{page}", commandData.getArgs().length > 1 ? commandData.getArgs()[1] : ""), true);
		
		// Command starts
		// Get all allowed commands
		List<String> list = new ArrayList<>();
		for (ADPCommand cmd : partyPlayer.getAllowedCommands()) {
			if (mainCommand.getEnabledSubCommands().contains(cmd))
				list.add(cmd.getHelp());
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
