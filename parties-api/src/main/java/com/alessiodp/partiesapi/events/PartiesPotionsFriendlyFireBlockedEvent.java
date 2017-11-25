package com.alessiodp.partiesapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PotionSplashEvent;

public class PartiesPotionsFriendlyFireBlockedEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	
	private Player victim;
	private Player attacker;
	private PotionSplashEvent originalEvent;

	public PartiesPotionsFriendlyFireBlockedEvent(Player victim, Player attacker, PotionSplashEvent originalEvent) {
		this.victim = victim;
		this.attacker = attacker;
		this.originalEvent = originalEvent;
	}
	
	
	/**
	 * Gets the victim of the event
	 * 
	 * @return The player entity
	 */
	public Player getVictim() {
		return victim;
	}
	/**
	 * Gets the attacker that threw the potion
	 * 
	 * @return The player entity
	 */
	public Player getAttacker() {
		return attacker;
	}
	/**
	 * Gets the original Bukkit event handled by Parties
	 * 
	 * @return The original Bukkit event
	 */
	public PotionSplashEvent getOriginalEvent() {
		return originalEvent;
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
