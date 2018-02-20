package com.alessiodp.parties.addons.external;

/*
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
*/
import org.bukkit.event.Listener;

import com.alessiodp.parties.Parties;
/*
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.utils.ConsoleColor;
import com.alessiodp.parties.utils.PartiesUtils;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
*/

public class MythicMobsHandler implements Listener {
	private Parties plugin;
	private static final String ADDON_NAME = "MythicMobs";
	private static boolean active;
	
	
	public MythicMobsHandler(Parties instance) {
		plugin = instance;
		init();
	}
	
	private void init() {
		active = false;
		/*
		if (ConfigMain.ADDITIONAL_EXP_ENABLE &&
				ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				plugin.getServer().getPluginManager().registerEvents(this, plugin);
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_ENABLE = false;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}*/
	}
	/*
	public static boolean isMythicMob(Entity entity) {
		boolean ret = false;
		if (active && MythicMobs.inst().getAPIHelper().isMythicMob(entity))
			ret = true;
		return ret;
	}
	
	@EventHandler
	public void onMythicMobDeath(MythicMobDeathEvent event) {
		if (active && event.getKiller() instanceof Player) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_MYTHICMOBS_HANDLING, true);
			Entity killedMob = event.getEntity();
			Player killer = (Player) event.getKiller();
			// Count experience
			double vanillaExp = 0;
			double skillApiExp = 0;
			if (ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_VANILLA)
				vanillaExp = event.getExp();
			if (ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_SKILLAPI)
				skillApiExp = event.getExperienceSkillAPI();
			
			if (PartiesUtils.handleExperienceDistribution(killer, killedMob, event.getMobType().getDisplayName(), vanillaExp, skillApiExp)) {
				// Remove exp from MythicMobs event
				if (ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_VANILLA) {
					event.setExp(0);
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_MYTHICMOBS_SET
							.replace("{type}", "vanilla"), true);
				}
				if (ConfigMain.ADDITIONAL_EXP_ADDONS_MYTHICMOBS_GET_SKILLAPI) {
					event.setExperienceSkillAPI(0);
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_MYTHICMOBS_SET
							.replace("{type}", "skillapi"), true);
				}
			}
		}
	}
	*/
}
