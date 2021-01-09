package com.alessiodp.parties.common.commands.main;

import com.alessiodp.core.common.commands.list.ADPCommand;
import com.alessiodp.core.common.commands.utils.ADPExecutableCommand;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandAccept;
import com.alessiodp.parties.common.commands.sub.CommandAsk;
import com.alessiodp.parties.common.commands.sub.CommandChat;
import com.alessiodp.parties.common.commands.sub.CommandColor;
import com.alessiodp.parties.common.commands.sub.CommandCreate;
import com.alessiodp.parties.common.commands.sub.CommandCreateFixed;
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
import com.alessiodp.parties.common.commands.sub.CommandNickname;
import com.alessiodp.parties.common.commands.sub.CommandPassword;
import com.alessiodp.parties.common.commands.sub.CommandProtection;
import com.alessiodp.parties.common.commands.sub.CommandRank;
import com.alessiodp.parties.common.commands.sub.CommandReload;
import com.alessiodp.parties.common.commands.sub.CommandRename;
import com.alessiodp.parties.common.commands.sub.CommandSpy;
import com.alessiodp.parties.common.commands.sub.CommandTag;
import com.alessiodp.parties.common.commands.sub.CommandVersion;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandParty extends ADPMainCommand {
	
	public CommandParty(PartiesPlugin plugin) {
		super(plugin, CommonCommands.PARTY, ConfigMain.COMMANDS_CMD_PARTY, true);
		
		subCommands = new HashMap<>();
		subCommandsByEnum = new HashMap<>();
		tabSupport = ConfigMain.COMMANDS_TABSUPPORT;
		
		register(new CommandHelp(plugin, this));
		
		if (!plugin.isBungeeCordEnabled()) { // In BungeeCord this bool is false
			register(new CommandAccept(plugin, this));
			register(new CommandCreate(plugin, this));
			register(new CommandDelete(plugin, this));
			register(new CommandDeny(plugin, this));
			register(new CommandIgnore(plugin, this));
			register(new CommandInfo(plugin, this));
			register(new CommandInvite(plugin, this));
			register(new CommandKick(plugin, this));
			register(new CommandLeave(plugin, this));
			register(new CommandRank(plugin, this));
			register(new CommandRename(plugin, this));
			register(new CommandSpy(plugin, this));
		}
		
		register(new CommandReload(plugin, this));
		register(new CommandVersion(plugin, this));
		
		if (!plugin.isBungeeCordEnabled()) {
			// Ask
			if (ConfigParties.ADDITIONAL_ASK_ENABLE)
				register(new CommandAsk(plugin, this));
			
			// Chat
			if (ConfigParties.GENERAL_CHAT_TOGGLECOMMAND)
				register(new CommandChat(plugin, this));
			
			// Color
			if (ConfigParties.ADDITIONAL_COLOR_ENABLE)
				register(new CommandColor(plugin, this));
			
			// Create fixed
			if (ConfigParties.ADDITIONAL_FIXED_ENABLE)
				register(new CommandCreateFixed(plugin, this));
			
			// Desc
			if (ConfigParties.ADDITIONAL_DESC_ENABLE)
				register(new CommandDesc(plugin, this));
			
			// Follow
			if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE && ConfigMain.ADDITIONAL_FOLLOW_TOGGLECMD)
				register(new CommandFollow(plugin, this));
			
			// Join
			if (ConfigParties.ADDITIONAL_JOIN_ENABLE)
				register(new CommandJoin(plugin, this));
			
			// List
			if (ConfigParties.ADDITIONAL_LIST_ENABLE)
				register(new CommandList(plugin, this));
			
			// Motd
			if (ConfigParties.ADDITIONAL_MOTD_ENABLE)
				register(new CommandMotd(plugin, this));
			
			// Mute
			if (ConfigMain.ADDITIONAL_MUTE_ENABLE)
				register(new CommandMute(plugin, this));
			
			// Nickname
			if (ConfigParties.ADDITIONAL_NICKNAME_ENABLE)
				register(new CommandNickname(plugin, this));
			
			// Password
			if (ConfigParties.ADDITIONAL_JOIN_PASSWORD_ENABLE)
				register(new CommandPassword(plugin, this));
			
			// Protection
			if (ConfigParties.ADDITIONAL_FRIENDLYFIRE_TYPE.equalsIgnoreCase("command"))
				super.register(new CommandProtection(plugin, this));
			
			// Tag
			if (ConfigParties.ADDITIONAL_TAG_ENABLE)
				register(new CommandTag(plugin, this));
		}
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		String subCommand;
		if (sender.isPlayer() && ((PartiesPlugin) plugin).isBungeeCordEnabled()) {
			return false; // If BungeeCord enabled, commands are only allowed via console
		}
		
		if (sender.isPlayer()) {
			if (args.length == 0) {
				// Set /party to /party help
				subCommand = CommonUtils.toLowerCase(ConfigMain.COMMANDS_CMD_HELP);
			} else {
				subCommand = CommonUtils.toLowerCase(args[0]);
			}
			
			if (exists(subCommand)) {
				plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
			} else {
				sender.sendMessage(Messages.PARTIES_COMMON_INVALIDCMD, true);
			}
		} else {
			// Console
			if (args.length > 0) {
				subCommand = CommonUtils.toLowerCase(args[0]);
				if (exists(subCommand) && getSubCommand(subCommand).isExecutableByConsole()) {
					plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), getSubCommand(subCommand), args);
				} else {
					plugin.logConsole(Color.translateAndStripColor(Messages.PARTIES_COMMON_INVALIDCMD), false);
				}
			} else {
				// Print help
				plugin.logConsole(Messages.HELP_CONSOLEHELP_HEADER, false);
				for(Map.Entry<ADPCommand, ADPExecutableCommand> e : plugin.getCommandManager().getOrderedCommands().entrySet()) {
					if (e.getValue().isExecutableByConsole()  && e.getValue().isListedInHelp()) {
						plugin.logConsole(Messages.HELP_CONSOLEHELP_COMMAND
								.replace("%command%", e.getValue().getConsoleSyntax())
								.replace("%description%", e.getValue().getDescription()), false);
					}
				}
			}
		}
		return true;
	}
}
