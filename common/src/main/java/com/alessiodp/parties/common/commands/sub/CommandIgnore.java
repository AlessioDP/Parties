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
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.UUID;

public class CommandIgnore extends PartiesSubCommand {
	
	public CommandIgnore(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.IGNORE,
				PartiesPermission.USER_IGNORE,
				ConfigMain.COMMANDS_SUB_IGNORE,
				false
		);
		
		syntax = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_IGNORE;
		help = Messages.HELP_MAIN_COMMANDS_IGNORE;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
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
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntax));
				return;
			}
			
			ignoredParty = commandData.getArgs()[1];
			
			if (!((PartiesPlugin) plugin).getPartyManager().existsParty(ignoredParty)) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", ignoredParty));
				return;
			}
		}
		
		// Command starts
		if (isPrompt) {
			StringBuilder builder = new StringBuilder();
			for (UUID uuid : partyPlayer.getIgnoredParties()) {
				if (builder.length() > 0) {
					builder.append(Messages.MAINCMD_IGNORE_LIST_SEPARATOR);
				}
				PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(uuid);
				if (party != null) {
					builder.append(Messages.MAINCMD_IGNORE_LIST_PARTYPREFIX)
							.append(party.getName());
				} else {
					partyPlayer.getIgnoredParties().remove(uuid);
				}
			}
			
			String ignores = builder.toString();
			if (partyPlayer.getIgnoredParties().size() == 0)
				ignores = Messages.MAINCMD_IGNORE_LIST_EMPTY;
			
			sendMessage(sender, partyPlayer, Messages.MAINCMD_IGNORE_LIST_HEADER
					.replace("%number%", Integer.toString(partyPlayer.getIgnoredParties().size())));
			sendMessage(sender, partyPlayer, ignores);
		} else {
			PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(ignoredParty);
			if (party != null) {
				if (partyPlayer.getIgnoredParties().contains(party.getId())) {
					// Remove
					partyPlayer.getIgnoredParties().remove(party.getId());
					sendMessage(sender, partyPlayer, Messages.MAINCMD_IGNORE_STOP
							.replace("%party%", ignoredParty));
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_IGNORE_START,
							partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
				} else {
					// Add
					partyPlayer.getIgnoredParties().add(party.getId());
					sendMessage(sender, partyPlayer, Messages.MAINCMD_IGNORE_START
							.replace("%party%", ignoredParty));
					
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_IGNORE_STOP,
							partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
				}
			}
		}
	}
}
