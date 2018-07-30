package com.alessiodp.parties.bungeecord.scheduling;

import com.alessiodp.parties.common.scheduling.ExecutorDispatcher;
import org.jetbrains.annotations.NotNull;

public class BungeeExecutorDispatcher extends ExecutorDispatcher {
	private BungeePartiesScheduler scheduler;
	private boolean allowBungeeScheduler;
	
	public BungeeExecutorDispatcher(BungeePartiesScheduler scheduler, boolean allowBukkitScheduler) {
		super();
		this.scheduler = scheduler;
		this.allowBungeeScheduler = allowBukkitScheduler;
	}
	
	@Override
	public void execute(@NotNull Runnable runnable) {
		if (allowBungeeScheduler && scheduler.isUseBungeeScheduler()) {
			scheduler.getBungeeScheduler().runAsync(scheduler.getPlugin(), runnable);
		} else {
			super.execute(runnable);
		}
	}
}
