package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyRenameEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

public class CommandRename extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandRename(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(PartiesPermission.RENAME.toString())) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.RENAME);
				return false;
			}
			
			if (!sender.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS.toString())) {
				if (partyPlayer.getPartyName().isEmpty()) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return false;
				}
				
				if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_ADMIN_RENAME))
					return false;
				
				if (commandData.getArgs().length != 2) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_RENAME_WRONGCMD);
					return false;
				}
			} else {
				if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_RENAME_WRONGCMD_ADMIN);
					return false;
				}
			}
			
			if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_RENAME_WRONGCMD);
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(PartiesPermission.ADMIN_RENAME_OTHERS);
		} else {
			if (commandData.getArgs().length != 3) {
				sendMessage(sender, null, Messages.MAINCMD_RENAME_WRONGCMD_ADMIN);
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		PartyImpl party = null;
		String partyName;
		
		if (commandData.getArgs().length == 2) {
			// 2 arguments - own party
			if (!partyPlayer.getPartyName().isEmpty())
				party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyName());
			
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return;
			}
			
			if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_ADMIN_RENAME))
				return;
			
			partyName = commandData.getArgs()[1];
		} else {
			// 3 arguments - another party
			if (!sender.isPlayer() || commandData.havePermission(PartiesPermission.ADMIN_RENAME_OTHERS)) {
				party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
			}
			
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
						.replace("%party%", commandData.getArgs()[1]));
				return;
			}
			
			partyName = commandData.getArgs()[2];
		}
		
		if (partyName.length() > ConfigParties.GENERAL_NAME_MAXLENGTH) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMETOOLONG);
			return;
		}
		if (partyName.length() < ConfigParties.GENERAL_NAME_MINLENGTH) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMETOOSHORT);
			return;
		}
		if (!((PartiesPlugin) plugin).getCensorUtils().checkAllowedCharacters(ConfigParties.GENERAL_NAME_ALLOWEDCHARS, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_AC)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_INVALIDNAME);
			return;
		}
		if (((PartiesPlugin) plugin).getCensorUtils().checkCensor(ConfigParties.GENERAL_NAME_CENSORREGEX, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_CEN)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CENSORED);
			return;
		}
		if (((PartiesPlugin) plugin).getPartyManager().existParty(partyName)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMEEXISTS
					.replace("%party%", partyName));
			return;
		}
		
		// Command starts
		String oldPartyName = party.getName();
		
		// Calling API event
		IPartyRenameEvent partiesRenameEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyRenameEvent(party, partyName, partyPlayer, commandData.getArgs().length > 2);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesRenameEvent);
		
		partyName = partiesRenameEvent.getNewPartyName();
		if (!partiesRenameEvent.isCancelled()) {
			party.rename(partyName);
			
			sendMessage(sender, partyPlayer, Messages.MAINCMD_RENAME_RENAMED
					.replace("%old%", oldPartyName), party);
			party.broadcastMessage(Messages.MAINCMD_RENAME_BROADCAST
					.replace("%old%", oldPartyName), partyPlayer);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_RENAME
					.replace("{player}", sender.getName())
					.replace("{value}", oldPartyName)
					.replace("{party}", party.getName()), true);
		} else {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_RENAMEEVENT_DENY
					.replace("{party}", partyName)
					.replace("{player}", sender.getName()), true);
		}
	}
}