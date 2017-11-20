package com.alessiodp.partiesapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartiesPartyRenameEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private String party;
	private String newName;
	private Player player;
	private boolean isAdmin;
	
	public PartiesPartyRenameEvent(String party, String newName, Player player, boolean isAdmin) {
		this.party = party;
		this.newName = newName;
		this.player = player;
		this.isAdmin = isAdmin;
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
	 * Gets the new party name
	 * 
	 * @return New party name
	 */
	public String getNewPartyName() {
		return newName;
	}
	/**
	 * Set the new name of the party
	 * 
	 * @param name		New party name as String
	 */
	public void setNewPartyName(String name) {
		newName = name;
	}
	/**
	 * Gets the player who performed the command
	 * 
	 * @return The player entity
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * Is the party fixed?
	 * 
	 * @return boolean
	 */
	public boolean isAdmin() {
		return isAdmin;
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
