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
import com.alessiodp.parties.common.utils.HashUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommandPassword extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandPassword(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.PASSWORD.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.PASSWORD);
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
			sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_WRONGCMD);
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
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			if (!Pattern.compile(ConfigParties.PASSWORD_ALLOWEDCHARS).matcher(commandData.getArgs()[1]).matches()
					|| (commandData.getArgs()[1].length() > ConfigParties.PASSWORD_MAXLENGTH)
					|| (commandData.getArgs()[1].length() < ConfigParties.PASSWORD_MINLENGTH)) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_INVALID);
				return;
			}
			
			password = HashUtils.hashText(commandData.getArgs()[1]);
		}
		
		// Command starts
		party.setPassword(password);
		party.updateParty();
		
		if (isRemove) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_REMOVED);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_PASSWORD_REM
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_PASSWORD_CHANGED);
			party.broadcastMessage(Messages.ADDCMD_PASSWORD_BROADCAST, partyPlayer);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_PASSWORD
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
