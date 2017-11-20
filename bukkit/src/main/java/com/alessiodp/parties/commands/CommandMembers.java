package com.alessiodp.parties.commands;

import java.util.HashMap;
import java.util.Map.Entry;
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
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.interfaces.Rank;

public class CommandMembers implements CommandInterface {
	private Parties plugin;
	 
	public CommandMembers(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.MEMBERS.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.MEMBERS.toString()));
			return true;
		}
		if (tp.getPartyName().isEmpty() && args.length == 1) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		if (args.length > 1 && !p.hasPermission(PartiesPermissions.MEMBERS_OTHERS.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.MEMBERS_OTHERS.toString()));
			return true;
		}
		
		String partyName = args.length > 1 ? args[1] : tp.getPartyName();
		Party party = plugin.getPartyHandler().getParty(partyName);
		if (party == null) {
			tp.sendMessage(Messages.members_noexist.replace("%party%", partyName));
			return true;
		}
		/*
		 * 
		 */
		
		StringBuilder sb = new StringBuilder();
		for (String str : Messages.members_content)
			sb.append(str + "\n");
		
		HashMap<UUID, Object[]> playersList = plugin.getDataHandler().getPlayersRank(party.getName());
		String text = sb.toString();
		Matcher mat = Pattern.compile("%list_(.*?)%").matcher(text);
		while (mat.find()) {
			Rank rr = plugin.getPartyHandler().searchRank(mat.group().substring(6, mat.group().length()-1));
			if (rr != null) {
				StringBuilder list = new StringBuilder();
				int counter = 0;
				for (Entry<UUID, Object[]> entry : playersList.entrySet()) {
					String entryName = (String)entry.getValue()[0];
					int entryRank = (int)entry.getValue()[1];
					if (entryRank == rr.getLevel()) {
						if (list.length() > 0) {
							list.append(Messages.members_separator);
						}
						OfflinePlayer op = Bukkit.getOfflinePlayer(entry.getKey());
						if (op != null)
							list.append((op.isOnline() && !isVanished((Player)op) ? Messages.members_online : Messages.members_offline)
									+ entryName);
						else
							list.append(Messages.members_someone);
						counter++;
					}
				}
				text = text.replace(mat.group(), list.toString().isEmpty() ? Messages.members_empty : list.toString());
				text = text.replace("%number_" + mat.group().substring(6, mat.group().length()-1) + "%", Integer.toString(counter));
			}
		}
		tp.sendMessage(text, party);
		
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] used command members for " + party.getName(), true);
		return true;
	}
	
	private boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}
}
