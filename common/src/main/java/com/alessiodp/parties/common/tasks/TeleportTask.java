package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class TeleportTask implements Runnable {
	@NonNull private PartiesPlugin plugin;
	@NonNull private UUID player;
	
	@Override
	public void run() {
		plugin.getCooldownManager().getTeleportCooldown().remove(player);
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_TELEPORT_EXPIRED
				.replace("{player}", player.toString()), true);
	}
}