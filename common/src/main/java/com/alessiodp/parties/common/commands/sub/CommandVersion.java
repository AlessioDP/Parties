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

public class CommandVersion extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandVersion(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = sender.isPlayer() ? ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID()) : null;
		
		// Checks for command prerequisites
		if (partyPlayer != null && !sender.hasPermission(PartiesPermission.ADMIN_VERSION.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.ADMIN_VERSION);
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
		if (sender.isPlayer()) {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_VERSION
					.replace("{player}", sender.getName()), true);
		} else {
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_VERSION_CONSOLE, true);
		}
		
		// Command starts
		String version = plugin.getVersion();
		String newVersion = plugin.getAdpUpdater().getFoundVersion().isEmpty() ? version : plugin.getAdpUpdater().getFoundVersion();
		String message = version.equals(newVersion) ? Messages.MAINCMD_VERSION_UPDATED : Messages.MAINCMD_VERSION_OUTDATED;
		
		sendMessage(sender, partyPlayer, message
				.replace("%version%", version)
				.replace("%newversion%", newVersion)
				.replace("%platform%", plugin.getPlatform()));
	}
}