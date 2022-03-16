package com.alessiodp.parties.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PartyAskRequest {
	/**
	 * Get the referencing party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get who is asking to join the party
	 *
	 * @return the {@link PartyPlayer}
	 */
	@NotNull PartyPlayer getAsker();
	
	/**
	 * Accept the ask request
	 */
	default void accept() {
		accept(true, null);
	}
	
	/**
	 * Accept the ask request
	 *
	 * @param sendMessages should messages be sent?
	 */
	default void accept(boolean sendMessages) {
		accept(sendMessages, null);
	}
	
	/**
	 * Accept the ask request
	 *
	 * @param accepter who accepted the request
	 */
	default void accept(@Nullable PartyPlayer accepter) {
		accept(true, accepter);
	}
	
	/**
	 * Accept the ask request
	 *
	 * @param sendMessages should messages be sent?
	 * @param accepter who accepted the request
	 */
	void accept(boolean sendMessages, @Nullable PartyPlayer accepter);
	
	/**
	 * Deny the ask request
	 */
	default void deny() {
		deny(true, null);
	}
	
	/**
	 * Deny the ask request
	 *
	 * @param sendMessages should messages be sent?
	 */
	default void deny(boolean sendMessages) {
		deny(sendMessages, null);
	}
	
	/**
	 * Deny the ask request
	 *
	 * @param denier who denied the request
	 */
	default void deny(@Nullable PartyPlayer denier) {
		deny(true, denier);
	}
	
	/**
	 * Deny the ask request
	 *
	 * @param sendMessages should messages be sent?
	 * @param denier who denied the request
	 */
	void deny(boolean sendMessages, @Nullable PartyPlayer denier);
	
	/**
	 * Timeout the ask request
	 */
	default void timeout() {
		timeout(true);
	}
	
	/**
	 * Timeout the ask request
	 *
	 * @param sendMessages should messages be sent?
	 */
	void timeout(boolean sendMessages);
}
