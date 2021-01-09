package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.Getter;

public class BungeeConfigParties extends ConfigParties {
	@Getter private final String fileName = "parties.yml";
	@Getter private final String resourceName = "bungee/parties.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUNGEE_CONFIG_PARTIES;
	
	@ConfigOption(path = "additional.home.cross-server")
	public static boolean		ADDITIONAL_HOME_CROSS_SERVER;
	@ConfigOption(path = "additional.home.cross-server-delay")
	public static int			ADDITIONAL_HOME_CROSS_SERVER_DELAY;
	
	public BungeeConfigParties(PartiesPlugin plugin) {
		super(plugin);
	}
}
