package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.RankImpl;
import com.alessiodp.parties.common.user.OfflineUser;
import com.alessiodp.parties.common.user.User;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInfo extends AbstractCommand {
	
	public CommandInfo(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.INFO.toString())) {
			pp.sendNoPermission(PartiesPermission.INFO);
			return false;
		}
		
		if (commandData.getArgs().length == 1 && pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.INFO_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		String partyName;
		if (commandData.getArgs().length > 1) {
			if (!commandData.havePermission(PartiesPermission.INFO_OTHERS)) {
				pp.sendNoPermission(PartiesPermission.INFO_OTHERS);
				return;
			}
			
			partyName = commandData.getArgs()[1];
		} else if (!pp.getPartyName().isEmpty()) {
			partyName = pp.getPartyName();
		} else {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		PartyImpl party = plugin.getPartyManager().getParty(partyName);
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", partyName));
			return;
		}
		
		/*
		 * Command starts
		 */
		for (String line : Messages.MAINCMD_INFO_CONTENT) {
			// List ranks
			Matcher mat = Pattern.compile("%list_(.*?)%").matcher(line);
			while (mat.find()) {
				RankImpl rr = plugin.getRankManager().searchRankByHardName(mat.group().substring(6, mat.group().length() - 1));
				if (rr != null) {
					StringBuilder list = new StringBuilder();
					int counter = 0;
					
					for (UUID playerUUID : party.getMembers()) {
						PartyPlayerImpl pl = plugin.getPlayerManager().getPlayer(playerUUID);
						
						// Check rank level
						if (pl.getRank() == rr.getLevel()) {
							if (list.length() > 0) {
								list.append(Messages.MAINCMD_INFO_LIST_SEPARATOR);
							}
							
							OfflineUser offlinePlayer = plugin.getOfflinePlayer(pl.getPlayerUUID());
							if (offlinePlayer != null) {
								if (offlinePlayer.isOnline() && !pl.isVanished()) {
									list.append(Messages.MAINCMD_INFO_LIST_ONLINEPREFIX);
								} else {
									list.append(Messages.MAINCMD_INFO_LIST_OFFLINEPREFIX);
								}
								list.append(pl.getName());
							} else
								list.append(Messages.MAINCMD_INFO_LIST_UNKNOWN);
							counter++;
						}
					}
					line = line
							.replace(mat.group(), list.toString().isEmpty() ? Messages.MAINCMD_INFO_LIST_EMPTY : list.toString())
							.replace("%number_" + mat.group().substring(6, mat.group().length()-1) + "%",Integer.toString(counter));
				}
			}
			
			// Online players
			if (line.contains("%online%")) {
				StringBuilder sb = new StringBuilder();
				if (party.getNumberOnlinePlayers() == 0)
					sb.append(Messages.MAINCMD_INFO_LIST_EMPTY);
				else {
					for (PartyPlayerImpl partyPlayer : party.getOnlinePlayers()) {
						if (sb.length() > 0) {
							sb.append(Messages.MAINCMD_INFO_LIST_SEPARATOR);
						}
						if (!partyPlayer.isVanished())
							sb.append(Messages.MAINCMD_INFO_LIST_ONLINEPREFIX)
									.append(partyPlayer.getName());
					}
				}
				line = line.replace("%online%", sb.toString());
			}
			
			// Other placeholders
			line = plugin.getMessageUtils().convertPartyPlaceholders(line, party, Messages.MAINCMD_INFO_LIST_MISSING);
			
			pp.sendMessage(line);
		}
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_INFO
				.replace("{player}", pp.getName())
				.replace("{party}", party.getName()), true);
	}
}