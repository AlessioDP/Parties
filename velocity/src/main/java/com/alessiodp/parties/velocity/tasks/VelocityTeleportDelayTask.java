package com.alessiodp.parties.velocity.tasks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.velocity.user.VelocityUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.TeleportDelayTask;
import com.alessiodp.parties.velocity.commands.sub.VelocityCommandTeleport;

public class VelocityTeleportDelayTask extends TeleportDelayTask {
	
	public VelocityTeleportDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime, PartyPlayerImpl targetPlayer) {
		super(plugin, partyPlayer, delayTime, targetPlayer);
	}
	
	@Override
	protected void performTeleport() {
		User user = plugin.getPlayer(targetPlayer.getPlayerUUID());
		if (user != null) {
			VelocityCommandTeleport.teleportSinglePlayer(plugin, partyPlayer, targetPlayer, ((VelocityUser) user).getServer());
		}
	}
}
