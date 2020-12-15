package com.alessiodp.parties.bungeecord.tasks;

import com.alessiodp.parties.bungeecord.commands.sub.BungeeCommandHome;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeTask;

public class BungeeHomeTask extends HomeTask {
	
	public BungeeHomeTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, PartyImpl party, long delayTime, PartyHomeImpl home) {
		super(plugin, partyPlayer, party, delayTime, home);
	}
	
	@Override
	protected boolean canRunning() {
		return true;
	}
	
	@Override
	protected void performTeleport() {
		BungeeCommandHome.teleportToPartyHome(plugin, player, partyPlayer, party, home);
	}
}
