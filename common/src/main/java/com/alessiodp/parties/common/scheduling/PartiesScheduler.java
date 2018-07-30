package com.alessiodp.parties.common.scheduling;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public abstract class PartiesScheduler {
	protected PartiesPlugin plugin;
	
	@Getter protected ExecutorDispatcher commandsExecutor;
	@Getter protected ExecutorDispatcher databaseExecutor;
	@Getter protected ExecutorDispatcher logExecutor;
	@Getter protected ExecutorDispatcher eventsExecutor;
	
	@Getter protected Set<Integer> currentTasks;
	
	
	protected PartiesScheduler(PartiesPlugin instance) {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT
				.replace("{class}", getClass().getSimpleName()), true);
		plugin = instance;
		currentTasks = new HashSet<>();
	}
	
	public void shutdown() {
		commandsExecutor.shutdown();
		databaseExecutor.shutdown();
		logExecutor.shutdown();
		eventsExecutor.shutdown();
	}
	
	public abstract void runSync(Runnable runnable);
	public abstract void runAsync(Runnable runnable);
	
	public abstract void runAsyncTaskTimer(Runnable runnable, long seconds);
	public abstract int scheduleTaskLater(Runnable runnable, long seconds);
	
	public abstract void cancelTask(int taskId);
}
