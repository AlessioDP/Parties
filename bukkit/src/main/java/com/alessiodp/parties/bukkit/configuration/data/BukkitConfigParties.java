package com.alessiodp.parties.bukkit.configuration.data;

import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.Getter;

public class BukkitConfigParties extends ConfigParties {
	@Getter private final String fileName = "parties.yml";
	@Getter private final String resourceName = "bukkit/parties.yml";
	@Getter private final int latestVersion = PartiesConstants.VERSION_BUKKIT_CONFIG_PARTIES;
	
	// Additional settings
	public static boolean		FRIENDLYFIRE_ENABLE;
	public static String		FRIENDLYFIRE_TYPE;
	public static boolean		FRIENDLYFIRE_WARNONFIGHT;
	public static boolean		FRIENDLYFIRE_CRACKSHOT_ENABLE;
	
	public static boolean		HOME_ENABLE;
	public static int			HOME_DELAY;
	public static boolean		HOME_HIT;
	public static boolean		HOME_MOVING;
	public static int			HOME_DISTANCE;
	
	public static boolean		KILLS_ENABLE;
	public static boolean		KILLS_MOB_NEUTRAL;
	public static boolean		KILLS_MOB_HOSTILE;
	public static boolean		KILLS_MOB_PLAYERS;
	
	
	public BukkitConfigParties(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		// Bukkit configurations
		// Additional settings
		FRIENDLYFIRE_ENABLE = true;
		FRIENDLYFIRE_TYPE = "global";
		FRIENDLYFIRE_WARNONFIGHT = true;
		FRIENDLYFIRE_CRACKSHOT_ENABLE = false;
		
		HOME_ENABLE = false;
		HOME_DELAY = 0;
		HOME_HIT = true;
		HOME_MOVING = true;
		HOME_DISTANCE = 3;
		
		KILLS_ENABLE = false;
		KILLS_MOB_NEUTRAL = false;
		KILLS_MOB_HOSTILE = false;
		KILLS_MOB_PLAYERS = true;
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bukkit configuration
		// Additional settings
		FRIENDLYFIRE_ENABLE = confAdapter.getBoolean("additional.friendly-fire.enable", FRIENDLYFIRE_ENABLE);
		FRIENDLYFIRE_TYPE = confAdapter.getString("additional.friendly-fire.type", FRIENDLYFIRE_TYPE);
		FRIENDLYFIRE_WARNONFIGHT = confAdapter.getBoolean("additional.friendly-fire.warn-players-on-fight", FRIENDLYFIRE_WARNONFIGHT);
		FRIENDLYFIRE_CRACKSHOT_ENABLE = confAdapter.getBoolean("additional.friendly-fire.crackshot.enable", FRIENDLYFIRE_CRACKSHOT_ENABLE);
		
		HOME_ENABLE = confAdapter.getBoolean("additional.home.enable", HOME_ENABLE);
		HOME_DELAY = confAdapter.getInt("additional.home.delay", HOME_DELAY);
		HOME_HIT = confAdapter.getBoolean("additional.home.cancel.hit", HOME_HIT);
		HOME_MOVING = confAdapter.getBoolean("additional.home.cancel.moving", HOME_MOVING);
		HOME_DISTANCE = confAdapter.getInt("additional.home.cancel.distance", HOME_DISTANCE);
		
		KILLS_ENABLE = confAdapter.getBoolean("additional.kills.enable", KILLS_ENABLE);
		KILLS_MOB_NEUTRAL = confAdapter.getBoolean("additional.kills.which-save.neutral-mobs", KILLS_MOB_NEUTRAL);
		KILLS_MOB_HOSTILE = confAdapter.getBoolean("additional.kills.which-save.hostile-mobs", KILLS_MOB_HOSTILE);
		KILLS_MOB_PLAYERS = confAdapter.getBoolean("additional.kills.which-save.players", KILLS_MOB_PLAYERS);
	}
}
