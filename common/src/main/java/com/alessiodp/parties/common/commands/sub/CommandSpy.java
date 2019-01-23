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

public class CommandSpy extends AbstractCommand {
	
	public CommandSpy(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
			pp.sendNoPermission(PartiesPermission.ADMIN_SPY);
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
		Boolean spy = PartiesUtils.handleOnOffCommand(pp.isSpy(), commandData.getArgs());
		if (spy == null) {
			pp.sendMessage(Messages.MAINCMD_SPY_WRONGCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		pp.setSpy(spy);
		pp.updatePlayer();
		
		if (spy) {
			plugin.getSpyManager().addSpy(pp.getPlayerUUID());
			pp.sendMessage(Messages.MAINCMD_SPY_ENABLED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SPY_ENABLE
					.replace("{player}", pp.getName()), true);
		} else {
			plugin.getSpyManager().removeSpy(pp.getPlayerUUID());
			pp.sendMessage(Messages.MAINCMD_SPY_DISABLED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SPY_DISABLE
					.replace("{player}", pp.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return PartiesUtils.tabCompleteOnOff(args);
	}
}
