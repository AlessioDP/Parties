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
import com.alessiodp.parties.common.user.User;

import java.util.ArrayList;
import java.util.List;

public class CommandList extends AbstractCommand {
	
	public CommandList(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.LIST.toString())) {
			pp.sendNoPermission(PartiesPermission.LIST);
			return false;
		}
		
		if (commandData.getArgs().length > 2) {
			pp.sendMessage(Messages.ADDCMD_LIST_WRONGCMD);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.KICK_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		int selectedPage = 1;
		if (commandData.getArgs().length == 2) {
			try {
				selectedPage = Integer.parseInt(commandData.getArgs()[1]);
			} catch(NumberFormatException ex) {
				pp.sendMessage(Messages.ADDCMD_LIST_WRONGCMD);
				return;
			}
		}
		
		/*
		 * Command starts
		 */
		// Get all parties
		List<PartyImpl> parties = new ArrayList<>();
		for (PartyImpl party : plugin.getDatabaseManager().getAllParties().join()) {
			if (party != null) {
				if (!ConfigParties.LIST_HIDDENPARTIES.contains(party.getName())) {
					party.reloadOnlinePlayers();
					if (party.getNumberOnlinePlayers() >= ConfigParties.LIST_FILTERMIN)
						parties.add(party);
				}
			}
		}
		
		// Order parties
		switch (ConfigParties.LIST_ORDEREDBY.toLowerCase()) {
		case "kills":
			if (!plugin.getPlayerManager().isBukkit_killSystem())
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
		
		// Start printing
		int currentPage = 0;
		pp.sendMessage(Messages.ADDCMD_LIST_HEADER
				.replace("%number%",		Integer.toString(parties.size()))
				.replace("%page%",		Integer.toString(selectedPage))
				.replace("%maxpages%",	Integer.toString(maxPages)));
		
		if (parties.size() > 0) {
			for (PartyImpl party : parties) {
				int currentChoosenPage = (selectedPage - 1) * ConfigParties.LIST_PERPAGE;
				if (currentPage >= currentChoosenPage && currentPage < (currentChoosenPage + ConfigParties.LIST_PERPAGE)) {
					pp.sendMessage(plugin.getMessageUtils().convertPartyPlaceholders(Messages.ADDCMD_LIST_FORMATPARTY, party));
				}
				currentPage++;
			}
		} else {
			pp.sendMessage(Messages.ADDCMD_LIST_NOONE);
		}
		
		pp.sendMessage(Messages.ADDCMD_LIST_FOOTER
				.replace("%number%",		Integer.toString(parties.size()))
				.replace("%page%",		Integer.toString(selectedPage))
				.replace("%maxpages%",	Integer.toString(maxPages)));
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_IGNORE_STOP
				.replace("{player}", pp.getName()), true);
	}
	
	
	private List<PartyImpl> orderName(List<PartyImpl> list) {
		int from;
		
		for (int c = 0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getName().trim().compareTo(list.get(from).getName().trim()) < 0) {
					from = c2;
				}
			}
			if (from != c) {
				PartyImpl temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyImpl> orderPlayers(List<PartyImpl> list) {
		int from;
		
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getNumberOnlinePlayers() > list.get(from).getNumberOnlinePlayers()) {
					from = c2;
				}
			}
			if (from != c) {
				PartyImpl temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyImpl> orderAllPlayers(List<PartyImpl> list) {
		int from;
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getMembers().size() > list.get(from).getMembers().size()) {
					from = c2;
				}
			}
			if (from != c) {
				PartyImpl temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyImpl> orderKills(List<PartyImpl> list) {
		int from;
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getKills() > list.get(from).getKills()) {
					from = c2;
				}
			}
			if (from != c) {
				PartyImpl temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<PartyImpl> limitList(List<PartyImpl> list) {
		List<PartyImpl> ret = list;
		if (ConfigParties.LIST_LIMITPARTIES >= 0) {
			ret = new ArrayList<>();
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