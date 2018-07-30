package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.utils.ConsoleColor;
import org.bukkit.Bukkit;

import com.alessiodp.parties.bukkit.addons.external.hooks.PAPIHook;

import java.util.UUID;

public class PlaceholderAPIHandler {
	private PartiesPlugin plugin;
	private static final String ADDON_NAME = "PlaceholderAPI";
	private static boolean active;
	private static PAPIHook hook;
	
	public PlaceholderAPIHandler(PartiesPlugin instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		active = false;
		if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			hook = new PAPIHook(plugin);
			if (hook.register()) {
				active = true;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			}
		}
	}
	
	public static String getPlaceholders(UUID uuid, String message) {
		String ret = message;
		if (active && hook != null)
			ret = hook.setPlaceholders(Bukkit.getOfflinePlayer(uuid), message);
		return ret;
	}
}
