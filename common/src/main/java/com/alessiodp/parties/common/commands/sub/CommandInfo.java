package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.jetbrains.annotations.NotNull;

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
	public @NotNull String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.USER_INFO_OTHERS))
			return syntaxOthers;
		return syntax;
	}
	
	@Override
	public @NotNull String getConsoleSyntax() {
		return syntaxOthers;
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		boolean ret = handlePreRequisitesFull(commandData, null, 1, 2);
		if (ret) {
			commandData.addPermission(PartiesPermission.USER_INFO_OTHERS);
		}
		return ret;
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party;
		
		// Command handling
		if (commandData.getArgs().length == 1 && sender.isPlayer()) {
			party = getPlugin().getPartyManager().getPartyOfPlayer(partyPlayer);
		} else if (commandData.getArgs().length == 2 && commandData.havePermission(PartiesPermission.USER_INFO_OTHERS)) {
			party = getPlugin().getPartyManager().getParty(commandData.getArgs()[1]);
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
			return;
		}
		
		if (party == null) {
			if (commandData.getArgs().length > 1)
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", commandData.getArgs()[1]));
			else
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		// Command starts
		boolean ownParty = partyPlayer != null && party.getMembers().contains(partyPlayer.getPlayerUUID());
		for (String line : ownParty ? Messages.MAINCMD_INFO_CONTENT_OWN : Messages.MAINCMD_INFO_CONTENT_OTHER) {
			line = getPlugin().getMessageUtils().convertPlaceholders(line, partyPlayer, party, Messages.PARTIES_LIST_MISSING);
			
			sendMessage(sender, partyPlayer, line);
		}
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_INFO,
				sender.getName(), party.getName() != null ? party.getName() : "_"), true);
	}
}