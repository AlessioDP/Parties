package com.alessiodp.parties.bungeecord.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alessiodp.parties.bungeecord.PartiesBungee;
import com.alessiodp.parties.bungeecord.utils.ConsoleColor;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigurationManager {
	private PartiesBungee plugin;

	public ConfigurationManager(PartiesBungee instance) {
		plugin = instance;
		new ConfigMain();
		reloadConfig();
	}
	
	public void reloadConfig() {
		File cfgFile = new File(plugin.getDataFolder(), "config_bungee.yml");
		if (!cfgFile.exists()) {
			try {
				InputStream in = plugin.getResourceAsStream("config_bungee.yml");
				byte[] buffer = new byte[in.available()];
				in.read(buffer);
				in.close();
				cfgFile.getParentFile().mkdirs();
				
				OutputStream out = new FileOutputStream(cfgFile);
				out.write(buffer);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Configuration cfg;
		try {
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfgFile);
		} catch (IOException e) {return;}
		
		if (cfg.getInt("dont-edit-this.config-version") < Constants.VERSION_CONFIG_MAIN)
			plugin.log(ConsoleColor.RED.getCode() + "Configuration file outdated");
		
		if (cfg.get("follow-party.enable") != null)
			ConfigMain.follow_enable = cfg.getBoolean("follow-party.enable");
		if (cfg.get("follow-party.needed-rank") != null)
			ConfigMain.follow_neededrank = cfg.getInt("follow-party.needed-rank");
		if (cfg.get("follow-party.minimum-rank") != null)
			ConfigMain.follow_minimumrank = cfg.getInt("follow-party.minimum-rank");
		ConfigMain.follow_listserver = cfg.getStringList("follow-party.list-server");
		
	}
}
