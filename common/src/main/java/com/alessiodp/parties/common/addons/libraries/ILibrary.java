package com.alessiodp.parties.common.addons.libraries;

import com.alessiodp.parties.common.configuration.Constants;
import lombok.Getter;

public enum ILibrary {
	CONFIGURATE_CORE("configurate-core",
			"3.3",
			"ninja.leaping.configurate"),
	CONFIGURATE_YAML("configurate-yaml",
			"3.3",
			"ninja.leaping.configurate"),
	HIKARI("HikariCP",
			"3.1.0",
			"com.zaxxer"),
	SLF4J_API("slf4j-api",
			"1.7.25",
			"org.slf4j"),
	SLF4J_SIMPLE("slf4j-simple",
			"1.7.25",
			"org.slf4j"),
	SQLITE_JDBC("sqlite-jdbc",
			"3.21.0.1",
			"org.xerial");
	
	@Getter private String name;
	@Getter private String version;
	private String file;
	private String pack;
	
	ILibrary(String name, String version, String pack) {
		this.name = name;
		this.version = version;
		this.file = "%name%-%version%.jar";
		this.pack = pack;
	}
	
	public String getFile() {
		return file
				.replace("%name%", name)
				.replace("%version%", version);
	}
	public String getDownloadUrl() {
		return Constants.LIBRARY_URL
				.replace("%package%", pack.replace(".", "/"))
				.replace("%file%", file) // Replace file first to replace next version & name
				.replace("%version%", version)
				.replace("%name%", name);
	}
}
