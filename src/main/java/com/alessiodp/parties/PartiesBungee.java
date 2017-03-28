package com.alessiodp.parties;

import java.util.logging.Level;

import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.bungeecord.BungeeHandler;
import com.alessiodp.parties.utils.bungeecord.ConfigHandlerBungee;
import com.alessiodp.parties.utils.bungeecord.Metrics;
import com.alessiodp.parties.utils.bungeecord.VariablesBungee;

import net.md_5.bungee.api.plugin.Plugin;


public class PartiesBungee extends Plugin{
	private static PartiesBungee instance;
	
	private ConfigHandlerBungee config;
	
	public static final String channel = "PartiesBungee";
	private static final int ver_config = 1;
	
	public static PartiesBungee getInstance() {return instance;}

	
	@Override
	public void onEnable(){
		log(ConsoleColors.CYAN.getCode() + "Initializing Parties " + this.getDescription().getVersion() + " Bungeecord Version");
		instance = this;
		handle();
		log(ConsoleColors.CYAN.getCode() + "Parties enabled");
	}
	@Override
	public void onDisable() {
		log(ConsoleColors.CYAN.getCode() + "Parties disabled");
	}

	
	private void handle() {
		config = new ConfigHandlerBungee(this);
		this.getProxy().registerChannel(channel);
		this.getProxy().getPluginManager().registerListener(this, new BungeeHandler(this));
		
		Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.SimplePie("how_many_follow_party_enabled") {
			@Override
			public String getValue(){
				if(VariablesBungee.follow_enable)
					return "Enabled";
				return "Disabled";
			}
		});
	}


	public ConfigHandlerBungee getConfigHandler(){return config;}
	public int getConfigVersion(){return ver_config;}
	
	public void log(String msg) {
		getProxy().getLogger().log(Level.INFO, msg + ConsoleColors.RESET.getCode());
	}
}
