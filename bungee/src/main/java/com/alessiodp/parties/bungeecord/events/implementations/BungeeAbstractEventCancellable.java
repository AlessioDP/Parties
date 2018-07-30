package com.alessiodp.parties.bungeecord.events.implementations;

import com.alessiodp.parties.api.events.Cancellable;

public abstract class BungeeAbstractEventCancellable extends BungeeAbstractEvent implements Cancellable {
	private boolean cancelled;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
