package com.alessiodp.parties.commands;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandMembers implements CommandInterface{
	private Parties plugin;
	 
    public CommandMembers(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.MEMBERS.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.MEMBERS.toString()));
			return true;
		}
		if (!tp.haveParty() && args.length == 1) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		if(args.length > 1 && !p.hasPermission(PartiesPermissions.MEMBERS_OTHERS.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.MEMBERS_OTHERS.toString()));
			return true;
		}
		String partyName = tp.getPartyName();
		if(args.length > 1)
			partyName = args[1];
		
		Party party = plugin.getPartyHandler().loadParty(partyName);
		if(party == null){
			tp.sendMessage(Messages.members_noexist.replace("%party%", partyName));
			return true;
		}
		/*
		 * 
		 */
		
		StringBuilder sb = new StringBuilder();
		for(String str : Messages.members_content)
			sb.append(str + "\n");
		
		
		String text = sb.toString();
		Matcher mat = Pattern.compile("%list_(.*?)%").matcher(text);
		while(mat.find()){
			StringBuilder list = new StringBuilder();
			Rank rr = plugin.getPartyHandler().searchRank(mat.group().substring(6, mat.group().length()-1));
			if(rr==null)
				continue;
			int rank = rr.getLevel();
			int counter = 0;
			OfflinePlayer op;
			for(UUID uuid : party.getMembers()){
				if(plugin.getConfigHandler().getData().getRank(uuid) == rank){
					if (list.length() > 0) {
						list.append(Messages.members_separator);
					}
					op = Bukkit.getOfflinePlayer(uuid);
					if(op != null){
						if(op.isOnline())
							list.append(Messages.members_online + op.getName());
						else
							list.append(Messages.members_offline + op.getName());
					} else
						list.append(Messages.members_someone);
					counter++;
				}
			}
			text = text.replace(mat.group(), list.toString().isEmpty() ? Messages.members_empty : list.toString());
			text = text.replace("%number_" + mat.group().substring(6, mat.group().length()-1) + "%", counter+"");
		}
		tp.sendMessage(text, party);
		
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] used command members for " + party.getName());
		return true;
	}
}
