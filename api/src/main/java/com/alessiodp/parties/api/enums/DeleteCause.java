package com.alessiodp.parties.api.enums;

public enum DeleteCause {
	
	/**
	 * The last player or leader left the party
	 */
	LEAVE,
	/**
	 * The last player or leader has been kicked from the party
	 */
	KICK,
	/**
	 * The party has been deleted
	 */
	DELETE,
	/**
	 * The last player or leader has been banned from the server
	 */
	BAN,
	/**
	 * The party got deleted after all players or leader left the server
	 */
	TIMEOUT,
	/**
	 * Other reasons
	 */
	OTHERS
}
