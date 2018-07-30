package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

public class CommandIgnore extends AbstractCommand {
	
	public CommandIgnore(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.IGNORE.toString())) {
			pp.sendNoPermission(PartiesPermission.IGNORE);
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
		boolean isPrompt = false;
		String ignoredParty = "";
		if (commandData.getArgs().length == 1) {
			// Shows ignore list
			isPrompt = true;
		} else {
			// Edit party ignore
			if (commandData.getArgs().length > 2) {
				pp.sendMessage(Messages.MAINCMD_IGNORE_WRONGCMD);
				return;
			}
			
			ignoredParty = commandData.getArgs()[1];
			
			if (!plugin.getPartyManager().existParty(ignoredParty)) {
				pp.sendMessage(Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", ignoredParty));
				return;
			}
		}
		
		/*
		 * Command starts
		 */
		if (isPrompt) {
			StringBuilder builder = new StringBuilder();
			for (String name : pp.getIgnoredParties()) {
				if (builder.length() > 0) {
					builder.append(Messages.MAINCMD_IGNORE_LIST_SEPARATOR);
				}
				builder.append(Messages.MAINCMD_IGNORE_LIST_PARTYPREFIX)
						.append(name);
			}
			
			String ignores = builder.toString();
			if (pp.getIgnoredParties().size() == 0)
				ignores = Messages.MAINCMD_IGNORE_LIST_EMPTY;
			
			pp.sendMessage(Messages.MAINCMD_IGNORE_LIST_HEADER
					.replace("%number%", Integer.toString(pp.getIgnoredParties().size())));
			pp.sendMessage(ignores);
		} else {
			if (pp.getIgnoredParties().contains(ignoredParty)) {
				// Remove
				pp.getIgnoredParties().remove(ignoredParty);
				pp.sendMessage(Messages.MAINCMD_IGNORE_STOP
						.replace("%party%", ignoredParty));
				
				LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_IGNORE_START
						.replace("{player}", pp.getName())
						.replace("{party}", ignoredParty), true);
			} else {
				// Add
				pp.getIgnoredParties().add(ignoredParty);
				pp.sendMessage(Messages.MAINCMD_IGNORE_START
						.replace("%party%", ignoredParty));
				
				LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_IGNORE_STOP
						.replace("{player}", pp.getName())
						.replace("{party}", ignoredParty), true);
			}
		}
	}
}
