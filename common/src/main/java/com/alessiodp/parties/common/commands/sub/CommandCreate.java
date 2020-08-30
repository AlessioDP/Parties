package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyPostCreateEvent;
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
import com.alessiodp.parties.common.utils.PasswordUtils;


public class CommandCreate extends PartiesSubCommand {
	private final String syntaxPassword;
	private final String syntaxFixed;
	private final String syntaxPasswordFixed;
	
	public CommandCreate(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.CREATE,
				PartiesPermission.USER_CREATE,
				ConfigMain.COMMANDS_CMD_CREATE,
				ConfigParties.ADDITIONAL_FIXED_ENABLE
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_NAME
		);
		
		syntaxPassword = String.format("%s <%s> [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_NAME,
				Messages.PARTIES_SYNTAX_PASSWORD
		);
		
		syntaxFixed = String.format("%s <%s> [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_NAME,
				ConfigMain.COMMANDS_SUB_FIXED
		);
		
		syntaxPasswordFixed = String.format("%s <%s> [%s] [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_NAME,
				Messages.PARTIES_SYNTAX_PASSWORD,
				ConfigMain.COMMANDS_SUB_FIXED
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_CREATE;
		help = Messages.HELP_MAIN_COMMANDS_CREATE;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (ConfigParties.ADDITIONAL_JOIN_ENABLE && ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE) {
			if (ConfigParties.ADDITIONAL_FIXED_ENABLE && user.hasPermission(PartiesPermission.ADMIN_CREATE_FIXED))
				return syntaxPasswordFixed;
			return syntaxPassword;
		} else if (ConfigParties.ADDITIONAL_FIXED_ENABLE && user.hasPermission(PartiesPermission.ADMIN_CREATE_FIXED))
			return syntaxFixed;
		return syntax;
	}
	
	@Override
	public String getConsoleSyntax() {
		if (ConfigParties.ADDITIONAL_JOIN_ENABLE && ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE)
			return syntaxPasswordFixed;
		return syntaxFixed;
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
			commandData.addPermission(PartiesPermission.ADMIN_CREATE_FIXED);
		} else if (!ConfigParties.ADDITIONAL_FIXED_ENABLE) {
			// Console can only create fixed parties
			sender.sendMessage(Messages.MAINCMD_CREATE_CONSOLE_MUST_FIXED, true);
			return false;
		}
		
		if (commandData.getArgs().length < 2) {
			sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
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
		
		if (!CensorUtils.checkAllowedCharacters(ConfigParties.GENERAL_NAME_ALLOWEDCHARS, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_ALLOWEDCHARS)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_INVALIDNAME);
			return;
		}
		
		if (CensorUtils.checkCensor(ConfigParties.GENERAL_NAME_CENSORREGEX, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_CENSORED)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CENSORED);
			return;
		}
		
		if (((PartiesPlugin) plugin).getPartyManager().existsParty(partyName)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMEEXISTS.replace("%party%", partyName));
			return;
		}
		
		boolean isFixed = false;
		String tag = null;
		String password = null;
		if (commandData.getArgs().length > 2) {
			if (commandData.getArgs().length == 3) {
				if (ConfigParties.ADDITIONAL_FIXED_ENABLE
						&& commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_FIXED)
						&& commandData.havePermission(PartiesPermission.ADMIN_CREATE_FIXED)) {
					isFixed = true;
				} else if (ConfigParties.ADDITIONAL_JOIN_ENABLE && ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE) {
					password = commandData.getArgs()[2];
				} else {
					sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", getSyntaxForUser(sender)));
					return;
				}
			} else if (commandData.getArgs().length == 4) {
				if (ConfigParties.ADDITIONAL_JOIN_ENABLE && ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE) {
					password = commandData.getArgs()[2];
				} else {
					sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", getSyntaxForUser(sender)));
					return;
				}
				
				if (ConfigParties.ADDITIONAL_FIXED_ENABLE
						&& commandData.getArgs()[3].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_FIXED)
						&& commandData.havePermission(PartiesPermission.ADMIN_CREATE_FIXED)) {
					isFixed = true;
				} else {
					sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", getSyntaxForUser(sender)));
					return;
				}
			} else {
				sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntaxForUser(sender)));
				return;
			}
			
			if (password != null) {
				if (!PasswordUtils.isValid(commandData.getArgs()[1])) {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_INVALID);
					return;
				}
				
				password = PasswordUtils.hashText(password);
			}
		}
		
		if (partyPlayer == null && !isFixed) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntaxForUser(sender)));
			return;
		}
		
		if (partyPlayer != null && ((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.CREATE, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		PartyImpl party;
		
		// Calling Pre API event
		IPartyPreCreateEvent partiesPreEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPreCreateEvent(partyPlayer, partyName, tag, isFixed);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreEvent);
		
		String newPartyName = partiesPreEvent.getPartyName();
		String newTag = partiesPreEvent.getPartyTag();
		boolean newIsFixed = partiesPreEvent.isFixed();
		if (!partiesPreEvent.isCancelled()) {
			party = ((PartiesPlugin) plugin).getPartyManager().initializeParty();
			party.create(newPartyName, newTag, newIsFixed ? null : partyPlayer);
			if (password != null)
				party.setPassword(password);
			
			if (newIsFixed) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CREATEDFIXED, party);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_CREATE_FIXED,
						sender.getName(), party.getName()), true);
			} else {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CREATED, party);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_CREATE,
						sender.getName(), party.getName()), true);
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
