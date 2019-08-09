package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

public class CommandReload extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandReload(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			// If the sender is a player
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			if (!sender.hasPermission(PartiesPermission.ADMIN_RELOAD.toString())) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.ADMIN_RELOAD);
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
		
		if (partyPlayer != null)
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_RELOAD
					.replace("{player}", sender.getName()), true);
		else
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_RELOAD_CONSOLE, true);
		
		plugin.reloadConfiguration();
		
		if (partyPlayer != null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_CONFIGRELOAD);
			
			plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_RELOADED
					.replace("{player}", sender.getName()), true);
		} else {
			plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_RELOADED_CONSOLE, true);
		}
	}
}
