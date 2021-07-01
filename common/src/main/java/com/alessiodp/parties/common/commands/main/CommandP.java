package com.alessiodp.parties.common.commands.main;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Color;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.CensorUtils;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.HashMap;

public abstract class CommandP extends ADPMainCommand {
	private final CommandSendMessage commandSendMessage;
	
	public CommandP(PartiesPlugin plugin) {
		super(plugin, CommonCommands.P, ConfigMain.COMMANDS_MAIN_P_COMMAND, false);
		
		aliases = ConfigMain.COMMANDS_MAIN_P_ALIASES;
		subCommands = new HashMap<>();
		subCommandsByEnum = new HashMap<>();
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
			plugin.logConsole(Color.translateAndStripColor(Messages.PARTIES_COMMON_INVALIDCMD));
		}
		return true;
	}
	
	private static class CommandSendMessage extends PartiesSubCommand {
		private CommandSendMessage(ADPPlugin plugin, ADPMainCommand mainCommand) {
			super(
					plugin,
					mainCommand,
					CommonCommands.P,
					PartiesPermission.USER_SENDMESSAGE,
					ConfigMain.COMMANDS_MAIN_P_COMMAND,
					false
			);
			
			syntax = String.format("%s <%s>",
					mainCommand.getCommandName(),
					Messages.PARTIES_SYNTAX_MESSAGE
			);
			
			description = Messages.HELP_MAIN_DESCRIPTIONS_P;
			help = Messages.HELP_MAIN_COMMANDS_P;
		}
		
		@Override
		public boolean preRequisites(CommandData commandData) {
			User sender = commandData.getSender();
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
			
			if (commandData.getArgs().length == 0) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntax));
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
			
			if (ConfigParties.GENERAL_CHAT_PREVENT_MUTED_PLAYERS && partyPlayer.isChatMuted()) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_P_MUTED);
				return;
			}
			
			if (CensorUtils.checkCensor(ConfigParties.GENERAL_CHAT_CENSORREGEX, message, PartiesConstants.DEBUG_CMD_P_REGEXERROR)) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_P_CENSORED);
				return;
			}
			
			boolean mustStartCooldown = false;
			if (ConfigParties.GENERAL_CHAT_COOLDOWN > 0  && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_CHAT_BYPASS)) {
				mustStartCooldown = true;
				long remainingCooldown = ((PartiesPlugin) plugin).getCooldownManager().canChat(partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_CHAT_COOLDOWN);
				
				if (remainingCooldown > 0) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_P_COOLDOWN
							.replace("%seconds%", String.valueOf(remainingCooldown)));
					return;
				}
			}
			
			// Command starts
			
			partyPlayer.performPartyMessage(message);
			
			if (mustStartCooldown)
				((PartiesPlugin) plugin).getCooldownManager().startChatCooldown(partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_CHAT_COOLDOWN);
			
			if (ConfigMain.PARTIES_LOGGING_PARTY_CHAT)
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_P,
						partyPlayer.getName(), party.getName() != null ? party.getName() : "_", message), true);
		}
	}
}
