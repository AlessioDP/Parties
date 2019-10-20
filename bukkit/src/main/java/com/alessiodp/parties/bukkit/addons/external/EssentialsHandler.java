package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.PartiesPlugin;
import com.earth2me.essentials.IEssentials;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class EssentialsHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "Essentials";
	private static boolean active;
	
	private static Plugin essentials;
	
	public void init() {
		active = false;
		if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			active = true;
			essentials = Bukkit.getPluginManager().getPlugin(ADDON_NAME);
			
			plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
					.replace("{addon}", ADDON_NAME), true);
		}
	}
	
	public static void updateLastTeleportLocation(Player player) {
		if (active && essentials != null) {
			((IEssentials) essentials).getUser(player).setLastLocation();
		}
	}
}
