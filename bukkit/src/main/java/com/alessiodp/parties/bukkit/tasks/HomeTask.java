package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeTask implements Runnable {
	private final PartiesPlugin plugin;
	private final BukkitPartyPlayerImpl partyPlayer;
	private final Player player;
	private final double distanceLimitSquared;
	
	private final long startTime;
	private final Location startLocation;
	
	private final long delayTime;
	private final Location homeLocation;
	
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
		if (partyPlayer.getHomeDelayTask() != null) {
			if (player.isOnline()) {
				User user = plugin.getPlayer(player.getUniqueId());
				
				if (BukkitConfigParties.HOME_MOVING && player.getLocation().distanceSquared(startLocation) > distanceLimitSquared) {
					// Cancel teleport
					cancel();
					
					user.sendMessage(plugin.getMessageUtils().convertPlayerPlaceholders(BukkitMessages.ADDCMD_HOME_TELEPORTDENIED, partyPlayer), true);
					plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_HOME_DENIED_MOVING
							.replace("{player}", partyPlayer.getName()), true);
					return;
				}
				// Check if delay is timed out
				long timestamp = System.currentTimeMillis();
				if (timestamp - startTime > delayTime) {
					// Teleport player via sync Bukkit API
					plugin.getScheduler().getSyncExecutor().execute(() -> {
						player.teleport(homeLocation);
						user.sendMessage(plugin.getMessageUtils().convertPlayerPlaceholders(BukkitMessages.ADDCMD_HOME_TELEPORTED, partyPlayer), true);
						
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_TELEPORT_DONE
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
		if (partyPlayer.getHomeDelayTask() != null) {
			partyPlayer.getHomeDelayTask().cancel();
			partyPlayer.setHomeDelayTask(null);
		}
	}
}
