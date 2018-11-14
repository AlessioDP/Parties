package com.alessiodp.parties.bukkit.scheduling;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.scheduling.PartiesScheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitPartiesScheduler extends PartiesScheduler {
	@Getter private BukkitScheduler bukkitScheduler;
	
	@Getter @Setter private boolean useBukkitScheduler;
	
	public BukkitPartiesScheduler(PartiesPlugin instance) {
		super(instance);
		commandsExecutor = new BukkitExecutorDispatcher(this, true);
		databaseExecutor = new BukkitExecutorDispatcher(this,false);
		eventsExecutor = new BukkitExecutorDispatcher(this,true);
		logExecutor = new BukkitExecutorDispatcher(this,false);
		messagingExecutor = new BukkitExecutorDispatcher(this,true);
		
		bukkitScheduler = getPlugin().getServer().getScheduler();
		// Make an async task to enable bukkit scheduler
		// This task will be started when the server loads completely
		bukkitScheduler.runTaskAsynchronously(getPlugin(), () -> useBukkitScheduler = true);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		bukkitScheduler.cancelTasks(getPlugin());
	}
	
	@Override
	public void runSync(Runnable runnable) {
		bukkitScheduler.runTask(getPlugin(), runnable);
	}
	
	@Override
	public void runAsync(Runnable runnable) {
		bukkitScheduler.runTaskAsynchronously(getPlugin(), runnable);
	}
	
	@Override
	public void runAsyncTaskTimer(Runnable runnable, long seconds) {
		bukkitScheduler.runTaskTimerAsynchronously(getPlugin(), runnable, 0, seconds * 20);
	}
	
	@Override
	public int scheduleTaskLater(Runnable runnable, long seconds) {
		int taskId = bukkitScheduler.runTaskLater(getPlugin(), runnable, seconds * 20).getTaskId();
		getCurrentTasks().add(taskId);
		return taskId;
	}
	
	@Override
	public void cancelTask(int taskId) {
		bukkitScheduler.cancelTask(taskId);
		getCurrentTasks().remove(taskId);
	}
	
	public Plugin getPlugin() {
		return ((BukkitPartiesPlugin) plugin).getBootstrap();
	}
}
