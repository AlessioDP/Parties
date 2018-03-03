package com.alessiodp.parties.commands;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.internal.PasswordFilter;
import com.alessiodp.parties.addons.internal.TabCompleterHandler;
import com.alessiodp.parties.commands.list.CommandAccept;
import com.alessiodp.parties.commands.list.CommandChat;
import com.alessiodp.parties.commands.list.CommandClaim;
import com.alessiodp.parties.commands.list.CommandColor;
import com.alessiodp.parties.commands.list.CommandConfirm;
import com.alessiodp.parties.commands.list.CommandCreate;
import com.alessiodp.parties.commands.list.CommandDelete;
import com.alessiodp.parties.commands.list.CommandDeny;
import com.alessiodp.parties.commands.list.CommandDesc;
import com.alessiodp.parties.commands.list.CommandHelp;
import com.alessiodp.parties.commands.list.CommandHome;
import com.alessiodp.parties.commands.list.CommandIgnore;
import com.alessiodp.parties.commands.list.CommandInfo;
import com.alessiodp.parties.commands.list.CommandInvite;
import com.alessiodp.parties.commands.list.CommandJoin;
import com.alessiodp.parties.commands.list.CommandKick;
import com.alessiodp.parties.commands.list.CommandLeave;
import com.alessiodp.parties.commands.list.CommandList;
import com.alessiodp.parties.commands.list.CommandMigrate;
import com.alessiodp.parties.commands.list.CommandMotd;
import com.alessiodp.parties.commands.list.CommandNotify;
import com.alessiodp.parties.commands.list.CommandP;
import com.alessiodp.parties.commands.list.CommandParty;
import com.alessiodp.parties.commands.list.CommandPassword;
import com.alessiodp.parties.commands.list.CommandPrefix;
import com.alessiodp.parties.commands.list.CommandRank;
import com.alessiodp.parties.commands.list.CommandReload;
import com.alessiodp.parties.commands.list.CommandRename;
import com.alessiodp.parties.commands.list.CommandSetHome;
import com.alessiodp.parties.commands.list.CommandSpy;
import com.alessiodp.parties.commands.list.CommandSuffix;
import com.alessiodp.parties.commands.list.CommandTeleport;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;

public class CommandManager {
	private Parties plugin;
	
	public CommandManager(Parties instance) {
		plugin = instance;
	}
	
	public void registerCommands() {
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_COMMANDS_REGISTER_PRE, true);
		CommandDispatcher handler = new CommandDispatcher(plugin);
		
