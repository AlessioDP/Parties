package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.CensorUtils;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;


public class CommandCreate extends PartiesSubCommand {

	public CommandCreate(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.CREATE,
				PartiesPermission.USER_CREATE,
				ConfigMain.COMMANDS_SUB_CREATE,
				false
		);
		
		if (ConfigParties.GENERAL_NAME_DYNAMIC_ENABLE) {
			if (ConfigParties.GENERAL_NAME_DYNAMIC_ALLOW_IN_CREATE) {
				syntax = String.format("%s [%s]",
						baseSyntax(),
						Messages.PARTIES_SYNTAX_NAME
				);
			} else {
				syntax = baseSyntax();
			}
		} else {
			syntax = String.format("%s <%s>",
					baseSyntax(),
					Messages.PARTIES_SYNTAX_NAME
			);
		}
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_CREATE;
		help = Messages.HELP_MAIN_COMMANDS_CREATE;
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
		
		if (partyPlayer.isInParty()) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
			return false;
		}
		
		if (commandData.getArgs().length > 2) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
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
		String partyName;
		
		if (commandData.getArgs().length > 1) {
			if (!ConfigParties.GENERAL_NAME_DYNAMIC_ENABLE || ConfigParties.GENERAL_NAME_DYNAMIC_ALLOW_IN_CREATE) {
				partyName = commandData.getArgs()[1];
				
				if (!checkName(this, sender, partyPlayer, partyName))
					return;
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntax));
				return;
			}
		} else {
			if (!ConfigParties.GENERAL_NAME_DYNAMIC_ENABLE) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntax));
				return;
			}
			partyName = ((PartiesPlugin) plugin).getMessageUtils().convertPlaceholders(ConfigParties.GENERAL_NAME_DYNAMIC_FORMAT, partyPlayer, null);
		}
		
		if (((PartiesPlugin) plugin).getPartyManager().existsParty(partyName)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMEEXISTS.replace("%party%", partyName));
			return;
		}
		
		if (partyPlayer != null && ((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.CREATE, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		createParty((PartiesPlugin) plugin, this, sender, partyPlayer, partyName, false);
	}
	
	public static boolean checkName(PartiesSubCommand subCommand, User sender, PartyPlayerImpl partyPlayer, String partyName) {
		if (partyName.length() > ConfigParties.GENERAL_NAME_MAXLENGTH) {
			subCommand.sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMETOOLONG);
			return false;
		}
		
		if (partyName.length() < ConfigParties.GENERAL_NAME_MINLENGTH) {
			subCommand.sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMETOOSHORT);
			return false;
		}
		
		if (!CensorUtils.checkAllowedCharacters(ConfigParties.GENERAL_NAME_ALLOWEDCHARS, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_ALLOWEDCHARS)) {
			subCommand.sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_INVALIDNAME);
			return false;
		}
		
		if (CensorUtils.checkCensor(ConfigParties.GENERAL_NAME_CENSORREGEX, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_CENSORED)) {
			subCommand.sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CENSORED);
			return false;
		}
		return true;
	}
	
	public static PartyImpl createParty(PartiesPlugin plugin, PartiesSubCommand subCommand, User sender, PartyPlayerImpl partyPlayer, String partyName, boolean fixed) {
		PartyImpl ret = null;
		IPartyPreCreateEvent partiesPreEvent = plugin.getEventManager().preparePartyPreCreateEvent(partyPlayer, partyName, fixed);
		plugin.getEventManager().callEvent(partiesPreEvent);
		
		String newPartyName = partiesPreEvent.getPartyName();
		boolean isFixed = partiesPreEvent.isFixed();
		if (!partiesPreEvent.isCancelled() && (isFixed || partyPlayer != null)) {
			PartyImpl party = plugin.getPartyManager().initializeParty();
			party.create(newPartyName, isFixed ? null : partyPlayer, partyPlayer);
			
			if (isFixed) {
				subCommand.sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CREATEDFIXED, party);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_CREATE_FIXED,
						sender.getName(), party.getName() != null ? party.getName() : "_"), true);
			} else {
				subCommand.sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CREATED, party);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_CREATE,
						sender.getName(), party.getName() != null ? party.getName() : "_"), true);
			}
			
			ret = party;
		} else {
			plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_API_CREATEEVENT_DENY, partyName, sender.getName(), sender.getUUID().toString()), true);
		}
		return ret;
	}
}
