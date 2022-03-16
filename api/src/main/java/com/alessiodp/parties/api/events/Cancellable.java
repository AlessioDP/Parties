package com.alessiodp.parties.api.events;

public interface Cancellable {
	/**
	 * Is the event cancelled?
	 *
	 * @return {@code true} if the event is cancelled
	 */
	boolean isCancelled();
	
	/**
	 * Set the event as cancelled
	 *
	 * @param cancel {@code true} to cancel
	 */
	void setCancelled(boolean cancel);
}
