package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;

public class BungeeConfigParties extends ConfigParties {
	
	public BungeeConfigParties(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public String getFileName() {
		return "parties.yml";
	}
	
	@Override
	public String getResourceName() {
		return "bungee/parties.yml";
	}
}
