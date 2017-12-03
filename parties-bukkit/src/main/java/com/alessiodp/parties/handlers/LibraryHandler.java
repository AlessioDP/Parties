package com.alessiodp.parties.handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.utils.enums.Library;
import com.alessiodp.parties.utils.enums.LogLevel;

public class LibraryHandler {
	private Parties plugin;
	private File dataFolder;
	
	public LibraryHandler(Parties instance) {
		plugin = instance;
		dataFolder = new File(plugin.getDataFolder(), "lib/");
	}
	
	public boolean initLibrary(Library lib) {
		boolean ret = true;
		LogHandler.log(LogLevel.DEBUG, "Initializing library " + lib.getName(), true);
		if (!existLibrary(lib)) {
			LogHandler.log(LogLevel.DEBUG, "Downloading library " + lib.getName(), true);
			ret = downloadLibrary(lib);
		}
		if (ret) {
			LogHandler.log(LogLevel.DEBUG, "Loading library " + lib.getName(), true);
			ret = loadLibrary(lib);
		}
		
		if (!ret) {
			LogHandler.printError("Cannot load library " + lib.getName());
		}
		return ret;
	}
	
	private boolean existLibrary(Library lib) {
		File file = new File(dataFolder, lib.getFile());
		return file.exists();
	}
	
	private boolean downloadLibrary(Library lib) {
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
			LogHandler.log(LogLevel.BASE, "Failed to download library " + lib.getName() + ": " + ex.getMessage(), true);
		}
		return ret;
	}
	
	private boolean loadLibrary(Library lib) {
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
				LogHandler.printError("Something gone wrong with library load, report this to the developer!");
				ex.printStackTrace();
			}
		}
		return ret;
	}
}
