package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeTask implements Runnable {
	private PartiesPlugin plugin;
	private BukkitPartyPlayerImpl partyPlayer;
	private Player player;
	private double distanceLimitSquared;
	
	private long startTime;
	private Location startLocation;
	
	private long delayTime;
	private Location homeLocation;
	
	public HomeTask(PartiesPlugin plugin, BukkitPartyPlayerImpl partyPlayer, Player player, long delayTime, Location homeLocation) {
		this.plugin = plugin;
		this.partyPlayer = partyPlayer;
		this.player = player;
		distanceLimitSquared = BukkitConfigParties.HOME_DISTANCE * BukkitConfigParties.HOME_DISTANCE;
		
		startTime = System.currentTimeMillis();
		startLocation = player.getLocation();
		
		this.delayTime = delayTime * 1000; // Get milliseconds instead of seconds
		this.homeLocation = homeLocation;
	}

	@Override
	public void run() {
		if (partyPlayer.getHomeDelayTask() != -1) {
			if (player.isOnline()) {
				if (BukkitConfigParties.HOME_MOVING && player.getLocation().distanceSquared(startLocation) > distanceLimitSquared) {
					// Cancel teleport
					cancel();
					
					partyPlayer.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTDENIED);
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_HOME_DENIED_MOVING
							.replace("{player}", partyPlayer.getName()), true);
					return;
				}
				// Check if delay is timed out
				long timestamp = System.currentTimeMillis();
				if (timestamp - startTime > delayTime) {
					// Teleport player via sync Bukkit API
					plugin.getPartiesScheduler().runSync(() -> {
						player.teleport(homeLocation);
						partyPlayer.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTED);
						
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_DONE
								.replace("{player}", player.getName()), true);
						
						cancel();
					});
				}
			} else {
				cancel(); // Player offline
			}
		}
	}
	
	private void cancel() {
		plugin.getPartiesScheduler().cancelTask(partyPlayer.getHomeDelayTask());
		partyPlayer.setHomeDelayTask(-1);
	}
	
	/*
	@Override
	public void run() {
		if (Bukkit.getOfflinePlayer(player.getPlayerUUID()).isOnline()) {
			if (player.getHomeCooldown() != null) {
				HomeCooldown homeCooldown = player.getHomeCooldown();
				if (!player.getPartyName().isEmpty()) {
					// Teleport
					Bukkit.getPlayer(player.getPlayerUUID()).teleport(loc);
					
					// Send player message
					player.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTED
							.replace("%price%", Double.toString(BukkitConfigMain.ADDONS_VAULT_PRICE_HOME)));
					
					// Set task id to -1
					player.setHomeCooldown(null);
					
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_DONE
							.replace("{player}", player.getName()), true);
				}
				// Remove home cooldown from the queue
				homeCooldown.delete();
			}
		}
	}*/
}
