package com.alessiodp.parties.bukkit.events.implementations;

import com.alessiodp.parties.api.events.Cancellable;

public abstract class BukkitAbstractEventCancellable extends BukkitAbstractEvent implements Cancellable {
	private boolean cancelled;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
