package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import lombok.Getter;

public class CommandCreate extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandCreate(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(PartiesPermission.CREATE.toString())) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.CREATE);
				return false;
			}
			
			if (!partyPlayer.getPartyName().isEmpty()) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(PartiesPermission.ADMIN_CREATE_FIXED);
		} else if (!ConfigParties.FIXED_ENABLE) {
			// Console can only create fixed parties
			sender.sendMessage(Messages.PARTIES_COMMON_INVALIDCMD, true);
			return false;
		}
		
		if (commandData.getArgs().length < 2) {
			sender.sendMessage(Messages.MAINCMD_CREATE_WRONGCMD, true);
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
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMEEXISTS.replace("%party%", partyName));
			return;
		}
		
		boolean isFixed = false;
		if (ConfigParties.FIXED_ENABLE
				&& commandData.getArgs().length > 2
				&& commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_FIXED)) {
			if (commandData.havePermission(PartiesPermission.ADMIN_CREATE_FIXED))
				isFixed = true;
		}
		
		if (partyPlayer == null && !isFixed) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_WRONGCMD);
			return;
		}
		
		if (partyPlayer != null && ((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.CREATE, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		PartyImpl party;
		
		// Calling Pre API event
		IPartyPreCreateEvent partiesPreEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPreCreateEvent(partyPlayer, partyName, isFixed);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreEvent);
		
		String newPartyName = partiesPreEvent.getPartyName();
		boolean newIsFixed = partiesPreEvent.isFixed();
		if (!partiesPreEvent.isCancelled()) {
			party = ((PartiesPlugin) plugin).getPartyManager().initializeParty(newPartyName);
			party.create(newIsFixed ? null : partyPlayer);
			
			if (newIsFixed) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CREATEDFIXED, party);
				
				plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_CREATE_FIXED
						.replace("{player}", sender.getName())
						.replace("{party}", party.getName()), true);
			} else {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CREATED, party);
				
				plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_CREATE
						.replace("{player}", sender.getName())
						.replace("{party}", party.getName()), true);
			}
			
			// Calling API event
			IPartyPostCreateEvent partiesPostEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPostCreateEvent(partyPlayer, party);
			((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostEvent);
		} else {
			plugin.getLoggerManager().log(PartiesConstants.DEBUG_API_CREATEEVENT_DENY
					.replace("{party}", partyName)
					.replace("{player}", sender.getName()), true);
		}
	}
}
