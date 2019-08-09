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

public class CommandIgnore extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandIgnore(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.IGNORE.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.IGNORE);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		boolean isPrompt = false;
		String ignoredParty = "";
		if (commandData.getArgs().length == 1) {
			// Shows ignore list
			isPrompt = true;
		} else {
			// Edit party ignore
			if (commandData.getArgs().length > 2) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_IGNORE_WRONGCMD);
				return;
			}
			
			ignoredParty = commandData.getArgs()[1];
			
			if (!((PartiesPlugin) plugin).getPartyManager().existParty(ignoredParty)) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", ignoredParty));
				return;
			}
		}
		
		// Command starts
		if (isPrompt) {
			StringBuilder builder = new StringBuilder();
			for (String name : partyPlayer.getIgnoredParties()) {
				if (builder.length() > 0) {
					builder.append(Messages.MAINCMD_IGNORE_LIST_SEPARATOR);
				}
				builder.append(Messages.MAINCMD_IGNORE_LIST_PARTYPREFIX)
						.append(name);
			}
			
			String ignores = builder.toString();
			if (partyPlayer.getIgnoredParties().size() == 0)
				ignores = Messages.MAINCMD_IGNORE_LIST_EMPTY;
			
			sendMessage(sender, partyPlayer, Messages.MAINCMD_IGNORE_LIST_HEADER
					.replace("%number%", Integer.toString(partyPlayer.getIgnoredParties().size())));
			sendMessage(sender, partyPlayer, ignores);
		} else {
			if (partyPlayer.getIgnoredParties().contains(ignoredParty)) {
				// Remove
				partyPlayer.getIgnoredParties().remove(ignoredParty);
				sendMessage(sender, partyPlayer, Messages.MAINCMD_IGNORE_STOP
						.replace("%party%", ignoredParty));
				
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_IGNORE_START
						.replace("{player}", sender.getName())
						.replace("{party}", ignoredParty), true);
			} else {
				// Add
				partyPlayer.getIgnoredParties().add(ignoredParty);
				sendMessage(sender, partyPlayer, Messages.MAINCMD_IGNORE_START
						.replace("%party%", ignoredParty));
				
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_IGNORE_STOP
						.replace("{player}", sender.getName())
						.replace("{party}", ignoredParty), true);
			}
		}
	}
}
