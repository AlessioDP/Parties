package com.alessiodp.parties.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.tasks.PortalTask;

public class FollowListener implements Listener {
	Parties plugin;
	
	public FollowListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onEntityPortalEvent(PlayerPortalEvent event) {
		if (!event.isCancelled()) {
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());
			
			if (tp.getPortalTimeout() != -1)
				event.setCancelled(true);
		}
	}
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		if (Variables.follow_enable
				&& (Variables.follow_listworlds.contains("*")
						|| Variables.follow_listworlds.contains(event.getPlayer().getWorld().getName()))) {
			Player player = event.getPlayer();
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());
			if (!tp.getPartyName().isEmpty()) {
				Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
				if (party != null) {
					if (tp.getRank() >= Variables.follow_neededrank) {
						// Init teleport
						for (Player pl : party.getOnlinePlayers()) {
							if (!pl.getUniqueId().equals(player.getUniqueId())
									&& !pl.getWorld().equals(player.getWorld())) {
								ThePlayer tpVictim = plugin.getPlayerHandler().getPlayer(pl.getUniqueId());
								
								if (tpVictim.getRank() >= Variables.follow_minimumrank) {
									switch (Variables.follow_type) {
									case 1:
										tpVictim.setPortalTimeout(
												new PortalTask(pl.getUniqueId())
												.runTaskLaterAsynchronously(plugin, Variables.follow_timeoutportal)
												.getTaskId());
										tpVictim.sendMessage(Messages.follow_following_world
												.replace("%player%", player.getName())
												.replace("%world%", player.getWorld().getName()));
										pl.teleport(player.getWorld().getSpawnLocation());
										break;
									case 2:
										tpVictim.setPortalTimeout(
												new PortalTask(pl.getUniqueId())
												.runTaskLaterAsynchronously(plugin, Variables.follow_timeoutportal)
												.getTaskId());
										tpVictim.sendMessage(Messages.follow_following_world
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
	}
}
