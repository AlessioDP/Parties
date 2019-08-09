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

public class CommandSpy extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandSpy(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.ADMIN_SPY);
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
		Boolean spy = plugin.getCommandManager().getCommandUtils().handleOnOffCommand(partyPlayer.isSpy(), commandData.getArgs());
		if (spy == null) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_SPY_WRONGCMD);
			return;
		}
		
		// Command starts
		partyPlayer.setSpy(spy);
		partyPlayer.updatePlayer();
		
		if (spy) {
			((PartiesPlugin) plugin).getSpyManager().addSpy(partyPlayer.getPlayerUUID());
			sendMessage(sender, partyPlayer, Messages.MAINCMD_SPY_ENABLED);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_SPY_ENABLE
					.replace("{player}", sender.getName()), true);
		} else {
			((PartiesPlugin) plugin).getSpyManager().removeSpy(partyPlayer.getPlayerUUID());
			sendMessage(sender, partyPlayer, Messages.MAINCMD_SPY_DISABLED);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_SPY_DISABLE
					.replace("{player}", sender.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return plugin.getCommandManager().getCommandUtils().tabCompleteOnOff(args);
	}
}
