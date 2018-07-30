package com.alessiodp.parties.common.bootstrap;

import java.io.InputStream;
import java.nio.file.Path;

public abstract class AbstractPartiesBootstrap implements PartiesBootstrap {
	private PartiesBootstrap bootstrap;
	
	protected AbstractPartiesBootstrap(PartiesBootstrap instance) {
		bootstrap = instance;
	}
	
	@Override
	public PartiesBootstrap getBootstrap() {
		return bootstrap;
	}
	
	@Override
	public Path getFolder() {
		return bootstrap.getFolder();
	}
	
	@Override
	public String getVersion() {
		return bootstrap.getVersion();
	}
	
	@Override
	public void stopPlugin() {
		bootstrap.stopPlugin();
	}
	
	@Override
	public InputStream getResource(String resource) {
		return bootstrap.getResource(resource);
	}
}
