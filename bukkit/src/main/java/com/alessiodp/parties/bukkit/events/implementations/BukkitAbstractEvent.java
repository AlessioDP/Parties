package com.alessiodp.parties.bukkit.events.implementations;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitAbstractEvent extends Event implements PartiesEvent {
	private PartiesAPI api;
	private static final HandlerList HANDLERS = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	@NotNull
	@Override
	public PartiesAPI getApi() {
		return api;
	}
	
	public void setApi(PartiesAPI instance) {
		api = instance;
	}
}
