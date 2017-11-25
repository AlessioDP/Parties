package com.alessiodp.parties.utils.tasks;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.objects.ThePlayer;

public class PortalTask extends BukkitRunnable {
	private Parties plugin;
	private UUID playerUUID;
	
	public PortalTask(UUID uuid) {
		plugin = Parties.getInstance();
		playerUUID = uuid;
	}
	
	public void run() {
		ThePlayer tp = plugin.getPlayerHandler().getListPlayers().get(playerUUID);
		if (tp != null)
			tp.setPortalTimeout(-1);
	}

}
