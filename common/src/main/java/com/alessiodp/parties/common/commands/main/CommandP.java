package com.alessiodp.parties.common.commands.main;

import com.alessiodp.parties.api.events.common.player.IChatEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.commands.utils.PartiesCommandExecutor;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.ChatTask;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.PartiesUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandP extends PartiesCommandExecutor {
	private CommandSendMessage commandSendMessage;
	
	protected CommandP(PartiesPlugin instance) {
		super(instance);
		commandSendMessage = new CommandSendMessage(plugin);
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		if (sender.isPlayer()) {
			// Player
			PartiesUtils.executeCommand(plugin, sender, ConfigMain.COMMANDS_CMD_P, commandSendMessage, args);
		} else {
			// Console
			sender.sendMessage(Constants.ONLY_PLAYERS, false);
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		return new ArrayList<>();
	}
	
	private static class CommandSendMessage extends AbstractCommand {
		
		private CommandSendMessage(PartiesPlugin instance) {
			super(instance);
		}
		
		public boolean preRequisites(CommandData commandData) {
			User sender = commandData.getSender();
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
			
			/*
			 * Checks for command prerequisites
			 */
			if (!sender.hasPermission(PartiesPermission.SENDMESSAGE.toString())) {
				pp.sendNoPermission(PartiesPermission.SENDMESSAGE);
				return false;
			}
			
			PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
			if (party == null) {
				pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
			
			if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_SENDMESSAGE))
				return false;
			
			if (commandData.getArgs().length == 0) {
				pp.sendMessage(Messages.MAINCMD_P_WRONGCMD);
				return false;
			}
			
			commandData.setPartyPlayer(pp);
			commandData.setParty(party);
			commandData.addPermission(PartiesPermission.ADMIN_KICK_OTHERS);
			return true;
		}
		
		public void onCommand(CommandData commandData) {
			PartyPlayerImpl pp = commandData.getPartyPlayer();
			PartyImpl party = commandData.getParty();
			
			/*
			 * Command handling
			 */
			StringBuilder sb = new StringBuilder();
			for (String word : commandData.getArgs()) {
				if (sb.length() > 0) {
					sb.append(" ");
				}
				sb.append(word);
			}
			String message = sb.toString();
			
			if (PartiesUtils.checkCensor(ConfigParties.GENERAL_CHAT_CENSORREGEX, message, Constants.DEBUG_CMD_P_REGEXERROR)) {
				pp.sendMessage(Messages.MAINCMD_P_CENSORED);
				return;
			}
			
			if (ConfigParties.GENERAL_CHAT_CHATCD > 0
					&& !plugin.getRankManager().checkPlayerRank(pp, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
				Long unixTimestamp = plugin.getCooldownManager().getChatCooldown().get(pp.getPlayerUUID());
				long unixNow = System.currentTimeMillis() / 1000L;
				// Check cooldown
				if (unixTimestamp != null && (unixNow - unixTimestamp) < ConfigParties.GENERAL_CHAT_CHATCD) {
					pp.sendMessage(Messages.MAINCMD_P_COOLDOWN
							.replace("%seconds%", String.valueOf(ConfigParties.GENERAL_CHAT_CHATCD - (unixNow - unixTimestamp))));
					return;
				}
				
				plugin.getCooldownManager().getChatCooldown().put(pp.getPlayerUUID(), unixNow);
				plugin.getPartiesScheduler().scheduleTaskLater(
						new ChatTask(plugin, pp.getPlayerUUID()), ConfigParties.GENERAL_INVITE_TIMEOUT * 20L);
				
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_CMD_P_TASK
						.replace("{value}", Integer.toString(ConfigParties.GENERAL_CHAT_CHATCD * 20))
						.replace("{player}", pp.getName()), true);
			}
			/*
			 * Command starts
			 */
			
			// Calling API event
			IChatEvent partiesChatEvent = plugin.getEventManager().prepareChatEvent(pp, party, message);
			plugin.getEventManager().callEvent(partiesChatEvent);
			
			String newMessage = partiesChatEvent.getMessage();
			if (!partiesChatEvent.isCancelled()) {
				party.sendChatMessage(pp, newMessage);
				
				if (ConfigMain.STORAGE_LOG_CHAT)
					LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_P
							.replace("{party}", party.getName())
							.replace("{player}", pp.getName())
							.replace("{message}", newMessage), true);
			} else
				LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_CHATEVENT_DENY
						.replace("{player}", pp.getName())
						.replace("{message}", message), true);
		}
	}
}
