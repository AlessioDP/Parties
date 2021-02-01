package com.alessiodp.parties.common.tasks;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public abstract class TeleportDelayTask extends TeleportingDelayTask implements Runnable {
	protected PartyPlayerImpl targetPlayer;
	
	public TeleportDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime, PartyPlayerImpl targetPlayer) {
		super(plugin, partyPlayer, delayTime);
		this.targetPlayer = targetPlayer;
	}
	
	@Override
	protected CancellableTask getPendingDelay() {
		return partyPlayer.getPendingTeleportDelay();
	}
	
	@Override
	public void cancel() {
		if (partyPlayer.getPendingTeleportDelay() != null) {
			partyPlayer.getPendingTeleportDelay().cancel();
			partyPlayer.setPendingTeleportDelay(null);
		}
	}
}
