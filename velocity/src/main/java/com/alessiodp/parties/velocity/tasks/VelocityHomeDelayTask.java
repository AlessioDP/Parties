package com.alessiodp.parties.velocity.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeDelayTask;
import com.alessiodp.parties.velocity.commands.sub.VelocityCommandHome;

public class VelocityHomeDelayTask extends HomeDelayTask {
	
	public VelocityHomeDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime, PartyHomeImpl home) {
		super(plugin, partyPlayer, delayTime, home);
	}
	
	@Override
	protected void performTeleport() {
		VelocityCommandHome.teleportToPartyHome(plugin, player, partyPlayer, home);
	}
}
