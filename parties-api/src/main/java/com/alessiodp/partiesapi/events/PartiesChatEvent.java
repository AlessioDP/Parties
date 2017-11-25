package com.alessiodp.partiesapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartiesChatEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private Player player;
	private String partyName;
	private String message;
	
	public PartiesChatEvent(Player player, String partyName, String message) {
		this.player = player;
		this.partyName = partyName;
		this.message = message;
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
	 * Gets the party name
	 * 
	 * @return Party name as string
	 */
	public String getPartyName() {
		return partyName;
	}
	/**
	 * Gets the player message
	 * 
	 * @return The message as String
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Sets the player message
	 * 
	 * @param message	Message as String
	 */
	public void setMessage(String message) {
		this.message = message;
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
