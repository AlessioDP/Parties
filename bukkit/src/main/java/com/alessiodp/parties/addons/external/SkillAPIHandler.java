package com.alessiodp.parties.addons.external;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.utils.ConsoleColor;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.listener.ListenerUtil;
import com.sucy.skill.listener.MechanicListener;

public class SkillAPIHandler {
	private static final String ADDON_NAME = "SkillAPI";
	
	public SkillAPIHandler() {
		init();
	}
	
	private void init() {
		if (ConfigMain.ADDITIONAL_EXP_ENABLE
				&& ConfigMain.ADDITIONAL_EXP_ADDONS_SKILLAPI_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				ConfigMain.ADDITIONAL_EXP_ADDONS_SKILLAPI_ENABLE = false;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	
	public static void blockEvent(Entity entity) {
		if (!SkillAPI.getSettings().isUseOrbs()) {
			// Used to block SkillAPI experience gain on kill event (otherwise killer will get exp 2 times)
			SkillAPI.setMeta(entity, MechanicListener.SUMMON_DAMAGE, true);
		}
	}
	public static double getExp(Entity entity) {
		double ret = 0;
		if (!SkillAPI.getSettings().isUseOrbs()) {
			ret = SkillAPI.getSettings().getYield(ListenerUtil.getName(entity));
		}
		return ret;
	}
	
	public static void giveExp(OfflinePlayer player, double exp) {
		SkillAPI.getPlayerData(player).giveExp(exp, ExpSource.valueOf(ConfigMain.ADDITIONAL_EXP_ADDONS_SKILLAPI_SOURCE));
	}
}
