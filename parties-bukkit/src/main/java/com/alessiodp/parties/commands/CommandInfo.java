package com.alessiodp.parties.commands;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.partiesapi.interfaces.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandInfo implements CommandInterface {
	private Parties plugin;
	 
	public CommandInfo(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.INFO.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.INFO.toString()));
			return true;
		}
		if (tp.getPartyName().isEmpty() && args.length == 1) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		if (args.length > 1 && !p.hasPermission(PartiesPermissions.INFO_OTHERS.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.INFO_OTHERS.toString()));
			return true;
		}
		String partyName;
		if (args.length > 1) {
			partyName = args[1];
			if (!plugin.getPartyHandler().existParty(partyName)) {
				tp.sendMessage(Messages.info_noexist.replace("%party%", partyName));
				return true;
			}
		} else if (!tp.getPartyName().isEmpty()) {
			partyName = tp.getPartyName();
		} else {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Party party = plugin.getPartyHandler().getParty(partyName);
		if (party == null) {
			tp.sendMessage(Messages.info_noexist.replace("%party%", partyName));
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		StringBuilder sb = new StringBuilder();
		for (String str : Messages.info_content)
			sb.append(str + "\n");
		
		HashMap<UUID, Object[]> playersList = plugin.getDatabaseDispatcher().getPlayersRank(party.getName());
		String text = sb.toString();
		Matcher mat = Pattern.compile("%list_(.*?)%").matcher(text);
		while (mat.find()) {
			Rank rr = plugin.getPartyHandler().searchRank(mat.group().substring(6, mat.group().length() - 1));
			if (rr != null) {
				StringBuilder list = new StringBuilder();
				int counter = 0;
				for (Entry<UUID, Object[]> entry : playersList.entrySet()) {
					String entryName = (String)entry.getValue()[0];
					int entryRank = (int)entry.getValue()[1];
					
					if (entryRank == rr.getLevel()) {
						if (list.length() > 0) {
							list.append(Messages.info_separator);
						}
						OfflinePlayer op = Bukkit.getOfflinePlayer(entry.getKey());
						if (op != null)
							list.append((op.isOnline() && !isVanished((Player)op) ? Messages.members_online : Messages.members_offline)
									+ (entryName != null ? entryName : op.getName()));
						else
							list.append(Messages.info_someone);
						counter++;
					}
				}
				text = text.replace(mat.group(), list.toString().isEmpty() ? Messages.info_empty : list.toString());
				text = text.replace("%number_" + mat.group().substring(6, mat.group().length()-1) + "%", Integer.toString(counter));
			}
		}
		int numOnlinePlayers = party.getNumberOnlinePlayers();
		sb = new StringBuilder();
		if (text.contains("%online%")) {
			if (numOnlinePlayers == 0)
				sb.append(Messages.info_empty);
			else {
				for (Player pl : party.getOnlinePlayers()) {
					if (sb.length() > 0) {
						sb.append(Messages.info_separator);
					}
					if (!isVanished(pl))
						sb.append(Messages.info_online + pl.getName());
				}
			}
			text = text.replace("%online%", sb.toString());
		}
		text = text
				.replace("%party%", party.getName())
				.replace("%onlinenumber%", numOnlinePlayers != 0 ? Integer.toString(numOnlinePlayers) : Messages.info_empty)
				.replace("%desc%", party.getDescription().isEmpty() ? Messages.info_missing : party.getDescription())
				.replace("%motd%", party.getMOTD().isEmpty() ? Messages.info_missing : party.getMOTD())
				.replace("%prefix%", party.getPrefix().isEmpty() ? Messages.info_missing : party.getPrefix())
				.replace("%suffix%", party.getSuffix().isEmpty() ? Messages.info_missing : party.getSuffix())
				.replace("%kills%", Integer.toString(party.getKills()));
		
		tp.sendMessage(text, party);
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] used command info for " + party.getName(), true);
		return true;
	}
	
	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}
}