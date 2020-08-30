package com.alessiodp.parties.common.commands.list;

import com.alessiodp.core.common.commands.list.ADPCommand;

public enum CommonCommands implements ADPCommand {
	P,
	PARTY,
	
	HELP,
	ACCEPT,
	ASK,
	CHAT,
	CREATE,
	DELETE,
	DENY,
	IGNORE,
	INFO,
	INVITE,
	KICK,
	LEAVE,
	RANK,
	RELOAD,
	RENAME,
	SPY,
	TAG,
	VERSION,
	
	COLOR,
	DEBUG,
	DESC,
	FOLLOW,
	JOIN,
	LIST,
	MOTD,
	MUTE,
	PASSWORD,
	TELEPORT;
	
	@Override
	public String getOriginalName() {
		return this.name();
	}
}
