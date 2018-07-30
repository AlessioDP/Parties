package com.alessiodp.parties.bukkit.bootstrap;

import com.alessiodp.parties.bukkit.BukkitPartiesPlugin;
import com.alessiodp.parties.common.bootstrap.PartiesBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BukkitPartiesBootstrap extends JavaPlugin implements PartiesBootstrap {
	private BukkitPartiesPlugin plugin;
	
	public BukkitPartiesBootstrap() {
		plugin = new BukkitPartiesPlugin(this);
	}
	
	@Override
	public void onEnable() {
		plugin.enabling();
	}
	@Override
	public void onDisable() {
		plugin.disabling();
	}
	
	@Override
	public PartiesBootstrap getBootstrap() {
		return this;
	}
	
	@Override
	public Path getFolder() {
		return Paths.get(super.getDataFolder().getAbsolutePath());
	}
	
	@Override
	public String getVersion() {
		return super.getDescription().getVersion();
	}
	
	@Override
	public void stopPlugin() {
		super.getPluginLoader().disablePlugin(this);
	}
	
	@Override
	public InputStream getResource(String resource) {
		return super.getResource(resource);
	}
}
