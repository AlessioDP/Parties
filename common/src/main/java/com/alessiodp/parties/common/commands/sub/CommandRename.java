package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyRenameEvent;
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
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public class CommandRename extends PartiesSubCommand {
	private final String syntaxOthers;
	private final String syntaxOthersConsole;
	
	public CommandRename(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.RENAME,
				PartiesPermission.USER_RENAME,
				ConfigMain.COMMANDS_CMD_RENAME,
				true
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_NAME
		);
		
		syntaxOthers = String.format("%s [%s] <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY,
				Messages.PARTIES_SYNTAX_NAME
		);
		
		syntaxOthersConsole = String.format("%s <%s> <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY,
				Messages.PARTIES_SYNTAX_NAME
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_RENAME;
		help = Messages.HELP_MAIN_COMMANDS_RENAME;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS))
			return syntaxOthers;
		return syntax;
	}
	
	@Override
	public String getConsoleSyntax() {
		return syntaxOthersConsole;
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
			
			if (!sender.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS)) {
				if (!partyPlayer.isInParty()) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return false;
				}
				
				if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_ADMIN_RENAME))
					return false;
				
				if (commandData.getArgs().length != 2) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", syntax));
					return false;
				}
			}
			
			if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntax));
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(PartiesPermission.ADMIN_RENAME_OTHERS);
		} else {
			if (commandData.getArgs().length != 3) {
				sendMessage(sender, null, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntax));
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
			if (partyPlayer.isInParty())
				party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId());
			
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
		if (!CensorUtils.checkAllowedCharacters(ConfigParties.GENERAL_NAME_ALLOWEDCHARS, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_ALLOWEDCHARS)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_INVALIDNAME);
			return;
		}
		if (CensorUtils.checkCensor(ConfigParties.GENERAL_NAME_CENSORREGEX, partyName, PartiesConstants.DEBUG_CMD_CREATE_REGEXERROR_CENSORED)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_CENSORED);
			return;
		}
		if (((PartiesPlugin) plugin).getPartyManager().existsParty(partyName)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMEEXISTS
					.replace("%party%", partyName));
			return;
		}
		
		boolean mustStartCooldown = false;
		if (ConfigParties.GENERAL_NAME_RENAME_COOLDOWN > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_RENAME_BYPASS)) {
			mustStartCooldown = true;
			long remainingCooldown = ((PartiesPlugin) plugin).getCooldownManager().canRename(party, ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_RENAME_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
		}
		
		if (partyPlayer != null && ((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.RENAME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown) {
			((PartiesPlugin) plugin).getCooldownManager().startRenameCooldown(party, ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
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
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_RENAME, sender.getName(), oldPartyName, party.getName()), true);
		} else {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_RENAMEEVENT_DENY, party.getName(), oldPartyName, sender.getUUID().toString()), true);
		}
	}
}