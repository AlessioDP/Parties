package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
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
		if (BukkitConfigMain.ADDITIONAL_EXP_ENABLE && BukkitConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS) {
			Entity killedEntity = event.getEntity();
			
			if (event.getEntity().getKiller() != null) {
				PartyPlayerImpl killer = plugin.getPlayerManager().getPlayer(event.getEntity().getKiller().getUniqueId());
				if (killer.isInParty()) {
					PartyImpl party = plugin.getPartyManager().getParty(killer.getPartyId());
					if (party != null) {
						party.giveExperience(event.getDroppedExp(), killer, killedEntity, true);
					}
				}
			}
		}
	}
}
