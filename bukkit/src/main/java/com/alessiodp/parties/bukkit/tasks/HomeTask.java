package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
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
		distanceLimitSquared = BukkitConfigParties.ADDITIONAL_HOME_CANCEL_DISTANCE * BukkitConfigParties.ADDITIONAL_HOME_CANCEL_DISTANCE;
		
		startTime = System.currentTimeMillis();
		startLocation = player.getLocation();
		
		this.delayTime = delayTime * 1000; // Get milliseconds instead of seconds
		this.homeLocation = homeLocation;
	}

	@Override
	public void run() {
		if (partyPlayer.getHomeTeleporting() != null) {
			if (player.isOnline()) {
				User user = plugin.getPlayer(player.getUniqueId());
				
				if (BukkitConfigParties.ADDITIONAL_HOME_CANCEL_MOVING && player.getLocation().distanceSquared(startLocation) > distanceLimitSquared) {
					// Cancel teleport
					cancel();
					
					user.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_HOME_TELEPORTDENIED, partyPlayer, null), true);
					plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_HOME_DENIED_MOVING
							.replace("{player}", partyPlayer.getName()), true);
					return;
				}
				// Check if delay is timed out
				long timestamp = System.currentTimeMillis();
				if (timestamp - startTime > delayTime) {
					((BukkitUser) user).teleportAsync(homeLocation).thenAccept(result -> {
						if (result) {
							EssentialsHandler.updateLastTeleportLocation(user.getUUID());
							user.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.ADDCMD_HOME_TELEPORTED, partyPlayer, null), true);
							
							plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_TELEPORT_DONE
									.replace("{player}", player.getName()), true);
						} else {
							plugin.getLoggerManager().printError(PartiesConstants.DEBUG_TELEPORT_ASYNC);
						}
					});
					cancel();
				}
			} else {
				cancel(); // Player offline
			}
		}
	}
	
	private void cancel() {
		if (partyPlayer.getHomeTeleporting() != null) {
			partyPlayer.getHomeTeleporting().cancel();
			partyPlayer.setHomeTeleporting(null);
			if (BukkitConfigParties.ADDITIONAL_HOME_CANCEL_RESET_COOLDOWN)
				((BukkitCooldownManager) plugin.getCooldownManager()).resetHomeCooldown(player.getUniqueId());
		}
	}
}
