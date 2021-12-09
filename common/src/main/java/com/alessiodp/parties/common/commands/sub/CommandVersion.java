package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public class CommandVersion extends PartiesSubCommand {
	
	public CommandVersion(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.VERSION,
				PartiesPermission.ADMIN_VERSION,
				ConfigMain.COMMANDS_SUB_VERSION,
				true
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_VERSION;
		help = Messages.HELP_MAIN_COMMANDS_VERSION;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		return handlePreRequisitesFull(commandData, null);
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command starts
		String version = plugin.getVersion();
		String newVersion = plugin.getAdpUpdater().getFoundVersion().isEmpty() ? version : plugin.getAdpUpdater().getFoundVersion();
		String message = version.equals(newVersion) ? Messages.MAINCMD_VERSION_UPDATED : Messages.MAINCMD_VERSION_OUTDATED;
		
		sendMessage(sender, partyPlayer, message
				.replace("%version%", version)
				.replace("%newversion%", newVersion)
				.replace("%platform%", plugin.getPlatform().getName()));
	}
}