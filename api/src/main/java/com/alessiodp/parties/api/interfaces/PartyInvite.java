package com.alessiodp.parties.api.interfaces;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PartyInvite {
	/**
	 * Get the referencing party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull Party getParty();
	
	/**
	 * Get the invited player
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull PartyPlayer getInvitedPlayer();
	
	/**
	 * Get who sent the invite request
	 *
	 * @return Returns the {@link PartyPlayer}
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
	 * @param sendMessages Should messages be sent?
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
	 * @param sendMessages Should messages be sent?
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
	 * @param sendMessages Should messages be sent?
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
	 * @param sendMessages Should messages be sent?
	 */
	void timeout(boolean sendMessages);
}
