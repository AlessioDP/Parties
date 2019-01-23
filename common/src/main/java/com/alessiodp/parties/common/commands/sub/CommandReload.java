package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.ConsoleColor;

public class CommandReload extends AbstractCommand {
	
	public CommandReload(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
			
			if (pp != null && !sender.hasPermission(PartiesPermission.ADMIN_RELOAD.toString())) {
				pp.sendNoPermission(PartiesPermission.ADMIN_RELOAD);
				return false;
			}
			
			commandData.setPartyPlayer(pp);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		plugin.reloadConfiguration();
		
		if (pp != null) {
			pp.sendMessage(Messages.PARTIES_COMMON_CONFIGRELOAD);
			
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_RELOAD
					.replace("{player}", pp.getName()), true);
		} else {
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_RELOAD_CONSOLE, true, ConsoleColor.GREEN);
		}
	}
}
