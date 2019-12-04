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
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

public class CommandInfo extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandInfo(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(PartiesPermission.INFO.toString())) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.INFO);
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
		
		commandData.addPermission(PartiesPermission.INFO_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		if (party == null && commandData.getArgs().length > 1) {
			if (!commandData.havePermission(PartiesPermission.INFO_OTHERS)) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.INFO_OTHERS);
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
			line = ((PartiesPlugin) plugin).getMessageUtils().convertPartyPlaceholders(line, party, Messages.PARTIES_LIST_MISSING);
			
			sendMessage(sender, partyPlayer, line);
		}
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_INFO
				.replace("{player}", sender.getName())
				.replace("{party}", party.getName()), true);
	}
}