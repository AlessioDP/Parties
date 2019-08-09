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

public class CommandMotd extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandMotd(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.MOTD.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.MOTD);
			return false;
		}
		
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}

		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_MOTD))
			return false;
		
		if (commandData.getArgs().length < 2) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_MOTD_WRONGCMD);
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
		String motd = "";
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			motd = plugin.getCommandManager().getCommandUtils().handleCommandString(commandData.getArgs(), 1);
			
			if (!((PartiesPlugin) plugin).getCensorUtils().checkAllowedCharacters(ConfigParties.MOTD_ALLOWEDCHARS, motd, PartiesConstants.DEBUG_CMD_MOTD_REGEXERROR_AC)
					|| (motd.length() > ConfigParties.MOTD_MAXLENGTH)
					|| (motd.length() < ConfigParties.MOTD_MINLENGTH)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_MOTD_INVALID);
				return;
			}
			if (((PartiesPlugin) plugin).getCensorUtils().checkCensor(ConfigParties.MOTD_CENSORREGEX, motd, PartiesConstants.DEBUG_CMD_MOTD_REGEXERROR_CEN)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_MOTD_CENSORED);
				return;
			}
			
			if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.MOTD, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
				return;
		}
		
		// Command starts
		party.setMotd(motd);
		party.updateParty();
		
		if (isRemove) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_MOTD_REMOVED);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_MOTD
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_MOTD_CHANGED);
			party.broadcastMessage(Messages.ADDCMD_MOTD_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_MOTD_REM
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