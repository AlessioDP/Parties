package com.alessiodp.parties.utils;

public class PartyColor {
	private String name;
	private String command;
	private String code;
	
	public PartyColor(String name, String command, String code) {
		this.name = name;
		this.command = command;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public String getCommand() {
		return command;
	}
	public String getCode() {
		return code;
	}
}
