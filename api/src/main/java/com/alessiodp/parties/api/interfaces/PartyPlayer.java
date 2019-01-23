package com.alessiodp.parties.api.interfaces;

import java.util.UUID;

public interface PartyPlayer {
	
	/**
	 * Get the player {@link UUID}
	 *
	 * @return Returns the {@link UUID} of the player
	 */
	UUID getPlayerUUID();
	
	/**
	 * Get the name
	 *
	 * @return Returns the name of the player
	 */
	String getName();
	
	/**
	 * Set the name
	 *
	 * @param name The name to set
	 */
	void setName(String name);
	
	/**
	 * Get the name timestamp
	 *
	 * @return Returns the name timestamp
	 */
	long getNameTimestamp();
	
	/**
	 * Set the name timestamp
	 *
	 * @param nameTimestamp The timestamp to set
	 */
	void setNameTimestamp(long nameTimestamp);
	
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
	 * Get the party name
	 *
	 * @return Returns the party name, empty if the player is not in a party
	 */
	String getPartyName();
	
	/**
	 * Set the party name
	 *
	 * @param partyName The party name to set
	 */
	void setPartyName(String partyName);
	
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
}
