package com.alessiodp.parties.api.events;

public interface Cancellable {
	/**
	 * Is the event cancelled?
	 *
	 * @return Returns {@code true} if the event is cancelled
	 */
	boolean isCancelled();
	
	/**
	 * Set the event as cancelled
	 *
	 * @param cancel {@code True} to cancel
	 */
	void setCancelled(boolean cancel);
}
