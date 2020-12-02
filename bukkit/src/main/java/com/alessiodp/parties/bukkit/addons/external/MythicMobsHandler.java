package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.parties.BukkitExpManager;
import com.alessiodp.parties.bukkit.players.objects.ExpDrop;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.droppables.ExperienceDrop;
import io.lumine.xikage.mythicmobs.drops.droppables.SkillAPIDrop;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class MythicMobsHandler implements Listener {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "MythicMobs";
	private static boolean active;
	private boolean registered = false;
	
	@Getter private static List<UUID> mobsExperienceToSuppress;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE
				&& BukkitConfigMain.ADDITIONAL_EXP_ENABLE
				&& BukkitConfigMain.ADDITIONAL_EXP_DROP_ENABLE) {
			if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
				try {
					Class.forName("io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobLootDropEvent");
					
					active = true;
					mobsExperienceToSuppress = new ArrayList<>();
					if (!registered) {
						Bukkit.getServer().getPluginManager().registerEvents(this, ((Plugin) plugin.getBootstrap()));
						registered = true;
					}
					
					plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
							.replace("{addon}", ADDON_NAME), true);
				} catch (Exception ex) {
					plugin.getLoggerManager().printError(Constants.DEBUG_ADDON_OUTDATED
							.replace("{addon}", ADDON_NAME));
				}
			} else {
				BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE = false;
				
				HandlerList.unregisterAll(this);
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_FAILED
						.replace("{addon}", ADDON_NAME), true);
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
				if (killer.isInParty()) {
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_EXP_MMHANDLING, event.getMobType().getInternalName(), killer.getName(), killer.getPlayerUUID().toString()), true);
					
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
					
					ExpDrop drop = new ExpDrop(killer, killedEntity, vanillaExp, skillapiExp);
					boolean result = ((BukkitExpManager) plugin.getExpManager()).distributeExp(drop);
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
