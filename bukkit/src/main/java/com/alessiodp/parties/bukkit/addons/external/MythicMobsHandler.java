package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.players.objects.ExpDrop;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.ConsoleColor;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.droppables.ExperienceDrop;
import io.lumine.xikage.mythicmobs.drops.droppables.SkillAPIDrop;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MythicMobsHandler implements Listener {
	private BukkitPartiesPlugin plugin;
	private static final String ADDON_NAME = "MythicMobs";
	private static boolean active;
	
	@Getter private static List<UUID> mobsExperienceToSuppress;
	
	public MythicMobsHandler(BukkitPartiesPlugin instance) {
		plugin = instance;
		plugin.getBootstrap().getServer().getPluginManager().registerEvents(this, plugin.getBootstrap());
	}
	
	public void init() {
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE
				&& BukkitConfigMain.ADDITIONAL_EXP_ENABLE
				&& BukkitConfigMain.ADDITIONAL_EXP_DROP_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				active = true;
				mobsExperienceToSuppress = new ArrayList<>();
				
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_HOOKED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.CYAN);
			} else {
				BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE = false;
				active = false;
				
				HandlerList.unregisterAll(this);
				LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_GENERAL_FAILED
						.replace("{addon}", ADDON_NAME), true, ConsoleColor.RED);
			}
		}
	}
	
	public static boolean isMythicMob(Entity entity) {
		boolean ret = false;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE && MythicMobs.inst().getAPIHelper().isMythicMob(entity))
			ret = true;
		return ret;
	}
	
	@EventHandler
	public void onMythicMobDeath(MythicMobLootDropEvent event) {
		if (active && event.getKiller() instanceof Player) {
			Entity killedEntity = event.getEntity();
			
			if (event.getKiller() != null) {
				PartyPlayerImpl killer = plugin.getPlayerManager().getPlayer(event.getKiller().getUniqueId());
				if (!killer.getPartyName().isEmpty()) {
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_EXP_MMHANDLING
							.replace("{name}", event.getMobType().getInternalName())
							.replace("{player}", killer.getName()), true);
					
					double vanillaExp = 0;
					double skillapiExp = 0;
					
					if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL)
						vanillaExp = event.getExp();
					if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_SKILLAPI) {
						for (Drop drop : event.getDrops().getDrops()) {
							if (drop instanceof SkillAPIDrop) {
								skillapiExp += drop.getAmount();
							}
						}
					}
					
					ExpDrop drop = new ExpDrop((int) vanillaExp, (int) skillapiExp, killer, killedEntity);
					boolean result = plugin.getExpManager().distributeExp(drop);
					if (result) {
						if (BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP) {
							// Remove exp from the event if hooked
							if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL) {
								// Be sure that we are removing exp from intangible drops
								for (Drop d : event.getDrops().getLootTableIntangible().values()) {
									if (d instanceof ExperienceDrop) {
										d.setAmount(0);
									}
								}
								// Add it to an array list that contains a list of entities
								// that we need to manually remove drop experience
								mobsExperienceToSuppress.add(event.getEntity().getUniqueId());
							}
							
							// Remove skillapi exp from the event if hooked
							if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_SKILLAPI) {
								event.getDrops().getLootTableIntangible().remove(SkillAPIDrop.class);
							}
						}
					}
				}
			}
		}
	}
}
