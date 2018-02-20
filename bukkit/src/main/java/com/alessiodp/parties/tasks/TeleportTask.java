package com.alessiodp.parties.tasks;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.PlayerManager;

public class TeleportTask extends BukkitRunnable {
	private PlayerManager ph;
	private UUID player;
	
	public TeleportTask(UUID p, PlayerManager instance) {
		ph = instance;
		player = p;
	}
	
	@Override
	public void run() {
		if (ph.getTeleportCooldown().containsKey(player))
			ph.getTeleportCooldown().remove(player);
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_EXPIRED
				.replace("{player}", player.toString()), true);
	}
}