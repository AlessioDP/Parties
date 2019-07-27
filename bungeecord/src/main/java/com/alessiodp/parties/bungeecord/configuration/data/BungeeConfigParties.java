package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.Getter;

public class BungeeConfigParties extends ConfigParties {
	@Getter private final String fileName = "parties.yml";
	@Getter private final String resourceName = "bungee/parties.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUNGEE_CONFIG_PARTIES;
	
	public BungeeConfigParties(PartiesPlugin plugin) {
		super(plugin);
	}
}
