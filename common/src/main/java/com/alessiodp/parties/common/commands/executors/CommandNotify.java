package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

public class CommandNotify extends AbstractCommand {
	
	public CommandNotify(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.NOTIFY.toString())) {
			pp.sendNoPermission(PartiesPermission.NOTIFY);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		Boolean notify = pp.isPreventNotify();
		if (commandData.getArgs().length > 1) {
			if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_ON))
				notify = false;
			else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_OFF))
				notify = true;
			else
				notify = null;
		} else {
			notify = !notify;
		}
		if (notify == null) {
			pp.sendMessage(Messages.ADDCMD_NOTIFY_WRONGCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		pp.setPreventNotify(notify);
		pp.updatePlayer();
		
		if (notify) {
			pp.sendMessage(Messages.ADDCMD_NOTIFY_ON);
		} else {
			pp.sendMessage(Messages.ADDCMD_NOTIFY_OFF);
		}
		
		LoggerManager.log(LogLevel.MEDIUM,
				(notify ? Constants.DEBUG_CMD_NOTIFY_ON : Constants.DEBUG_CMD_NOTIFY_OFF)
						.replace("{player}", pp.getName()),
				true);
	}
}
