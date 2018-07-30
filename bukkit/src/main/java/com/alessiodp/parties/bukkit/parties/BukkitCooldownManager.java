package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.parties.common.parties.CooldownManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public class BukkitCooldownManager extends CooldownManager {
	@Getter private NumberedQueue homeQueue;
	@Getter private HashMap<UUID, Long> teleportCooldown;
	
	public BukkitCooldownManager() {
		super();
	}
	
	@Override
	public void reload() {
		super.reload();
		homeQueue = new NumberedQueue();
		teleportCooldown = new HashMap<>();
	}
	
	
	public class NumberedQueue {
		private int queue;
		
		NumberedQueue() {
			queue = 0;
		}
		
		public boolean isAlive() {
			return queue > 0;
		}
		
		public void enter() {
			queue++;
		}
		
		public void leave() {
			queue--;
		}
	}
}
