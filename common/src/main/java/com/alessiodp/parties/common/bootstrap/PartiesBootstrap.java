package com.alessiodp.parties.common.bootstrap;

import java.io.InputStream;
import java.nio.file.Path;

public interface PartiesBootstrap {
	PartiesBootstrap getBootstrap();
	
	Path getFolder();
	String getVersion();
	void stopPlugin();
	
	InputStream getResource(String resource);
}
