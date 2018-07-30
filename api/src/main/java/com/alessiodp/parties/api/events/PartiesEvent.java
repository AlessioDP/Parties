package com.alessiodp.parties.api.events;

import com.alessiodp.parties.api.interfaces.PartiesAPI;
import org.jetbrains.annotations.NotNull;

public interface PartiesEvent {
	/**
	 * Get the Parties API instance
	 *
	 * @return Returns the {@link PartiesAPI}
	 */
	@NotNull
	PartiesAPI getApi();
}
