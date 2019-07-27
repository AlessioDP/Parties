package com.alessiodp.parties.api.interfaces;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface PartyPlayer {
	
	/**
	 * Get the player {@link UUID}
	 *
	 * @return Returns the {@link UUID} of the player
	 */
	@NonNull UUID getPlayerUUID();
	
	/**
	 * Get the name
	 *
	 * @return Returns the name of the player
	 */
	@NonNull String getName();
	
	/**
	 * Get the party name
	 *
	 * @return Returns the party name, empty if the player is not in a party
	 */
	@NonNull String getPartyName();
	
	/**
	 * Set the party name
	 *
	 * @param partyName The party name to set
	 */
	void setPartyName(@NonNull String partyName);
	
	/**
	 * Get the rank level
	 *
	 * @return Returns the rank level
	 */
	int getRank();
	
	/**
	 * Set the rank level
	 *
	 * @param rank The rank level to set
	 */
	void setRank(int rank);
	
	/**
	 * Is the player a spy?
	 *
	 * @return Returns true if the player is a spy
	 */
	boolean isSpy();
	
	/**
	 * Set the player as a spy
	 *
	 * @param value True to be a spy
	 */
	void setSpy(boolean value);
	
	/**
	 * Is the player muted?
	 *
	 * @return Returns true if the player is muted
	 */
	boolean isMuted();
	
	/**
	 * Toggle player mute
	 *
	 * @param value True to mute notifications
	 */
	void setMuted(boolean value);
	
	/**
	 * Set the name
	 *
	 * @param name The name to set
	 * @deprecated Parties does not handle player names anymore (use LastLoginAPI plugin instead)
	 */
	@Deprecated
	default void setName(String name) {
		// Nothing to do
	}
	
	/**
	 * Get the name timestamp
	 *
	 * @return Returns the name timestamp
	 * @deprecated Parties does not handle login timestamp anymore (use LastLoginAPI plugin instead)
	 */
	@Deprecated
	default long getNameTimestamp() {
		return 0L;
	}
	
	/**
	 * Set the name timestamp
	 *
	 * @param nameTimestamp The timestamp to set
	 * @deprecated Parties does not handle login timestamp anymore (use LastLoginAPI plugin instead)
	 */
	@Deprecated
	default void setNameTimestamp(long nameTimestamp) {
		// Nothing to do
	}
}
