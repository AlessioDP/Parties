package com.alessiodp.parties.utils.api;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.parties.objects.Rank;

public abstract interface Api
{
	/**
	 *   Create a party
	 * 
	 * @param paramLeader     UUID of the leader
	 * @param paramPartyName  Party name
	 * 
	 * @return Return the result of the method as Status
	 */
	public abstract Status createParty(Player paramLeader, String paramPartyName);
	/**
	 *   Delete a party
	 * 
	 * @param paramParty  Party name
	 * 
	 * @return Return the result of the method as Status
	 */
	public abstract Status deleteParty(String paramParty);
	 /**
	    *  	Add the player into the party
	    *
	    * @param paramPlayer  Player
	    * @param paramParty   Party name
	    * 
	    * @return Return the result of the method as Status
	    */
	public abstract Status addPlayerInParty(Player paramPlayer, String paramParty);
	/**
	    *  	Remove the player Player(paramUUID) from the party
	    *
	    * @param paramUUID    UUID of the player
	    * 
	    * @return Return the result of the method in "Error"
	    */
	public abstract Status removePlayerFromParty(UUID paramUUID);
	/**
	    *  	Have the player Player(paramUUID) a party?
	    *  
	    * @param paramUUID    UUID of the player
	    * 
	    * @return Return if player have a party
	    */
	public abstract boolean haveParty(UUID paramUUID);
	
	/**
	    *  	Is a spy the Player(paramUUID)?
	    *  
	    * @param paramUUID    UUID of the player
	    * 
	    * @return Return if player is a spy
	    */
	public abstract boolean isSpy(UUID paramUUID);
	
	/**
	    *  	Get the party name of Player(paramUUID)
	    * 
	    * @param paramUUID    UUID of the player
	    * 
	    * @return Return party name
	    */
	public abstract String getPartyName(UUID paramUUID);
	
	/**
	    *  	Get the rank of Player(paramUUID)
	    *  
	    * @param paramUUID    UUID of the player
	    * 
	    * @return Return rank value
	    */
	public abstract int getRank(UUID paramUUID);
	
	/**
	    *  	Get the leader of the party paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return UUID of the party leader
	    */
	public abstract UUID getPartyLeader(String paramPartyName);
	
	/**
	    *  	Get the party members of the party paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return UUID ArrayList of the party members
	    */
	public abstract ArrayList<UUID> getPartyMembers(String paramPartyName);
	
	/**
	    *  	Get players online of the party paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return Player ArrayList of the party members online
	    */
	public abstract ArrayList<Player> getPartyOnlinePlayers(String paramPartyName);
	
	/**
	    *  	Get the party description of paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return party description
	    */
	public abstract String getPartyDescription(String paramPartyName);
	
	/**
	    *  	Get the party motd of paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return party motd
	    */
	public abstract String getPartyMotd(String paramPartyName);
	
	/**
	    *  	Get the party prefix of paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return party prefix
	    */
	public abstract String getPartyPrefix(String paramPartyName);
	
	/**
	    *  	Get the party suffix of paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return party suffix
	    */
	public abstract String getPartySuffix(String paramPartyName);
	
	/**
	    *  	Get the party kills of paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return party kills
	    */
	public abstract int getPartyKills(String paramPartyName);
	
	/**
	    *  	Get the party home of paramPartyName
	    *  
	    * @param paramPartyName   Party name
	    * 
	    * @return Return party home as Location
	    */
	public abstract Location getPartyHome(String paramPartyName);
	
	/**
	    *  	Get list of available ranks
	    *  
	    * 
	    * @return Return ranks arraylist
	    */
	public abstract ArrayList<Rank> getRankList();
}
