package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.utils.ConsoleColor;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.listener.ListenerUtil;
import com.sucy.skill.listener.MechanicListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class SkillAPIHandler {
	private static final String ADDON_NAME = "SkillAPI";
	
	public SkillAPIHandler() {}
	
	public void init() {
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE = false;
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	public static double getExp(Entity entity) {
		double ret = 0;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE) {
			if (!SkillAPI.getSettings().isUseOrbs()) {
				ret = SkillAPI.getSettings().getYield(ListenerUtil.getName(entity));
			}
		}
		return ret;
	}
	
	public static void fakeEntity(Entity entity) {
		// Used to block SkillAPI experience gain on kill event (otherwise the killer will get exp 2 times)
		SkillAPI.setMeta(entity, MechanicListener.SUMMON_DAMAGE, true);
	}
	
	public static void giveExp(UUID player, int experience) {
		SkillAPI.getPlayerData(Bukkit.getOfflinePlayer(player)).giveExp((double) experience, ExpSource.valueOf(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_EXPSOURCE));
	}
}
