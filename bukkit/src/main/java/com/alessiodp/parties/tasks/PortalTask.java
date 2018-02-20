package com.alessiodp.parties.tasks;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class PortalTask extends BukkitRunnable {
	private Parties plugin;
	private UUID playerUUID;
	
	public PortalTask(UUID uuid) {
		plugin = Parties.getInstance();
		playerUUID = uuid;
	}
	
	@Override
	public void run() {
		PartyPlayerEntity pp = plugin.getPlayerManager().getListPartyPlayers().get(playerUUID);
		if (pp != null)
			pp.setPortalTimeoutTask(-1);
	}

}
