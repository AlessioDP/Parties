package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AsyncEvents;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class LevelPointsHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "LevelPoints";
	private static boolean active;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_LEVELPOINTS_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_LEVELPOINTS_ENABLE = false;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
				
			}
		}
	}
	
	public static void giveExp(Player player, double experience) {
		if (active) {
			PlayerContainer playerContainer = AsyncEvents.getPlayerContainer(player);
			if (playerContainer != null) {
				try {
					playerContainer.addEXP(experience);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
