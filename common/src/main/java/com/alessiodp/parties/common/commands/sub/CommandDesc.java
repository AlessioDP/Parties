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
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommandDesc extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandDesc(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.DESC.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.DESC);
			return false;
		}
		
		PartyImpl party = partyPlayer.getPartyName().isEmpty() ? null : ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyName());
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_DESC))
			return false;
		
		if (commandData.getArgs().length < 2) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_WRONGCMD);
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
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
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
			
			if (!((PartiesPlugin) plugin).getCensorUtils().checkAllowedCharacters(ConfigParties.DESC_ALLOWEDCHARS, description, PartiesConstants.DEBUG_CMD_DESC_REGEXERROR_AC)
					|| (description.length() > ConfigParties.DESC_MAXLENGTH)
					|| (description.length() < ConfigParties.DESC_MINLENGTH)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_INVALID);
				return;
			}
			if (((PartiesPlugin) plugin).getCensorUtils().checkCensor(ConfigParties.DESC_CENSORREGEX, description, PartiesConstants.DEBUG_CMD_DESC_REGEXERROR_CEN)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_CENSORED);
				return;
			}
			
			if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.DESC, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
		}
		
		// Command starts
		party.setDescription(description);
		party.updateParty();
		
		if (isRemove) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_REMOVED);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_DESC_REM
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DESC_CHANGED);
			party.broadcastMessage(Messages.ADDCMD_DESC_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_DESC
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_SUB_REMOVE);
		}
		return ret;
	}
}