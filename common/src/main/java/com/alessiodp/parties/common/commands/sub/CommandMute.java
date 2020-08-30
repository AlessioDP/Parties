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
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.List;

public class CommandMute extends PartiesSubCommand {
	
	public CommandMute(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.MUTE,
				PartiesPermission.USER_MUTE,
				ConfigMain.COMMANDS_CMD_MUTE,
				false
		);
		
		syntax = String.format("%s [%s/%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_ON,
				ConfigMain.COMMANDS_SUB_OFF
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_MUTE;
		help = Messages.HELP_ADDITIONAL_COMMANDS_MUTE;
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
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
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
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_MUTE,
				partyPlayer.getName(), mute), true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompleteOnOff(args);
	}
}
