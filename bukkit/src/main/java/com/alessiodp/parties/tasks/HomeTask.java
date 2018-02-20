package com.alessiodp.parties.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class HomeTask extends BukkitRunnable {
	private Parties plugin;
	private Location loc;
	private PartyPlayerEntity player;
	
	public HomeTask(Parties plugin, PartyPlayerEntity player, Location loc) {
		this.plugin = plugin; 
		this.player = player;
		this.loc = loc;
	}

	@Override
	public void run() {
		if (Bukkit.getOfflinePlayer(player.getPlayerUUID()).isOnline()) {
			if (!player.getPartyName().isEmpty()) {
				player.getPlayer().teleport(loc);
				player.sendMessage(Messages.ADDCMD_HOME_TELEPORTED
						.replace("%price%", Double.toString(ConfigMain.ADDONS_VAULT_PRICE_HOME)));
				player.setHomeTask(-1);
				
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_TELEPORT_DONE
						.replace("{player}", player.getName()), true);
			}
		}
		plugin.getPlayerManager().remHomeCount();
	}
}
