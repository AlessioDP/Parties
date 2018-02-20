package com.alessiodp.parties.addons.libraries;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;

public class LibraryManager {
	private Parties plugin;
	private File dataFolder;
	
	public LibraryManager(Parties instance) {
		plugin = instance;
		dataFolder = new File(plugin.getDataFolder(), Constants.LIBRARY_FOLDER);
	}
	
	public boolean initLibrary(ILibrary lib) {
		boolean ret = true;
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_LIB_INIT_INIT
				.replace("{lib}", lib.getName()), true);
		if (!existLibrary(lib)) {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_LIB_INIT_DL
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
		File file = new File(dataFolder, lib.getFile());
		return file.exists();
	}
	
	private boolean downloadLibrary(ILibrary lib) {
		boolean ret = false;
		try {
			URL url = new URL(lib.getUrl());
			if (!dataFolder.exists())
				dataFolder.mkdirs();
			File file = new File(dataFolder, lib.getFile());
			try (
					BufferedInputStream input = new BufferedInputStream(url.openStream());
					FileOutputStream output = new FileOutputStream(file);
					){
				byte data[] = new byte[1024];
				int count;
				while ((count = input.read(data, 0, 1024)) != -1) {
					output.write(data, 0, count);
				}
				ret = true;
			}
		} catch (Exception ex) {
			LoggerManager.log(LogLevel.BASE, Constants.DEBUG_LIB_FAILED_DL
					.replace("{lib}", lib.getName())
					.replace("{message}", ex.getMessage()), true);
		}
		return ret;
	}
	
	private boolean loadLibrary(ILibrary lib) {
		boolean ret = false;
		File file = new File(dataFolder, lib.getFile());
		if (file.exists()) {
			try {
				Class<URLClassLoader> sysclass = URLClassLoader.class;
				Method m = sysclass.getDeclaredMethod("addURL", new Class[] {URL.class});
				m.setAccessible(true);
				
				m.invoke(plugin.getClass().getClassLoader(), file.toURI().toURL());
				
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
