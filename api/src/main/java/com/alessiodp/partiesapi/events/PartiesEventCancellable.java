package com.alessiodp.partiesapi.events;

import org.bukkit.event.Cancellable;

public abstract class PartiesEventCancellable extends PartiesEvent implements Cancellable {
	private boolean cancelled;
	
	/**
	 * Is the event cancelled?
	 * 
	 * @return Returns {@code true} if the event is cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Set the event as cancelled
	 * 
	 * @param cancel {@code True} to cancel
	 */
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