		handler.register(ConfigMain.COMMANDS_CMD_P, new CommandP(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_PARTY, new CommandParty(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_HELP, new CommandHelp(plugin));
		
		handler.register(ConfigMain.COMMANDS_CMD_ACCEPT, new CommandAccept(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_CREATE, new CommandCreate(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_DELETE, new CommandDelete(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_DENY, new CommandDeny(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_IGNORE, new CommandIgnore(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_INFO, new CommandInfo(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_INVITE, new CommandInvite(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_KICK, new CommandKick(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_LEAVE, new CommandLeave(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_MIGRATE, new CommandMigrate(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_RANK, new CommandRank(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_RELOAD, new CommandReload(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_RENAME, new CommandRename(plugin));
		handler.register(ConfigMain.COMMANDS_CMD_SPY, new CommandSpy(plugin));
		
		// Chat
		if (ConfigParties.GENERAL_CHAT_TOGGLECHATCMD)
			handler.register(ConfigMain.COMMANDS_CMD_CHAT, new CommandChat(plugin));
		
		// Color
		if (ConfigParties.COLOR_ENABLE && ConfigParties.COLOR_COLORCMD)
			handler.register(ConfigMain.COMMANDS_CMD_COLOR, new CommandColor(plugin));
		
		// Confirm
		if (ConfigMain.ADDONS_VAULT_ENABLE && ConfigMain.ADDONS_VAULT_CONFIRM_ENABLE)
			handler.register(ConfigMain.COMMANDS_CMD_CONFIRM, new CommandConfirm(plugin));
		
		// Desc
		if (ConfigParties.DESC_ENABLE)
			handler.register(ConfigMain.COMMANDS_CMD_DESC, new CommandDesc(plugin));
		
		// GriefPrevention
		if (ConfigMain.ADDONS_GRIEFPREVENTION_ENABLE)
			handler.register(ConfigMain.COMMANDS_CMD_CLAIM, new CommandClaim(plugin));
		
		// Home
		if (ConfigParties.HOME_ENABLE) {
			handler.register(ConfigMain.COMMANDS_CMD_HOME, new CommandHome(plugin));
			handler.register(ConfigMain.COMMANDS_CMD_SETHOME, new CommandSetHome(plugin));
		}
		
		// List
		if (ConfigParties.LIST_ENABLE)
			handler.register(ConfigMain.COMMANDS_CMD_LIST, new CommandList(plugin));
		
		// Motd
		if (ConfigParties.MOTD_ENABLE)
			handler.register(ConfigMain.COMMANDS_CMD_MOTD, new CommandMotd(plugin));
		
		// Notify
		if (ConfigMain.ADDITIONAL_NOTIFY_ENABLE)
			handler.register(ConfigMain.COMMANDS_CMD_NOTIFY, new CommandNotify(plugin));
		
		// Password
		if (ConfigParties.PASSWORD_ENABLE) {
			handler.register(ConfigMain.COMMANDS_CMD_JOIN, new CommandJoin(plugin));
			handler.register(ConfigMain.COMMANDS_CMD_PASSWORD, new CommandPassword(plugin));
			((org.apache.logging.log4j.core.Logger)LogManager.getRootLogger()).addFilter(new PasswordFilter());
		}
		
		// Tag
		if (ConfigMain.ADDITIONAL_TAG_ENABLE && ConfigMain.ADDITIONAL_TAG_TYPE.isCustom()) {
			if (ConfigMain.ADDITIONAL_TAG_CUSTOM_PREFIX)
				handler.register(ConfigMain.COMMANDS_CMD_PREFIX, new CommandPrefix(plugin));
			if (ConfigMain.ADDITIONAL_TAG_CUSTOM_SUFFIX)
				handler.register(ConfigMain.COMMANDS_CMD_SUFFIX, new CommandSuffix(plugin));
		}
		
		// Teleport
		if (ConfigParties.TELEPORT_ENABLE)
			handler.register(ConfigMain.COMMANDS_CMD_TELEPORT, new CommandTeleport(plugin));
		
		
		// Setup
		try {
			PartiesCommand cmdParty = new PartiesCommand(ConfigMain.COMMANDS_CMD_PARTY);
			PartiesCommand cmdP = new PartiesCommand(ConfigMain.COMMANDS_CMD_P);
			cmdParty.setDescription(ConfigMain.COMMANDS_DESC_PARTY);
			cmdP.setDescription(ConfigMain.COMMANDS_DESC_P);
			
			final Field bukkitCommandMap = Bukkit.getServer().getClass()
					.getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit
					.getServer());
			
			commandMap.register(ConfigMain.COMMANDS_CMD_PARTY, cmdParty);
			commandMap.register(ConfigMain.COMMANDS_CMD_P, cmdP);
			
			cmdParty.setExecutor(handler);
			if (ConfigMain.COMMANDS_TABSUPPORT)
				cmdParty.setTabCompleter(new TabCompleterHandler(plugin));
			cmdP.setExecutor(handler);
			
			bukkitCommandMap.setAccessible(false);
			if (plugin.getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_PARTY) != null
					&& !plugin.getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_PARTY).getPlugin().getName().equals("Parties")) {
				// Register party command
				plugin.getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_PARTY).setExecutor(handler);
				if (ConfigMain.COMMANDS_TABSUPPORT)
					plugin.getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_PARTY).setTabCompleter(new TabCompleterHandler(plugin));
			}
			if (plugin.getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_P) != null
					&& !plugin.getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_P).getPlugin().getName().equals("Parties"))
				// Register p command
				plugin.getServer().getPluginCommand(ConfigMain.COMMANDS_CMD_P).setExecutor(handler);
			
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_COMMANDS_REGISTER_POST
					.replace("{value}", Integer.toString(handler.getCommandsNumber())), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
