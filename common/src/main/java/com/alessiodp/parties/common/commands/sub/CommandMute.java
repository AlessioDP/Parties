package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.PartiesUtils;

import java.util.List;

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
		Boolean mute = PartiesUtils.handleOnOffCommand(pp.isMuted(), commandData.getArgs());
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
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return PartiesUtils.tabCompleteOnOff(args);
	}
}
