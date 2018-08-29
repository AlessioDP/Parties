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

public class CommandMute extends AbstractCommand {
	
	public CommandMute(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.MUTE.toString())) {
			pp.sendNoPermission(PartiesPermission.MUTE);
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
		Boolean mute = pp.isMuted();
		if (commandData.getArgs().length > 1) {
			if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_ON))
				mute = false;
			else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_OFF))
				mute = true;
			else
				mute = null;
		} else {
			mute = !mute;
		}
		if (mute == null) {
			pp.sendMessage(Messages.ADDCMD_MUTE_WRONGCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		pp.setMuted(mute);
		pp.updatePlayer();
		
		if (mute) {
			pp.sendMessage(Messages.ADDCMD_MUTE_ON);
		} else {
			pp.sendMessage(Messages.ADDCMD_MUTE_OFF);
		}
		
		LoggerManager.log(LogLevel.MEDIUM,
				(mute ? Constants.DEBUG_CMD_MUTE_ON : Constants.DEBUG_CMD_MUTE_OFF)
						.replace("{player}", pp.getName()),
				true);
	}
}
