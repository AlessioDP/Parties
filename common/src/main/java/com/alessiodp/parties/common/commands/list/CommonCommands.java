package com.alessiodp.parties.common.commands.list;

import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.PartiesPermission;
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
	VERSION,
	
	COLOR,
	DESC,
	FOLLOW,
	JOIN,
	LIST,
	MOTD,
	MUTE,
	PASSWORD,
	TELEPORT;
	
	
	@Getter private String command;
	@Getter private String help;
	@Getter private String permission;
	
	CommonCommands() {
		command = "";
		help = "";
		permission = "";
	}
	
	public static void setup() {
		CommonCommands.P.command = ConfigMain.COMMANDS_CMD_P;
		CommonCommands.P.help = Messages.HELP_MAINCMD_P;
		CommonCommands.P.permission = PartiesPermission.SENDMESSAGE.toString();
		CommonCommands.PARTY.command = ConfigMain.COMMANDS_CMD_PARTY;
		
		CommonCommands.HELP.command = ConfigMain.COMMANDS_CMD_HELP;
		CommonCommands.HELP.help = Messages.HELP_MAINCMD_HELP;
		CommonCommands.HELP.permission = PartiesPermission.HELP.toString();
		CommonCommands.ACCEPT.command = ConfigMain.COMMANDS_CMD_ACCEPT;
		CommonCommands.ACCEPT.help = Messages.HELP_MAINCMD_ACCEPT;
		CommonCommands.ACCEPT.permission = PartiesPermission.ACCEPT.toString();
		CommonCommands.CHAT.command = ConfigMain.COMMANDS_CMD_CHAT;
		CommonCommands.CHAT.help = Messages.HELP_MAINCMD_CHAT;
		CommonCommands.CHAT.permission = PartiesPermission.CHAT.toString();
		CommonCommands.CREATE.command = ConfigMain.COMMANDS_CMD_CREATE;
		CommonCommands.CREATE.help = Messages.HELP_MAINCMD_CREATE;
		CommonCommands.CREATE.permission = PartiesPermission.CREATE.toString();
		CommonCommands.DELETE.command = ConfigMain.COMMANDS_CMD_DELETE;
		CommonCommands.DELETE.help = Messages.HELP_MAINCMD_DELETE;
		CommonCommands.DELETE.permission = PartiesPermission.ADMIN_DELETE.toString();
		CommonCommands.DENY.command = ConfigMain.COMMANDS_CMD_DENY;
		CommonCommands.DENY.help = Messages.HELP_MAINCMD_DENY;
		CommonCommands.DENY.permission = PartiesPermission.DENY.toString();
		CommonCommands.IGNORE.command = ConfigMain.COMMANDS_CMD_IGNORE;
		CommonCommands.IGNORE.help = Messages.HELP_MAINCMD_IGNORE;
		CommonCommands.IGNORE.permission = PartiesPermission.IGNORE.toString();
		CommonCommands.INFO.command = ConfigMain.COMMANDS_CMD_INFO;
		CommonCommands.INFO.help = Messages.HELP_MAINCMD_INFO;
		CommonCommands.INFO.permission = PartiesPermission.INFO.toString();
		CommonCommands.INVITE.command = ConfigMain.COMMANDS_CMD_INVITE;
		CommonCommands.INVITE.help = Messages.HELP_MAINCMD_INVITE;
		CommonCommands.INVITE.permission = PartiesPermission.INVITE.toString();
		CommonCommands.KICK.command = ConfigMain.COMMANDS_CMD_KICK;
		CommonCommands.KICK.help = Messages.HELP_MAINCMD_KICK;
		CommonCommands.KICK.permission = PartiesPermission.KICK.toString();
		CommonCommands.LEAVE.command = ConfigMain.COMMANDS_CMD_LEAVE;
		CommonCommands.LEAVE.help = Messages.HELP_MAINCMD_LEAVE;
		CommonCommands.LEAVE.permission = PartiesPermission.LEAVE.toString();
		CommonCommands.MIGRATE.command = ConfigMain.COMMANDS_CMD_MIGRATE;
		CommonCommands.MIGRATE.help = Messages.HELP_MAINCMD_MIGRATE;
		CommonCommands.MIGRATE.permission = PartiesPermission.ADMIN_MIGRATE.toString();
		CommonCommands.RANK.command = ConfigMain.COMMANDS_CMD_RANK;
		CommonCommands.RANK.help = Messages.HELP_MAINCMD_RANK;
		CommonCommands.RANK.permission = PartiesPermission.RANK.toString();
		CommonCommands.RELOAD.command = ConfigMain.COMMANDS_CMD_RELOAD;
		CommonCommands.RELOAD.help = Messages.HELP_MAINCMD_RELOAD;
		CommonCommands.RELOAD.permission = PartiesPermission.ADMIN_RELOAD.toString();
		CommonCommands.RENAME.command = ConfigMain.COMMANDS_CMD_RENAME;
		CommonCommands.RENAME.help = Messages.HELP_MAINCMD_RENAME;
		CommonCommands.RENAME.permission = PartiesPermission.RENAME.toString();
		CommonCommands.SPY.command = ConfigMain.COMMANDS_CMD_SPY;
		CommonCommands.SPY.help = Messages.HELP_MAINCMD_SPY;
		CommonCommands.SPY.permission = PartiesPermission.ADMIN_SPY.toString();
		CommonCommands.VERSION.command = ConfigMain.COMMANDS_CMD_VERSION;
		CommonCommands.VERSION.help = Messages.HELP_MAINCMD_VERSION;
		CommonCommands.VERSION.permission = PartiesPermission.ADMIN_VERSION.toString();
		
		CommonCommands.COLOR.command = ConfigMain.COMMANDS_CMD_COLOR;
		CommonCommands.COLOR.help = Messages.HELP_ADDCMD_COLOR;
		CommonCommands.COLOR.permission = PartiesPermission.COLOR.toString();
		CommonCommands.DESC.command = ConfigMain.COMMANDS_CMD_DESC;
		CommonCommands.DESC.help = Messages.HELP_ADDCMD_DESC;
		CommonCommands.DESC.permission = PartiesPermission.DESC.toString();
		CommonCommands.FOLLOW.command = ConfigMain.COMMANDS_CMD_FOLLOW;
		CommonCommands.FOLLOW.help = Messages.HELP_ADDCMD_FOLLOW;
		CommonCommands.FOLLOW.permission = PartiesPermission.FOLLOW.toString();
		CommonCommands.JOIN.command = ConfigMain.COMMANDS_CMD_JOIN;
		CommonCommands.JOIN.help = Messages.HELP_ADDCMD_JOIN;
		CommonCommands.JOIN.permission = PartiesPermission.JOIN.toString();
		CommonCommands.LIST.command = ConfigMain.COMMANDS_CMD_LIST;
		CommonCommands.LIST.help = Messages.HELP_ADDCMD_LIST;
		CommonCommands.LIST.permission = PartiesPermission.LIST.toString();
		CommonCommands.MOTD.command = ConfigMain.COMMANDS_CMD_MOTD;
		CommonCommands.MOTD.help = Messages.HELP_ADDCMD_MOTD;
		CommonCommands.MOTD.permission = PartiesPermission.MOTD.toString();
		CommonCommands.MUTE.command = ConfigMain.COMMANDS_CMD_MUTE;
		CommonCommands.MUTE.help = Messages.HELP_ADDCMD_MUTE;
		CommonCommands.MUTE.permission = PartiesPermission.MUTE.toString();
		CommonCommands.PASSWORD.command = ConfigMain.COMMANDS_CMD_PASSWORD;
		CommonCommands.PASSWORD.help = Messages.HELP_ADDCMD_PASSWORD;
		CommonCommands.PASSWORD.permission = PartiesPermission.PASSWORD.toString();
		CommonCommands.TELEPORT.command = ConfigMain.COMMANDS_CMD_TELEPORT;
		CommonCommands.TELEPORT.help = Messages.HELP_ADDCMD_TELEPORT;
		CommonCommands.TELEPORT.permission = PartiesPermission.TELEPORT.toString();
	}
	
	public String getType() {
		return this.name();
	}
}
