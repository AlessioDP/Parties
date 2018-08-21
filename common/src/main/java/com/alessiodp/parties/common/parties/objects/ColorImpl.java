package com.alessiodp.parties.common.parties.objects;

import com.alessiodp.parties.api.interfaces.Color;

import java.util.Objects;

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
	
	@Override
	public int hashCode() {
		return Objects.hash(name, command, code, dynamicPriority, dynamicMembers, dynamicKills);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this || other instanceof ColorImpl) {
			return Objects.equals(name, ((ColorImpl) other).name)
					&& Objects.equals(command, ((ColorImpl) other).command)
					&& Objects.equals(code, ((ColorImpl) other).code)
					&& dynamicPriority == ((ColorImpl) other).dynamicPriority
					&& dynamicMembers == ((ColorImpl) other).dynamicMembers
					&& dynamicKills == ((ColorImpl) other).dynamicKills;
		}
		return false;
	}
}
