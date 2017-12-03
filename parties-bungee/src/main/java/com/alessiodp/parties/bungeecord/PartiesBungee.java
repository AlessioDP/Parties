package com.alessiodp.parties.bungeecord;

import java.util.logging.Level;

import com.alessiodp.parties.bungeecord.configuration.Variables;
import com.alessiodp.parties.bungeecord.handlers.BungeeHandler;
import com.alessiodp.parties.bungeecord.handlers.ConfigHandler;
import com.alessiodp.parties.bungeecord.utils.BungeeMetrics;
import com.alessiodp.parties.bungeecord.utils.ConsoleColors;

import net.md_5.bungee.api.plugin.Plugin;


public class PartiesBungee extends Plugin {
	private static PartiesBungee instance;
	
	private ConfigHandler		configHandler;
	private BungeeHandler		bungeeHandler;
	
	private static final String	channel = "PartiesBungee";
	private final int			configVersion = 1;
	private final static boolean isDebug = true;
	
	
	public static PartiesBungee getInstance() {return instance;}

	
	@Override
	public void onEnable() {
		/* init */
		instance = this;
		log(ConsoleColors.CYAN.getCode() + "Initializing Parties v" + this.getDescription().getVersion() + " Bungeecord Version");
		
		handle();
		
		log(ConsoleColors.CYAN.getCode() + "Parties v" + this.getDescription().getVersion() + " enabled");
	}
	@Override
	public void onDisable() {
		log(ConsoleColors.CYAN.getCode() + "Parties disabled");
	}
	
	private void handle() {
		configHandler = new ConfigHandler(this);
		bungeeHandler = new BungeeHandler(this);
		
		registerMetrics();
	}
	private void registerMetrics() {
		BungeeMetrics metrics = new BungeeMetrics(this);
		metrics.addCustomChart(new BungeeMetrics.SimplePie("how_many_follow_party_enabled") {
			@Override
			public String getValue() {
				if (Variables.follow_enable)
					return "Enabled";
				return "Disabled";
			}
		});
	}
	
	
	
	public ConfigHandler getConfigHandler() {return configHandler;}
	public BungeeHandler getBungeeHandler() {return bungeeHandler;}
	
	public static String getPartiesChannel() {return channel;}
	public int getConfigVersion() {return configVersion;}
	
	
	public void log(String msg) {
		getProxy().getLogger().log(Level.INFO, msg + ConsoleColors.RESET.getCode());
	}
	
	public static void debugLog(String message) {
		if (isDebug)
			System.out.println(ConsoleColors.PURPLE.getCode() + "[Parties Debug] " + message + ConsoleColors.RESET.getCode());
	}
}
