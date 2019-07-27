package com.alessiodp.parties.api.enums;

/**
 * Status is an enum that provides a result for some Parties API methods.
 * @deprecated No longer used
 */
@Deprecated
public enum Status {
	/**
	 * The party doesn't exist
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
	 * The party already exists
	 */
	ALREADYEXISTPARTY,
	/**
	 * The party is full
	 */
	PARTYFULL,
	/**
	 * Action performed successfully
	 */
	SUCCESS
}
