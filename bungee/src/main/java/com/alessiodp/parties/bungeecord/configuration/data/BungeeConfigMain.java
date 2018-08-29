package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

import java.util.ArrayList;

public class BungeeConfigMain extends ConfigMain {
	
	
	public BungeeConfigMain(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bungee configuration
		ConfigMain.COMMANDS_ORDER = new ArrayList<>();
		ConfigMain.COMMANDS_ORDER.add("help");
		ConfigMain.COMMANDS_ORDER.add("create");
		ConfigMain.COMMANDS_ORDER.add("accept");
		ConfigMain.COMMANDS_ORDER.add("deny");
		ConfigMain.COMMANDS_ORDER.add("join");
		ConfigMain.COMMANDS_ORDER.add("ignore");
		ConfigMain.COMMANDS_ORDER.add("mute");
		ConfigMain.COMMANDS_ORDER.add("p");
		ConfigMain.COMMANDS_ORDER.add("leave");
		ConfigMain.COMMANDS_ORDER.add("invite");
		ConfigMain.COMMANDS_ORDER.add("info");
		ConfigMain.COMMANDS_ORDER.add("list");
		ConfigMain.COMMANDS_ORDER.add("chat");
		ConfigMain.COMMANDS_ORDER.add("desc");
		ConfigMain.COMMANDS_ORDER.add("motd");
		ConfigMain.COMMANDS_ORDER.add("color");
		ConfigMain.COMMANDS_ORDER.add("password");
		ConfigMain.COMMANDS_ORDER.add("rank");
		ConfigMain.COMMANDS_ORDER.add("rename");
		ConfigMain.COMMANDS_ORDER.add("kick");
		ConfigMain.COMMANDS_ORDER.add("spy");
		ConfigMain.COMMANDS_ORDER.add("delete");
		ConfigMain.COMMANDS_ORDER.add("reload");
		ConfigMain.COMMANDS_ORDER.add("migrate");
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		super.loadConfiguration(confAdapter);
		
		// Bungee configuration
	}
	
	@Override
	public String getFileName() {
		return "config.yml";
	}
	
	@Override
	public String getResourceName() {
		return "bungee/config.yml";
	}
}
