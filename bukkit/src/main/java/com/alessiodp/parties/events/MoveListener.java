package com.alessiodp.parties.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class MoveListener implements Listener {
	Parties plugin;
	
	public MoveListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (ConfigParties.HOME_MOVING) {
			// Make it async
			plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
				if (plugin.getPlayerManager().getHomeCounts() != 0) {
					if (event.getFrom().getBlockX() != event.getTo().getBlockX()
							|| event.getFrom().getBlockY() != event.getTo().getBlockY()
							|| event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
						// Player moved
						PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
						if (pp.getHomeTask() != -1) {
							if (pp.getHomeFrom().distance(event.getTo()) > ConfigParties.HOME_DISTANCE) {
								// Cancel home
								plugin.getServer().getScheduler().cancelTask(pp.getHomeTask());
								plugin.getPlayerManager().remHomeCount();
								pp.setHomeTask(-1);
								
								pp.sendMessage(Messages.ADDCMD_HOME_TELEPORTDENIED);
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_HOME_DENIED_MOVING
										.replace("{player}", pp.getName()), true);
							}
						}
					}
				}
			});
		}
	}
	@EventHandler
	public void onPlayerGotHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && ConfigParties.HOME_HIT) {
			// Make it async
			plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
				if (plugin.getPlayerManager().getHomeCounts() != 0) {
					PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(event.getEntity().getUniqueId());
					if (pp.getHomeTask() != -1) {
						plugin.getServer().getScheduler().cancelTask(pp.getHomeTask());
						plugin.getPlayerManager().remHomeCount();
						pp.setHomeTask(-1);
						
						pp.sendMessage(Messages.ADDCMD_HOME_TELEPORTDENIED);
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_HOME_DENIED_FIGHT
								.replace("{player}", pp.getName()), true);
					}
				}
			});
		}
	}
}
