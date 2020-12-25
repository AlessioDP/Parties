package com.alessiodp.parties.api.interfaces;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;
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
	 * Is the player inside a party?
	 *
	 * @return Returns true if the player is inside a party
	 */
	default boolean isInParty() {
		return getPartyId() != null;
	}
	
	/**
	 * Get the party id
	 *
	 * @return Returns the {@code UUID} of the party, null if the player is not in a party
	 */
	@Nullable UUID getPartyId();
	
	/**
	 * Get the party name
	 *
	 * @return Returns the party name, null if the player is not in a party
	 */
	@Nullable String getPartyName();
	
	/**
	 * Set the party id
	 *
	 * @param partyId The party id to set
	 */
	void setPartyId(@Nullable UUID partyId);
	
	/**
	 * Set the party name
	 *
	 * @param partyName The party name to set
	 * @deprecated use setPartyId instead
	 */
	@Deprecated
	default void setPartyName(String partyName) {
		// Nothing to do
	}
	
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
	 * Get a list of pending invite requests
	 *
	 * @return A set of {@link PartyInvite}
	 */
	Set<PartyInvite> getPendingInvites();
	
	/**
	 * Get a list of pending ask requests
	 *
	 * @return A set of {@link PartyAskRequest}
	 */
	Set<PartyAskRequest> getPendingAskRequests();
	
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
	
	/**
	 * Ask to the party if the player can join
	 *
	 * @param party The {@link Party} to ask
	 * @return Returns the {@link PartyAskRequest} instance
	 */
	default PartyAskRequest askToJoin(@NonNull Party party) {
		return askToJoin(party, true);
	}
	
	/**
	 * Ask to the party if the player can join
	 *
	 * @param party The {@link PartyPlayer} to ask
	 * @param sendMessages True if the event should send messages to players
	 * @return Returns the {@link PartyAskRequest} instance
	 */
	PartyAskRequest askToJoin(@NonNull Party party, boolean sendMessages);
}
