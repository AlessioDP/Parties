package com.alessiodp.parties.bukkit.addons.external;

import java.util.UUID;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.utils.ConsoleColor;
import org.bukkit.Bukkit;

import com.alessiodp.parties.bukkit.addons.external.hooks.BMHook;

public class BanManagerHandler {
	private PartiesPlugin plugin;
	private static final String ADDON_NAME = "BanManager";
	private static BMHook hook;
	
	public BanManagerHandler(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public void init() {
		hook = null;
		if (BukkitConfigMain.ADDONS_BANMANAGER_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				hook = new BMHook(plugin);
				
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				BukkitConfigMain.ADDONS_BANMANAGER_ENABLE = false;
				
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
