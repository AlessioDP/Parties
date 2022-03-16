package com.alessiodp.parties.api.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface PartyPlayer {
	
	/**
	 * Get the player {@link UUID}
	 *
	 * @return the {@link UUID} of the player
	 */
	@NotNull UUID getPlayerUUID();
	
	/**
	 * Get the name
	 *
	 * @return the name of the player
	 */
	@NotNull String getName();
	
	/**
	 * Is the player inside a party?
	 *
	 * @return true if the player is inside a party
	 */
	default boolean isInParty() {
		return getPartyId() != null;
	}
	
	/**
	 * Get the party id
	 *
	 * @return the {@code UUID} of the party, null if the player is not in a party
	 */
	@Nullable UUID getPartyId();
	
	/**
	 * Get the party name
	 *
	 * @return the party name, null if the player is not in a party
	 */
	@Nullable String getPartyName();
	
	/**
	 * Set the party id
	 *
	 * @param partyId the party id to set
	 */
	void setPartyId(@Nullable UUID partyId);
	
	/**
	 * Set the party name
	 *
	 * @param partyName the party name to set
	 * @deprecated use setPartyId instead
	 */
	@Deprecated
	default void setPartyName(String partyName) {
		// Nothing to do
	}
	
	/**
	 * Get the rank level
	 *
	 * @return the rank level
	 */
	int getRank();
	
	/**
	 * Set the rank level
	 *
	 * @param rank the rank level to set
	 */
	void setRank(int rank);
	
	/**
	 * Get the nickname
	 *
	 * @return the nickname
	 */
	String getNickname();
	
	/**
	 * Set the nickname
	 *
	 * @param nickname the nickname to set
	 */
	void setNickname(String nickname);
	
	/**
	 * Get a list of pending invite requests
	 *
	 * @return a set of {@link PartyInvite}
	 */
	Set<PartyInvite> getPendingInvites();
	
	/**
	 * Get a list of pending ask requests
	 *
	 * @return a set of {@link PartyAskRequest}
	 */
	Set<PartyAskRequest> getPendingAskRequests();
	
	/**
	 * Is the player a spy?
	 *
	 * @return true if the player is a spy
	 */
	boolean isSpy();
	
	/**
	 * Set the player as a spy
	 *
	 * @param value true to be a spy
	 */
	void setSpy(boolean value);
	
	/**
	 * Is the player muted?
	 *
	 * @return true if the player is muted
	 */
	boolean isMuted();
	
	/**
	 * Toggle player mute
	 *
	 * @param value true to mute notifications
	 */
	void setMuted(boolean value);
	
	/**
	 * Set the name
	 *
	 * @param name the name to set
	 * @deprecated Parties does not handle player names anymore (use LastLoginAPI plugin instead)
	 */
	@Deprecated
	default void setName(String name) {
		// Nothing to do
	}
	
	/**
	 * Get the name timestamp
	 *
	 * @return the name timestamp
	 * @deprecated Parties does not handle login timestamp anymore (use LastLoginAPI plugin instead)
	 */
	@Deprecated
	default long getNameTimestamp() {
		return 0L;
	}
	
	/**
	 * Set the name timestamp
	 *
	 * @param nameTimestamp the timestamp to set
	 * @deprecated Parties does not handle login timestamp anymore (use LastLoginAPI plugin instead)
	 */
	@Deprecated
	default void setNameTimestamp(long nameTimestamp) {
		// Nothing to do
	}
	
	/**
	 * Ask to the party if the player can join
	 *
	 * @param party the {@link Party} to ask
	 * @return the {@link PartyAskRequest} instance
	 */
	default PartyAskRequest askToJoin(@NotNull Party party) {
		return askToJoin(party, true);
	}
	
	/**
	 * Ask to the party if the player can join
	 *
	 * @param party the {@link PartyPlayer} to ask
	 * @param sendMessages true if the event should send messages to players
	 * @return the {@link PartyAskRequest} instance
	 */
	PartyAskRequest askToJoin(@NotNull Party party, boolean sendMessages);
}
