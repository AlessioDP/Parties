package com.alessiodp.parties.bungeecord.scheduling;

import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.scheduling.PartiesScheduler;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.TimeUnit;

public class BungeePartiesScheduler extends PartiesScheduler {
	@Getter private TaskScheduler bungeeScheduler;
	
	@Getter @Setter private boolean useBungeeScheduler;
	
	public BungeePartiesScheduler(PartiesPlugin instance) {
		super(instance);
		commandsExecutor = new BungeeExecutorDispatcher(this, true);
		databaseExecutor = new BungeeExecutorDispatcher(this,false);
		logExecutor = new BungeeExecutorDispatcher(this,false);
		eventsExecutor = new BungeeExecutorDispatcher(this,true);
		
		bungeeScheduler = getPlugin().getProxy().getScheduler();
		// Make an async task to enable bukkit scheduler
		// This task will be started when the server loads completely
		bungeeScheduler.runAsync(getPlugin(), () -> useBungeeScheduler = true);
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		bungeeScheduler.cancel(getPlugin());
	}
	
	@Override
	public void runSync(Runnable runnable) {
		runnable.run();
	}
	
	@Override
	public void runAsync(Runnable runnable) {
		bungeeScheduler.runAsync(getPlugin(), runnable);
	}
	
	@Override
	public int scheduleAsyncTaskTimer(Runnable runnable, long seconds) {
		int taskId = bungeeScheduler.schedule(getPlugin(), runnable, seconds * 50, TimeUnit.MILLISECONDS).getId();
		getCurrentTasks().add(taskId);
		return taskId;
	}
	
	@Override
	public int scheduleTaskLater(Runnable runnable, long seconds) {
		int taskId = bungeeScheduler.schedule(getPlugin(), runnable, seconds, TimeUnit.SECONDS).getId();
		getCurrentTasks().add(taskId);
		return taskId;
	}
	
	@Override
	public void cancelTask(int taskId) {
		bungeeScheduler.cancel(taskId);
		getCurrentTasks().remove(taskId);
	}
	
	public Plugin getPlugin() {
		return ((BungeePartiesPlugin) plugin).getBootstrap();
	}
}
