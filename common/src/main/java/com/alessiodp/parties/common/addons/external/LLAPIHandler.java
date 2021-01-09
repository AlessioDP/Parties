package com.alessiodp.parties.common.addons.external;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.addons.external.hooks.LLAPIHook;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LLAPIHandler {
	private static ADPPlugin plugin;
	private static final String ADDON_NAME = "LastLoginAPI";
	private static boolean active;
	private static LLAPIHook hook;
	
	public LLAPIHandler(@NonNull ADPPlugin adpPlugin) {
		plugin = adpPlugin;
	}
	
	public void init() {
		active = false;
		if (plugin.isPluginEnabled(ADDON_NAME)) {
			hook = new LLAPIHook(plugin);
			if (hook.register()) {
				active = true;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isEnabled() {
		return active;
	}
	
	public static String getPlayerName(UUID uuid) {
		String ret;
		if (active) {
			ret = hook.getPlayerName(uuid);
		} else {
			User user = plugin.getPlayer(uuid);
			ret = user != null ? user.getName() : "";
		}
		return ret;
	}
	
	public static Set<UUID> getPlayerByName(String name) {
		Set<UUID> ret;
		if (active) {
			ret = hook.getPlayerByName(name);
		} else {
			ret = new HashSet<>();
			User user = plugin.getPlayerByName(name);
			if (user != null)
				ret.add(user.getUUID());
		}
		
		return ret;
	}
}
