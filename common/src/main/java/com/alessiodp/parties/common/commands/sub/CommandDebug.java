package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;

import java.util.Set;
import java.util.UUID;

public abstract class CommandDebug extends PartiesSubCommand {
	private final String syntaxConfig;
	private final String syntaxExp;
	private final String syntaxParty;
	private final String syntaxPlayer;
	
	public CommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.DEBUG,
				PartiesPermission.ADMIN_DEBUG,
				ConfigMain.COMMANDS_CMD_DEBUG,
				false
		);
		
		syntax = String.format("%s <%s/%s/%s/%s> ...",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_CONFIG,
				ConfigMain.COMMANDS_SUB_EXP,
				ConfigMain.COMMANDS_SUB_PARTY,
				ConfigMain.COMMANDS_SUB_PLAYER
		);
		syntaxConfig = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_CONFIG
		);
		syntaxExp = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_EXP
		);
		syntaxParty = String.format("%s %s <%s>",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_PARTY,
				Messages.PARTIES_SYNTAX_PARTY
		);
		syntaxPlayer = String.format("%s %s [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_SUB_PLAYER,
				Messages.PARTIES_SYNTAX_PLAYER
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_DEBUG;
		help = Messages.HELP_ADDITIONAL_COMMANDS_DEBUG;
	}

	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		if (!sender.hasPermission(permission)) {
			sendNoPermissionMessage(partyPlayer, permission);
			return false;
		}
		
		if (commandData.getArgs().length < 2) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		String playerName;
		PartyImpl targetParty = null;
		PartyPlayerImpl targetPlayer = null;
		CommandType commandType;
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_CONFIG)) {
			commandType = CommandType.CONFIG;
			if (commandData.getArgs().length != 2) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntaxConfig));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_EXP)) {
			commandType = CommandType.EXP;
			if (commandData.getArgs().length != 2) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntaxExp));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_PARTY)) {
			commandType = CommandType.PARTY;
			if (commandData.getArgs().length == 3) {
				targetParty = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[2]);
				
				if (targetParty == null) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
							.replace("%party%", commandData.getArgs()[2]));
					return;
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntaxParty));
				return;
			}
		} else if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_PLAYER)) {
			commandType = CommandType.PLAYER;
			if (commandData.getArgs().length == 2) {
				targetPlayer = partyPlayer;
			} else if (commandData.getArgs().length == 3) {
				playerName = commandData.getArgs()[2];
				
				User targetUser = plugin.getPlayerByName(playerName);
				if (targetUser != null) {
					targetPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(targetUser.getUUID());
				} else {
					Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(playerName);
					if (targetPlayersUuid.size() > 0) {
						targetPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(targetPlayersUuid.iterator().next());
					} else {
						// Not found
						sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PLAYER_PLAYER_OFFLINE
								.replace("%player%", playerName));
						return;
					}
				}
			} else {
				sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", syntaxPlayer));
				return;
			}
		} else {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		
		// Command starts
		if (commandType == CommandType.CONFIG) {
			// Config
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_CONFIG_HEADER);
			
			StringBuilder ranks = new StringBuilder();
			for (PartyRankImpl rank : ConfigParties.RANK_LIST) {
				if (ranks.length() > 0)
					ranks.append(Messages.ADDCMD_DEBUG_CONFIG_RANK_SEPARATOR);
				ranks.append(rank.parseWithPlaceholders((PartiesPlugin) plugin, Messages.ADDCMD_DEBUG_CONFIG_RANK_FORMAT));
			}
			
			for (String line : Messages.ADDCMD_DEBUG_CONFIG_TEXT) {
				sendMessage(sender, partyPlayer, line
						.replace("%outdated_config%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getConfigMain().isOutdated()))
						.replace("%outdated_parties%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getConfigParties().isOutdated()))
						.replace("%outdated_messages%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getMessages().isOutdated()))
						.replace("%storage%", plugin.getDatabaseManager().getDatabaseType().toString())
						.replace("%ranks%", ranks.toString())
				);
			}
		} else if (commandType == CommandType.EXP) {
			// Config
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_EXP_HEADER);
			
			for (String line : Messages.ADDCMD_DEBUG_EXP_TEXT) {
				sendMessage(sender, partyPlayer, parseDebugExp(line));
			}
		} else if (commandType == CommandType.PARTY) {
			// Party
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PARTY_HEADER);
			
			for (String line : Messages.ADDCMD_DEBUG_PARTY_TEXT) {
				sendMessage(sender, partyPlayer, line
						.replace("%id%", targetParty.getId().toString())
						.replace("%name%", ((PartiesPlugin) plugin).getMessageUtils().formatText(targetParty.getName()))
						.replace("%tag%", ((PartiesPlugin) plugin).getMessageUtils().formatText(targetParty.getTag()))
						.replace("%leader%", targetParty.getLeader() != null ? targetParty.getLeader().toString() : Messages.PARTIES_OPTIONS_NONE)
						.replace("%members%", Integer.toString(targetParty.getMembers().size()))
						.replace("%members_online%", Integer.toString(targetParty.getOnlineMembers(true).size()))
						.replace("%description%", ((PartiesPlugin) plugin).getMessageUtils().formatText(targetParty.getDescription()))
						.replace("%motd_size%", Integer.toString(targetParty.getMotd() != null ? targetParty.getMotd().length() : 0))
						.replace("%homes%", Integer.toString(targetParty.getHomes().size()))
						.replace("%kills%", Integer.toString(targetParty.getKills()))
						.replace("%password%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(targetParty.getPassword() != null))
						.replace("%protection%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(targetParty.getProtection()))
						.replace("%follow%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(targetParty.isFollowEnabled()))
						.replace("%color%", (targetParty.getColor() != null ? targetParty.getColor().getName() : Messages.PARTIES_OPTIONS_NONE))
						.replace("%color_active%", (targetParty.getCurrentColor() != null ? targetParty.getCurrentColor().getName() : Messages.PARTIES_OPTIONS_NONE))
						.replace("%color_dynamic%", (targetParty.getDynamicColor() != null ? targetParty.getDynamicColor().getName() : Messages.PARTIES_OPTIONS_NONE))
						.replace("%experience%", Integer.toString((int) targetParty.getExperience()))
				);
			}
		} else {
			// Player
			sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PLAYER_HEADER);
			
			for (String line : Messages.ADDCMD_DEBUG_PLAYER_TEXT) {
				sendMessage(sender, partyPlayer, line
						.replace("%uuid%", targetPlayer.getPlayerUUID().toString())
						.replace("%name%", targetPlayer.getName())
						.replace("%rank%", Integer.toString(targetPlayer.getRank()))
						.replace("%party%", targetPlayer.getPartyId() != null ? targetPlayer.getPartyId().toString() : Messages.PARTIES_OPTIONS_NONE)
						.replace("%chat%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(targetPlayer.isChatParty()))
						.replace("%spy%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(targetPlayer.isSpy()))
						.replace("%muted%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(targetPlayer.isMuted()))
				);
			}
		}
	}
	
	protected abstract String parseDebugExp(String line);
	
	private enum CommandType {
		CONFIG, EXP, PARTY, PLAYER
	}
}
