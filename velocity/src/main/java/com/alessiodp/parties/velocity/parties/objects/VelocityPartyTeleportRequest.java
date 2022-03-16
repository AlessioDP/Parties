package com.alessiodp.parties.velocity.parties.objects;

import com.alessiodp.core.common.user.User;
import com.alessiodp.core.velocity.user.VelocityUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyTeleportRequest;
import com.alessiodp.parties.velocity.commands.sub.VelocityCommandTeleport;
import org.jetbrains.annotations.NotNull;

public class VelocityPartyTeleportRequest extends PartyTeleportRequest {
	public VelocityPartyTeleportRequest(@NotNull PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl requester) {
		super(plugin, player, requester);
	}
	
	@Override
	protected void teleportPlayer() {
		User user = plugin.getPlayer(requester.getPlayerUUID());
		if (user != null) {
			VelocityCommandTeleport.teleportSinglePlayer(plugin, player, requester, ((VelocityUser) user).getServer());
		}
	}
}
