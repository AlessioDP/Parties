package com.alessiodp.parties.api.events.bukkit;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesEvent extends Event implements PartiesEvent {
	private PartiesAPI api;
	private static final HandlerList HANDLERS = new HandlerList();
	
	public BukkitPartiesEvent(boolean async) {
		super(async);
	}
	
	@Override
	@NotNull
	public PartiesAPI getApi() {
		return api;
	}
	
	@Override
	public void setApi(PartiesAPI instance) {
		api = instance;
	}
	
	@Override
	@NotNull
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
