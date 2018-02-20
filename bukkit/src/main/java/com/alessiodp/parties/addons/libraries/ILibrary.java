package com.alessiodp.parties.addons.libraries;

public enum ILibrary {
	HIKARI("HikariCP",
			"2.7.4",
			"%name%-%version%.jar",
			"https://repo1.maven.org/maven2/com/zaxxer/%name%/%version%/%file%"),
	SLF4J_API("slf4j-api",
			"1.7.25",
			"%name%-%version%.jar",
			"https://repo1.maven.org/maven2/org/slf4j/%name%/%version%/%file%"),
	SLF4J_SIMPLE("slf4j-simple",
			"1.7.25",
			"%name%-%version%.jar",
			"https://repo1.maven.org/maven2/org/slf4j/%name%/%version%/%file%"),
	SQLITE_JDBC("sqlite-jdbc",
			"3.21.0",
			"%name%-%version%.jar",
			"https://repo1.maven.org/maven2/org/xerial/%name%/%version%/%file%");
	
	private String name;
	private String version;
	private String file;
	private String url;
	
	ILibrary(String n, String v, String f, String u) {
		name = n;
		version = v;
		file = f;
		url = u;
	}
	
	public String getName() {
		return name;
	}
	public String getVersion() {
		return version;
	}
	public String getFile() {
		return file
				.replace("%name%", name)
				.replace("%version%", version);
	}
	public String getUrl() {
		return url
				.replace("%file%", file) // Replace file first to replace next version & name
				.replace("%version%", version)
				.replace("%name%", name);
	}
}
