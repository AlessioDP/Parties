package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.listener.ListenerUtil;
import com.sucy.skill.listener.MechanicListener;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.UUID;

@RequiredArgsConstructor
public class SkillAPIHandler {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "SkillAPI";
	private static boolean active;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE = false;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
				
			}
		}
	}
	
	public static double getExp(Entity entity) {
		double ret = 0;
		if (active
				&& BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE
				&& !SkillAPI.getSettings().isUseOrbs()) {
			ret = SkillAPI.getSettings().getYield(ListenerUtil.getName(entity));
		}
		return ret;
	}
	
	public static void fakeEntity(Entity entity) {
		if (active) {
			// Used to block SkillAPI experience gain on kill event (otherwise the killer will get exp 2 times)
			SkillAPI.setMeta(entity, MechanicListener.SUMMON_DAMAGE, true);
		}
	}
	
	public static void giveExp(UUID player, double experience) {
		if (active) {
			SkillAPI.getPlayerData(Bukkit.getOfflinePlayer(player)).giveExp(experience, ExpSource.valueOf(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_EXPSOURCE));
		}
	}
}
