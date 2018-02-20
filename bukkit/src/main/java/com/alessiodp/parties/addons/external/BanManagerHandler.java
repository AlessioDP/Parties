package com.alessiodp.parties.addons.external;

import java.util.UUID;

import org.bukkit.Bukkit;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.hooks.BMHook;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.utils.ConsoleColor;

public class BanManagerHandler {
	private Parties plugin;
	private static final String ADDON_NAME = "BanManager";
	private static BMHook hook;
	
	public BanManagerHandler(Parties instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		hook = null;
		if (ConfigMain.ADDONS_BANMANAGER_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				hook = new BMHook(plugin);
				
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				ConfigMain.ADDONS_BANMANAGER_ENABLE = false;
				
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	
	public static boolean isMuted(UUID uuid) {
		if (hook != null)
			return hook.isMuted(uuid);
		return false;
	}
}
