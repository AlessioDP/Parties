package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.ColorImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.api.interfaces.Color;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommandColor extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandColor(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.COLOR.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.COLOR);
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
			sendMessage(sender, partyPlayer, Messages.ADDCMD_COLOR_WRONGCMD);
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
		Color color = null;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
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
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_COLOR_REM
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_COLOR_CHANGED, party);
			party.broadcastMessage(Messages.ADDCMD_COLOR_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_COLOR
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName())
					.replace("{value}", color.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			for (ColorImpl color : ConfigParties.COLOR_LIST) {
				ret.add(color.getCommand());
			}
			if (!args[1].isEmpty()) {
				ret = plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[1]);
			}
		}
		return ret;
	}
}
