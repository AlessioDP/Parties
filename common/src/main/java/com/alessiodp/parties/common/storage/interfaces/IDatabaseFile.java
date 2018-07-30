package com.alessiodp.parties.common.storage.interfaces;

import ninja.leaping.configurate.ConfigurationNode;

import java.io.IOException;
import java.nio.file.Path;

public interface IDatabaseFile {
	
	void initFile();
	void saveFile() throws IOException;
	Path createDataFile();
	boolean prepareNewOutput();
	boolean isFailed();
	
	ConfigurationNode getRootNode();
}
