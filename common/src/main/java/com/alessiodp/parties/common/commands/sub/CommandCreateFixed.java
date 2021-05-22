package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;


public class CommandCreateFixed extends PartiesSubCommand {
	
	public CommandCreateFixed(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.CREATEFIXED,
				PartiesPermission.ADMIN_CREATE_FIXED,
				ConfigMain.COMMANDS_SUB_CREATEFIXED,
				true
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_NAME
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_CREATEFIXED;
		help = Messages.HELP_ADDITIONAL_COMMANDS_CREATEFIXED;
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
			
			if (partyPlayer.getPartyId() != null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		}
		
		if (commandData.getArgs().length != 2) {
			sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		String partyName = commandData.getArgs()[1];
		
		if (!CommandCreate.checkName(this, sender, partyPlayer, partyName))
			return;
		
		if (((PartiesPlugin) plugin).getPartyManager().existsParty(partyName)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMEEXISTS.replace("%party%", partyName));
			return;
		}
		
		// Command starts
		CommandCreate.createParty((PartiesPlugin) plugin, this, sender, partyPlayer, partyName, true);
	}
}
