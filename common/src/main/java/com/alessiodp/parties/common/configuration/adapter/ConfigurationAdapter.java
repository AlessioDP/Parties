package com.alessiodp.parties.common.configuration.adapter;

import java.util.List;

public interface ConfigurationAdapter {
	void reload();
	boolean contains(String path);
	ConfigurationSectionAdapter getConfigurationSection(String path);
	
	boolean getBoolean(String path, boolean def);
	double getDouble(String path, double def);
	int getInt(String path, int def);
	String getString(String path, String def);
	List<String> getStringList(String path, List<String> def);
}
