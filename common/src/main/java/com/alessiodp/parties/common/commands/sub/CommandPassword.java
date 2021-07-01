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
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PasswordUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandPassword extends PartiesSubCommand {
	
	public CommandPassword(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.PASSWORD,
				PartiesPermission.USER_PASSWORD,
				ConfigMain.COMMANDS_SUB_PASSWORD,
				false
		);
		
		syntax = String.format("%s <%s/%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PASSWORD,
				ConfigMain.COMMANDS_MISC_REMOVE
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_PASSWORD;
		help = Messages.HELP_ADDITIONAL_COMMANDS_PASSWORD;
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
		
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_PASSWORD))
			return false;
		
		if (commandData.getArgs().length > 2) {
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
		String password = "";
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE)) {
			// Remove command
			isRemove = true;
			password = null;
		} else {
			// Normal command
			if (!PasswordUtils.isValid(commandData.getArgs()[1])) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_INVALID);
				return;
			}
			
			password = PasswordUtils.hashText(commandData.getArgs()[1]);
			
			if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.PASSWORD, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
		}
		
		// Command starts
		party.setPassword(password);
		
		if (isRemove) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_REMOVED);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_PASSWORD_REM,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_CHANGED);
			party.broadcastMessage(Messages.ADDCMD_PASSWORD_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_PASSWORD,
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
