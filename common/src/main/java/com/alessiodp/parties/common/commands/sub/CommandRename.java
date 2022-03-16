package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.api.events.common.party.IPartyPreRenameEvent;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.CooldownManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.CensorUtils;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.RankPermission;
import org.jetbrains.annotations.NotNull;

public class CommandRename extends PartiesSubCommand {
	private final String syntaxOthers;
	private final String syntaxOthersConsole;
	
	public CommandRename(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.RENAME,
				PartiesPermission.USER_RENAME,
				ConfigMain.COMMANDS_SUB_RENAME,
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
	public @NotNull String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.ADMIN_RENAME_OTHERS))
			return syntaxOthers;
		return syntax;
	}
	
	@Override
	public @NotNull String getConsoleSyntax() {
		return syntaxOthersConsole;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = getPlugin().getPlayerManager().getPlayer(sender.getUUID());
			
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
				
				// Not using handlePreRequesitesFull for this
				if (!getPlugin().getRankManager().checkPlayerRankAlerter(partyPlayer, RankPermission.ADMIN_RENAME))
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
			commandData.addPermission(PartiesPermission.ADMIN_COOLDOWN_RENAME_BYPASS);
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
				party = getPlugin().getPartyManager().getParty(partyPlayer.getPartyId());
			
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return;
			}
			
			if (!getPlugin().getRankManager().checkPlayerRankAlerter(partyPlayer, RankPermission.ADMIN_RENAME))
				return;
			
			partyName = commandData.getArgs()[1];
		} else {
			// 3 arguments - another party
			if (!sender.isPlayer() || commandData.havePermission(PartiesPermission.ADMIN_RENAME_OTHERS)) {
				party = getPlugin().getPartyManager().getParty(commandData.getArgs()[1]);
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
		if (getPlugin().getPartyManager().existsParty(partyName)) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_CREATE_NAMEEXISTS
					.replace("%party%", partyName));
			return;
		}
		
		boolean mustStartCooldown = false;
		if (ConfigParties.GENERAL_NAME_RENAME_COOLDOWN > 0 && !commandData.havePermission(PartiesPermission.ADMIN_COOLDOWN_RENAME_BYPASS)) {
			mustStartCooldown = true;
			long remainingCooldown = getPlugin().getCooldownManager().canAction(CooldownManager.Action.RENAME, party.getId(), ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
			
			if (remainingCooldown > 0) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_RENAME_COOLDOWN
						.replace("%seconds%", String.valueOf(remainingCooldown)));
				return;
			}
		}
		
		if (partyPlayer != null && getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.RENAME, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		if (mustStartCooldown) {
			getPlugin().getCooldownManager().startAction(CooldownManager.Action.RENAME, party.getId(), ConfigParties.GENERAL_NAME_RENAME_COOLDOWN);
		}
		
		// Command starts
		String oldPartyName = party.getName();
		
		// Calling API event
		IPartyPreRenameEvent partiesPreRenameEvent = getPlugin().getEventManager().preparePartyPreRenameEvent(party, party.getName(), partyName, partyPlayer, commandData.getArgs().length > 2);
		getPlugin().getEventManager().callEvent(partiesPreRenameEvent);
		
		partyName = partiesPreRenameEvent.getNewPartyName();
		if (!partiesPreRenameEvent.isCancelled()) {
			party.rename(partyName, partyPlayer, commandData.getArgs().length > 2);
			
			sendMessage(sender, partyPlayer, Messages.MAINCMD_RENAME_RENAMED
					.replace("%old%", CommonUtils.getOr(oldPartyName, "null")), party);
			party.broadcastMessage(Messages.MAINCMD_RENAME_BROADCAST
					.replace("%old%", CommonUtils.getOr(oldPartyName, "null")), partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_RENAME, sender.getName(), oldPartyName, party.getName()), true);
		} else {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_RENAMEEVENT_DENY, party.getName(), oldPartyName, sender.getUUID().toString()), true);
		}
	}
}