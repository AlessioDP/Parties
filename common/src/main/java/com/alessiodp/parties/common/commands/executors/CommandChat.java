package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

public class CommandChat extends AbstractCommand {
	
	public CommandChat(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.CHAT.toString())) {
			pp.sendNoPermission(PartiesPermission.CHAT);
			return false;
		}
		if (pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
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
		pp.setChatParty(chat);
		
		if (chat)
			pp.sendMessage(Messages.MAINCMD_CHAT_ENABLED);
		else
			pp.sendMessage(Messages.MAINCMD_CHAT_DISABLED);
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_CHAT
				.replace("{player}", pp.getName())
				.replace("{value}", chat.toString()), true);
	}
}
