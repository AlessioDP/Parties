package com.alessiodp.parties.bungeecord.configuration.data;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.data.ConfigMain;

import java.util.ArrayList;
import java.util.List;

public class BungeeConfigMain extends ConfigMain {
	// Additional settings
	public static boolean ADDITIONAL_FOLLOW_ENABLE;
	public static boolean ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE;
	public static int ADDITIONAL_FOLLOW_PERFORMCMD_DELAY;
	public static List<String> ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS;
	public static List<String> ADDITIONAL_FOLLOW_ALLOWEDSERVERS;
	
	public BungeeConfigMain(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void loadDefaults() {
		super.loadDefaults();
		
		// Bungee configuration
		//Additional settings
		ADDITIONAL_FOLLOW_ENABLE = false;
		ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE = false;
		ADDITIONAL_FOLLOW_PERFORMCMD_DELAY = 2000;
		ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS = new ArrayList<>();
		ADDITIONAL_FOLLOW_ALLOWEDSERVERS = new ArrayList<>();
		ADDITIONAL_FOLLOW_ALLOWEDSERVERS.add("*");
		
		// Commands
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
		ConfigMain.COMMANDS_ORDER.add("teleport");
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
		// Additional settings
		ADDITIONAL_FOLLOW_ENABLE = confAdapter.getBoolean("additional.follow-party.enable", ADDITIONAL_FOLLOW_ENABLE);
		ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE = confAdapter.getBoolean("additional.follow-party.perform-commands.enable", ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE);
		ADDITIONAL_FOLLOW_PERFORMCMD_DELAY = confAdapter.getInt("additional.follow-party.perform-commands.delay", ADDITIONAL_FOLLOW_PERFORMCMD_DELAY);
		ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS = confAdapter.getStringList("additional.follow-party.perform-commands.commands", ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS);
		ADDITIONAL_FOLLOW_ALLOWEDSERVERS = confAdapter.getStringList("additional.follow-party.allowed-servers", ADDITIONAL_FOLLOW_ALLOWEDSERVERS);
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
