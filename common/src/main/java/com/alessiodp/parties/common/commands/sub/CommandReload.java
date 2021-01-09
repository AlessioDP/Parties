package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public class CommandReload extends PartiesSubCommand {
	
	public CommandReload(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.RELOAD,
				PartiesPermission.ADMIN_RELOAD,
				ConfigMain.COMMANDS_CMD_RELOAD,
				true
		);
		
		syntax = baseSyntax();
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_RELOAD;
		help = Messages.HELP_MAIN_COMMANDS_RELOAD;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			// If the sender is a player
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			if (!sender.hasPermission(permission)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		plugin.reloadConfiguration();
		
		sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_CONFIGRELOAD);
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_RELOADED,
				sender.getName()), true);
	}
}
