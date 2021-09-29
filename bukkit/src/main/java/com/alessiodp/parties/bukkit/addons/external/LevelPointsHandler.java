package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Player.PlayerStorage;
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
			PlayerStorage playerStorage = LevelPoints.getPlayerStorage();
			if (playerStorage != null) {
				try {
					PlayerData pd = playerStorage.getLoadedData(player.getUniqueId());
					pd.addEXP(experience);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
