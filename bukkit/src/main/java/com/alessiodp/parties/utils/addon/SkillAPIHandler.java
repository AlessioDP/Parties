package com.alessiodp.parties.utils.addon;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.listener.ListenerUtil;
import com.sucy.skill.listener.MechanicListener;

public class SkillAPIHandler {
	Parties main;
	
	public SkillAPIHandler(Parties instance) {
		main = instance;
	}
	public static void blockEvent(Entity entity) {
		if (!SkillAPI.getSettings().isUseOrbs()) {
			// Used to block SkillAPI kill event (else killer got exp 2 times)
			SkillAPI.setMeta(entity, MechanicListener.SUMMON_DAMAGE, true);
		}
	}
	public static double getExp(double base, Entity entity) {
		if (!SkillAPI.getSettings().isUseOrbs()) {
			base = SkillAPI.getSettings().getYield(ListenerUtil.getName(entity));
		}
		return base;
	}
	
	public static void giveExp(OfflinePlayer player, double exp) {
		SkillAPI.getPlayerData(player).giveExp(exp, ExpSource.valueOf(Variables.exp_skillapi_source));
	}
}
