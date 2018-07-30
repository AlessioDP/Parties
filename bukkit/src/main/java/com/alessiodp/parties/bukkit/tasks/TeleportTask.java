package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;

import java.util.UUID;

public class TeleportTask implements Runnable {
	private PartiesPlugin plugin;
	private UUID player;
	
	public TeleportTask(PartiesPlugin instance, UUID uuid) {
		plugin = instance;
		player = uuid;
	}
	
	@Override
	public void run() {
		((BukkitCooldownManager) plugin.getCooldownManager()).getTeleportCooldown().remove(player);
		
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_EXPIRED
				.replace("{player}", player.toString()), true);
	}
}