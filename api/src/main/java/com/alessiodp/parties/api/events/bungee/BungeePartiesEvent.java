package com.alessiodp.parties.api.events.bungee;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

public abstract class BungeePartiesEvent extends Event implements PartiesEvent {
	private PartiesAPI api;
	
	@Override
	public @NotNull PartiesAPI getApi() {
		return api;
	}
	
	@Override
	public void setApi(PartiesAPI instance) {
		api = instance;
	}
}
