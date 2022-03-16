package com.alessiodp.parties.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PartyInvite {
	/**
	 * Get the referencing party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the invited player
	 *
	 * @return the {@link PartyPlayer}
	 */
	@NotNull PartyPlayer getInvitedPlayer();
	
	/**
	 * Get who sent the invite request
	 *
	 * @return the {@link PartyPlayer}
	 */
	@Nullable PartyPlayer getInviter();
	
	/**
	 * Accept the invite request
	 */
	default void accept() {
		accept(true);
	}
	
	/**
	 * Accept the invite request
	 *
	 * @param sendMessages should messages be sent?
	 */
	void accept(boolean sendMessages);
	
	/**
	 * Deny the invite request
	 */
	default void deny() {
		deny(true);
	}
	
	/**
	 * Deny the invite request
	 *
	 * @param sendMessages should messages be sent?
	 */
	void deny(boolean sendMessages);
	
	/**
	 * Revoke the invite request
	 */
	default void revoke() {
		revoke(true);
	}
	
	/**
	 * Revoke the invite request
	 *
	 * @param sendMessages should messages be sent?
	 */
	void revoke(boolean sendMessages);
	
	/**
	 * Timeout the invite request
	 */
	default void timeout() {
		timeout(true);
	}
	
	/**
	 * Timeout the invite request
	 *
	 * @param sendMessages should messages be sent?
	 */
	void timeout(boolean sendMessages);
}
