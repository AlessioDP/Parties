package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartiesPlayerLeaveEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private Player player;
	private String party;
	private boolean isKicked;
	private UUID kickedBy;
	
	public PartiesPlayerLeaveEvent(Player player, String party, boolean isKicked, UUID kickedBy) {
		this.player = player;
		this.party = party;
		this.isKicked = isKicked;
		this.kickedBy = kickedBy;
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
	 * @return Party name
	 */
	public String getParty() {
		return party;
	}
	/**
	 * Has been kicked?
	 * 
	 * @return boolean
	 */
	public boolean isKicked() {
		return isKicked;
	}
	/**
	 * Gets the kicker
	 * 
	 * @return UUID of the kicker
	 */
	public UUID getKicker() {
		return kickedBy;
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
