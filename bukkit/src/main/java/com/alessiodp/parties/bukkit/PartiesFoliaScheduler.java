package com.alessiodp.parties.bukkit;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.scheduling.ADPScheduler;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Executor;

public class PartiesFoliaScheduler extends ADPScheduler {
	private final Executor bukkitSync;
	
	public PartiesFoliaScheduler(ADPPlugin plugin) {
		super(plugin);
		Plugin bukkitPlugin = ((Plugin) plugin.getBootstrap());
		bukkitSync = r -> bukkitPlugin.getServer().getGlobalRegionScheduler().execute(bukkitPlugin, r);
	}
	
	@Override
	public Executor getSyncExecutor() {
		return bukkitSync;
	}
}
