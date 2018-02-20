package com.alessiodp.parties.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;

import lombok.Getter;
import lombok.Setter;

public class PartiesScheduler {
	private Parties plugin;
	
	@Getter private ExecutorDispatcher commandsExecutor;
	@Getter private ExecutorDispatcher databaseExecutor;
	@Getter private ExecutorDispatcher logExecutor;
	@Getter private ExecutorDispatcher eventsExecutor;
	
	@Setter private boolean useBukkitScheduler;
	
	/*
	 * This class will dispatch the correct executor for each caller.
	 * 
	 * There are two types of executors:
	 * - Parties: Parties thread executor
	 * - Bukkit: Bukkit scheduler
	 * 
	 * Bukkit task cannot be used when the server is starting or stopping,
	 * for that Parties will use own thread executor to finish queries.
	 */
	
	public PartiesScheduler(Parties instance) {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		plugin = instance;
		useBukkitScheduler = false;
		commandsExecutor = new ExecutorDispatcher();
		databaseExecutor = new ExecutorDispatcher();
		logExecutor = new ExecutorDispatcher();
		eventsExecutor = new ExecutorDispatcher();
		
		// Make an async task to enable bukkit scheduler
		// This task will be started when the server completes the loading
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {useBukkitScheduler = true;});
	}
	
	public void shutdown() {
		commandsExecutor.shutdown();
		databaseExecutor.shutdown();
		logExecutor.shutdown();
		eventsExecutor.shutdown();
		plugin.getServer().getScheduler().cancelTasks(plugin);
	}
	
	
	public class ExecutorDispatcher implements Executor {
		private ExecutorService partiesExecutor;
		
		public ExecutorDispatcher() {
			partiesExecutor = Executors.newSingleThreadExecutor();
		}

		@Override
		public void execute(Runnable runnable) {
			if (useBukkitScheduler) {
				plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
			} else
				partiesExecutor.submit(runnable);
		}
		
		public void shutdown() {
			try {
				partiesExecutor.shutdown();
				partiesExecutor.awaitTermination(10, TimeUnit.SECONDS);
			} catch (Exception ex) {}
			finally {
				partiesExecutor.shutdownNow();
			}
		}
	}
}
