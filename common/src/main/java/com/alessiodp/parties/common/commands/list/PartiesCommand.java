package com.alessiodp.parties.common.commands.list;

public interface PartiesCommand {
	String getCommand();
	String getHelp();
	String getType();
	String getPermission();
}
