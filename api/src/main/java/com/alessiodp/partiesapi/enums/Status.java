package com.alessiodp.partiesapi.enums;

public enum Status {
	/**
	 * Party doesn't exist
	 */
	NOEXIST,
	/**
	 * The player isn't in a party
	 */
	NOPARTY,
	/**
	 * The player is already in a party
	 */
	ALREADYINPARTY,
	/**
	 * Party already exist
	 */
	ALREADYEXISTPARTY,
	/**
	 * The party is full
	 */
	PARTYFULL,
	/**
	 * Action performed successfully
	 */
	SUCCESS;
}
