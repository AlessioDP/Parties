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
		commandData.addPermission(PartiesPermission.ADMIN_KICK_OTHERS);
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
		order(parties, OrderType.parse(ConfigParties.LIST_ORDEREDBY));
		
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
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_LIST
				.replace("{player}", pp.getName()), true);
	}
	
	private void order(List<PartyImpl> list, OrderType order) {
		int from;
		for (int c = 0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (switchParty(list.get(c2), list.get(from), order)) {
					from = c2;
				}
			}
			if (from != c) {
				PartyImpl temp = list.get(c);
				list.set(c, list.get(from));
				list.set(from, temp);
			}
		}
	}
	private boolean switchParty(PartyImpl first, PartyImpl second, OrderType order) {
		boolean ret;
		switch (order) {
			case PLAYERS:
				// Online players order
				ret = first.getNumberOnlinePlayers() > second.getNumberOnlinePlayers();
				break;
			case ALLPLAYERS:
				// Total players order
				ret = first.getMembers().size() > second.getMembers().size();
				break;
			case KILLS:
				// Party kills order
				ret = first.getKills() > second.getKills();
				break;
			case EXPERIENCE:
				// Party level order
				ret = first.getExperience() > second.getExperience();
				break;
			default:
				ret = first.getName().trim().compareTo(second.getName().trim()) < 0;
		}
		return ret;
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
	
	private enum OrderType {
		NAME, PLAYERS, ALLPLAYERS, KILLS, EXPERIENCE;
		
		static OrderType parse(String name) {
			OrderType ret = NAME;
			switch (name.toLowerCase()) {
				case "players":
					ret = PLAYERS;
					break;
				case "allplayers":
					ret = ALLPLAYERS;
					break;
				case "kills":
					ret = KILLS;
					break;
				case "experience":
					ret = EXPERIENCE;
					break;
			}
			return ret;
		}
	}
}