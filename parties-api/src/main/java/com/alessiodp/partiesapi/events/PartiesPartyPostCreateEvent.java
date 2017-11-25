package com.alessiodp.partiesapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartiesPartyPostCreateEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private String party;
	private boolean fixed;
	
	public PartiesPartyPostCreateEvent(Player player, String party, boolean fixed) {
		this.player = player;
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
	 * Gets the name of the party
	 * 
	 * @return Party name
	 */
	public String getPartyName() {
		return party;
	}
	/**
	 * Is the party fixed?
	 * 
	 * @return boolean
	 */
	public boolean isFixed() {
		return fixed;
	}
	
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
