package com.alessiodp.partiesapi.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartiesPartyPostDeleteEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private String party;
	private DeleteCause cause;
	private UUID player;
	private Player commandSender;
	
	public PartiesPartyPostDeleteEvent(String party, DeleteCause cause, UUID player, Player commandSender) {
		this.party = party;
		this.cause = cause;
		this.player = player;
		this.commandSender = commandSender;
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
	 * Gets the name of the party
	 * 
	 * @return Party name
	 */
	public DeleteCause getCause() {
		return cause;
	}
	/**
	 * Gets the kicked player UUID
	 * 
	 * @return UUID, returns null if delete cause is DELETE, TIMEOUT
	 */
	public UUID getPlayer() {
		return player;
	}
	/**
	 * Gets the player who performed the command
	 * 
	 * @return The player entity, returns null if delete cause is TIMEOUT
	 */
	public Player getSender() {
		return commandSender;
	}
	
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public enum DeleteCause {
		JOIN, LEAVE, KICK, DELETE, BAN, TIMEOUT;
	}
}
