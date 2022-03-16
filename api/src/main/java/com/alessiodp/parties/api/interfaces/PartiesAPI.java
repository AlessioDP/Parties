package com.alessiodp.parties.api.interfaces;

import com.alessiodp.parties.api.enums.Status;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PartiesAPI {
	
	/**
	 * Get main plugin options
	 *
	 * @return the {@link PartiesOptions}
	 */
	PartiesOptions getOptions();
	
	/**
	 * Reload Parties configuration files
	 */
	void reloadPlugin();
	
	/**
	 * Check if the plugin have BungeeCord option enabled
	 *
	 * @return true if BungeeCord support is enabled
	 */
	boolean isBungeeCordEnabled();
	
	/**
	 * Create a party
	 *
	 * @param party  the party name
	 * @param leader the leader of the party as {@link PartyPlayer}, null if the party should be fixed
	 * @return true if successfully created
	 */
	boolean createParty(@Nullable String party, @Nullable PartyPlayer leader);
	
	/**
	 * Get the party by its name
	 *
	 * @param party the name of the {@link Party}
	 * @return the {@link Party}
	 */
	@Nullable Party getParty(@NotNull String party);
	
	/**
	 * Get the party by its id
	 *
	 * @param party the id of the {@link Party}
	 * @return the {@link Party}
	 */
	@Nullable Party getParty(@NotNull UUID party);
	
	/**
	 * Get the player by his {@link UUID}
	 *
	 * @param uuid the {@link UUID} of the player
	 * @return the {@link PartyPlayer} of the relative player
	 */
	@Nullable PartyPlayer getPartyPlayer(@NotNull UUID uuid);
	
	/**
	 * Get the party of the player
	 *
	 * @param uuid the {@link UUID} of the player
	 * @return the {@link Party} of the relative player
	 */
	@Nullable Party getPartyOfPlayer(@NotNull UUID uuid);
	
	/**
	 * Get online parties
	 *
	 * @return a list of {@link Party}
	 */
	@NotNull List<Party> getOnlineParties();
	
	/**
	 * Get the list of available ranks
	 *
	 * @return a set of {@link PartyRank}
	 */
	@NotNull Set<PartyRank> getRanks();
	
	/**
	 * Get the list of available colors
	 *
	 * @return a set of {@link PartyColor}
	 */
	@NotNull Set<PartyColor> getColors();
	
	/**
	 * Reload Parties configuration files
	 * @deprecated use reloadPlugin() instead
	 */
	@Deprecated
	default void reloadParties() {
		reloadPlugin();
	}
	
	/**
	 * Send changes to the database. Used to save parties data.
	 *
	 * @param party the {@link Party} to save
	 * @deprecated no longer needed
	 */
	@Deprecated
	default void updateParty(Party party) {
		// Nothing to do
	}
	
	/**
	 * Send changes to the database. Used to save players data.
	 *
	 * @param player the {@link PartyPlayer} to save
	 * @deprecated no longer needed
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
	 * @param party   the {@link Party} that will see the message
	 * @param player  the {@link PartyPlayer} who sent the message
	 * @param message the message to broadcast
	 * @deprecated use Party.broadcastMessage(String, PartyPlayer) instead
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
	 * @param player the {@link PartyPlayer} to insert
	 * @param party  the {@link Party}
	 * @return the result of the method as {@link Status}
	 * @deprecated use Party.addMember(PartyPlayer) instead
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
	 * @param player the {@link PartyPlayer} to remove
	 * @return the result of the method as {@link Status}
	 * @deprecated use Party.removeMember(PartyPlayer) instead
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
	 * @param party the {@link Party} to delete
	 * @return the {@link Status} of the method
	 * @deprecated use Party.delete() instead
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
	 * @param party the {@code Party}
	 * @return a list of {@code Set<PartyPlayer>}, if the party
	 * doesn't exist returns {@code null}
	 * @deprecated use Party.getOnlineMembers(boolean) instead
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
	 * @param party the {@link Party}
	 * @deprecated no longer needed
	 */
	@Deprecated
	default void refreshOnlinePlayers(Party party) {
		// Nothing to do
	}
	
	/**
	 * Is the player in a party?
	 *
	 * @param player the player UUID to check
	 * @return true if its in a party
	 */
	default boolean isPlayerInParty(UUID player) {
		PartyPlayer partyPlayer = getPartyPlayer(player);
		return partyPlayer != null && partyPlayer.isInParty();
	}
	
	/**
	 * Get list of parties ordered by name
	 *
	 * @param numberOfParties number of parties to get
	 * @param offset offset of parties list
	 * @return a list of {@link Party}
	 */
	@NotNull LinkedList<Party> getPartiesListByName(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of online members
	 *
	 * @param numberOfParties number of parties to get
	 * @param offset offset of parties list
	 * @return a list of {@link Party}
	 */
	@NotNull LinkedList<Party> getPartiesListByOnlineMembers(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of members
	 *
	 * @param numberOfParties number of parties to get
	 * @param offset offset of parties list
	 * @return a list of {@link Party}
	 */
	@NotNull LinkedList<Party> getPartiesListByMembers(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of kills
	 *
	 * @param numberOfParties number of parties to get
	 * @param offset offset of parties list
	 * @return a list of {@link Party}
	 */
	@NotNull LinkedList<Party> getPartiesListByKills(int numberOfParties, int offset);
	
	/**
	 * Get list of parties ordered by number of experience
	 *
	 * @param numberOfParties number of parties to get
	 * @param offset offset of parties list
	 * @return a list of {@link Party}
	 */
	@NotNull LinkedList<Party> getPartiesListByExperience(int numberOfParties, int offset);
	
	/**
	 * Check if the given players are in the same party
	 *
	 * @param player1 the first player
	 * @param player2 the second player
	 * @return true if they are in the same party
	 */
	boolean areInTheSameParty(UUID player1, UUID player2);
}
