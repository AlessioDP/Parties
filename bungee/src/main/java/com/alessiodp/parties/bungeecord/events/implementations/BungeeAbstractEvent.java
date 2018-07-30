package com.alessiodp.parties.bungeecord.events.implementations;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

public abstract class BungeeAbstractEvent extends Event implements PartiesEvent {
	private PartiesAPI api;
	
	@NotNull
	@Override
	public PartiesAPI getApi() {
		return api;
	}
	
	public void setApi(PartiesAPI instance) {
		api = instance;
	}
}
