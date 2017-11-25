package com.alessiodp.parties.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.enums.LogLevel;

public class MoveListener implements Listener {
	Parties plugin;
	
	public MoveListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (Variables.home_cancelmove && plugin.getPlayerHandler().getHomeCount() != 0) {
			if (event.getFrom().getBlockX() != event.getTo().getBlockX()
					|| event.getFrom().getBlockY() != event.getTo().getBlockY()
					|| event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
				// Player moved
				ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());
				if (tp.getHomeTask() != -1) {
					if (tp.getHomeFrom().distance(event.getTo()) > Variables.home_distance) {
						// Cancel home
						plugin.getServer().getScheduler().cancelTask(tp.getHomeTask());
						plugin.getPlayerHandler().remHomeCount();
						tp.setHomeTask(-1);
						
						tp.sendMessage(Messages.home_denied);
						LogHandler.log(LogLevel.DEBUG, "Denied home of " + tp.getName() + "[" + tp.getUUID() + "] because he moves", true);
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerGotHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player
				&& Variables.home_cancelmove
				&& plugin.getPlayerHandler().getHomeCount() != 0) {
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getEntity().getUniqueId());
			if (tp.getHomeTask() != -1) {
				plugin.getServer().getScheduler().cancelTask(tp.getHomeTask());
				plugin.getPlayerHandler().remHomeCount();
				tp.setHomeTask(-1);
				
				tp.sendMessage(Messages.home_denied);
				LogHandler.log(LogLevel.DEBUG, "Denied home of " + tp.getName() + "[" + tp.getUUID() + "] because he was hit", true);
			}
		}
	}
}
