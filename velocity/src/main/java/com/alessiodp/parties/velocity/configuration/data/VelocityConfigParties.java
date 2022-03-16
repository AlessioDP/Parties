package com.alessiodp.parties.velocity.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.Getter;

public class VelocityConfigParties extends ConfigParties {
	@Getter private final String fileName = "parties.yml";
	@Getter private final String resourceName = "velocity/parties.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_VELOCITY_CONFIG_PARTIES;
	
	@ConfigOption(path = "additional.home.cross-server")
	public static boolean		ADDITIONAL_HOME_CROSS_SERVER;
	@ConfigOption(path = "additional.home.cross-server-delay")
	public static int			ADDITIONAL_HOME_CROSS_SERVER_DELAY;
	
	@ConfigOption(path = "additional.teleport.exact-location")
	public static boolean		ADDITIONAL_TELEPORT_EXACT_LOCATION;
	@ConfigOption(path = "additional.teleport.exact-location-delay")
	public static int			ADDITIONAL_TELEPORT_EXACT_LOCATION_DELAY;
	
	public VelocityConfigParties(PartiesPlugin plugin) {
		super(plugin);
	}
}
