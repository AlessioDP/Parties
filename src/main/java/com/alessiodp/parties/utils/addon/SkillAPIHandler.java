package com.alessiodp.parties.utils.addon;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.listener.ListenerUtil;
import com.sucy.skill.listener.MechanicListener;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.drops.DropManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class SkillAPIHandler {
	Parties main;
	public static boolean active = false;
	
	public static boolean mythicmobs_support = false;
	
	public SkillAPIHandler(Parties instance){
		main = instance;
		active = false;
		mythicmobs_support = false;
	}
	
	public boolean init(){
		if(Variables.exp_enable && Variables.exp_skillapi_enable){
			if(Bukkit.getPluginManager().getPlugin("SkillAPI") != null && Bukkit.getPluginManager().isPluginEnabled("SkillAPI")){
				active = true;
				
				if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null && Bukkit.getPluginManager().isPluginEnabled("MythicMobs")){
					mythicmobs_support = true;
				}
				return true;
			}
		}
		return false;
	}
	
	public static double MythicMobs_changeEXP(double old, Entity entity) {
		MythicMob mob = MythicMobs.inst().getAPIHelper().getMythicMobInstance(entity).getType();
		double newexp = -1;
		for(String s : mob.getDrops()){
			if(s.contains("skillapi-exp")){
				newexp = DropManager.parseAmount(s.split(" ")[1]);
				break;
			}
		}
		if(newexp != -1){
			LogHandler.log(3, "Changing exp dropped from " + old + " to " + newexp);
			old = newexp;
		}
		return old;
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
