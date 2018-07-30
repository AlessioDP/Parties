package com.alessiodp.parties.bukkit.tasks;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.bukkit.players.objects.HomeCooldown;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HomeTask implements Runnable {
	private Location loc;
	private BukkitPartyPlayerImpl player;
	
	public HomeTask(BukkitPartyPlayerImpl player, Location loc) {
		this.player = player;
		this.loc = loc;
	}

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
	}
}
