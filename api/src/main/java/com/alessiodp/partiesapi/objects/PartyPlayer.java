package com.alessiodp.partiesapi.objects;

import java.util.UUID;

import org.bukkit.Bukkit;

public class PartyPlayer {
	
	private UUID playerUUID;
	private String name;
	private long nameTimestamp;
	private int rank;
	private String partyName;
	private boolean spy;
	private boolean preventNotify;
	
	public PartyPlayer(UUID uuid, int defaultRank) {
		playerUUID = uuid;
		name = Bukkit.getOfflinePlayer(uuid).getName() != null ? Bukkit.getOfflinePlayer(uuid).getName() : "";
		nameTimestamp = System.currentTimeMillis();
		rank = defaultRank;
		partyName = "";
		spy = false;
		preventNotify = false;
	}
	
	public PartyPlayer(PartyPlayer copy) {
		playerUUID = copy.playerUUID;
		name = copy.name;
		nameTimestamp = copy.nameTimestamp;
		rank = copy.rank;
		partyName = copy.partyName;
		spy = copy.spy;
		preventNotify = copy.preventNotify;
	}
	
	/**
	 * Get the player {@link UUID}
	 * 
	 * @return Returns the {@link UUID} of the player
	 */
	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	/**
	 * Get the name
	 * 
	 * @return Returns the name of the player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name
	 * 
	 * @param name
	 *            The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the name timestamp
	 * 
	 * @return Returns the name timestamp
	 */
	public long getNameTimestamp() {
		return nameTimestamp;
	}
	
	/**
	 * Set the name timestamp
	 * 
	 * @param nameTimestamp
	 *            The timestamp to set
	 */
	public void setNameTimestamp(long nameTimestamp) {
		this.nameTimestamp = nameTimestamp;
	}
	
	/**
	 * Get the rank level
	 * 
	 * @return Returns the rank level
	 */
	public int getRank() {
		return rank;
	}
	
	/**
	 * Set the rank level
	 * 
	 * @param rank
	 *            The rank level to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	/**
	 * Get the party name
	 * 
	 * @return Returns the party name, empty if the player is not in a party
	 */
	public String getPartyName() {
		return partyName;
	}
	
	/**
	 * Set the party name
	 * 
	 * @param partyName
	 *            The party name to set
	 */
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	/**
	 * Is the player a spy?
	 * 
	 * @return Returns true if the player is a spy
	 */
	public boolean isSpy() {
		return spy;
	}
	
	/**
	 * Set the player as a spy
	 * 
	 * @param value
	 *            True to be a spy
	 */
	public void setSpy(boolean value) {
		spy = value;
	}
	
	/**
	 * Have the player disabled the notifications?
	 * 
	 * @return Returns true if the player is a spy
	 */
	public boolean isPreventNotify() {
		return preventNotify;
	}
	
	/**
	 * Enable or disable the notifications of the player
	 * 
	 * @param value
	 *            True to disable notifications
	 */
	public void setPreventNotify(boolean value) {
		preventNotify = value;
	}
}
