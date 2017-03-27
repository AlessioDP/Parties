package com.alessiodp.parties.utils.addon;

import org.bukkit.Bukkit;
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
	public static boolean active = false;
	
	public SkillAPIHandler(Parties instance){
		main = instance;
		active = false;
	}
	
	public boolean init(){
		if(Variables.exp_enable && Variables.exp_skillapi_enable){
			if(Bukkit.getPluginManager().getPlugin("SkillAPI") != null && Bukkit.getPluginManager().isPluginEnabled("SkillAPI")){
				active = true;
				return true;
			}
		}
		return false;
	}
	
	public static double getExp(double base, Entity entity){
		if (!SkillAPI.getSettings().isUseOrbs()){
			base = SkillAPI.getSettings().getYield(ListenerUtil.getName(entity));
			
			// Bypass SkillAPI event
			SkillAPI.setMeta(entity, MechanicListener.SUMMON_DAMAGE, true);
		}
		return base;
	}
	
	public static void giveExp(OfflinePlayer player, double exp){
		if(!active)
			return;
		SkillAPI.getPlayerData(player).giveExp(exp, ExpSource.valueOf(Variables.exp_skillapi_source));
	}
}
