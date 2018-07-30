package com.alessiodp.parties.common.configuration;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.google.common.io.ByteStreams;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ConfigurationFile {
	protected PartiesPlugin plugin;
	
	protected ConfigurationFile(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public Path saveConfiguration(Path path) {
		Path ret = path.resolve(getFileName());
		try {
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			if (!Files.exists(ret)) {
				byte[] data = ByteStreams.toByteArray(plugin.getResource(getResourceName()));
				
				Files.write(ret, data);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
		return ret;
	}
	
	public void checkVersion(ConfigurationAdapter confAdapter, int currentVersion) {
		if (confAdapter.getInt("dont-edit-this.version", -1) < currentVersion) {
			plugin.logError(Constants.DEBUG_CONFIG_OUTDATED
					.replace("{name}", getFileName()));
		}
	}
	
	protected abstract String getFileName();
	protected abstract String getResourceName();
	public abstract void loadConfiguration(ConfigurationAdapter confAdapter);
}
