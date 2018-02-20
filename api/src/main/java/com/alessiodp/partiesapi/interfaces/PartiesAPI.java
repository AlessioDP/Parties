package com.alessiodp.partiesapi.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.enums.Status;
import com.alessiodp.partiesapi.objects.Party;
import com.alessiodp.partiesapi.objects.PartyPlayer;

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
	 * 
	 * @return Returns the {@code Status} of the method
	 */
	Status broadcastPartyMessage(Party party, PartyPlayer player, String message);
	
	/**
	 * @deprecated Replaced by
	 *             {@link #broadcastPartyMessage(Party, PartyPlayer, String)}
	 */
	@Deprecated
	Status broadcastPartyMessage(String party, UUID uuid, String message);
	
	/**
	 * Get online parties
	 *
	 * @return Returns the list of online parties; {@code List<Party>}
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
	 * @deprecated Replaced by {@link #addPlayerIntoParty(PartyPlayer, Party)}
	 */
	@Deprecated
	Status addPlayerIntoParty(UUID paramUUID, String paramParty);
	
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
	 * @deprecated Replaced by {@link #removePlayerFromParty(PartyPlayer)}
	 */
	@Deprecated
	Status removePlayerFromParty(UUID uuid);
	
	/**
	 * @deprecated Replaced by {@link PartyPlayer#getPartyName()}
	 */
	@Deprecated
	boolean haveParty(UUID uuid);
	
	/**
	 * @deprecated Replaced by {@link PartyPlayer#isSpy()}
	 */
	@Deprecated
	boolean isSpy(UUID uuid);
	
	/**
	 * @deprecated Replaced by {@link PartyPlayer#setSpy(boolean)}
	 */
	@Deprecated
	void setSpy(UUID uuid, boolean isSpy);
	
	/**
	 * @deprecated Replaced by {@link PartyPlayer#getPartyName()}
	 */
	@Deprecated
	String getPartyName(UUID uuid);
	
	/**
	 * @deprecated Replaced by {@link PartyPlayer#getRank()}
	 */
	@Deprecated
	int getRank(UUID uuid);
	
	/**
	 * @deprecated Replaced by {@link PartyPlayer#setRank(int)}
	 */
	@Deprecated
	void setRank(UUID uuid, int rank);
	
	/**
	 * Get the list of available ranks
	 * 
	 * @return Returns the list of ranks; {@code Set<Rank>}
	 */
	Set<Rank> getRanks();
	
	/**
	 * @deprecated Replaced by {@link #getRanks()}
	 */
	@Deprecated
	List<Rank> getRankList();
	
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
	 * @deprecated Replaced by {@link #createParty(PartyPlayer, String)}
	 */
	@Deprecated
	Status createParty(UUID leader, String party);
	
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
	 * @deprecated Replaced by {@link #deleteParty(Party)}
	 */
	@Deprecated
	Status deleteParty(String party);
	
	/**
	 * @deprecated Replaced by {@link Party#getLeader()} object
	 */
	@Deprecated
	UUID getPartyLeader(String party);
	
	/**
	 * @deprecated Replaced by {@link Party#setLeader(UUID)} object
	 */
	@Deprecated
	void setPartyLeader(String party, UUID leader);
	
	/**
	 * @deprecated Replaced by {@link Party#getMembers()} object
	 */
	@Deprecated
	List<UUID> getPartyMembers(String party);
	
	/**
	 * @deprecated Replaced by {@link Party#setMembers(List)} object
	 */
	@Deprecated
	void setPartyMembers(String party, List<UUID> list);
	
	/**
	 * Get the online players of the party
	 * 
	 * @param party
	 *            The {@code Party}
	 * 
	 * @return Returns the player list of the party members online, if the party
	 *         doesn't exist returns {@code null}; {@code Set<Player>}
	 */
	Set<Player> getPartyOnlinePlayers(Party party);
	
	/**
	 * @deprecated Replaced by {@link #getPartyOnlinePlayers(Party)}
	 */
	@Deprecated
	List<Player> getPartyOnlinePlayers(String party);
	
	/**
	 * Refresh the online players list of the party
	 * 
	 * @param party
	 *            The {@link Party}
	 */
	void refreshOnlinePlayers(Party party);
	
	/**
	 * @deprecated Replaced by {@link #getPartyOnlinePlayers(Party)}
	 */
	@Deprecated
	void refreshOnlinePlayers(String party);
	
	/**
	 * @deprecated Replaced by {@link Party#isFixed()} object
	 */
	@Deprecated
	boolean isPartyFixed(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setFixed(boolean)}
	 */
	@Deprecated
	void setPartyFixed(String paramParty, boolean paramFixed);
	
	/**
	 * @deprecated Replaced by {@link Party#getDescription()}
	 */
	@Deprecated
	String getPartyDescription(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setDescription(String)}
	 */
	@Deprecated
	void setPartyDescription(String paramParty, String paramDescription);
	
	/**
	 * @deprecated Replaced by {@link Party#getMotd()}
	 */
	@Deprecated
	String getPartyMotd(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setMotd(String)}
	 */
	@Deprecated
	void setPartyMotd(String paramParty, String paramMotd);
	
	/**
	 * @deprecated Replaced by {@link Party#getHome()}
	 */
	@Deprecated
	Location getPartyHome(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setHome(Location)}
	 */
	@Deprecated
	void setPartyHome(String paramParty, Location paramHome);
	
	/**
	 * @deprecated Replaced by {@link Party#getPrefix()} object
	 */
	@Deprecated
	String getPartyPrefix(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setSuffix(String)} object
	 */
	@Deprecated
	void setPartyPrefix(String paramParty, String paramPrefix);
	
	/**
	 * @deprecated Replaced by {@link Party#getSuffix()} object
	 */
	@Deprecated
	String getPartySuffix(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setSuffix(String)} object
	 */
	@Deprecated
	void setPartySuffix(String paramParty, String paramSuffix);
	
	/**
	 * @deprecated Replaced by {@link Party#getColor()} object
	 */
	@Deprecated
	String getPartyColor(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setColor(Color)}
	 */
	@Deprecated
	void setPartyColor(String paramParty, String paramColor);
	
	/**
	 * Get the list of available colors
	 * 
	 * 
	 * @return Returns the list of colors; {@code Set<Color>}
	 */
	Set<Color> getColors();
	
	/**
	 * @deprecated Replaced by {@link #getColors()}
	 */
	@Deprecated
	List<Color> getColorList();
	
	/**
	 * @deprecated Replaced by {@link Party#getKills()}
	 */
	@Deprecated
	int getPartyKills(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setKills(int)}
	 */
	@Deprecated
	void setPartyKills(String paramParty, int paramKills);
	
	/**
	 * @deprecated Replaced by {@link Party#getPassword()}
	 */
	@Deprecated
	String getPassword(String paramParty);
	
	/**
	 * @deprecated Replaced by {@link Party#setPassword(String)}
	 */
	@Deprecated
	void setPassword(String paramParty, String paramPassword);
}
