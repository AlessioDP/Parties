package com.alessiodp.parties.common.tasks;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public abstract class HomeDelayTask extends TeleportingDelayTask implements Runnable {
	protected final PartyHomeImpl home;
	
	public HomeDelayTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, long delayTime, PartyHomeImpl home) {
		super(plugin, partyPlayer, delayTime);
		this.home = home;
	}
	
	@Override
	protected CancellableTask getPendingDelay() {
		return partyPlayer.getPendingHomeDelay();
	}
	
	@Override
	public void cancel() {
		// Cancel the runnable task in scheduler
		if (partyPlayer.getPendingHomeDelay() != null) {
			partyPlayer.getPendingHomeDelay().cancel();
			partyPlayer.setPendingHomeDelay(null);
		}
	}
}
