package com.alessiodp.parties.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.tasks.PortalTask;

public class FollowListener implements Listener {
	Parties plugin;
	
	public FollowListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityPortalEvent(PlayerPortalEvent event) {
		if (!event.isCancelled()) {
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			
			if (pp.getPortalTimeoutTask() != -1)
				event.setCancelled(true);
		}
	}
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE) {
			// Make it async
			plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
				if (ConfigMain.ADDITIONAL_FOLLOW_LISTWORLDS.contains("*")
						|| ConfigMain.ADDITIONAL_FOLLOW_LISTWORLDS.contains(event.getPlayer().getWorld().getName())) {
					Player player = event.getPlayer();
					PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
					if (!pp.getPartyName().isEmpty()) {
						PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
						if (party != null) {
							if (pp.getRank() >= ConfigMain.ADDITIONAL_FOLLOW_RANKNEEDED) {
								// Init teleport
								for (Player pl : party.getOnlinePlayers()) {
									if (!pl.getUniqueId().equals(player.getUniqueId())
											&& !pl.getWorld().equals(player.getWorld())) {
										PartyPlayerEntity ppVictim = plugin.getPlayerManager().getPlayer(pl.getUniqueId());
										
										if (ppVictim.getRank() >= ConfigMain.ADDITIONAL_FOLLOW_RANKMINIMUM) {
											switch (ConfigMain.ADDITIONAL_FOLLOW_TYPE) {
											case 1:
												ppVictim.setPortalTimeoutTask(
														new PortalTask(pl.getUniqueId())
														.runTaskLaterAsynchronously(plugin, ConfigMain.ADDITIONAL_FOLLOW_TIMEOUT)
														.getTaskId());
												ppVictim.sendMessage(Messages.OTHER_FOLLOW_WORLD
														.replace("%player%", player.getName())
														.replace("%world%", player.getWorld().getName()));
												pl.teleport(player.getWorld().getSpawnLocation());
												break;
											case 2:
												ppVictim.setPortalTimeoutTask(
														new PortalTask(pl.getUniqueId())
														.runTaskLaterAsynchronously(plugin, ConfigMain.ADDITIONAL_FOLLOW_TIMEOUT)
														.getTaskId());
												ppVictim.sendMessage(Messages.OTHER_FOLLOW_WORLD
														.replace("%player%", player.getName())
														.replace("%world%", player.getWorld().getName()));
												pl.teleport(player);
											}
										}
									}
								}
							}
						}
					}
				}
			});
		}
	}
}
