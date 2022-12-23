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
import com.alessiodp.parties.common.parties.ExpManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandDebug extends PartiesSubCommand {
	private final String syntaxConfig;
	private final String syntaxExp;
	private final String syntaxParty;
	private final String syntaxPlayer;
	
	protected String extraBungeecord;
	
	public CommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.DEBUG,
				PartiesPermission.ADMIN_DEBUG,
				ConfigMain.COMMANDS_SUB_DEBUG,
				true
		);
		initializeExtraCommands();
		
		syntax = String.format("%s <%s%s/%s/%s/%s> ...",
				baseSyntax(),
				extraBungeecord != null ? extraBungeecord + "/" : "",
				ConfigMain.COMMANDS_MISC_CONFIG,
				ConfigMain.COMMANDS_MISC_EXP,
				ConfigMain.COMMANDS_MISC_PARTY,
				ConfigMain.COMMANDS_MISC_PLAYER
		);
		syntaxConfig = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_CONFIG
		);
		syntaxExp = String.format("%s %s",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_EXP
		);
		syntaxParty = String.format("%s %s <%s>",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_PARTY,
				Messages.PARTIES_SYNTAX_PARTY
		);
		syntaxPlayer = String.format("%s %s [%s]",
				baseSyntax(),
				ConfigMain.COMMANDS_MISC_PLAYER,
				Messages.PARTIES_SYNTAX_PLAYER
		);
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_DEBUG;
		help = Messages.HELP_ADDITIONAL_COMMANDS_DEBUG;
	}
	
	protected void initializeExtraCommands() {
		// Nothing to do
	}
	
	@Override
	public boolean preRequisites(@NotNull CommandData commandData) {
		return handlePreRequisitesFull(commandData, null, 2, Integer.MAX_VALUE);
	}
	
	@Override
	public void onCommand(@NotNull CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		PartyImpl targetParty = null;
		PartyPlayerImpl targetPlayer = null;
		CommandType commandType = CommandType.parse(commandData.getArgs()[1]);
		
		if (commandType == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return;
		}
		
		switch (commandType) {
			case CONFIG:
				if (commandData.getArgs().length != 2) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", syntaxConfig));
					return;
				}
				break;
			case EXP:
				if (commandData.getArgs().length != 2) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", syntaxExp));
					return;
				}
				break;
			case PARTY:
				if (commandData.getArgs().length == 3) {
					targetParty = getPlugin().getPartyManager().getParty(commandData.getArgs()[2]);
					
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
				break;
			case PLAYER:
				if (commandData.getArgs().length == 2) {
					if (partyPlayer != null)
						targetPlayer = partyPlayer;
					else {
						sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
								.replace("%syntax%", syntaxPlayer));
						return;
					}
				} else if (commandData.getArgs().length == 3) {
					String playerName = commandData.getArgs()[2];
					
					User targetUser = plugin.getPlayerByName(playerName);
					if (targetUser != null) {
						targetPlayer = getPlugin().getPlayerManager().getPlayer(targetUser.getUUID());
					} else {
						Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(playerName);
						if (targetPlayersUuid.size() > 0) {
							targetPlayer = getPlugin().getPlayerManager().getPlayer(targetPlayersUuid.iterator().next());
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
				break;
			default:
				if (commandHandleExtra(commandType, commandData, sender, partyPlayer)) {
					// Command handle extra stopped, return
					return;
				}
		}
		
		// Command starts
		commandStart(commandType, sender, partyPlayer, targetParty, targetPlayer);
	}
	
	@Override
	public List<String> onTabComplete(@NotNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission)) {
			if (args.length == 2) {
				if (extraBungeecord != null)
					ret.add(extraBungeecord);
				ret.add(ConfigMain.COMMANDS_MISC_CONFIG);
				ret.add(ConfigMain.COMMANDS_MISC_EXP);
				ret.add(ConfigMain.COMMANDS_MISC_PARTY);
				ret.add(ConfigMain.COMMANDS_MISC_PLAYER);
			} else if(args.length == 3) {
				if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PARTY)) {
					getPlugin().getPartyManager().getCacheParties().values().stream()
							.filter(p -> p.getName() != null && !p.getName().isEmpty())
							.forEach(p -> ret.add(p.getName()));
				} else if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PLAYER)) {
					return plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 2);
				}
			}
		}
		return plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[args.length - 1]);
	}
	
	protected boolean commandHandleExtra(CommandType commandType, CommandData commandData, User sender, PartyPlayerImpl partyPlayer) {
		return false;
	}
	
	protected void commandStart(CommandType commandType, User sender, PartyPlayerImpl partyPlayer, PartyImpl targetParty, PartyPlayerImpl targetPlayer) {
		switch (commandType) {
			case CONFIG:
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_CONFIG_HEADER);
				
				StringBuilder ranks = new StringBuilder();
				for (PartyRankImpl rank : ConfigParties.RANK_LIST) {
					if (ranks.length() > 0)
						ranks.append(Messages.ADDCMD_DEBUG_CONFIG_RANK_SEPARATOR);
					ranks.append(rank.parseWithPlaceholders((PartiesPlugin) plugin, Messages.ADDCMD_DEBUG_CONFIG_RANK_FORMAT));
				}
				
				for (String line : Messages.ADDCMD_DEBUG_CONFIG_TEXT) {
					sendMessage(sender, partyPlayer, line
							.replace("%outdated_config%", getPlugin().getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getConfigMain().isOutdated()))
							.replace("%outdated_parties%", getPlugin().getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getConfigParties().isOutdated()))
							.replace("%outdated_messages%", getPlugin().getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getMessages().isOutdated()))
							.replace("%storage%", plugin.getDatabaseManager().getDatabaseType().toString())
							.replace("%ranks%", ranks.toString())
					);
				}
				break;
			case EXP:
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_EXP_HEADER);
				
				for (String line : Messages.ADDCMD_DEBUG_EXP_TEXT) {
					sendMessage(sender, partyPlayer, parseDebugExp(line));
				}
				break;
			case PARTY:
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PARTY_HEADER);
				
				for (String line : Messages.ADDCMD_DEBUG_PARTY_TEXT) {
					sendMessage(sender, partyPlayer, line
							.replace("%id%", targetParty.getId().toString())
							.replace("%name%", getPlugin().getMessageUtils().formatText(targetParty.getName()))
							.replace("%tag%", getPlugin().getMessageUtils().formatText(targetParty.getTag()))
							.replace("%leader%", targetParty.getLeader() != null ? targetParty.getLeader().toString() : Messages.PARTIES_OPTIONS_NONE)
							.replace("%members%", Integer.toString(targetParty.getMembers().size()))
							.replace("%members_online%", Integer.toString(targetParty.getOnlineMembers(true).size()))
							.replace("%description%", getPlugin().getMessageUtils().formatText(targetParty.getDescription()))
							.replace("%motd_size%", Integer.toString(targetParty.getMotd() != null ? targetParty.getMotd().length() : 0))
							.replace("%homes%", Integer.toString(targetParty.getHomes().size()))
							.replace("%kills%", Integer.toString(targetParty.getKills()))
							.replace("%password%", getPlugin().getMessageUtils().formatYesNo(targetParty.getPassword() != null))
							.replace("%protection%", getPlugin().getMessageUtils().formatYesNo(targetParty.getProtection()))
							.replace("%follow%", getPlugin().getMessageUtils().formatYesNo(targetParty.isFollowEnabled()))
							.replace("%open%", getPlugin().getMessageUtils().formatYesNo(targetParty.isOpen()))
							.replace("%color%", (targetParty.getColor() != null ? targetParty.getColor().getName() : Messages.PARTIES_OPTIONS_NONE))
							.replace("%color_active%", (targetParty.getCurrentColor() != null ? targetParty.getCurrentColor().getName() : Messages.PARTIES_OPTIONS_NONE))
							.replace("%color_dynamic%", (targetParty.getDynamicColor() != null ? targetParty.getDynamicColor().getName() : Messages.PARTIES_OPTIONS_NONE))
							.replace("%experience%", Integer.toString((int) targetParty.getExperience()))
					);
				}
				break;
			case PLAYER:
				User targetUser = plugin.getPlayer(targetPlayer.getPlayerUUID());
				sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PLAYER_HEADER);
				
				for (String line : Messages.ADDCMD_DEBUG_PLAYER_TEXT) {
					sendMessage(sender, partyPlayer, line
							.replace("%uuid%", targetPlayer.getPlayerUUID().toString())
							.replace("%name%", targetPlayer.getName())
							.replace("%rank%", Integer.toString(targetPlayer.getRank()))
							.replace("%party%", targetPlayer.getPartyId() != null ? targetPlayer.getPartyId().toString() : Messages.PARTIES_OPTIONS_NONE)
							.replace("%chat%", getPlugin().getMessageUtils().formatYesNo(targetPlayer.isChatParty()))
							.replace("%spy%", getPlugin().getMessageUtils().formatYesNo(targetPlayer.isSpy()))
							.replace("%muted%", getPlugin().getMessageUtils().formatYesNo(targetPlayer.isMuted()))
							.replace("%protection_bypass%", getPlugin().getMessageUtils().formatYesNo(targetUser != null && targetUser.hasPermission(PartiesPermission.ADMIN_PROTECTION_BYPASS)))
					);
				}
				break;
			default:
				// Nothing
		}
	}
	
	protected String parseDebugExp(String line) {
		String newLine = line;
		if (newLine.contains("%mode_options%")) {
			if (getPlugin().getExpManager().getMode() == ExpManager.ExpMode.PROGRESSIVE) {
				
				newLine = newLine.replace("%mode_options%", Messages.ADDCMD_DEBUG_EXP_MODE_OPTIONS_PROGRESSIVE
						.replace("%start%", Integer.toString((int) ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_START))
						.replace("%formula%", getPlugin().getMessageUtils().formatText(ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_LEVEL_EXP)
						));
			} else {
				newLine = newLine.replace("%mode_options%", Messages.ADDCMD_DEBUG_EXP_MODE_OPTIONS_FIXED
						.replace("%repeat%", getPlugin().getMessageUtils().formatYesNo(ConfigMain.ADDITIONAL_EXP_FIXED_REPEAT))
						.replace("%levels%", Integer.toString(ConfigMain.ADDITIONAL_EXP_FIXED_LIST.size())
						));
			}
		}
		return newLine
				.replace("%exp%", getPlugin().getMessageUtils().formatEnabledDisabled(ConfigMain.ADDITIONAL_EXP_ENABLE))
				.replace("%earn%", getPlugin().getMessageUtils().formatYesNo(ConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS))
				.replace("%mode%", ConfigMain.ADDITIONAL_EXP_MODE);
	}
	
	protected static class TemporaryPartyPlayer extends PartyPlayerImpl {
		@Setter private boolean persistent = true;
		
		public TemporaryPartyPlayer(@NotNull PartiesPlugin plugin, @NotNull UUID uuid) {
			super(plugin, uuid);
		}
		
		@Override
		public boolean isPersistent() {
			return persistent;
		}
		
		@Override
		public void playSound(String sound, double volume, double pitch) {
			// Nothing to do
		}
		
		@Override
		public void playChatSound() {
			// Nothing to do
		}
		
		@Override
		public void playBroadcastSound() {
			// Nothing to do
		}
		
		@Override
		public void sendPacketUpdate() {
			// Nothing to do
		}
		
		@Override
		public boolean isVanished() {
			return false;
		}
	}
	
	protected enum CommandType {
		BUNGEECORD, CONFIG, EXP, PARTY, PLAYER;
		
		public static String commandBungeecord;
		
		public static CommandType parse(String command) {
			if (command.equalsIgnoreCase(commandBungeecord))
				return BUNGEECORD;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_CONFIG))
				return CONFIG;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_EXP))
				return EXP;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PARTY))
				return PARTY;
			else if (command.equalsIgnoreCase(ConfigMain.COMMANDS_MISC_PLAYER))
				return PLAYER;
			return null;
		}
	}
}
