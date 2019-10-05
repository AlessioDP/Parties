package com.alessiodp.parties.common.commands.main;

import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandAccept;
import com.alessiodp.parties.common.commands.sub.CommandChat;
import com.alessiodp.parties.common.commands.sub.CommandColor;
import com.alessiodp.parties.common.commands.sub.CommandCreate;
import com.alessiodp.parties.common.commands.sub.CommandDelete;
import com.alessiodp.parties.common.commands.sub.CommandDeny;
import com.alessiodp.parties.common.commands.sub.CommandDesc;
import com.alessiodp.parties.common.commands.sub.CommandFollow;
import com.alessiodp.parties.common.commands.sub.CommandHelp;
import com.alessiodp.parties.common.commands.sub.CommandIgnore;
import com.alessiodp.parties.common.commands.sub.CommandInfo;
import com.alessiodp.parties.common.commands.sub.CommandInvite;
import com.alessiodp.parties.common.commands.sub.CommandJoin;
import com.alessiodp.parties.common.commands.sub.CommandKick;
import com.alessiodp.parties.common.commands.sub.CommandLeave;
import com.alessiodp.parties.common.commands.sub.CommandList;
import com.alessiodp.parties.common.commands.sub.CommandMotd;
import com.alessiodp.parties.common.commands.sub.CommandMute;
import com.alessiodp.parties.common.commands.sub.CommandPassword;
import com.alessiodp.parties.common.commands.sub.CommandRank;
import com.alessiodp.parties.common.commands.sub.CommandReload;
import com.alessiodp.parties.common.commands.sub.CommandRename;
import com.alessiodp.parties.common.commands.sub.CommandSpy;
import com.alessiodp.parties.common.commands.sub.CommandVersion;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public abstract class CommandParty extends ADPMainCommand {
	
	public CommandParty(PartiesPlugin plugin) {
		super(plugin);
		
		commandName = ConfigMain.COMMANDS_CMD_PARTY;
		subCommands = new HashMap<>();
		enabledSubCommands = new ArrayList<>();
		tabSupport = ConfigMain.COMMANDS_TABSUPPORT;
		
		register(CommonCommands.HELP, new CommandHelp(plugin, this));
		register(CommonCommands.ACCEPT, new CommandAccept(plugin, this));
		register(CommonCommands.CHAT, new CommandChat(plugin, this));
		register(CommonCommands.CREATE, new CommandCreate(plugin, this));
		register(CommonCommands.DELETE, new CommandDelete(plugin, this));
		register(CommonCommands.DENY, new CommandDeny(plugin, this));
		register(CommonCommands.IGNORE, new CommandIgnore(plugin, this));
		register(CommonCommands.INFO, new CommandInfo(plugin, this));
		register(CommonCommands.INVITE, new CommandInvite(plugin, this));
		register(CommonCommands.KICK, new CommandKick(plugin, this));
		register(CommonCommands.LEAVE, new CommandLeave(plugin, this));
		register(CommonCommands.RANK, new CommandRank(plugin, this));
		register(CommonCommands.RELOAD, new CommandReload(plugin, this));
		register(CommonCommands.RENAME, new CommandRename(plugin, this));
		register(CommonCommands.SPY, new CommandSpy(plugin, this));
		register(CommonCommands.VERSION, new CommandVersion(plugin, this));
		
		// Color
		if (ConfigParties.COLOR_ENABLE)
			register(CommonCommands.COLOR, new CommandColor(plugin, this));
		
		// Desc
		if (ConfigParties.DESC_ENABLE)
			register(CommonCommands.DESC, new CommandDesc(plugin, this));
		
		// Follow
		if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE && ConfigMain.ADDITIONAL_FOLLOW_TOGGLECMD)
			register(CommonCommands.FOLLOW, new CommandFollow(plugin, this));
		
		
		// Join & Password
		if (ConfigParties.PASSWORD_ENABLE) {
			register(CommonCommands.JOIN, new CommandJoin(plugin, this));
			register(CommonCommands.PASSWORD, new CommandPassword(plugin, this));
		}
		
		// List
		if (ConfigParties.LIST_ENABLE)
			register(CommonCommands.LIST, new CommandList(plugin, this));
		
		// Motd
		if (ConfigParties.MOTD_ENABLE)
			register(CommonCommands.MOTD, new CommandMotd(plugin, this));
		
		// Mute
		if (ConfigMain.ADDITIONAL_MUTE_ENABLE)
			register(CommonCommands.MUTE, new CommandMute(plugin, this));
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		String subCommand;
		if (sender.isPlayer()) {
			if (args.length == 0) {
				// Set /party to /party help
				subCommand = ConfigMain.COMMANDS_CMD_HELP.toLowerCase(Locale.ENGLISH);
			} else {
				subCommand = args[0].toLowerCase(Locale.ENGLISH);
			}
			
			if (exists(subCommand)) {
				plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
			} else {
				sender.sendMessage(Messages.PARTIES_COMMON_INVALIDCMD, true);
			}
		} else {
			// Console
			if (args.length > 0) {
				subCommand = args[0].toLowerCase(Locale.ENGLISH);
				if (exists(subCommand) && getSubCommand(subCommand).isExecutableByConsole()) {
					plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
				} else {
					plugin.logConsole(plugin.getColorUtils().removeColors(Messages.PARTIES_COMMON_INVALIDCMD), false);
				}
			} else {
				// Print help
				for (String str : Messages.HELP_CONSOLEHELP) {
					plugin.logConsole(str, false);
				}
			}
		}
		return true;
	}
}
