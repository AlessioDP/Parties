package com.alessiodp.parties.api.interfaces;

import com.alessiodp.parties.api.enums.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PartiesAPI {
	/**
	 * Reload Parties configuration files
	 */
	void reloadPlugin();
	
	/**
	 * Check if the plugin have BungeeCord option enabled
	 *
	 * @return True if BungeeCord support is enabled
	 */
	boolean isBungeeCordEnabled();
	
	/**
	 * Create a party
	 *
	 * @param party  The party name
	 * @param leader The leader of the party as {@link PartyPlayer}, null if the party should be fixed
	 * @return Returns true if successfully created
	 */
	boolean createParty(@Nullable String party, @Nullable PartyPlayer leader);
	
	/**
	 * Get the party by its name
	 *
	 * @param party The name of the {@link Party}
	 * @return Returns the {@link Party}
	 */
	Party getParty(@NonNull String party);
	
	/**
	 * Get the party by its id
	 *
	 * @param party The id of the {@link Party}
	 * @return Returns the {@link Party}
	 */
	Party getParty(@NonNull UUID party);
	
	/**
	 * Get the player by his {@link UUID}
	 *
	 * @param uuid The {@link UUID} of the player
	 * @return Returns the {@link PartyPlayer} of the relative player
	 */
	PartyPlayer getPartyPlayer(UUID uuid);
	
	/**
	 * Get online parties
	 *
	 * @return Returns a list of {@link Party}
	 */
	List<Party> getOnlineParties();
	
	/**
	 * Get the list of available ranks
	 *
	 * @return Returns a set of {@link PartyRank}
	 */
	Set<PartyRank> getRanks();
	
	/**
	 * Get the list of available colors
	 *
	 * @return Returns a set of {@link PartyColor}
	 */
	Set<PartyColor> getColors();
	
	/**
	 * Reload Parties configuration files
	 * @deprecated Use reloadPlugin()
	 */
	@Deprecated
	default void reloadParties() {
		reloadPlugin();
	}
	
	/**
	 * Send changes to the database. Used to save parties data.
	 *
	 * @param party The {@link Party} to save
	 * @deprecated No longer needed
	 */
	@Deprecated
	default void updateParty(Party party) {
		// Nothing to do
	}
	
	/**
	 * Send changes to the database. Used to save players data.
	 *
	 * @param player The {@link PartyPlayer} to save
	 * @deprecated No longer needed
	 */
	@Deprecated
	default void updatePartyPlayer(PartyPlayer player) {
		// Nothing to do
	}
	
	/**
	 * Send a broadcast message to the party paramParty; It requires a player to
	 * send the message because Parties gets the placeholder info from the player;
	 * If it's a broadcast unrelated on players, you can insert a {@code null}
	 * player.
	 *
	 * @param party   The {@link Party} that will see the message
	 * @param player  The {@link PartyPlayer} who sent the message
	 * @param message The message to broadcast
	 * @deprecated Use Party.broadcastMessage(String, PartyPlayer)
	 */
	@Deprecated
	default void broadcastPartyMessage(Party party, PartyPlayer player, String message) {
		if (party != null) {
			party.broadcastMessage(message, player);
		}
	}
	
	/**
	 * Add the player into the party
	 *
	 * @param player The {@link PartyPlayer} to insert
	 * @param party  The {@link Party}
	 * @return Returns the result of the method as {@link Status}
	 * @deprecated Use Party.addMember(PartyPlayer)
	 */
	@Deprecated
	default Status addPlayerIntoParty(PartyPlayer player, Party party) {
		if (party == null || player == null)
			return Status.NOEXIST;
		if (player.isInParty())
			return Status.ALREADYINPARTY;
		if (party.addMember(player))
			return Status.SUCCESS;
		else
			return Status.PARTYFULL;
	}
	
	/**
	 * Remove the player from the party
	 *
	 * @param player The {@link PartyPlayer} to remove
	 * @return Returns the result of the method as {@link Status}
	 * @deprecated Use Party.removeMember(PartyPlayer)
	 */
	@Deprecated
	default Status removePlayerFromParty(PartyPlayer player) {
		if (player == null)
			return Status.NOEXIST;
		Party party = player.getPartyId() != null ? getParty(player.getPartyId()) : null;
		if (party == null)
			return Status.NOPARTY;
		party.removeMember(player);
		return Status.SUCCESS;
	}
	
	/**
	 * Delete the party
	 *
	 * @param party The {@link Party} to delete
	 * @return Returns the {@link Status} of the method
	 * @deprecated Use Party.delete()
	 */
	@Deprecated
	default Status deleteParty(Party party) {
		if (party == null)
			return Status.NOEXIST;
		party.delete();
		return Status.SUCCESS;
	}
	
	/**
	 * Get a list of online players of the party
	 *
	 * @param party The {@code Party}
	 * @return Returns a list of {@code Set<PartyPlayer>}, if the party
	 * doesn't exist returns {@code null}
	 * @deprecated Use Party.getOnlineMembers(boolean)
	 */
	@Deprecated
	default Set<PartyPlayer> getOnlinePlayers(Party party) {
		if (party == null)
			return null;
		return party.getOnlineMembers(true);
	}
	
	/**
	 * Refresh the online players list of the party
	 *
	 * @param party The {@link Party}
	 * @deprecated No longer needed
	 */
	@Deprecated
	default void refreshOnlinePlayers(Party party) {
		// Nothing to do
	}
	
	/**
	 * Is the player in a party?
	 *
	 * @param player The player UUID to check
	 * @return True if its in a party
	 */
	default boolean isPlayerInParty(UUID player) {
		PartyPlayer partyPlayer = getPartyPlayer(player);
		return partyPlayer != null && partyPlayer.isInParty();
	}
	
	/**
	 * Get list of parties ordered by name
	 *
	 * @param numberOfParties Number of parties to get
	 * @param offset Offset of parties list
	 * @return Returns a list of {@link Party}
	 */
	LinkedList<Party> getPartiesListByName(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of online members
	 *
	 * @param numberOfParties Number of parties to get
	 * @param offset Offset of parties list
	 * @return Returns a list of {@link Party}
	 */
	LinkedList<Party> getPartiesListByOnlineMembers(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of members
	 *
	 * @param numberOfParties Number of parties to get
	 * @param offset Offset of parties list
	 * @return Returns a list of {@link Party}
	 */
	LinkedList<Party> getPartiesListByMembers(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of kills
	 *
	 * @param numberOfParties Number of parties to get
	 * @param offset Offset of parties list
	 * @return Returns a list of {@link Party}
	 */
	LinkedList<Party> getPartiesListByKills(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of experience
	 *
	 * @param numberOfParties Number of parties to get
	 * @param offset Offset of parties list
	 * @return Returns a list of {@link Party}
	 */
	LinkedList<Party> getPartiesListByExperience(int numberOfParties, int offset);
	
	/**
	 * Check if the given players are in the same party
	 *
	 * @param player1 The first player
	 * @param player2 The second player
	 * @return True if they are in the same party
	 */
	boolean areInTheSameParty(UUID player1, UUID player2);
}
