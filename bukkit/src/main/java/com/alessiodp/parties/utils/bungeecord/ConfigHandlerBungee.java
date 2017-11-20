package com.alessiodp.parties.utils.bungeecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alessiodp.parties.PartiesBungee;
import com.alessiodp.parties.utils.enums.ConsoleColors;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigHandlerBungee {
	PartiesBungee plugin;
	VariablesBungee variables;

	public ConfigHandlerBungee(PartiesBungee instance) {
		plugin = instance;
		variables = new VariablesBungee();
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
		
		if (cfg.getInt("dont-edit-this.config-version") < plugin.getConfigVersion())
			plugin.log(ConsoleColors.RED.getCode() + "Configuration file outdated");
		
		if (cfg.get("follow-party.enable") != null)
			VariablesBungee.follow_enable = cfg.getBoolean("follow-party.enable");
		if (cfg.get("follow-party.needed-rank") != null)
			VariablesBungee.follow_neededrank = cfg.getInt("follow-party.needed-rank");
		if (cfg.get("follow-party.minimum-rank") != null)
			VariablesBungee.follow_minimumrank = cfg.getInt("follow-party.minimum-rank");
		VariablesBungee.follow_listserver = cfg.getStringList("follow-party.list-server");
		
	}
}
