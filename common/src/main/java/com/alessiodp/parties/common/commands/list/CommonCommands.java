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
	
	CLOSE,
	COLOR,
	CREATEFIXED,
	DEBUG,
	DESC,
	FOLLOW,
	HOME,
	JOIN,
	LIST,
	MOTD,
	MUTE,
	NICKNAME,
	OPEN,
	PASSWORD,
	PROTECTION,
	SETHOME,
	TELEPORT;
	
	@Override
	public String getOriginalName() {
		return this.name();
	}
}
