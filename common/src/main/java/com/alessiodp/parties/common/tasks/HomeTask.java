package com.alessiodp.parties.common.tasks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

public abstract class HomeTask implements Runnable {
	protected final PartiesPlugin plugin;
	protected final PartyPlayerImpl partyPlayer;
	protected final PartyImpl party;
	
	protected final long startTime;
	protected final long delayTime;
	protected final PartyHomeImpl home;
	protected final User player;
	
	public HomeTask(PartiesPlugin plugin, PartyPlayerImpl partyPlayer, PartyImpl party, long delayTime, PartyHomeImpl home) {
		this.plugin = plugin;
		this.partyPlayer = partyPlayer;
		this.party = party;
		startTime = System.currentTimeMillis();
		this.delayTime = delayTime * 1000; // Get milliseconds instead of seconds
		this.home = home;
		player = plugin.getPlayer(partyPlayer.getPlayerUUID());
	}
	
	@Override
	public void run() {
		if (partyPlayer.getHomeTeleporting() != null) {
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
	
	protected boolean canRunning() {
		return true;
	}
	
	protected abstract void performTeleport();
	
	protected void cancel() {
		if (partyPlayer.getHomeTeleporting() != null) {
			partyPlayer.getHomeTeleporting().cancel();
			partyPlayer.setHomeTeleporting(null);
		}
	}
}
