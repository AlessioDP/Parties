package com.alessiodp.parties.bukkit.scheduling;

import com.alessiodp.parties.common.scheduling.ExecutorDispatcher;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BukkitExecutorDispatcher extends ExecutorDispatcher {
	private BukkitPartiesScheduler scheduler;
	private boolean allowBukkitScheduler;
	
	BukkitExecutorDispatcher(BukkitPartiesScheduler scheduler, boolean allowBukkitScheduler) {
		super();
		this.scheduler = scheduler;
		this.allowBukkitScheduler = allowBukkitScheduler;
	}
	
	@Override
	public void execute(@NotNull Runnable runnable) {
		if (allowBukkitScheduler && scheduler.isUseBukkitScheduler()) {
			if (Bukkit.isPrimaryThread()) {
				scheduler.getBukkitScheduler().runTaskAsynchronously(scheduler.getPlugin(), runnable);
			} else
				runnable.run();
		} else {
			super.execute(runnable);
		}
	}
}
