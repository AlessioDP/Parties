package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import com.alessiodp.parties.bukkit.addons.external.hooks.PAPIHook;

import java.util.UUID;

@RequiredArgsConstructor
public class PlaceholderAPIHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "PlaceholderAPI";
	private static boolean active;
	private static PAPIHook hook;
	
	public void init() {
		active = false;
		if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			hook = new PAPIHook(plugin);
			if (hook.register()) {
				active = true;
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
						.replace("{addon}", ADDON_NAME), true);
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
