package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.internal.ADPUpdater;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

public class CommandVersion extends AbstractCommand {
	
	public CommandVersion(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.ADMIN_VERSION.toString())) {
			pp.sendNoPermission(PartiesPermission.ADMIN_VERSION);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command starts
		 */
		
		String version = plugin.getVersion();
		String newVersion = ADPUpdater.getFoundVersion().isEmpty() ? version : ADPUpdater.getFoundVersion();
		String message = version.equals(newVersion) ? Messages.MAINCMD_VERSION_UPDATED : Messages.MAINCMD_VERSION_OUTDATED;
		
		pp.sendMessage(message
				.replace("%version%", version)
				.replace("%newversion%", newVersion));
	}
}