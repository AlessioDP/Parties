package com.alessiodp.parties.common.addons.libraries;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.google.common.io.ByteStreams;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public class LibraryManager {
	private PartiesPlugin plugin;
	private Path dataFolder;
	
	public LibraryManager(PartiesPlugin instance) {
		plugin = instance;
		dataFolder = plugin.getFolder().resolve(Constants.LIBRARY_FOLDER);
	}
	
	public boolean initLibrary(ILibrary lib) {
		boolean ret = true;
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_LIB_INIT_INIT
				.replace("{lib}", lib.getName()), true);
		if (!existLibrary(lib)) {
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_INIT_DL
					.replace("{lib}", lib.getName()), true);
			ret = downloadLibrary(lib);
		}
		if (ret) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_LIB_INIT_LOAD
					.replace("{lib}", lib.getName()), true);
			ret = loadLibrary(lib);
		}
		
		if (!ret) {
			LoggerManager.printError(Constants.DEBUG_LIB_INIT_FAIL
					.replace("{lib}", lib.getName()));
		}
		return ret;
	}
	
	private boolean existLibrary(ILibrary lib) {
		return Files.exists(dataFolder.resolve(lib.getFile()));
	}
	
	private boolean downloadLibrary(ILibrary lib) {
		boolean ret = false;
		try {
			Path filePath = dataFolder.resolve(lib.getFile());
			
			if (!Files.exists(filePath)) {
				URL url = new URL(lib.getDownloadUrl());
				
				Files.createDirectories(dataFolder);
				
				try (InputStream input = url.openStream()) {
					byte[] data = ByteStreams.toByteArray(input);
					
					Files.write(filePath, data);
				}
			}
			ret = true;
		} catch (Exception ex) {
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_FAILED_DL
					.replace("{lib}", lib.getName())
					.replace("{message}", ex.getMessage()), true);
		}
		return ret;
	}
	
	private boolean loadLibrary(ILibrary lib) {
		boolean ret = false;
		Path filePath = dataFolder.resolve(lib.getFile());
		if (Files.exists(filePath)) {
			try {
				Class<URLClassLoader> sysclass = URLClassLoader.class;
				Method m = sysclass.getDeclaredMethod("addURL", URL.class);
				m.setAccessible(true);
				
				m.invoke(plugin.getClass().getClassLoader(), filePath.toUri().toURL());
				ret = true;
			} catch (Exception ex) {
				LoggerManager.printError(Constants.DEBUG_LIB_FAILED_DL
						.replace("{lib}", lib.getName()));
				ex.printStackTrace();
			}
		}
		return ret;
	}
}
