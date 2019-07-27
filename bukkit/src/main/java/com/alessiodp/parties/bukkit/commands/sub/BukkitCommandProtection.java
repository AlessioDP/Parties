package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.List;

public class BukkitCommandProtection extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public BukkitCommandProtection(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.PROTECTION.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.PROTECTION);
			return false;
		}
		
		PartyImpl party = partyPlayer.getPartyName().isEmpty() ? null : ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyName());
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_EDIT_PROTECTION))
			return false;
		
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
		Boolean protection = plugin.getCommandManager().getCommandUtils().handleOnOffCommand(partyPlayer.isChatParty(), commandData.getArgs());
		if (protection == null) {
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_PROTECTION_WRONGCMD);
			return;
		}
		
		// Command starts
		party.setProtection(protection);
		party.updateParty();
		
		if (protection)
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_PROTECTION_ON);
		else
			sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_PROTECTION_OFF);
		
		plugin.getLoggerManager().logDebug((protection ? PartiesConstants.DEBUG_CMD_PROTECTION_ON : PartiesConstants.DEBUG_CMD_PROTECTION_OFF)
						.replace("{player}", sender.getName())
						.replace("{party}", party.getName()),
				true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompleteOnOff(args);
	}
}