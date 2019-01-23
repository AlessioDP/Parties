package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.bukkit.tasks.PortalTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class BukkitFollowListener implements Listener {
	private PartiesPlugin plugin;
	
	public BukkitFollowListener(PartiesPlugin instance) {
		plugin = instance;
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onEntityPortalEvent(PlayerPortalEvent event) {
		if (!event.isCancelled()) {
			BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			
			if (pp.getPortalTimeoutTask() != -1) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		if (BukkitConfigMain.ADDITIONAL_FOLLOW_ENABLE) {
			// Make it async
			Player bukkitPlayer = event.getPlayer();
			if (bukkitPlayer != null) {
				plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
					if (BukkitConfigMain.ADDITIONAL_FOLLOW_LISTWORLDS.contains("*")
							|| BukkitConfigMain.ADDITIONAL_FOLLOW_LISTWORLDS.contains(bukkitPlayer.getWorld().getName())) {
						BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(bukkitPlayer.getUniqueId());
						if (!pp.getPartyName().isEmpty()) {
							PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
							if (party != null && party.isFollowEnabled()) {
								if (pp.getRank() >= BukkitConfigMain.ADDITIONAL_FOLLOW_RANKNEEDED) {
									
									// Init teleport
									for (PartyPlayerImpl pl : party.getOnlinePlayers()) {
										Player bukkitPl = Bukkit.getPlayer(pl.getPlayerUUID());
										if (bukkitPl != null
												&& !pl.getPlayerUUID().equals(bukkitPlayer.getUniqueId())
												&& bukkitPl.getWorld().equals(event.getFrom())) {
											BukkitPartyPlayerImpl ppVictim = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(pl.getPlayerUUID());
											
											if (ppVictim.getRank() >= BukkitConfigMain.ADDITIONAL_FOLLOW_RANKMINIMUM) {
												// Make it sync
												plugin.getPartiesScheduler().runSync(() -> {
													int taskId;
													switch (BukkitConfigMain.ADDITIONAL_FOLLOW_TYPE) {
														case 1:
															taskId = plugin.getPartiesScheduler().scheduleTaskLater(new PortalTask(plugin, pl.getPlayerUUID()), BukkitConfigMain.ADDITIONAL_FOLLOW_TIMEOUT);
															ppVictim.setPortalTimeoutTask(taskId);
															ppVictim.sendMessage(BukkitMessages.OTHER_FOLLOW_WORLD
																	.replace("%player%", bukkitPlayer.getName())
																	.replace("%world%", bukkitPlayer.getWorld().getName()));
															bukkitPl.teleport(bukkitPlayer.getWorld().getSpawnLocation());
															break;
														case 2:
															taskId = plugin.getPartiesScheduler().scheduleTaskLater(new PortalTask(plugin, pl.getPlayerUUID()), BukkitConfigMain.ADDITIONAL_FOLLOW_TIMEOUT);
															ppVictim.setPortalTimeoutTask(taskId);
															ppVictim.sendMessage(BukkitMessages.OTHER_FOLLOW_WORLD
																	.replace("%player%", bukkitPlayer.getName())
																	.replace("%world%", bukkitPlayer.getWorld().getName()));
															bukkitPl.teleport(bukkitPlayer);
													}
												});
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
}
