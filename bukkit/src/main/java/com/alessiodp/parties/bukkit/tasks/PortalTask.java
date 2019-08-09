package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;

import java.util.UUID;

public class PortalTask implements Runnable {
	private final PartiesPlugin plugin;
	private final UUID playerUUID;
	
	public PortalTask(PartiesPlugin instance, UUID uuid) {
		plugin = instance;
		playerUUID = uuid;
	}
	
	@Override
	public void run() {
		BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getListPartyPlayers().get(playerUUID);
		if (pp != null)
			pp.setPortalTimeoutTask(null);
	}

}
