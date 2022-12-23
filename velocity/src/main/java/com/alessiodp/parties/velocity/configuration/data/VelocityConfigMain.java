package com.alessiodp.parties.velocity.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import lombok.Getter;

import java.util.List;

public class VelocityConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "velocity/config.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_VELOCITY_CONFIG_MAIN;
	
	
	// Plugin settings
	@ConfigOption(path = "parties.bungeecord.redis-support")
	public static boolean		PARTIES_BUNGEECORD_REDIS;
	
	
	// Additional settings
	@ConfigOption(path = "additional.follow.blocked-servers")
	public static List<String> ADDITIONAL_FOLLOW_BLOCKEDSERVERS;
	
	public VelocityConfigMain(PartiesPlugin plugin) {
		super(plugin);
	}
}
