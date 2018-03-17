package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandChat implements ICommand {
	private Parties plugin;
	 
	public CommandChat(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.CHAT.toString())) {
			pp.sendNoPermission(PartiesPermission.CHAT);
			return false;
		}
		if (pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		Boolean chat = pp.isChatParty();
		if (commandData.getArgs().length > 1) {
			if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_ON))
				chat = true;
			else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_OFF))
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
				.replace("{player}", pp.getName())
				.replace("{value}", chat.toString()), true);
	}
}
