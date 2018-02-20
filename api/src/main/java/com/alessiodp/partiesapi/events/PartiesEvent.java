package com.alessiodp.partiesapi.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class PartiesEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();
	
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
