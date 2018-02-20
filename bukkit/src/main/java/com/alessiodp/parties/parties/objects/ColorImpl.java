package com.alessiodp.parties.parties.objects;

import com.alessiodp.partiesapi.interfaces.Color;

public class ColorImpl implements Color {
	private String name;
	private String command;
	private String code;
	
	private int dynamicPriority;
	private int dynamicMembers;
	private int dynamicKills;
	
	public ColorImpl(String nm, String cmd, String cd, int dynP, int dynM, int dynK) {
		name = nm;
		command = cmd;
		code = cd;
		dynamicPriority = dynP;
		dynamicMembers = dynM;
		dynamicKills = dynK;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public void setCommand(String command) {
		this.command = command;
	}
	@Override
	public String getCommand() {
		return command;
	}
	
	
	@Override
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String getCode() {
		return code;
	}
	
	
	@Override
	public void setDynamicPriority(int priority) {
		dynamicPriority = priority;
		
	}
	@Override
	public int getDynamicPriority() {
		return dynamicPriority;
	}
	
	
	@Override
	public void setDynamicMembers(int members) {
		dynamicMembers = members;
	}
	@Override
	public int getDynamicMembers() {
		return dynamicMembers;
	}
	
	
	@Override
	public void setDynamicKills(int kills) {
		dynamicKills = kills;
	}
	@Override
	public int getDynamicKills() {
		return dynamicKills;
	}
}
