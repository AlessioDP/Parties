package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PasswordUtils;
import org.jetbrains.annotations.NotNull;

public class CommandJoin extends PartiesSubCommand {
	private final String syntaxPassword;
	
	public CommandJoin(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.JOIN,
				PartiesPermission.USER_JOIN,
				ConfigMain.COMMANDS_SUB_JOIN,
				false
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		syntaxPassword = String.format("%s <%s> [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY,
				Messages.PARTIES_SYNTAX_PASSWORD
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_JOIN;
		help = Messages.HELP_ADDITIONAL_COMMANDS_JOIN;
	}
	
	@Override
	public @NotNull String getSyntaxForUser(User user) {
		if (ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE)
			return syntaxPassword;
		return syntax;
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		boolean ret = handlePreRequisitesFull(commandData, false, 2, 3);
		if (ret && ((PartiesCommandData) commandData).getPartyPlayer() != null) {
			commandData.addPermission(PartiesPermission.ADMIN_JOIN_BYPASS);
		}
		return ret;
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		String partyName = commandData.getArgs()[1];
		PartyImpl party = getPlugin().getPartyManager().getParty(partyName);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", partyName));
			return;
		}
		
		if (commandData.getArgs().length == 2) {
			if (!commandData.havePermission(PartiesPermission.ADMIN_JOIN_BYPASS)) {
				if (ConfigParties.ADDITIONAL_JOIN_OPENCLOSE_ENABLE && !party.isOpen()) {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_OPENCLOSE_CANNOT_JOIN);
					return;
				}
				
				if (ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE && party.getPassword() != null) {
					sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_PASSWORD_WRONGPASSWORD);
					return;
				}
			}
		} else if (commandData.getArgs().length == 3 && ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE) {
			if (!PasswordUtils.hashText(commandData.getArgs()[2]).equals(party.getPassword())) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_PASSWORD_WRONGPASSWORD);
				return;
			}
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
			return;
		}
		
		if (party.isFull()) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYFULL);
			return;
		}
		
		if (getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.JOIN, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		
		// Calling API Event
		IPlayerPreJoinEvent partiesPreJoinEvent = getPlugin().getEventManager().preparePlayerPreJoinEvent(partyPlayer, party, JoinCause.JOIN, partyPlayer);
		getPlugin().getEventManager().callEvent(partiesPreJoinEvent);
		
		if (!partiesPreJoinEvent.isCancelled()) {
			party.addMember(partyPlayer, JoinCause.JOIN, partyPlayer);
			
			sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_JOINED);
			
			party.broadcastMessage(Messages.ADDCMD_JOIN_PLAYERJOINED, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_JOIN,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_JOINEVENT_DENY,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
	}
}
