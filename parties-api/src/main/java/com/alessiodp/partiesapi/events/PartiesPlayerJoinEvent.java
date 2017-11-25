package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartiesPlayerJoinEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private Player player;
	private String party;
	private boolean isInvited;
	private UUID invitedBy;
	
	public PartiesPlayerJoinEvent(Player player, String party, boolean isInvited, UUID invitedBy) {
		this.player = player;
		this.party = party;
		this.isInvited = isInvited;
		this.invitedBy = invitedBy;
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
	 * Has been invited?
	 * 
	 * @return boolean
	 */
	public boolean isInvited() {
		return isInvited;
	}
	/**
	 * Gets the inviter
	 * 
	 * @return UUID of the inviter
	 */
	public UUID getInviter() {
		return invitedBy;
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
