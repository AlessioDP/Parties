package com.alessiodp.parties.common.commands.main;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.PartiesCommandExecutor;
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
import com.alessiodp.parties.common.commands.sub.CommandMigrate;
import com.alessiodp.parties.common.commands.sub.CommandMotd;
import com.alessiodp.parties.common.commands.sub.CommandMute;
import com.alessiodp.parties.common.commands.sub.CommandPassword;
import com.alessiodp.parties.common.commands.sub.CommandRank;
import com.alessiodp.parties.common.commands.sub.CommandReload;
import com.alessiodp.parties.common.commands.sub.CommandRename;
import com.alessiodp.parties.common.commands.sub.CommandSpy;
import com.alessiodp.parties.common.commands.sub.CommandVersion;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.list.PartiesCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.PartiesUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CommandParty extends PartiesCommandExecutor {
	private HashMap<String, AbstractCommand> subCommands;
	@Getter private List<PartiesCommand> enabledSubCommands;
	
	protected CommandParty(PartiesPlugin instance) {
		super(instance);
		reload();
	}
	
	public void reload() {
		subCommands = new HashMap<>();
		enabledSubCommands = new ArrayList<>();
		
		register(CommonCommands.HELP, new CommandHelp(plugin));
		register(CommonCommands.ACCEPT, new CommandAccept(plugin));
		register(CommonCommands.CHAT, new CommandChat(plugin));
		register(CommonCommands.CREATE, new CommandCreate(plugin));
		register(CommonCommands.DELETE, new CommandDelete(plugin));
		register(CommonCommands.DENY, new CommandDeny(plugin));
		register(CommonCommands.IGNORE, new CommandIgnore(plugin));
		register(CommonCommands.INFO, new CommandInfo(plugin));
		register(CommonCommands.INVITE, new CommandInvite(plugin));
		register(CommonCommands.KICK, new CommandKick(plugin));
		register(CommonCommands.LEAVE, new CommandLeave(plugin));
		register(CommonCommands.MIGRATE, new CommandMigrate(plugin));
		register(CommonCommands.RANK, new CommandRank(plugin));
		register(CommonCommands.RELOAD, new CommandReload(plugin));
		register(CommonCommands.RENAME, new CommandRename(plugin));
		register(CommonCommands.SPY, new CommandSpy(plugin));
		register(CommonCommands.VERSION, new CommandVersion(plugin));
		
		// Color
		if (ConfigParties.COLOR_ENABLE)
			register(CommonCommands.COLOR, new CommandColor(plugin));
		
		// Desc
		if (ConfigParties.DESC_ENABLE)
			register(CommonCommands.DESC, new CommandDesc(plugin));
		
		// Follow
		if (ConfigMain.ADDITIONAL_FOLLOW_ENABLE && ConfigMain.ADDITIONAL_FOLLOW_TOGGLECMD)
			register(CommonCommands.FOLLOW, new CommandFollow(plugin));
		
		
		// Join & Password
		if (ConfigParties.PASSWORD_ENABLE) {
			register(CommonCommands.JOIN, new CommandJoin(plugin));
			register(CommonCommands.PASSWORD, new CommandPassword(plugin));
		}
		
		// List
		if (ConfigParties.LIST_ENABLE)
			register(CommonCommands.LIST, new CommandList(plugin));
		
		// Motd
		if (ConfigParties.MOTD_ENABLE)
			register(CommonCommands.MOTD, new CommandMotd(plugin));
		
		// Mute
		if (ConfigMain.ADDITIONAL_MUTE_ENABLE)
			register(CommonCommands.MUTE, new CommandMute(plugin));
	}
	
	protected void register(PartiesCommand command, AbstractCommand commandExecutor) {
		subCommands.put(command.getCommand().toLowerCase(), commandExecutor);
		enabledSubCommands.add(command);
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		if (sender.isPlayer()) {
			if (args.length == 0) {
				// Set /party to /party help
				args = new String[]{ConfigMain.COMMANDS_CMD_HELP};
			}
			
			if (exists(args[0])) {
				PartiesUtils.executeCommand(plugin, sender, ConfigMain.COMMANDS_CMD_PARTY, getExecutor(args[0].toLowerCase()), args);
			} else {
				sender.sendMessage(Messages.PARTIES_COMMON_INVALIDCMD, true);
			}
		} else {
			// Console
			if (args.length > 0
					&& (args[0].equalsIgnoreCase(ConfigMain.COMMANDS_CMD_RELOAD)
					|| args[0].equalsIgnoreCase(ConfigMain.COMMANDS_CMD_MIGRATE))) {
				PartiesUtils.executeCommand(plugin, sender, ConfigMain.COMMANDS_CMD_PARTY, getExecutor(args[0].toLowerCase()), args);
			} else {
				// Print help
				for (String str : Messages.HELP_CONSOLEHELP) {
					plugin.log(str);
				}
			}
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(sender.getUUID());
		if (player != null) {
			List<String> commands = new ArrayList<>();
			for (PartiesCommand pc : player.getAllowedCommands()) {
				if (enabledSubCommands.contains(pc))
					commands.add(pc.getCommand().toLowerCase());
			}
			
			if (args.length > 1) {
				if (commands.contains(args[0])) {
					ret = getExecutor(args[0]).onTabComplete(sender, args);
				}
			} else {
				ret = PartiesUtils.tabCompleteParser(commands, args[0]);
			}
		}
		return ret;
	}
	
	private boolean exists(String name) {
		return subCommands.containsKey(name.toLowerCase());
	}
	
	private AbstractCommand getExecutor(String name) {
		return subCommands.get(name);
	}
}
