package com.alessiodp.parties.utils.tasks;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.handlers.PlayerHandler;
import com.alessiodp.parties.utils.enums.LogLevel;

public class TeleportTask extends BukkitRunnable {
	private PlayerHandler ph;
	private UUID player;

	public TeleportTask(UUID p, PlayerHandler instance) {
		ph = instance;
		player = p;
	}

	public void run() {
		if (ph.getTeleportCooldown().containsKey(player))
			ph.getTeleportCooldown().remove(player);
		LogHandler.log(LogLevel.DEBUG, "Teleport cooldown expired for " + player.toString(), true);
	}
}