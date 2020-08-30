package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.CooldownManager;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BukkitCooldownManager extends CooldownManager {
	@Getter private HashMap<UUID, Long> homeCooldown;
	@Getter private HashMap<UUID, Long> setHomeCooldown;
	
	public BukkitCooldownManager(@NonNull PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void reload() {
		super.reload();
		homeCooldown = new HashMap<>();
		setHomeCooldown = new HashMap<>();
	}
	
	public long canHome(UUID player, int cooldown) {
		if (player != null) {
			return calculateCooldown(homeCooldown.get(player), cooldown);
		}
		return 0;
	}
	
	public void startHomeCooldown(UUID player, int seconds) {
		if (player != null && seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			homeCooldown.put(player, unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				homeCooldown.remove(player);
				
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_HOME_EXPIRED
						.replace("{uuid}", player.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	public void resetHomeCooldown(UUID player) {
		homeCooldown.remove(player);
	}
	
	public long canSetHome(UUID player, int cooldown) {
		if (player != null) {
			return calculateCooldown(setHomeCooldown.get(player), cooldown);
		}
		return 0;
	}
	
	public void startSetHomeCooldown(UUID player, int seconds) {
		if (player != null && seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			setHomeCooldown.put(player, unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				setHomeCooldown.remove(player);
				
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_SETHOME_EXPIRED
						.replace("{uuid}", player.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
}
