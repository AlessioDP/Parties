package com.alessiodp.parties.bukkit.addons.external;

import java.util.UUID;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import com.alessiodp.parties.bukkit.addons.external.hooks.BMHook;

@RequiredArgsConstructor
public class BanManagerHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "BanManager";
	private static BMHook hook;
	
	public void init() {
		hook = null;
		if (BukkitConfigMain.ADDONS_BANMANAGER_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				hook = new BMHook(plugin);
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
						.replace("{addon}", ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDONS_BANMANAGER_ENABLE = false;
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_FAILED
						.replace("{addon}", ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isMuted(UUID uuid) {
		boolean ret = false;
		if (hook != null)
			ret = hook.isMuted(uuid);
		return ret;
	}
}
