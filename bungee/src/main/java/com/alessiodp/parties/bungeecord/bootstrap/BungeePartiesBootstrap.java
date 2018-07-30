package com.alessiodp.parties.bungeecord.bootstrap;

import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.common.bootstrap.PartiesBootstrap;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BungeePartiesBootstrap extends Plugin implements PartiesBootstrap {
	private BungeePartiesPlugin plugin;
	
	public BungeePartiesBootstrap() {
		plugin = new BungeePartiesPlugin(this);
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
		super.getProxy().getPluginManager().unregisterCommands(this);
		super.getProxy().getPluginManager().unregisterListeners(this);
		super.onDisable();
	}
	
	@Override
	public InputStream getResource(String resource) {
		return super.getResourceAsStream(resource);
	}
}
