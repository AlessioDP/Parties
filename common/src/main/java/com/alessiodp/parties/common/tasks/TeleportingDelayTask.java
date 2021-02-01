package com.alessiodp.parties.common.tasks;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public abstract class TeleportingDelayTask implements Runnable {
	protected final PartiesPlugin plugin;
	protected final PartyPlayerImpl partyPlayer;
	
	protected final long startTime;
	protected final long delayTime;
	protected final User player;
	
	public TeleportingDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime) {
		this.plugin = plugin;
		this.partyPlayer = partyPlayer;
		startTime = System.currentTimeMillis();
		this.delayTime = delayTime * 1000; // Get milliseconds instead of seconds
		player = plugin.getPlayer(partyPlayer.getPlayerUUID());
	}
	
	@Override
	public void run() {
		if (getPendingDelay() != null) {
			if (player.isOnline()) {
				
				if (canRunning()) {
					// Check if delay is timed out
					long timestamp = System.currentTimeMillis();
					if (timestamp - startTime > delayTime) {
						performTeleport();
						cancel();
					}
				}
			} else {
				cancel(); // Player offline
			}
		}
	}
	
	protected abstract CancellableTask getPendingDelay();
	
	protected boolean canRunning() {
		return true;
	}
	
	protected abstract void performTeleport();
	
	public abstract void cancel();
}
