package com.alessiodp.parties.bungeecord;

import java.util.logging.Level;

import com.alessiodp.parties.bungeecord.addons.BungeeMetrics;
import com.alessiodp.parties.bungeecord.configuration.ConfigurationManager;
import com.alessiodp.parties.bungeecord.configuration.Constants;
import com.alessiodp.parties.bungeecord.configuration.ConfigMain;
import com.alessiodp.parties.bungeecord.listeners.BungeeListener;
import com.alessiodp.parties.bungeecord.utils.ConsoleColor;

import net.md_5.bungee.api.plugin.Plugin;


public class PartiesBungee extends Plugin {
	private static PartiesBungee instance;
	
	private ConfigurationManager	configHandler;
	private BungeeListener			bungeeHandler;
	
	
	public static PartiesBungee getInstance() {return instance;}

	
	@Override
	public void onEnable() {
		/* init */
		instance = this;
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_ENABLING
				.replace("{version}", getDescription().getVersion()));
		
		handle();
		
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_ENABLED
				.replace("{version}", getDescription().getVersion()));
	}
	@Override
	public void onDisable() {
		log(ConsoleColor.CYAN.getCode() + Constants.DEBUG_PARTIES_DISABLED);
	}
	
	private void handle() {
		configHandler = new ConfigurationManager(this);
		bungeeHandler = new BungeeListener(this);
		
		registerMetrics();
	}
	
	private void registerMetrics() {
		BungeeMetrics metrics = new BungeeMetrics(this);
		metrics.addCustomChart(new BungeeMetrics.SimplePie("how_many_follow_party_enabled") {
			@Override
			public String getValue() {
				if (ConfigMain.follow_enable)
					return "Enabled";
				return "Disabled";
			}
		});
	}
	
	
	
	public ConfigurationManager getConfigHandler() {return configHandler;}
	public BungeeListener getBungeeHandler() {return bungeeHandler;}
	
	
	public void log(String msg) {
		getProxy().getLogger().log(Level.INFO, msg + ConsoleColor.RESET.getCode());
	}
	
	public static void debugLog(String message) {
		if (Constants.DEBUG_ENABLED)
			System.out.println(ConsoleColor.PURPLE.getCode() + "[Parties Debug] " + message + ConsoleColor.RESET.getCode());
	}
}
