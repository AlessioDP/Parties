package com.alessiodp.parties.common.commands.list;

import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import lombok.Getter;

public enum CommonCommands implements PartiesCommand {
	P,
	PARTY,
	HELP,
	
	ACCEPT,
	CHAT,
	CREATE,
	DELETE,
	DENY,
	IGNORE,
	INFO,
	INVITE,
	KICK,
	LEAVE,
	MIGRATE,
	RANK,
	RELOAD,
	RENAME,
	SPY,
	
	COLOR,
	DESC,
	JOIN,
	LIST,
	MOTD,
	MUTE,
	PASSWORD;
	
	
	@Getter private String command;
	@Getter private String help;
	
	CommonCommands() {
		command = "";
		help = "";
	}
	
	public static void setup() {
		CommonCommands.P.command = ConfigMain.COMMANDS_CMD_P;
		CommonCommands.P.help = Messages.HELP_MAINCMD_P;
		CommonCommands.PARTY.command = ConfigMain.COMMANDS_CMD_PARTY;
		CommonCommands.HELP.command = ConfigMain.COMMANDS_CMD_HELP;
		CommonCommands.HELP.help = Messages.HELP_MAINCMD_HELP;
		
		CommonCommands.ACCEPT.command = ConfigMain.COMMANDS_CMD_ACCEPT;
		CommonCommands.ACCEPT.help = Messages.HELP_MAINCMD_ACCEPT;
		CommonCommands.CHAT.command = ConfigMain.COMMANDS_CMD_CHAT;
		CommonCommands.CHAT.help = Messages.HELP_MAINCMD_CHAT;
		CommonCommands.CREATE.command = ConfigMain.COMMANDS_CMD_CREATE;
		CommonCommands.CREATE.help = Messages.HELP_MAINCMD_CREATE;
		CommonCommands.DELETE.command = ConfigMain.COMMANDS_CMD_DELETE;
		CommonCommands.DELETE.help = Messages.HELP_MAINCMD_DELETE;
		CommonCommands.DENY.command = ConfigMain.COMMANDS_CMD_DENY;
		CommonCommands.DENY.help = Messages.HELP_MAINCMD_DENY;
		CommonCommands.IGNORE.command = ConfigMain.COMMANDS_CMD_IGNORE;
		CommonCommands.IGNORE.help = Messages.HELP_MAINCMD_IGNORE;
		CommonCommands.INFO.command = ConfigMain.COMMANDS_CMD_INFO;
		CommonCommands.INFO.help = Messages.HELP_MAINCMD_INFO;
		CommonCommands.INVITE.command = ConfigMain.COMMANDS_CMD_INVITE;
		CommonCommands.INVITE.help = Messages.HELP_MAINCMD_INVITE;
		CommonCommands.KICK.command = ConfigMain.COMMANDS_CMD_KICK;
		CommonCommands.KICK.help = Messages.HELP_MAINCMD_KICK;
		CommonCommands.LEAVE.command = ConfigMain.COMMANDS_CMD_LEAVE;
		CommonCommands.LEAVE.help = Messages.HELP_MAINCMD_LEAVE;
		CommonCommands.MIGRATE.command = ConfigMain.COMMANDS_CMD_MIGRATE;
		CommonCommands.MIGRATE.help = Messages.HELP_MAINCMD_MIGRATE;
		CommonCommands.RANK.command = ConfigMain.COMMANDS_CMD_RANK;
		CommonCommands.RANK.help = Messages.HELP_MAINCMD_RANK;
		CommonCommands.RELOAD.command = ConfigMain.COMMANDS_CMD_RELOAD;
		CommonCommands.RELOAD.help = Messages.HELP_MAINCMD_RELOAD;
		CommonCommands.RENAME.command = ConfigMain.COMMANDS_CMD_RENAME;
		CommonCommands.RENAME.help = Messages.HELP_MAINCMD_RENAME;
		CommonCommands.SPY.command = ConfigMain.COMMANDS_CMD_SPY;
		CommonCommands.SPY.help = Messages.HELP_MAINCMD_SPY;
		
		CommonCommands.COLOR.command = ConfigMain.COMMANDS_CMD_COLOR;
		CommonCommands.COLOR.help = Messages.HELP_ADDCMD_COLOR;
		CommonCommands.DESC.command = ConfigMain.COMMANDS_CMD_DESC;
		CommonCommands.DESC.help = Messages.HELP_ADDCMD_DESC;
		CommonCommands.JOIN.command = ConfigMain.COMMANDS_CMD_JOIN;
		CommonCommands.JOIN.help = Messages.HELP_ADDCMD_JOIN;
		CommonCommands.LIST.command = ConfigMain.COMMANDS_CMD_LIST;
		CommonCommands.LIST.help = Messages.HELP_ADDCMD_LIST;
		CommonCommands.MOTD.command = ConfigMain.COMMANDS_CMD_MOTD;
		CommonCommands.MOTD.help = Messages.HELP_ADDCMD_MOTD;
		CommonCommands.MUTE.command = ConfigMain.COMMANDS_CMD_MUTE;
		CommonCommands.MUTE.help = Messages.HELP_ADDCMD_MUTE;
		CommonCommands.PASSWORD.command = ConfigMain.COMMANDS_CMD_PASSWORD;
		CommonCommands.PASSWORD.help = Messages.HELP_ADDCMD_PASSWORD;
	}
	
	public String getType() {
		return this.name();
	}
}
