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
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandRank extends PartiesSubCommand {
	
	public CommandRank(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.RANK,
				PartiesPermission.USER_RANK,
				ConfigMain.COMMANDS_SUB_RANK,
				true
		);
		
		syntax = String.format("%s <%s> <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PLAYER,
				Messages.PARTIES_SYNTAX_RANK
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_RANK;
		help = Messages.HELP_MAIN_COMMANDS_RANK;
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
			
			if (!sender.hasPermission(PartiesPermission.ADMIN_RANK_OTHERS)) {
				if (!partyPlayer.isInParty()) {
					sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
					return false;
				}
				
				if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_ADMIN_RANK))
					return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(PartiesPermission.ADMIN_RANK_OTHERS);
		}
		
		if (commandData.getArgs().length < 3 || commandData.getArgs().length > 4) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", syntax));
			return false;
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
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
		listPlayers.removeIf((uuid) -> !((PartiesPlugin) plugin).getPlayerManager().getPlayer(uuid).isInParty());
		Collections.sort(listPlayers);
		
		if (listPlayers.size() > 1) {
			// Multiple players
			// >>> /party rank <player> <rank> <number>
			// Check if the player have selected someone
			if (commandData.getArgs().length > 2) {
				try {
					int sel = Integer.parseInt(commandData.getArgs()[3]);
					playerUUID = listPlayers.get(sel-1);
				} catch(Exception ignored) {}
			}
			
			// The player didn't select a player
			if (playerUUID == null) {
				for (String str : Messages.MAINCMD_RANK_CONFLICT_CONTENT) {
					if (str.contains("%list_players%")) {
						int i = 1;
						for (UUID uuid : listPlayers) {
							PartyPlayerImpl pp = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(uuid);
							if (pp.isInParty()) {
								sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_CONFLICT_PLAYER
										.replace("%number%", Integer.toString(i))
										.replace("%username%", playerName), pp, ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(pp));
								i++;
							}
						}
					} else {
						sendMessage(sender, partyPlayer, str);
					}
				}
				return;
			}
		} else if (listPlayers.size() == 1) {
			// Player found
			playerUUID = listPlayers.get(0);
		} else {
			// Not found
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PLAYER_NOT_FOUND
					.replace("%player%", playerName));
			return;
		}
		
		OfflineUser promotedPlayer = plugin.getOfflinePlayer(playerUUID);
		PartyPlayerImpl promotedPp = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(playerUUID);
		PartyRankImpl rank = ((PartiesPlugin) plugin).getRankManager().searchRankByName(commandData.getArgs()[2]);
		
		if (rank == null) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_WRONGRANK
					.replace("%rank_name%", commandData.getArgs()[2])
					.replace("%rank_chat%", commandData.getArgs()[2]));
			return;
		}
		
		if (sender.isPlayer() && promotedPlayer.getUUID().equals(partyPlayer.getPlayerUUID())) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_CHANGINGYOURSELF);
			return;
		}
		
		PartyImpl party = partyPlayer != null ? ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer) : null;
		
		boolean otherParty = (party == null || !party.getMembers().contains(promotedPlayer.getUUID()));
		
		if (otherParty) {
			// Other party
			party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(promotedPp);
			
			if (sender.isPlayer() && !commandData.havePermission(PartiesPermission.ADMIN_RANK_OTHERS)) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_PLAYERNOTINPARTY, promotedPp);
				return;
			}
			
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PLAYER_NOT_IN_PARTY, promotedPp);
				return;
			}
		} else if (sender.isPlayer() && !commandData.havePermission(PartiesPermission.ADMIN_RANK_OTHERS)) {
			// Do not bypass rank restrictions
			if (rank.getLevel() == promotedPp.getRank()) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_SAMERANK
						.replace("%rank_name%", rank.getName())
						.replace("%rank_chat%", rank.getChat())
						.replace("%player%", promotedPlayer.getName()));
				return;
			}
			
			if (partyPlayer.getRank() <= promotedPp.getRank()) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_LOWRANK
						.replace("%rank_name%", rank.getName())
						.replace("%rank_chat%", rank.getChat())
						.replace("%player%", promotedPlayer.getName()));
				return;
			}
			if ((rank.getLevel() != ConfigParties.RANK_SET_HIGHER) && (rank.getLevel() >= partyPlayer.getRank())) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_TOHIGHERRANK
						.replace("%rank_name%", rank.getName())
						.replace("%rank_chat%", rank.getChat()));
				return;
			}
		}
		
		if (party.isFixed() && rank.getLevel() == ConfigParties.RANK_SET_HIGHER) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_FIXEDLEADER);
			return;
		}
		
		// Command start
		int oldRank = promotedPp.getRank();
		if (rank.getLevel() == ConfigParties.RANK_SET_HIGHER) {
			party.changeLeader(promotedPp);
		} else {
			promotedPp.setRank(rank.getLevel());
		}
		
		sendMessage(sender, partyPlayer, Messages.MAINCMD_RANK_CHANGED
				.replace("%rank_name%", rank.getName())
				.replace("%rank_chat%", rank.getChat()), promotedPp);
		party.broadcastMessage(Messages.MAINCMD_RANK_BROADCAST
				.replace("%rank_name%", rank.getName())
				.replace("%rank_chat%", rank.getChat()), promotedPp);
		
		plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_RANK,
				sender.getName(), promotedPp.getName(), party.getName() != null ? party.getName() : "_", oldRank, rank.getLevel(), otherParty), true);
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret = plugin.getCommandManager().getCommandUtils().tabCompletePlayerList(args, 1);
		} else if (args.length == 3) {
			for (PartyRankImpl rank : ConfigParties.RANK_LIST) {
				ret.add(rank.getName());
			}
		}
		return ret;
	}
}
