package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartiesPartyPreCreateEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private Player player;
	private UUID leader;
	private String party;
	private boolean fixed;
	
	public PartiesPartyPreCreateEvent(Player player, String party, boolean fixed) {
		this.player = player;
		if (fixed)
			leader = null;
		else
			leader = player.getUniqueId();
		this.party = party;
		this.fixed = fixed;
	}
	
	/**
	 * Gets the player
	 * 
	 * @return The player entity
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * Gets the uuid of the leader
	 * 
	 * @return Leader uuid, returns null if the party is fixed
	 */
	public UUID getLeader() {
		return leader;
	}
	/**
	 * Set a new uuid of the party leader
	 * 
	 * @param leader	Leader uuid as UUID
	 */
	public void setLeader(UUID leader) {
		this.leader = leader;
	}
	/**
	 * Gets the name of the party
	 * 
	 * @return Party name
	 */
	public String getPartyName() {
		return party;
	}
	/**
	 * Set a new name to the party
	 * 
	 * @param name		Party name as String
	 */
	public void setPartyName(String name) {
		party = name;
	}
	/**
	 * Is the party fixed?
	 * 
	 * @return boolean
	 */
	public boolean isFixed() {
		return fixed;
	}
	/**
	 * Set if the party is fixed
	 * 
	 * @param fixed		boolean
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
