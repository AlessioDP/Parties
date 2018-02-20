package com.alessiodp.parties.addons.external;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.hooks.PAPIHook;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.utils.ConsoleColor;

public class PlaceholderAPIHandler {
	private Parties plugin;
	private static final String ADDON_NAME = "PlaceholderAPI";
	private static boolean active;
	private static PAPIHook hook;
	
	public PlaceholderAPIHandler(Parties instance) {
		plugin = instance;
		init();
	}
	
	public void init() {
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
	
	public static String getPlaceholders(Player player, String message) {
		String ret = message;
		if (active && hook != null)
			ret = hook.setPlaceholders(player, message);
		return ret;
	}
}
