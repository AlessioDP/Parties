package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.List;

public class CommandMute extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandMute(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.MUTE.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.MUTE);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		Boolean mute = plugin.getCommandManager().getCommandUtils().handleOnOffCommand(partyPlayer.isMuted(), commandData.getArgs());
		if (mute == null) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_MUTE_WRONGCMD);
			return;
		}
		
		// Command starts
		partyPlayer.setMuted(mute);
		partyPlayer.updatePlayer();
		
		if (mute) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_MUTE_ON);
		} else {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_MUTE_OFF);
		}
		
		plugin.getLoggerManager().logDebug(
				(mute ? PartiesConstants.DEBUG_CMD_MUTE_ON : PartiesConstants.DEBUG_CMD_MUTE_OFF)
						.replace("{player}", sender.getName()),
				true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompleteOnOff(args);
	}
}
