package com.alessiodp.parties.common.scheduling;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class ExecutorDispatcher implements Executor {
	private ExecutorService partiesExecutor;
	
	protected ExecutorDispatcher() {
		partiesExecutor = Executors.newSingleThreadExecutor();
	}
	
	@Override
	public void execute(@NotNull Runnable runnable) {
		partiesExecutor.submit(runnable);
	}
	
	void shutdown() {
		try {
			partiesExecutor.shutdown();
			partiesExecutor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (Exception ignored) {}
		finally {
			partiesExecutor.shutdownNow();
		}
	}
}
