package com.alessiodp.parties.api.enums;

public enum LeaveCause {
	/**
	 * The player left the party
	 */
	LEAVE,
	/**
	 * The player has been kicked from the party
	 */
	KICK,
	/**
	 * The player has been banned from the server
	 */
	BAN,
	/**
	 * The player got kicked after leaving the server
	 */
	TIMEOUT,
	/**
	 * Other reasons
	 */
	OTHERS
}
