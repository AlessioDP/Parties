package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import lombok.Getter;

import java.util.List;

public class BungeeConfigMain extends ConfigMain {
	@Getter private final String fileName = "config.yml";
	@Getter private final String resourceName = "bungee/config.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUNGEE_CONFIG_MAIN;
	
	// Additional settings
	@ConfigOption(path = "additional.follow.blocked-servers")
	public static List<String> ADDITIONAL_FOLLOW_BLOCKEDSERVERS;
	
	@ConfigOption(path = "additional.moderation.plugins.bungeechat")
	public static boolean		ADDITIONAL_MODERATION_PLUGINS_BUNGEECHAT;
	
	public BungeeConfigMain(PartiesPlugin plugin) {
		super(plugin);
	}
}
