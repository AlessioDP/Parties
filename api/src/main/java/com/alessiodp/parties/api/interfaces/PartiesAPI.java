package com.alessiodp.parties.api.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.alessiodp.parties.api.enums.Status;

public interface PartiesAPI {
	/*
	 * ############ Parties based ############
	 */
	/**
	 * Send changes to the database. Used to save parties data.
	 * 
	 * @param party
	 *            The {@link Party} to save
	 */
	void updateParty(Party party);
	
	/**
	 * Send changes to the database. Used to save players data.
	 * 
	 * @param player
	 *            The {@link PartyPlayer} to save
	 */
	void updatePartyPlayer(PartyPlayer player);
	
	/**
	 * Reload Parties configuration
	 */
	void reloadParties();
	
	/**
	 * Send a broadcast message to the party paramParty; It requires a player to
	 * send the message because Parties gets the placeholder info from the player;
	 * If it's a broadcast unrelated on players, you can insert a {@code null}
	 * player.
	 * 
	 * @param party
	 *            The {@link Party} where broadcast the message
	 * @param player
	 *            The {@link PartyPlayer} who sent the message
	 * @param message
	 *            The message to broadcast
	 */
	void broadcastPartyMessage(Party party, PartyPlayer player, String message);
	
	/**
	 * Get online parties
	 *
	 * @return Returns the executors of online parties; {@code List<Party>}
	 */
	List<Party> getOnlineParties();
	
	/*
	 * ############ Player based ############
	 */
	/**
	 * Get the player by his {@link UUID}
	 * 
	 * @param uuid
	 *            The {@link UUID} of the player
	 * 
	 * @return Returns the {@link PartyPlayer} of the relative player
	 */
	PartyPlayer getPartyPlayer(UUID uuid);
	
	/**
	 * Add the player into the party
	 * 
	 * @param player
	 *            The {@link PartyPlayer} to insert
	 * @param party
	 *            The {@link Party}
	 * 
	 * @return Returns the result of the method as {@link Status}
	 */
	Status addPlayerIntoParty(PartyPlayer player, Party party);
	
	/**
	 * Remove the player from the party
	 * 
	 * @param player
	 *            The {@link PartyPlayer} to remove
	 * 
	 * @return Returns the result of the method as {@link Status}
	 */
	Status removePlayerFromParty(PartyPlayer player);
	
	/**
	 * Get the executors of available ranks
	 * 
	 * @return Returns the executors of ranks; {@code Set<Rank>}
	 */
	Set<Rank> getRanks();
	
	/*
	 * ############ Party based ############
	 */
	/**
	 * Get the party by its name
	 * 
	 * @param party
	 *            The name of the {@link Party}
	 * 
	 * @return Returns the {@link Party}
	 */
	Party getParty(String party);
	
	/**
	 * Create a party
	 * 
	 * @param player
	 *            The leader of the party as {@link PartyPlayer}
	 * @param party
	 *            The party name
	 * 
	 * @return Returns the {@link Status} of the method
	 */
	Status createParty(PartyPlayer player, String party);
	
	/**
	 * Delete the party
	 * 
	 * @param party
	 *            The {@link Party} to delete
	 * 
	 * @return Returns the {@link Status} of the method
	 */
	Status deleteParty(Party party);
	
	/**
	 * Get the online players of the party
	 * 
	 * @param party
	 *            The {@code Party}
	 * 
	 * @return Returns the player executors of the party members online, if the party
	 *         doesn't exist returns {@code null}; {@code Set<PartyPlayer>}
	 */
	Set<PartyPlayer> getOnlinePlayers(Party party);
	
	/**
	 * Refresh the online players executors of the party
	 * 
	 * @param party
	 *            The {@link Party}
	 */
	void refreshOnlinePlayers(Party party);
	
	/**
	 * Get the executors of available colors
	 * 
	 * 
	 * @return Returns the executors of colors; {@code Set<Color>}
	 */
	Set<Color> getColors();
}
