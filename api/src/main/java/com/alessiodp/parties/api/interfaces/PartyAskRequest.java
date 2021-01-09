package com.alessiodp.parties.api.interfaces;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PartyAskRequest {
	/**
	 * Get the referencing party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull Party getParty();
	
	/**
	 * Get who is asking to join the party
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull PartyPlayer getAsker();
	
	/**
	 * Accept the ask request
	 */
	default void accept() {
		accept(true, null);
	}
	
	/**
	 * Accept the ask request
	 *
	 * @param sendMessages Should messages be sent?
	 */
	default void accept(boolean sendMessages) {
		accept(sendMessages, null);
	}
	
	/**
	 * Accept the ask request
	 *
	 * @param accepter Who accepted the request
	 */
	default void accept(@Nullable PartyPlayer accepter) {
		accept(true, accepter);
	}
	
	/**
	 * Accept the ask request
	 *
	 * @param sendMessages Should messages be sent?
	 * @param accepter Who accepted the request
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
	 * @param sendMessages Should messages be sent?
	 */
	default void deny(boolean sendMessages) {
		deny(sendMessages, null);
	}
	
	/**
	 * Deny the ask request
	 *
	 * @param denier Who denied the request
	 */
	default void deny(@Nullable PartyPlayer denier) {
		deny(true, denier);
	}
	
	/**
	 * Deny the ask request
	 *
	 * @param sendMessages Should messages be sent?
	 * @param denier Who denied the request
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
	 * @param sendMessages Should messages be sent?
	 */
	void timeout(boolean sendMessages);
}
