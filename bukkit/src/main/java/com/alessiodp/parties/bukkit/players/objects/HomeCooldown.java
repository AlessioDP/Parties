package com.alessiodp.parties.bukkit.players.objects;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.bukkit.parties.BukkitCooldownManager;
import com.alessiodp.parties.common.PartiesPlugin;
import lombok.Getter;
import org.bukkit.Location;

public class HomeCooldown {
	private BukkitPartiesPlugin plugin;
	
	@Getter private int task;
	@Getter private Location from;
	
	public HomeCooldown(PartiesPlugin instance, int task, Location from) {
		plugin = (BukkitPartiesPlugin) instance;
		this.task = task;
		this.from = from;
	}
	
	public void save() {
		((BukkitCooldownManager) plugin.getCooldownManager()).getHomeQueue().enter();
	}
	
	public void delete() {
		((BukkitCooldownManager) plugin.getCooldownManager()).getHomeQueue().leave();
	}
}
