package com.alessiodp.parties.common.events;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartiesAPI;

public interface PartiesEventApi extends PartiesEvent {
	/**
	 * Set the Parties API
	 */
	void setApi(PartiesAPI intance);
}
