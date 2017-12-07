package com.alessiodp.parties.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandList implements CommandInterface {
	private Parties plugin;
	 
	public CommandList(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.LIST.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.LIST.toString()));
			return true;
		}
		if (args.length > 2) {
			tp.sendMessage(Messages.list_wrongcmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		List<Party> parties = new ArrayList<Party>();
		for (Party party : plugin.getDatabaseDispatcher().getAllParties()) {
			if (party != null) {
				party.reloadOnlinePlayers();
				if (party.getNumberOnlinePlayers() >= Variables.list_filter)
					parties.add(party);
			}
		}
		switch (Variables.list_orderedby.toLowerCase()) {
		case "kills":
			if (!Variables.kill_enable)
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
		parties = limitList(parties);
		int page = 1;
		int maxpages = parties.size() % Variables.list_maxpages == 0 ? parties.size() / Variables.list_maxpages : (parties.size() / Variables.list_maxpages) + 1;
		if (args.length > 1) {
			try {
				page = Integer.parseInt(args[1]);
			} catch(NumberFormatException ex) {
				tp.sendMessage(Messages.list_wrongcmd);
				return true;
			}
			if (page > maxpages)
				page = maxpages;
			else if (page < 1)
				page = 1;
		}
		if (parties.size() == 0)
			maxpages=1;
		int c = 0;
		tp.sendMessage(Messages.list_header
				.replace("%number%",	Integer.toString(parties.size()))
				.replace("%page%",		Integer.toString(page))
				.replace("%maxpages%",	Integer.toString(maxpages)));
		tp.sendMessage(Messages.list_subheader
				.replace("%number%",	Integer.toString(parties.size()))
				.replace("%page%",		Integer.toString(page))
				.replace("%maxpages%",	Integer.toString(maxpages)));
		
		for (Party party : parties) {
			if (c >= ((page-1) * Variables.list_maxpages) && c < ((page-1) * Variables.list_maxpages) + Variables.list_maxpages) {
				tp.sendMessage(Messages.list_formatparty, party);
			}
			c++;
		}
		if (parties.size() == 0)
			tp.sendMessage(Messages.list_offline);
		tp.sendMessage(Messages.list_footer
				.replace("%number%",	Integer.toString(parties.size()))
				.replace("%page%",		Integer.toString(page))
				.replace("%maxpages%",	Integer.toString(maxpages)));
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] used command list", true);
		return true;
	}
	
	private List<Party> orderName(List<Party> list) {
		int from;
		
		for (int c = 0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getName().trim().compareTo(list.get(from).getName().trim()) < 0) {
					from = c2;
				}
			}
			if (from != c) {
				Party temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<Party> orderPlayers(List<Party> list) {
		int from;
		
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getNumberOnlinePlayers() > list.get(from).getNumberOnlinePlayers()) {
					from = c2;
				}
			}
			if (from != c) {
				Party temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<Party> orderAllPlayers(List<Party> list) {
		int from;
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getMembers().size() > list.get(from).getMembers().size()) {
					from = c2;
				}
			}
			if (from != c) {
				Party temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<Party> orderKills(List<Party> list) {
		int from;
		for (int c=0; c < list.size() - 1;c++) {
			from = c;
			for (int c2 = c+1 ; c2 < list.size(); c2++) {
				if (list.get(c2).getKills() > list.get(from).getKills()) {
					from = c2;
				}
			}
			if (from != c) {
				Party temp = list.get(c);
				list.set(c, list.get(from)); 
				list.set(from, temp);
			}
		}
		return list;
	}
	private List<Party> limitList(List<Party> list) {
		List<Party> ret = list;
		if (Variables.list_limitparties >= 0) {
			ret = new ArrayList<Party>();
			for (int c=0; c < Variables.list_limitparties; c++) {
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