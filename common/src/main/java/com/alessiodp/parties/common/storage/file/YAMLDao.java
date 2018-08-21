package com.alessiodp.parties.common.storage.file;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.libraries.ILibrary;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.storage.interfaces.IDatabaseFile;
import lombok.Getter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class YAMLDao implements IDatabaseFile {
	private PartiesPlugin plugin;
	
	private Path dataPath;
	private ConfigurationLoader dataLoader;
	private ConfigurationNode dataNode;
	@Getter private boolean failed;
	
	public YAMLDao(PartiesPlugin instance) {
		plugin = instance;
	}
	
	
	@Override
	public void initFile() {
		LoggerManager.log(LogLevel.DEBUG, Constants.CLASS_INIT.replace("{class}", getClass().getSimpleName()), true);
		
		failed = false;
		if (plugin.getLibraryManager().initLibrary(ILibrary.CONFIGURATE_YAML)) {
			try {
				initData();
			} catch (IOException ex) {
				LoggerManager.printError(Constants.DEBUG_FILE_CREATEFAIL
						.replace("{message}", ex.getMessage()));
				failed = true;
			}
		} else {
			failed = true;
		}
		
		dataPath = createDataFile();
	}
	
	private void initData() throws IOException {
		dataPath = createDataFile();
		dataLoader = YAMLConfigurationLoader
				.builder()
				.setPath(dataPath)
				.setFlowStyle(DumperOptions.FlowStyle.BLOCK)
				.setIndent(2)
				.build();
		dataNode = dataLoader.load();
	}
	
	@Override
	public void saveFile() throws IOException {
		dataLoader.save(dataNode);
	}
	
	@Override
	public Path createDataFile() {
		Path ret = plugin.getFolder().resolve(ConfigMain.STORAGE_SETTINGS_FILE_YAML_DBNAME);
		if (!Files.exists(ret)) {
			// Create data file
			try {
				ConfigurationLoader<ConfigurationNode> loader = YAMLConfigurationLoader
						.builder()
						.setPath(ret)
						.setFlowStyle(DumperOptions.FlowStyle.BLOCK)
						.setIndent(2)
						.build();
				ConfigurationNode node = loader.load();
				node.getNode("database-version").setValue(Constants.VERSION_DATABASE_YAML);
				loader.save(node);
			} catch (Exception ex) {
				ex.printStackTrace();
				LoggerManager.printError(Constants.DEBUG_FILE_CREATEFAIL
						.replace("{message}", ex.getMessage()));
			}
		}
		return ret;
	}
	
	@Override
	public boolean prepareNewOutput() {
		boolean ret = false;
		try {
			String baseName = ConfigMain.STORAGE_SETTINGS_FILE_YAML_DBNAME + ConfigMain.STORAGE_MIGRATE_SUFFIX;
			String fileName = baseName;
			Path filePath = plugin.getFolder().resolve(fileName);
			int count = 1;
			while (Files.exists(filePath)) {
				fileName = baseName + count;
				filePath = plugin.getFolder().resolve(fileName);
				count++;
			}
			
			// Rename old data with the new name
			Files.move(dataPath, filePath);
			createDataFile();
			ret = true;
		} catch (Exception ex) {
			LoggerManager.printError(LoggerManager.formatErrorCallTrace(Constants.DEBUG_FILE_ERROR, ex));
		}
		return ret;
	}
	
	@Override
	public ConfigurationNode getRootNode() {
		return dataNode;
	}
}