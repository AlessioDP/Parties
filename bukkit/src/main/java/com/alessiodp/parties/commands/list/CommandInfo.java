package com.alessiodp.parties.commands.list;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.interfaces.Rank;
import com.alessiodp.partiesapi.objects.PartyPlayer;

public class CommandInfo implements ICommand {
	private Parties plugin;
	 
	public CommandInfo(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.INFO.toString())) {
			pp.sendNoPermission(PartiesPermission.INFO);
			return;
		}
		
		if (args.length == 1 && pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		/*
		 * Command handling
		 */
		String partyName;
		if (args.length > 1) {
			if (!p.hasPermission(PartiesPermission.INFO_OTHERS.toString())) {
				pp.sendNoPermission(PartiesPermission.INFO_OTHERS);
				return;
			}
			
			partyName = args[1];
		} else if (!pp.getPartyName().isEmpty()) {
			partyName = pp.getPartyName();
		} else {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		PartyEntity party = plugin.getPartyManager().getParty(partyName);
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", partyName));
			return;
		}
		
		/*
		 * Command starts
		 */
		StringBuilder sb = new StringBuilder();
		for (String str : Messages.MAINCMD_INFO_CONTENT)
			sb.append(str + "\n");
		
		String text = sb.toString();
		
		Matcher mat = Pattern.compile("%list_(.*?)%").matcher(text);
		while (mat.find()) {
			Rank rr = plugin.getRankManager().searchRankByHardName(mat.group().substring(6, mat.group().length() - 1));
			if (rr != null) {
				StringBuilder list = new StringBuilder();
				int counter = 0;
				
				for (UUID playerUUID : party.getMembers()) {
					PartyPlayer pl = plugin.getPlayerManager().getPlayer(playerUUID);
					
					// Check rank level
					if (pl.getRank() == rr.getLevel()) {
						if (list.length() > 0) {
							list.append(Messages.MAINCMD_INFO_LIST_SEPARATOR);
						}
						
						OfflinePlayer op = Bukkit.getOfflinePlayer(pl.getPlayerUUID());
						if (op != null)
							list.append((op.isOnline() && !PartiesUtils.isVanished((Player)op) ? Messages.MAINCMD_INFO_LIST_ONLINEPREFIX : Messages.MAINCMD_INFO_LIST_OFFLINEPREFIX)
									+ pl.getName());
						else
							list.append(Messages.MAINCMD_INFO_LIST_UNKNOWN);
						counter++;
					}
				}
				text = text.replace(mat.group(), list.toString().isEmpty() ? Messages.MAINCMD_INFO_LIST_EMPTY : list.toString());
				text = text.replace("%number_" + mat.group().substring(6, mat.group().length()-1) + "%", Integer.toString(counter));
			}
		}
		
		int numOnlinePlayers = party.getNumberOnlinePlayers();
		sb = new StringBuilder();
		if (text.contains("%online%")) {
			if (numOnlinePlayers == 0)
				sb.append(Messages.MAINCMD_INFO_LIST_EMPTY);
			else {
				for (Player pl : party.getOnlinePlayers()) {
					if (sb.length() > 0) {
						sb.append(Messages.MAINCMD_INFO_LIST_SEPARATOR);
					}
					if (!PartiesUtils.isVanished(pl))
						sb.append(Messages.MAINCMD_INFO_LIST_ONLINEPREFIX + pl.getName());
				}
			}
			text = text.replace("%online%", sb.toString());
		}
		text = PartiesUtils.convertPartyPlaceholders(text, party, Messages.MAINCMD_INFO_LIST_EMPTY);
		
		pp.sendMessage(text);
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_INFO
				.replace("{player}", p.getName())
				.replace("{party}", party.getName()), true);
	}
}