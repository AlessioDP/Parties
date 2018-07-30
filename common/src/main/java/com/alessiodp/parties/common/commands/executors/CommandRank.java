package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.RankImpl;
import com.alessiodp.parties.common.user.User;

import java.util.List;

public class CommandRank extends AbstractCommand {
	
	public CommandRank(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.RANK.toString())) {
			pp.sendNoPermission(PartiesPermission.RANK);
			return false;
		}
		
		if (!sender.hasPermission(PartiesPermission.ADMIN_RANK_OTHERS.toString())) {
			if (pp.getPartyName().isEmpty()) {
				pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
			
			if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_ADMIN_RANK))
				return false;
		}
		
		if (commandData.getArgs().length != 3) {
			pp.sendMessage(Messages.MAINCMD_RANK_WRONGCMD);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.ADMIN_RANK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		String playerName = commandData.getArgs()[1];
		PartyPlayerImpl promoted;
		
		// Conflict checker
		List<PartyPlayerImpl> listPlayers = plugin.getDatabaseManager().getPartyPlayersByName(playerName).join();
		if (!listPlayers.isEmpty()) {
			promoted = plugin.getPlayerManager().getPlayer(listPlayers.get(0).getPlayerUUID());
		} else {
			pp.sendMessage(Messages.MAINCMD_RANK_PLAYERNOTINPARTY_OTHER
					.replace("%player%", playerName));
			return;
		}
		
		PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
		
		if (commandData.havePermission(PartiesPermission.ADMIN_RANK_OTHERS)) {
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
			if (!commandData.havePermission(PartiesPermission.ADMIN_RANK_OTHERS) || promoted.getPartyName().isEmpty()) {
				pp.sendMessage(Messages.MAINCMD_RANK_PLAYERNOTINPARTY, promoted);
				return;
			}
			party = plugin.getPartyManager().getParty(promoted.getPartyName());
		}
		
		RankImpl rank = plugin.getRankManager().searchRankByName(commandData.getArgs()[2]);
		if (rank == null) {
			pp.sendMessage(Messages.MAINCMD_RANK_WRONGRANK
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
		if (!commandData.havePermission(PartiesPermission.ADMIN_RANK_OTHERS)) {
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
			PartyPlayerImpl oldLeader = plugin.getPlayerManager().getPlayer(party.getLeader());
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
				.replace("{user}", pp.getName()), true);
	}
}
