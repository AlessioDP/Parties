package com.alessiodp.parties.bungeecord.configuration.adapter;

import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationSectionAdapter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class BungeeConfigurationAdapter implements ConfigurationAdapter {
	private Configuration yaml;
	private Path filePath;
	
	public BungeeConfigurationAdapter(Path filePath) {
		this.filePath = filePath;
		reload();
	}
	
	@Override
	public void reload() {
		try {
			yaml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(filePath.toFile());
		} catch (IOException ex) {
			// Never tested error, let's print an error if encountered
			ex.printStackTrace();
		}
	}
	
	@Override
	public boolean contains(String path) {
		return yaml.contains(path);
	}
	
	@Override
	public ConfigurationSectionAdapter getConfigurationSection(String path) {
		return new BungeeConfigurationSectionAdapter(yaml.getSection(path));
	}
	
	@Override
	public boolean getBoolean(String path, boolean def) {
		return yaml.getBoolean(path, def);
	}
	
	@Override
	public double getDouble(String path, double def) {
		return yaml.getDouble(path, def);
	}
	
	@Override
	public int getInt(String path, int def) {
		return yaml.getInt(path, def);
	}
	
	@Override
	public String getString(String path, String def) {
		return yaml.getString(path, def);
	}
	
	@Override
	public List<String> getStringList(String path, List<String> def) {
		List<String> ret = def;
		if (contains(path)) {
			ret = yaml.getStringList(path);
		}
		return ret;
	}
}
