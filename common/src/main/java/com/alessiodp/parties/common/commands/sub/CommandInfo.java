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

public class CommandInfo extends PartiesSubCommand {
	private final String syntaxOthers;
	
	public CommandInfo(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.INFO,
				PartiesPermission.USER_INFO,
				ConfigMain.COMMANDS_SUB_INFO,
				true
		);
		
		syntax = baseSyntax();
		
		syntaxOthers = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_INFO;
		help = Messages.HELP_MAIN_COMMANDS_INFO;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.USER_INFO_OTHERS))
			return syntaxOthers;
		return syntax;
	}
	
	@Override
	public String getConsoleSyntax() {
		return syntaxOthers;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			PartyImpl party = null;
			if (commandData.getArgs().length == 1) {
				party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
				if (party == null) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return false;
				}
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			((PartiesCommandData) commandData).setParty(party);
		} else {
			if (commandData.getArgs().length == 1) {
				sendMessage(sender, null, Messages.PARTIES_COMMON_PARTYNOTFOUND);
				return false;
			}
		}
		
		commandData.addPermission(PartiesPermission.USER_INFO_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		if (party == null && commandData.getArgs().length > 1) {
			if (!commandData.havePermission(PartiesPermission.USER_INFO_OTHERS)) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.USER_INFO_OTHERS);
				return;
			}
			
			party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
		}
		
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", commandData.getArgs()[1]));
			return;
		}
		
		// Command starts
		for (String line : Messages.MAINCMD_INFO_CONTENT) {
			line = ((PartiesPlugin) plugin).getMessageUtils().convertPlaceholders(line, partyPlayer, party, Messages.PARTIES_LIST_MISSING);
			
			sendMessage(sender, partyPlayer, line);
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_INFO,
				sender.getName(), party.getName() != null ? party.getName() : "_"), true);
	}
}