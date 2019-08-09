package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.addons.external.MythicMobsHandler;
import com.alessiodp.parties.bukkit.addons.external.SkillAPIHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.players.objects.ExpDrop;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

@RequiredArgsConstructor
public class BukkitExpListener implements Listener {
	private final BukkitPartiesPlugin plugin;
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDie(EntityDeathEvent event) {
		if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_DROP_ENABLE) {
			Entity killedEntity = event.getEntity();
			
			if (event.getEntity().getKiller() != null) {
				PartyPlayerImpl killer = plugin.getPlayerManager().getPlayer(event.getEntity().getKiller().getUniqueId());
				if (!killer.getPartyName().isEmpty()) {
					if (checkForMythicMobsHandler(event)) {
						return;
					}
					double vanillaExp = 0;
					double skillapiExp = 0;
					
					if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL)
						vanillaExp = event.getDroppedExp();
					if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_SKILLAPI)
						skillapiExp = SkillAPIHandler.getExp(killedEntity);
					
					ExpDrop drop = new ExpDrop((int) vanillaExp, (int) skillapiExp, killer, killedEntity);
					boolean result = plugin.getExpManager().distributeExp(drop);
					if (result && BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP) {
						// Remove exp from vanilla event if hooked
						if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL)
							event.setDroppedExp(0);
						// Remove exp drop from SkillAPI
						if (BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_SKILLAPI)
							SkillAPIHandler.fakeEntity(killedEntity);
						
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_EXP_REMOVINGEXP
								.replace("{value1}", Boolean.toString(BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL))
								.replace("{value2}", Boolean.toString(BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_SKILLAPI)), true);
					}
				}
			}
		}
	}
	
	/**
	 * Used to hook into Mythic Mobs mobs handling
	 * necessary to completely remove exp from the mob
	 *
	 * @return Returns true to cancel main event
	 */
	private boolean checkForMythicMobsHandler(EntityDeathEvent event) {
		boolean ret = false;
		if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE) {
			if (MythicMobsHandler.isMythicMob(event.getEntity())) {
				// Mythic Mob
				
				// Remove experience of mobs if handled by MythicMobHandler
				if (BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP
						&& BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL
						&& event.getDroppedExp() > 0
						&& MythicMobsHandler.getMobsExperienceToSuppress().remove(event.getEntity().getUniqueId())) {
					// We already know that the killer is a Player
					// Remove entity experience if the event has been handled by MythicMobHandler
					// This is a workaround to fix MythicMobs exp drop bug
					event.setDroppedExp(0);
				}
				
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_EXP_MMBYPASS, true);
				// MythicMobHandler will handle this event
				ret = true;
			} else {
				// Non Mythic Mob
				if (BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_HANDLEONLYMMMOBS) {
					// Return after this method ends
					ret = true;
				}
			}
		}
		return ret;
	}
}
