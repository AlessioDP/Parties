package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.addons.internal.PartiesPlaceholder;
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
import com.alessiodp.parties.common.utils.RankPermission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
		
		syntax = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_COLOR
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_COLOR;
		help = Messages.HELP_ADDITIONAL_COMMANDS_COLOR;
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		return handlePreRequisitesFullWithParty(commandData, true, 1, 2, RankPermission.EDIT_COLOR);
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		Set<PartyColorImpl> availableColors = getPlugin().getColorManager().getAvailableColors(sender);
		
		if (commandData.getArgs().length == 1) {
			if (party.getColor() != null)
				sendMessage(sender, partyPlayer, parseWithAvailableColors(Messages.ADDCMD_COLOR_INFO, availableColors));
			else
				sendMessage(sender, partyPlayer, parseWithAvailableColors(Messages.ADDCMD_COLOR_EMPTY, availableColors));
			return;
		}
		
		boolean isRemove = false;
		PartyColor color = null;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			for (PartyColorImpl pc : availableColors) {
				if (pc.getCommand().equalsIgnoreCase(commandData.getArgs()[1])) {
					color = pc;
					break;
				}
			}
			
			if (color == null) {
				// Color doesn't exist
				sendMessage(sender, partyPlayer, parseWithAvailableColors(Messages.ADDCMD_COLOR_WRONGCOLOR, availableColors));
				return;
			}
			
			if (getPlugin().getEconomyManager().payCommand(EconomyManager.PaidCommand.COLOR, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
		}
		
		// Command starts
		party.setColor(color);
		
		if (isRemove) {
			sendMessage(sender, partyPlayer, parseWithAvailableColors(Messages.ADDCMD_COLOR_REMOVED, availableColors));
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_COLOR_REM,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else {
			sendMessage(sender, partyPlayer, parseWithAvailableColors(Messages.ADDCMD_COLOR_CHANGED, availableColors), party);
			party.broadcastMessage(parseWithAvailableColors(Messages.ADDCMD_COLOR_BROADCAST, availableColors), partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_COLOR,
					partyPlayer.getName(), party.getName() != null ? party.getName() : "_", color.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(@NotNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission) && args.length == 2) {
			for (PartyColorImpl color : ConfigParties.ADDITIONAL_COLOR_LIST) {
				ret.add(color.getCommand());
			}
		}
		return plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[args.length - 1]);
	}
	
	private String parseWithAvailableColors(String message, Set<PartyColorImpl> availableColors) {
		if (message.contains("%available_colors%")) {
			StringBuilder sb = new StringBuilder();
			for (PartyColorImpl pc : availableColors) {
				if (sb.length() > 0)
					sb.append(Messages.ADDCMD_COLOR_AVAILABLE_COLORS_SEPARATOR);
				
				sb.append(Messages.ADDCMD_COLOR_AVAILABLE_COLORS_COLOR
						.replace("%" + PartiesPlaceholder.COLOR_CODE.getSyntax() + "%", pc.getCode())
						.replace("%" + PartiesPlaceholder.COLOR_COMMAND.getSyntax() + "%", pc.getCommand())
						.replace("%" + PartiesPlaceholder.COLOR_NAME.getSyntax() + "%", pc.getName()));
			}
			return message.replace("%available_colors%", sb.toString());
		}
		return message;
	}
}
