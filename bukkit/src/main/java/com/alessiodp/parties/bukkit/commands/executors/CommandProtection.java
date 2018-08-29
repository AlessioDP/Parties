package com.alessiodp.parties.bukkit.commands.executors;

import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

public class CommandProtection extends AbstractCommand {
	
	public CommandProtection(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.PROTECTION.toString())) {
			pp.sendNoPermission(PartiesPermission.PROTECTION);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_PROTECTION))
			return false;
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		Boolean protection = party.isFriendlyFireProtected();
		if (commandData.getArgs().length > 1) {
			if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_ON))
				protection = true;
			else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_OFF))
				protection = false;
			else
				protection = null;
		} else {
			protection = !protection;
		}
		if (protection == null) {
			pp.sendMessage(BukkitMessages.ADDCMD_PROTECTION_WRONGCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		party.setProtection(protection);
		party.updateParty();
		
		if (protection)
			pp.sendMessage(BukkitMessages.ADDCMD_PROTECTION_ON);
		else
			pp.sendMessage(BukkitMessages.ADDCMD_PROTECTION_OFF);
		
		LoggerManager.log(LogLevel.MEDIUM,
				(protection ? Constants.DEBUG_CMD_PROTECTION_ON : Constants.DEBUG_CMD_PROTECTION_OFF)
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()),
				true);
	}
}