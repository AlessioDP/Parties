package com.alessiodp.parties.common.commands;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.executors.CommandAccept;
import com.alessiodp.parties.common.commands.executors.CommandChat;
import com.alessiodp.parties.common.commands.executors.CommandColor;
import com.alessiodp.parties.common.commands.executors.CommandCreate;
import com.alessiodp.parties.common.commands.executors.CommandDelete;
import com.alessiodp.parties.common.commands.executors.CommandDeny;
import com.alessiodp.parties.common.commands.executors.CommandDesc;
import com.alessiodp.parties.common.commands.executors.CommandHelp;
import com.alessiodp.parties.common.commands.executors.CommandIgnore;
import com.alessiodp.parties.common.commands.executors.CommandInfo;
import com.alessiodp.parties.common.commands.executors.CommandInvite;
import com.alessiodp.parties.common.commands.executors.CommandJoin;
import com.alessiodp.parties.common.commands.executors.CommandKick;
import com.alessiodp.parties.common.commands.executors.CommandLeave;
import com.alessiodp.parties.common.commands.executors.CommandList;
import com.alessiodp.parties.common.commands.executors.CommandMigrate;
import com.alessiodp.parties.common.commands.executors.CommandMotd;
import com.alessiodp.parties.common.commands.executors.CommandNotify;
import com.alessiodp.parties.common.commands.executors.CommandP;
import com.alessiodp.parties.common.commands.executors.CommandParty;
import com.alessiodp.parties.common.commands.executors.CommandPassword;
import com.alessiodp.parties.common.commands.executors.CommandRank;
import com.alessiodp.parties.common.commands.executors.CommandReload;
import com.alessiodp.parties.common.commands.executors.CommandRename;
import com.alessiodp.parties.common.commands.executors.CommandSpy;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import lombok.Getter;

import java.util.LinkedList;

public abstract class CommandManager {
	protected PartiesPlugin plugin;
	protected CommandDispatcher dispatcher;
	@Getter protected LinkedList<PartiesCommand> orderedCommands;
	
	protected CommandManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public final void setup() {
		prepareCommands();
		registerCommands();
		setupCommands();
		orderCommands();
	}
	
	protected void prepareCommands() {
		CommonCommands.setup();
	}
	
	protected void registerCommands() {
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_COMMANDS_REGISTER_PRE, true);
		dispatcher.register(CommonCommands.P, new CommandP(plugin));
		dispatcher.register(CommonCommands.PARTY, new CommandParty(plugin));
		dispatcher.register(CommonCommands.HELP, new CommandHelp(plugin));
		
		dispatcher.register(CommonCommands.ACCEPT, new CommandAccept(plugin));
		dispatcher.register(CommonCommands.CHAT, new CommandChat(plugin));
		dispatcher.register(CommonCommands.CREATE, new CommandCreate(plugin));
		dispatcher.register(CommonCommands.DELETE, new CommandDelete(plugin));
		dispatcher.register(CommonCommands.DENY, new CommandDeny(plugin));
		dispatcher.register(CommonCommands.IGNORE, new CommandIgnore(plugin));
		dispatcher.register(CommonCommands.INFO, new CommandInfo(plugin));
		dispatcher.register(CommonCommands.INVITE, new CommandInvite(plugin));
		dispatcher.register(CommonCommands.KICK, new CommandKick(plugin));
		dispatcher.register(CommonCommands.LEAVE, new CommandLeave(plugin));
		dispatcher.register(CommonCommands.MIGRATE, new CommandMigrate(plugin));
		dispatcher.register(CommonCommands.RANK, new CommandRank(plugin));
		dispatcher.register(CommonCommands.RELOAD, new CommandReload(plugin));
		dispatcher.register(CommonCommands.RENAME, new CommandRename(plugin));
		dispatcher.register(CommonCommands.SPY, new CommandSpy(plugin));
		
		// Color
		if (ConfigParties.COLOR_ENABLE)
			dispatcher.register(CommonCommands.COLOR, new CommandColor(plugin));
		
		// Desc
		if (ConfigParties.DESC_ENABLE)
			dispatcher.register(CommonCommands.DESC, new CommandDesc(plugin));
		
		// Join & Password
		if (ConfigParties.PASSWORD_ENABLE) {
			dispatcher.register(CommonCommands.JOIN, new CommandJoin(plugin));
			dispatcher.register(CommonCommands.PASSWORD, new CommandPassword(plugin));
		}
		
		// List
		if (ConfigParties.LIST_ENABLE)
			dispatcher.register(CommonCommands.LIST, new CommandList(plugin));
		
		// Motd
		if (ConfigParties.MOTD_ENABLE)
			dispatcher.register(CommonCommands.MOTD, new CommandMotd(plugin));
		
		// Notify
		if (ConfigMain.ADDITIONAL_NOTIFY_ENABLE)
			dispatcher.register(CommonCommands.NOTIFY, new CommandNotify(plugin));
	}
	
	protected abstract void setupCommands();
	
	private void orderCommands() {
		orderedCommands = new LinkedList<>();
		for (String command : ConfigMain.COMMANDS_ORDER) {
			for (PartiesCommand pc : dispatcher.getEnabledCommands()) {
				if (pc.getType().equalsIgnoreCase(command)) {
					orderedCommands.add(pc);
				}
			}
		}
	}
}
