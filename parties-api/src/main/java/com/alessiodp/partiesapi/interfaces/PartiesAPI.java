package com.alessiodp.partiesapi.interfaces;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.partiesapi.enums.Status;

public interface PartiesAPI {
	/*
	 * 
	 * Parties based
	 * 
	 */
	/**
	 *	Reload Parties configuration
	 */
	public void reloadParties();
	/**
	 *	Send a broadcast message to the party paramParty;
	 *	It requires a player to send the message because Parties get the placeholder info from the player;
	 *	If it's a broadcast unrelated on players, you can insert leader uuid on paramUUID
	 * 
	 * @param paramParty	Party name
	 * @param paramPlayer	UUID of the player broadcaster
	 * @param paramMessage	Broadcast message
	 * 
	 * @return Return the result of the method as Status
	 */
	public Status broadcastPartyMessage(String paramParty, UUID paramUUID, String paramMessage);
	/**
	 *	Get online parties
	 *
	 *	@return Return the string list of online party names
	 */
	public List<String> getOnlineParties();
	/*
	 * 
	 * Player based
	 * 
	 */
	/**
	 *	Add the player paramPlayer into the party paramParty
	 *	
	 *	@param paramUUID	UUID of the player
	 *	@param paramParty	Party name
	 *	
	 *	@return Return the result of the method as Status
	 */
	public Status addPlayerIntoParty(UUID paramUUID, String paramParty);
	/**
	 *	Remove the player paramUUID from the party
	 *	
	 *	@param paramUUID	UUID of the player
	 *	
	 *	@return Return the result of the method in "Error"
	 */
	public Status removePlayerFromParty(UUID paramUUID);
	
	/**
	 *	Has the player paramUUID a party?
	 *	
	 *	@param paramUUID	UUID of the player
	 *	
	 *	@return Return if player have a party
	 */
	public boolean haveParty(UUID paramUUID);
	
	/**
	 *	Is a spy the Player(paramUUID)?
	 *	
	 *	@param paramUUID	UUID of the player
	 *	
	 *	@return Return if player is a spy
	 */
	public boolean isSpy(UUID paramUUID);
	/**
	 *	Set the player paramUUID as a spy
	 *	
	 *	@param paramUUID	UUID of the player
	 *	@param paramSpy		Boolean
	 */
	public void setSpy(UUID paramUUID, boolean paramSpy);
	
	/**
	 *	Get the party name of Player(paramUUID)
	 *	
	 *	@param paramUUID	UUID of the player
	 *	
	 *	@return Return party name
	 */
	public String getPartyName(UUID paramUUID);
	
	/**
	 *	Get the rank of the player paramUUID
	 *	
	 *	@param paramUUID	UUID of the player
	 *	
	 *	@return Return rank value
	 */
	public int getRank(UUID paramUUID);
	/**
	 *	Set the rank of the player paramUUID
	 *	
	 *	@param paramUUID	UUID of the player
	 *	@param paramRank	New rank
	 */
	public void setRank(UUID paramUUID, int paramRank);
	
	/**
	 *	Get list of available ranks
	 *	
	 *	
	 *	@return Return ranks arraylist
	 */
	public List<Rank> getRankList();
	
	/*
	 * 
	 * Party based
	 * 
	 */
	/**
	 *	Create the party paramParty with the leader paramLeader
	 *	
	 *	@param paramLeader	UUID of the leader
	 *	@param paramParty	Party name
	 *	
	 *	@return Return the result of the method as Status
	 */
	public Status createParty(UUID paramLeader, String paramParty);
	
	/**
	 *	Delete the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return the result of the method as Status
	 */
	public Status deleteParty(String paramParty);
	
	/**
	 *	Get the leader of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return UUID of the party leader, if party doesn't exist returns null
	 */
	public UUID getPartyLeader(String paramParty);
	/**
	 *	Set the leader of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramUUID	UUID of the new leader
	 */
	public void setPartyLeader(String paramParty, UUID paramUUID);
	
	/**
	 *	Get the members of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return the UUID list of the party members, if party doesn't exist returns null
	 */
	public List<UUID> getPartyMembers(String paramParty);
	/**
	 *	Set the member list of the party paramParty
	 *	
	 *	@param paramParty		Party name
	 *	@param paramListPlayers	List of the players
	 */
	public void setPartyMembers(String paramParty, List<UUID> paramListPlayers);
	
	/**
	 *	Get online players of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return the player list of the party members online, if party doesn't exist returns null
	 */
	public List<Player> getPartyOnlinePlayers(String paramParty);
	/**
	 *	Refresh online players list of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 */
	public void refreshOnlinePlayers(String paramParty);
	
	/**
	 *	Is the party paramParty fixed?
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return if it's fixed, if party doesn't exist returns null
	 */
	public boolean isPartyFixed(String paramParty);
	/**
	 *	Set if the party paramParty is fixed
	 *	
	 *	@param paramParty	Party name
	 *	@param paramFixed	Boolean
	 */
	public void setPartyFixed(String paramParty, boolean paramFixed);
	
	/**
	 *	Get the description of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party description, if party doesn't exist returns null
	 */
	public String getPartyDescription(String paramParty);
	/**
	 *	Set the description of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramDescription	New description
	 */
	public void setPartyDescription(String paramParty, String paramDescription);
	
	/**
	 *	Get the motd of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party motd, if party doesn't exist returns null
	 */
	public String getPartyMotd(String paramParty);
	/**
	 *	Set the motd of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramMotd	New motd
	 */
	public void setPartyMotd(String paramParty, String paramMotd);
	
	/**
	 *	Get the home of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party home as Location, if party doesn't exist returns null
	 */
	public Location getPartyHome(String paramParty);
	/**
	 *	Set the home location of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramHome	New home location
	 */
	public void setPartyHome(String paramParty, Location paramHome);
	
	/**
	 *	Get the prefix of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party prefix, if party doesn't exist returns null
	 */
	public String getPartyPrefix(String paramParty);
	/**
	 *	Set the prefix of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramPrefix	New prefix
	 */
	public void setPartyPrefix(String paramParty, String paramPrefix);
	
	/**
	 *	Get the suffix of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party suffix, if party doesn't exist returns null
	 */
	public String getPartySuffix(String paramParty);
	/**
	 *	Set the suffix of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramSuffix	New suffix
	 */
	public void setPartySuffix(String paramParty, String paramSuffix);
	
	/**
	 *	Get the color of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party color, if party doesn't exist returns null
	 */
	public String getPartyColor(String paramParty);
	/**
	 *	Set the color of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramColor	New color
	 */
	public void setPartyColor(String paramParty, String paramColor);
	
	/**
	 *	Get list of available colors
	 *	
	 *	
	 *	@return Return ranks arraylist
	 */
	public List<Color> getColorList();
	
	/**
	 *	Get the kills of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party kills, if party doesn't exist returns -1
	 */
	public int getPartyKills(String paramParty);
	/**
	 *	Set the kills of the party paramParty
	 *	
	 *	@param paramParty	Party name
	 *	@param paramKills	New kills
	 */
	public void setPartyKills(String paramParty, int paramKills);
	
	/**
	 *	Get the password of the party paramParty (The password is HASHED!)
	 *	
	 *	@param paramParty	Party name
	 *	
	 *	@return Return party kills
	 */
	public String getPassword(String paramParty);
	/**
	 *	Set the password of the party paramParty (The password needs to be HASHED!)
	 *	
	 *	@param paramParty	Party name
	 *	@param paramKills	New hashed password
	 */
	public void setPassword(String paramParty, String paramPassword);
}
