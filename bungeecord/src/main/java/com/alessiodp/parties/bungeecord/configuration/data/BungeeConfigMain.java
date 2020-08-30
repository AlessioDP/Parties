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
	@ConfigOption(path = "additional.follow-party.perform-commands.enable")
	public static boolean ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE;
	@ConfigOption(path = "additional.follow-party.perform-commands.delay")
	public static int ADDITIONAL_FOLLOW_PERFORMCMD_DELAY;
	@ConfigOption(path = "additional.follow-party.perform-commands.commands")
	public static List<String> ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS;
	@ConfigOption(path = "additional.follow-party.blocked-servers")
	public static List<String> ADDITIONAL_FOLLOW_BLOCKEDSERVERS;
	
	public BungeeConfigMain(PartiesPlugin plugin) {
		super(plugin);
	}
}
