package com.alessiodp.parties.commands.list;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.objects.Party;

public class CommandList implements ICommand {
	private Parties plugin;
	 
	public CommandList(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.LIST.toString())) {
			pp.sendNoPermission(PartiesPermission.LIST);
			return;
		}
		
		if (args.length > 2) {
			pp.sendMessage(Messages.ADDCMD_LIST_WRONGCMD);
			return;
		}
		
		int selectedPage = 1;
		if (args.length == 2) {
			try {
				selectedPage = Integer.parseInt(args[1]);
			} catch(NumberFormatException ex) {
				pp.sendMessage(Messages.ADDCMD_LIST_WRONGCMD);
				return;
			}
		}
		/*
		 * Command starts
		 */
		// Get all parties
		List<PartyEntity> parties = new ArrayList<PartyEntity>();
		for (Party party : plugin.getDatabaseManager().getAllParties().join()) {
			if (party != null) {
				PartyEntity partyImpl = new PartyEntity(party, plugin);
				partyImpl.reloadOnlinePlayers();
				if (partyImpl.getNumberOnlinePlayers() >= ConfigParties.LIST_FILTERMIN)
					parties.add(partyImpl);
			}
		}
		
		// Order parties
		switch (ConfigParties.LIST_ORDEREDBY.toLowerCase()) {
		case "kills":
			if (!ConfigParties.KILLS_ENABLE)
				parties = orderName(parties);
			else
				parties = orderKills(parties);
			break;
		case "players":
			parties = orderPlayers(parties);
			break;
		case "allplayers":
			parties = orderAllPlayers(parties);
			break;
		default:
			parties = orderName(parties);
		}
		
		// Group up parties
		parties = limitList(parties);
		int maxPages;
		if (parties.size() == 0)
			maxPages = 1;
		else if (parties.size() % ConfigParties.LIST_PERPAGE == 0)
			maxPages = parties.size() / ConfigParties.LIST_PERPAGE;
		else
			maxPages = (parties.size() / ConfigParties.LIST_PERPAGE) + 1;
		
		if (selectedPage > maxPages)
			selectedPage = maxPages;
		else if (selectedPage < 1)
			selectedPage = 1;
		
		int currentPage = 0;
		StringBuilder sb = new StringBuilder();
		
		sb.append(Messages.ADDCMD_LIST_HEADER).append("\n");
		for (PartyEntity party : parties) {
			int currentChoosenPage = (selectedPage-1) * ConfigParties.LIST_PERPAGE;
			if (currentPage >= currentChoosenPage && currentPage < (currentChoosenPage + ConfigParties.LIST_PERPAGE)) {
				sb.append(PartiesUtils.convertPartyPlaceholders(Messages.ADDCMD_LIST_FORMATPARTY, party)).append("\n");
			}
			currentPage++;
		}
		if (parties.size() == 0)
			sb.append(Messages.ADDCMD_LIST_NOONE).append("\n");
		sb.append(Messages.ADDCMD_LIST_FOOTER);
		
		pp.sendMessage(sb.toString()
				.replace("%number%",	Integer.toString(parties.size()))
				.replace("%page%",		Integer.toString(selectedPage))
				.replace("%maxpages%",	Integer.toString(maxPages)));
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_IGNORE_STOP
				.replace("{player}", p.getName()), true);
	}
	
	
	private List<PartyEntity> orderName(List<PartyEntity> list) {
		int from;
		
		for (int c = 0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getName().trim().compareTo(list.get(from).getName().trim()) < 0) {
					from = c2;
				}
			}
			if (from != c) {
				PartyEntity temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyEntity> orderPlayers(List<PartyEntity> list) {
		int from;
		
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getNumberOnlinePlayers() > list.get(from).getNumberOnlinePlayers()) {
					from = c2;
				}
			}
			if (from != c) {
				PartyEntity temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyEntity> orderAllPlayers(List<PartyEntity> list) {
		int from;
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getMembers().size() > list.get(from).getMembers().size()) {
					from = c2;
				}
			}
			if (from != c) {
				PartyEntity temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyEntity> orderKills(List<PartyEntity> list) {
		int from;
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getKills() > list.get(from).getKills()) {
					from = c2;
				}
			}
			if (from != c) {
				PartyEntity temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyEntity> limitList(List<PartyEntity> list) {
		List<PartyEntity> ret = list;
		if (ConfigParties.LIST_LIMITPARTIES >= 0) {
			ret = new ArrayList<PartyEntity>();
			for (int c=0; c < ConfigParties.LIST_LIMITPARTIES; c++) {
				try {
					ret.add(list.get(c));
				} catch (Exception ex) {
					break;
				}
			}
		}
		return ret;
	}
}