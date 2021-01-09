package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.CensorUtils;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesPermission;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandNickname extends PartiesSubCommand {
	
	public CommandNickname(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.NICKNAME,
				PartiesPermission.USER_NICKNAME,
				ConfigMain.COMMANDS_CMD_NICKNAME,
				true
		);
		
		syntax = String.format("%s <%s> [%s/%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PLAYER,
				Messages.PARTIES_SYNTAX_NICKNAME,
				ConfigMain.COMMANDS_SUB_REMOVE
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_NICKNAME;
		help = Messages.HELP_ADDITIONAL_COMMANDS_NICKNAME;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = null;
		if (sender.isPlayer()) {
			partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			if (!sender.hasPermission(PartiesPermission.ADMIN_NICKNAME_OTHERS)) {
				if (!partyPlayer.isInParty()) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return false;
				}
				
				if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRank(partyPlayer, PartiesPermission.PRIVATE_EDIT_NICKNAME_OWN)
						&& !((PartiesPlugin) plugin).getRankManager().checkPlayerRank(partyPlayer, PartiesPermission.PRIVATE_EDIT_NICKNAME_OTHERS)) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_PERM_NORANK_GENERAL);
					return false;
				}
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(PartiesPermission.ADMIN_NICKNAME_OTHERS);
		}
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 4) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl senderPp = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		String playerName = commandData.getArgs()[1];
		UUID playerUUID = null;
		
		Set<UUID> matchingPlayers;
		if (LLAPIHandler.isEnabled()) {
			// Use LastLoginAPI to get a list of players with the same name
			matchingPlayers = LLAPIHandler.getPlayerByName(playerName);
		} else {
			// Get only the online player with the same name
			matchingPlayers = new HashSet<>();
			User user = plugin.getPlayerByName(playerName);
			if (user != null)
				matchingPlayers.add(user.getUUID());
		}
		List<UUID> listPlayers = new LinkedList<>(matchingPlayers);
		
		if (listPlayers.size() == 0) {
			sendMessage(sender, senderPp, Messages.PARTIES_COMMON_PLAYER_NOT_FOUND
					.replace("%player%", playerName));
			return;
		}
		
		listPlayers.removeIf((uuid) -> !((PartiesPlugin) plugin).getPlayerManager().getPlayer(uuid).isInParty());
		playerUUID = listPlayers.stream().findAny().orElse(null);
		
		if (playerUUID == null) {
			sendMessage(sender, senderPp, Messages.PARTIES_COMMON_PLAYER_NOT_IN_PARTY
					.replace("%player%", playerName));
			return;
		}
		
		OfflineUser targetPlayer = plugin.getOfflinePlayer(playerUUID);
		PartyPlayerImpl targetPp = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(playerUUID);
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(targetPp.getPartyId());
		boolean isOwn = senderPp != null && senderPp.getPlayerUUID().equals(targetPp.getPlayerUUID());
		
		if (party == null) {
			sendMessage(sender, senderPp, Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (senderPp != null) {
			if (isOwn) {
				if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRank(senderPp, PartiesPermission.PRIVATE_EDIT_NICKNAME_OWN)) {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_OWN_NO_PERMISSION);
					return;
				}
			} else {
				if (!targetPp.getPartyId().equals(senderPp.getPartyId()) && !commandData.havePermission(PartiesPermission.ADMIN_NICKNAME_OTHERS)) {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_OTHERS_NO_PERMISSION);
					return;
				}
				
				if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRank(senderPp, PartiesPermission.PRIVATE_EDIT_NICKNAME_OTHERS)) {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_OTHERS_NO_PERMISSION);
					return;
				}
			}
		}
		
		if (commandData.getArgs().length == 2) {
			// Show
			
			if (targetPp.getNickname() != null) {
				sendMessage(sender, senderPp, isOwn ? Messages.ADDCMD_NICKNAME_SHOW_OWN : Messages.ADDCMD_NICKNAME_SHOW_OTHER, targetPp);
			} else {
				sendMessage(sender, senderPp, isOwn ? Messages.ADDCMD_NICKNAME_SHOW_OWN_NONE : Messages.ADDCMD_NICKNAME_SHOW_OTHER_NONE, targetPp);
			}
		} else if (commandData.getArgs().length >= 3) {
			// Set/remove
			boolean isRemove = false;
			String nickname = "";
			if (commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
				// Remove command
				isRemove = true;
			} else {
				nickname = plugin.getCommandManager().getCommandUtils().handleCommandString(commandData.getArgs(), 2);
				
				if (!CensorUtils.checkAllowedCharacters(ConfigParties.ADDITIONAL_NICKNAME_ALLOWEDCHARS, nickname, PartiesConstants.DEBUG_CMD_NICKNAME_REGEXERROR_ALLOWEDCHARS)
						|| (nickname.length() > ConfigParties.ADDITIONAL_NICKNAME_MAXLENGTH)
						|| (nickname.length() < ConfigParties.ADDITIONAL_NICKNAME_MINLENGTH)) {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_INVALID);
					return;
				}
				if (CensorUtils.checkCensor(ConfigParties.ADDITIONAL_NICKNAME_CENSORREGEX, nickname, PartiesConstants.DEBUG_CMD_NICKNAME_REGEXERROR_CENSORED)) {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_CENSORED);
					return;
				}
				
				if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.NICKNAME, senderPp, commandData.getCommandLabel(), commandData.getArgs()))
					return;
			}
			
			// Command starts
			if (isRemove) {
				targetPp.setNickname(null);
				
				if (isOwn) {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_OWN_REMOVED, targetPp);
				} else {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_OTHERS_REMOVED, targetPp);
					
					if (senderPp != null && targetPlayer.isOnline())
						targetPp.sendMessage(Messages.ADDCMD_NICKNAME_OTHERS_TARGET_REMOVED, senderPp);
				}
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_NICKNAME,
						sender.getName(), targetPp.getName()), true);
			} else {
				targetPp.setNickname(nickname);
				
				if (isOwn) {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_OWN_CHANGED, targetPp);
				} else {
					sendMessage(sender, senderPp, Messages.ADDCMD_NICKNAME_OTHERS_CHANGED, targetPp);
					
					if (senderPp != null && targetPlayer.isOnline())
						targetPp.sendMessage(Messages.ADDCMD_NICKNAME_OTHERS_TARGET_CHANGED
								.replace("%nickname%", nickname), senderPp);
				}
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_NICKNAME_REM,
						sender.getName(), targetPp.getName()), true);
			}
			
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
		} else if (args.length == 3)
			ret.add(ConfigMain.COMMANDS_SUB_REMOVE);
		return ret;
	}
}