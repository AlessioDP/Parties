package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.addons.external.hooks.MagicHook;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MagicHandler {
	@NotNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "Magic";
	private static boolean active;
	private boolean registered = false;
	
	public void init() {
		active = false;
		if (ConfigParties.ADDITIONAL_FRIENDLYFIRE_ENABLE && ConfigParties.ADDITIONAL_FRIENDLYFIRE_PREVENT_DAMAGE_MAGIC) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				Plugin magicPlugin = Bukkit.getPluginManager().getPlugin(ADDON_NAME);
				active = true;
				
				if (!registered) { // Register only 1 time
					registered = true;
					MagicHook magicHook = new MagicHook(plugin);
					magicHook.register(magicPlugin);
				}
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
				return;
			}
			
			ConfigParties.ADDITIONAL_FRIENDLYFIRE_PREVENT_DAMAGE_MAGIC = false;
			plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
		}
	}
	
	public static boolean isActive() {
		return active;
	}
}
