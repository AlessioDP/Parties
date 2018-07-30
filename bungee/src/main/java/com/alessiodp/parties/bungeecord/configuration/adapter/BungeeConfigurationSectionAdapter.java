package com.alessiodp.parties.bungeecord.configuration.adapter;

import com.alessiodp.parties.common.configuration.adapter.ConfigurationSectionAdapter;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.Set;

public class BungeeConfigurationSectionAdapter implements ConfigurationSectionAdapter {
	private Configuration configurationSection;
	
	public BungeeConfigurationSectionAdapter(Configuration configurationSection) {
		this.configurationSection = configurationSection;
	}
	
	@Override
	public void reload() {}
	
	@Override
	public boolean contains(String path) {
		return configurationSection.contains(path);
	}
	
	@Override
	public ConfigurationSectionAdapter getConfigurationSection(String path) {
		return new BungeeConfigurationSectionAdapter(configurationSection.getSection(path));
	}
	
	@Override
	public boolean getBoolean(String path, boolean def) {
		return configurationSection.getBoolean(path, def);
	}
	
	@Override
	public double getDouble(String path, double def) {
		return configurationSection.getDouble(path, def);
	}
	
	@Override
	public int getInt(String path, int def) {
		return configurationSection.getInt(path, def);
	}
	
	@Override
	public String getString(String path, String def) {
		return configurationSection.getString(path, def);
	}
	
	@Override
	public List<String> getStringList(String path, List<String> def) {
		List<String> ret = def;
		if (contains(path)) {
			ret = configurationSection.getStringList(path);
		}
		return ret;
	}
	
	@Override
	public Set<String> getKeys() {
		return (Set<String>) configurationSection.getKeys();
	}
}
