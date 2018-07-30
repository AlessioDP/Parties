package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.interfaces.Color;


public class CommandColor extends AbstractCommand {
	
	public CommandColor(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.COLOR.toString())) {
			pp.sendNoPermission(PartiesPermission.COLOR);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_COLOR))
			return false;
		
		if (commandData.getArgs().length > 2) {
			pp.sendMessage(Messages.ADDCMD_COLOR_WRONGCMD);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		if (commandData.getArgs().length == 1) {
			// Automatically pp.sendMessage put color placeholders
			if (party.getColor() != null)
				pp.sendMessage(Messages.ADDCMD_COLOR_INFO);
			else
				pp.sendMessage(Messages.ADDCMD_COLOR_EMPTY);
			return;
		}
		
		boolean isRemove = false;
		Color color = null;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			color = plugin.getColorManager().searchColorByCommand(commandData.getArgs()[1]);
			if (color == null) {
				// Color doesn't exist
				pp.sendMessage(Messages.ADDCMD_COLOR_WRONGCOLOR);
				return;
			}
			
			if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.COLOR, pp, commandData.getCommandLabel(), commandData.getArgs()))
				return;
		}
		
		/*
		 * Command starts
		 */
		party.setColor(color);
		party.updateParty();
		party.callChange();
		
		if (isRemove) {
			pp.sendMessage(Messages.ADDCMD_COLOR_REMOVED);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_COLOR_REM
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		} else {
			pp.sendMessage(Messages.ADDCMD_COLOR_CHANGED, party);
			party.sendBroadcast(pp, Messages.ADDCMD_COLOR_BROADCAST);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_COLOR
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName())
					.replace("{value}", color.getName()), true);
		}
	}
}
