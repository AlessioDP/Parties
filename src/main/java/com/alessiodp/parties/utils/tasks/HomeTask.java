package com.alessiodp.parties.utils.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;

public class HomeTask extends BukkitRunnable {
	private Parties plugin;
	private Location loc;
	private ThePlayer tp;

	public HomeTask(Parties plugin, ThePlayer tp, Location loc) {
		this.plugin = plugin; 
		this.tp = tp;
		this.loc = loc;
	}

	public void run() {
		if(Bukkit.getOfflinePlayer(tp.getUUID()).isOnline()){
			if(tp.haveParty()){
				tp.getPlayer().teleport(loc);
				tp.sendMessage(Messages.home_teleported.replace("%price%", Variables.vault_home_price+""));
				tp.setHomeTask(-1);
				LogHandler.log(3, tp.getName() + "["+tp.getUUID()+"] teleported to the party home");
			}
		}
		plugin.getPlayerHandler().remHomeCount();
	}
}
