package com.alessiodp.parties.bungeecord.tasks;

import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandHome;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeDelayTask;

public class BungeeHomeDelayTask extends HomeDelayTask {
	
	public BungeeHomeDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime, PartyHomeImpl home) {
		super(plugin, partyPlayer, delayTime, home);
	}
	
	@Override
	protected void performTeleport() {
		BungeeCommandHome.teleportToPartyHome(plugin, player, partyPlayer, home);
	}
}
