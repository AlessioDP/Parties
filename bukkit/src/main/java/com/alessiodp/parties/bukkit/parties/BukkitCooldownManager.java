package com.alessiodp.parties.bukkit.parties;

import com.alessiodp.parties.common.parties.CooldownManager;
import lombok.Getter;

public class BukkitCooldownManager extends CooldownManager {
	@Getter private NumberedQueue homeQueue;
	
	public BukkitCooldownManager() {
		super();
	}
	
	@Override
	public void reload() {
		super.reload();
		homeQueue = new NumberedQueue();
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
