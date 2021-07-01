package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
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

import java.util.ArrayList;
import java.util.List;

public class CommandDesc extends PartiesSubCommand {
	
	public CommandDesc(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.DESC,
				PartiesPermission.USER_DESC,
				ConfigMain.COMMANDS_SUB_DESC,
				false
		);
		
		syntax = String.format("%s <%s/%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_DESCRIPTION,
				ConfigMain.COMMANDS_MISC_REMOVE
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_DESC;
		help = Messages.HELP_ADDITIONAL_COMMANDS_DESC;
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
		
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId());
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_DESC))
			return false;
		
		if (commandData.getArgs().length < 2) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		((PartiesCommandData) commandData).setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		boolean isRemove = false;
		String description = "";
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			StringBuilder sb = new StringBuilder();
			for (int word = 1; word < commandData.getArgs().length; word++) {
				if (sb.length() > 0)
					sb.append(" ");
				sb.append(commandData.getArgs()[word]);
			}
			description = sb.toString();
			
			if (!CensorUtils.checkAllowedCharacters(ConfigParties.ADDITIONAL_DESC_ALLOWEDCHARS, description, PartiesConstants.DEBUG_CMD_DESC_REGEXERROR_ALLOWEDCHARS)
					|| (description.length() > ConfigParties.ADDITIONAL_DESC_MAXLENGTH)
					|| (description.length() < ConfigParties.ADDITIONAL_DESC_MINLENGTH)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_INVALID);
				return;
			}
			if (CensorUtils.checkCensor(ConfigParties.ADDITIONAL_DESC_CENSORREGEX, description, PartiesConstants.DEBUG_CMD_DESC_REGEXERROR_CENSORED)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_CENSORED);
				return;
			}
			
			if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.DESC, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
		}
		
		// Command starts
		party.setDescription(description);
		
		if (isRemove) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_REMOVED);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_DESC_REM,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_CHANGED);
			party.broadcastMessage(Messages.ADDCMD_DESC_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_DESC,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_MISC_REMOVE);
		}
		return ret;
	}
}