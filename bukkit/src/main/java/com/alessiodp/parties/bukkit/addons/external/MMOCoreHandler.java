package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.Indyuce.mmocore.api.experience.EXPSource;
import net.Indyuce.mmocore.api.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

@RequiredArgsConstructor
public class MMOCoreHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "MMOCore";
	private static boolean active;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MMOCORE_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MMOCORE_ENABLE = false;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
				
			}
		}
	}
	
	public static void giveExp(UUID player, Location location, double experience) {
		if (active) {
			PlayerData.get(player).giveExperience((int) experience, location, EXPSource.OTHER);
		}
	}
}
