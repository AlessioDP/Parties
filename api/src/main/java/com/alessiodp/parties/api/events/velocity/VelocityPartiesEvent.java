package com.alessiodp.parties.api.events.velocity;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;
import org.jetbrains.annotations.NotNull;

public abstract class VelocityPartiesEvent implements PartiesEvent {
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
