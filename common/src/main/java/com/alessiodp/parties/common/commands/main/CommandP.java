package com.alessiodp.parties.common.commands.main;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.player.IChatEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.ChatTask;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class CommandP extends ADPMainCommand {
	private final CommandSendMessage commandSendMessage;
	
	public CommandP(PartiesPlugin plugin) {
		super(plugin);
		
		commandName = ConfigMain.COMMANDS_CMD_P;
		subCommands = new HashMap<>();
		enabledSubCommands = new ArrayList<>();
		tabSupport = false;
		
		commandSendMessage = new CommandSendMessage(plugin, this);
	}
	
	@Override
	public boolean onCommand(User sender, String command, String[] args) {
		if (sender.isPlayer()) {
			// Player
			plugin.getCommandManager().getCommandUtils().executeCommand(sender, getCommandName(), commandSendMessage, args);
		} else {
			// Console
			plugin.logConsole(plugin.getColorUtils().removeColors(Messages.PARTIES_COMMON_INVALIDCMD), false);
		}
		return true;
	}
	
	private static class CommandSendMessage extends PartiesSubCommand {
		@Getter private final boolean executableByConsole = false;
		
		private CommandSendMessage(ADPPlugin plugin, ADPMainCommand mainCommand) {
			super(plugin, mainCommand);
		}
		
		@Override
		public boolean preRequisites(CommandData commandData) {
			User sender = commandData.getSender();
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(PartiesPermission.SENDMESSAGE.toString())) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.SENDMESSAGE);
				return false;
			}
			
			PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
			
			if (commandData.getArgs().length == 0) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_P_WRONGCMD);
				return false;
			}
			
			if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_SENDMESSAGE))
				return false;
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			((PartiesCommandData) commandData).setParty(party);
			return true;
		}
		
		@Override
		public void onCommand(CommandData commandData) {
			User sender = commandData.getSender();
			PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
			PartyImpl party = ((PartiesCommandData) commandData).getParty();
			
			// Command handling
			String message = plugin.getCommandManager().getCommandUtils().handleCommandString(commandData.getArgs(), 0);
			
			if (((PartiesPlugin) plugin).getCensorUtils().checkCensor(ConfigParties.GENERAL_CHAT_CENSORREGEX, message, PartiesConstants.DEBUG_CMD_P_REGEXERROR)) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_P_CENSORED);
				return;
			}
			
			if (ConfigParties.GENERAL_CHAT_CHATCD > 0
					&& !((PartiesPlugin) plugin).getRankManager().checkPlayerRank(partyPlayer, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
				Long unixTimestamp = ((PartiesPlugin) plugin).getCooldownManager().getChatCooldown().get(partyPlayer.getPlayerUUID());
				long unixNow = System.currentTimeMillis() / 1000L;
				// Check cooldown
				if (unixTimestamp != null && (unixNow - unixTimestamp) < ConfigParties.GENERAL_CHAT_CHATCD) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_P_COOLDOWN
							.replace("%seconds%", String.valueOf(ConfigParties.GENERAL_CHAT_CHATCD - (unixNow - unixTimestamp))));
					return;
				}
				
				((PartiesPlugin) plugin).getCooldownManager().getChatCooldown().put(partyPlayer.getPlayerUUID(), unixNow);
				plugin.getScheduler().scheduleAsyncLater(new ChatTask((PartiesPlugin) plugin, partyPlayer.getPlayerUUID()), ConfigParties.GENERAL_INVITE_TIMEOUT, TimeUnit.SECONDS);
				
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_P_TASK
						.replace("{value}", Integer.toString(ConfigParties.GENERAL_CHAT_CHATCD))
						.replace("{player}", partyPlayer.getName()), true);
			}
			
			// Command starts
			// Calling API event
			IChatEvent partiesChatEvent = ((PartiesPlugin) plugin).getEventManager().prepareChatEvent(partyPlayer, party, message);
			((PartiesPlugin) plugin).getEventManager().callEvent(partiesChatEvent);
			
			String newMessage = partiesChatEvent.getMessage();
			if (!partiesChatEvent.isCancelled()) {
				partyPlayer.performPartyMessage(newMessage);
				
				if (ConfigParties.GENERAL_CHAT_LOG)
					plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_P
							.replace("{party}", party.getName())
							.replace("{player}", partyPlayer.getName())
							.replace("{message}", newMessage), ConfigParties.GENERAL_CHAT_LOGTOCONSOLE);
			} else
				plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_CHATEVENT_DENY
						.replace("{player}", partyPlayer.getName())
						.replace("{message}", message), true);
		}
	}
}
