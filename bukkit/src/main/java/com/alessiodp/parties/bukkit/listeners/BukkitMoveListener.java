package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class BukkitMoveListener implements Listener {
	private BukkitPartiesPlugin plugin;
	
	public BukkitMoveListener(PartiesPlugin instance) {
		plugin = (BukkitPartiesPlugin) instance;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (BukkitConfigParties.HOME_MOVING) {
			// Make it async
			plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
				// Check if there is an home command awaiting
				if (((BukkitCooldownManager) plugin.getCooldownManager()).getHomeQueue().isAlive()) {
					
					BukkitPartyPlayerImpl partyPlayer = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
					// Check if the player is on home cooldown
					if (partyPlayer.getHomeCooldown() != null) {
						// Check if the new location is different from the old one
						if (event.getFrom().getBlockX() != event.getTo().getBlockX()
								|| event.getFrom().getBlockY() != event.getTo().getBlockY()
								|| event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
							// Check how far away is the start position from the new position
							if (partyPlayer.getHomeCooldown().getFrom().distance(event.getTo()) > BukkitConfigParties.HOME_DISTANCE) {
								// Cancelling home command
								plugin.getPartiesScheduler().cancelTask(partyPlayer.getHomeCooldown().getTask());
								partyPlayer.getHomeCooldown().delete();
								partyPlayer.setHomeCooldown(null);
								
								partyPlayer.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTDENIED);
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_HOME_DENIED_MOVING
										.replace("{player}", partyPlayer.getName()), true);
							}
						}
					}
				}
			});
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onPlayerGotHit(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && BukkitConfigParties.HOME_HIT) {
			// Make it async
			plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
				// Check if there is an home command awaiting
				if (((BukkitCooldownManager) plugin.getCooldownManager()).getHomeQueue().isAlive()) {
					
					BukkitPartyPlayerImpl partyPlayer = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(event.getEntity().getUniqueId());
					// Check if the player is on home cooldown
					if (partyPlayer.getHomeCooldown() != null) {
						// Cancelling home command
						plugin.getPartiesScheduler().cancelTask(partyPlayer.getHomeCooldown().getTask());
						partyPlayer.getHomeCooldown().delete();
						partyPlayer.setHomeCooldown(null);
						
						partyPlayer.sendMessage(BukkitMessages.ADDCMD_HOME_TELEPORTDENIED);
						LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_HOME_DENIED_FIGHT
								.replace("{player}", partyPlayer.getName()), true);
					}
				}
			});
		}
	}
}
