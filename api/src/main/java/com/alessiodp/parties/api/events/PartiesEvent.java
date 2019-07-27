package com.alessiodp.parties.api.events;

import com.alessiodp.parties.api.interfaces.PartiesAPI;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface PartiesEvent {
	/**
	 * Get the Parties API instance
	 *
	 * @return Returns the {@link PartiesAPI}
	 */
	@NonNull
	PartiesAPI getApi();
	
	/**
	 * Set the Parties API instance. Used by Parties instance to let you hook directly to the main API.
	 *
	 * @param instance {@link PartiesAPI} instance to set
	 */
	void setApi(PartiesAPI instance);
}
