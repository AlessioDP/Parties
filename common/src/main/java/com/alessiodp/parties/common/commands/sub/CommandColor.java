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
import com.alessiodp.parties.common.parties.objects.PartyColorImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.interfaces.PartyColor;

import java.util.ArrayList;
import java.util.List;

public class CommandColor extends PartiesSubCommand {
	
	public CommandColor(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.COLOR,
				PartiesPermission.USER_COLOR,
				ConfigMain.COMMANDS_SUB_COLOR,
				false
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_COLOR
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_COLOR;
		help = Messages.HELP_ADDITIONAL_COMMANDS_COLOR;
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
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_COLOR))
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
		if (commandData.getArgs().length == 1) {
			if (party.getColor() != null)
				sendMessage(sender, partyPlayer, Messages.ADDCMD_COLOR_INFO);
			else
				sendMessage(sender, partyPlayer, Messages.ADDCMD_COLOR_EMPTY);
			return;
		}
		
		boolean isRemove = false;
		PartyColor color = null;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			color = ((PartiesPlugin) plugin).getColorManager().searchColorByCommand(commandData.getArgs()[1]);
			if (color == null) {
				// Color doesn't exist
				sendMessage(sender, partyPlayer, Messages.ADDCMD_COLOR_WRONGCOLOR);
				return;
			}
			
			if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.COLOR, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
		}
		
		// Command starts
		party.setColor(color);
		
		if (isRemove) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_COLOR_REMOVED);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_COLOR_REM,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_COLOR_CHANGED, party);
			party.broadcastMessage(Messages.ADDCMD_COLOR_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_COLOR,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_", color.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission) && args.length == 2) {
			for (PartyColorImpl color : ConfigParties.ADDITIONAL_COLOR_LIST) {
				ret.add(color.getCommand());
			}
		}
		return ret;
	}
}
