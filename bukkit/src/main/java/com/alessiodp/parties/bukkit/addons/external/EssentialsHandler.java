package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.earth2me.essentials.IEssentials;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public class EssentialsHandler {
	@NotNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "Essentials";
	private static boolean active;
	
	private static Plugin essentials;
	
	public void init() {
		active = false;
		if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			active = true;
			essentials = Bukkit.getPluginManager().getPlugin(ADDON_NAME);
			
			plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
		}
	}
	
	public static void updateLastTeleportLocation(UUID player) {
		if (active && essentials != null) {
			((IEssentials) essentials).getUser(player).setLastLocation();
		}
	}
	
	public static boolean isPlayerMuted(UUID player) {
		if (active && essentials != null && BukkitConfigMain.ADDITIONAL_MODERATION_PLUGINS_ESSENTIALS) {
			return ((IEssentials) essentials).getUser(player).isMuted();
		}
		return false;
	}
}
