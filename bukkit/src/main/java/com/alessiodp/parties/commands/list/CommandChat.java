package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandChat implements ICommand {
	private Parties plugin;
	 
	public CommandChat(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.CHAT.toString())) {
			pp.sendNoPermission(PartiesPermission.CHAT);
			return;
		}
		if (pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		/*
		 * Command handling
		 */
		Boolean chat = pp.isChatParty();
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_ON))
				chat = true;
			else if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_OFF))
				chat = false;
			else
				chat = null;
		} else {
			chat = !chat;
		}
		if (chat == null) {
			pp.sendMessage(Messages.MAINCMD_CHAT_WRONGCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		if (chat)
			pp.sendMessage(Messages.MAINCMD_CHAT_ENABLED);
		else
			pp.sendMessage(Messages.MAINCMD_CHAT_DISABLED);
		
		pp.setChatParty(chat);
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_CHAT
				.replace("{player}", p.getName())
				.replace("{value}", chat.toString()), true);
	}
}
