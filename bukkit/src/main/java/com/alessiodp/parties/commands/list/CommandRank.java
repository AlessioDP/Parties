package com.alessiodp.parties.commands.list;

import java.util.List;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.interfaces.Rank;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class CommandRank implements ICommand {
	private Parties plugin;
	 
	public CommandRank(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.RANK.toString())) {
			pp.sendNoPermission(PartiesPermission.RANK);
			return false;
		}
		
		if (!player.hasPermission(PartiesPermission.RANK_OTHERS.toString())) {
			if (pp.getPartyName().isEmpty()) {
				pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
			
			if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_ADMIN_RANK))
				return false;
		}
		
		if (commandData.getArgs().length != 3) {
			pp.sendMessage(Messages.MAINCMD_RANK_WRONGCMD);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.RANK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		String playerName = commandData.getArgs()[1];
		PartyPlayerEntity promoted;
		
		// Conflict checker
		List<PartyPlayer> listPlayers = plugin.getDatabaseManager().getPartyPlayersByName(playerName).join();
		if (!listPlayers.isEmpty()) {
			promoted = plugin.getPlayerManager().getPlayer(listPlayers.get(0).getPlayerUUID());
		} else {
			pp.sendMessage(Messages.MAINCMD_RANK_PLAYERNOTINPARTY_OTHER
					.replace("%player%", playerName));
			return;
		}
		
		PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		if (commandData.havePermission(PartiesPermission.RANK_OTHERS)) {
			party = plugin.getPartyManager().getParty(promoted.getPartyName());
			if (party == null) {
				pp.sendMessage(Messages.MAINCMD_RANK_PLAYERNOTINPARTY_OTHER, promoted);
				return;
			}
		} else {
			if (party == null || !party.getMembers().contains(promoted.getPlayerUUID())) {
				pp.sendMessage(Messages.MAINCMD_RANK_PLAYERNOTINPARTY, promoted);
				return;
			}
		}
		
		if (!party.getMembers().contains(promoted.getPlayerUUID())) {
			if (!commandData.havePermission(PartiesPermission.RANK_OTHERS) || promoted.getPartyName().isEmpty()) {
				pp.sendMessage(Messages.MAINCMD_RANK_PLAYERNOTINPARTY, promoted);
				return;
			}
			party = plugin.getPartyManager().getParty(promoted.getPartyName());
		}
		
		Rank rank = plugin.getRankManager().searchRankByName(commandData.getArgs()[2]);
		if (rank == null) {
			pp.sendMessage(Messages.MAINCMD_RANK_WRONGRANK
					.replace("%rank_typed%", commandData.getArgs()[2]) // TODO: Bypass, remove in future updates
					.replace("%rank_name%", commandData.getArgs()[2])
					.replace("%rank_chat%", commandData.getArgs()[2]));
			return;
		}
		if (rank.getLevel() == promoted.getRank()) {
			pp.sendMessage(Messages.MAINCMD_RANK_SAMERANK
					.replace("%rank_name%", rank.getName())
					.replace("%rank_chat%", rank.getChat())
					.replace("%player%", promoted.getName()));
			return;
		}
		if (!commandData.havePermission(PartiesPermission.RANK_OTHERS)) {
			if (pp.getRank() <= promoted.getRank()) {
				pp.sendMessage(Messages.MAINCMD_RANK_LOWRANK
						.replace("%rank_name%", rank.getName())
						.replace("%rank_chat%", rank.getChat())
						.replace("%player%", promoted.getName()));
				return;
			}
			if (promoted.getPlayerUUID().equals(pp.getPlayerUUID())) {
				pp.sendMessage(Messages.MAINCMD_RANK_CHANGINGYOURSELF);
				return;
				
			}
			if ((rank.getLevel() != ConfigParties.RANK_SET_HIGHER) && (rank.getLevel() >= pp.getRank())) {
				pp.sendMessage(Messages.MAINCMD_RANK_TOHIGHERRANK
						.replace("%rank_name%", rank.getName())
						.replace("%rank_chat%", rank.getChat()));
				return;
			}
		}
		
		/*
		 * Command starts
		 */
		int oldRank = promoted.getRank();
		if (rank.getLevel() == ConfigParties.RANK_SET_HIGHER) {
			PartyPlayerEntity oldLeader = plugin.getPlayerManager().getPlayer(party.getLeader());
			oldLeader.setRank(promoted.getRank());
			oldLeader.updatePlayer();
			party.setLeader(promoted.getPlayerUUID());
			party.updateParty();
		}
		
		promoted.setRank(rank.getLevel());
		promoted.updatePlayer();
		
		party.callChange();
		
		pp.sendMessage(Messages.MAINCMD_RANK_CHANGED, promoted);
		party.sendBroadcast(promoted, Messages.MAINCMD_RANK_BROADCAST);
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_RANK
				.replace("{player}", promoted.getName())
				.replace("{value1}", Integer.toString(oldRank))
				.replace("{value2}", Integer.toString(rank.getLevel()))
				.replace("{sender}", pp.getName()), true);
	}
}
