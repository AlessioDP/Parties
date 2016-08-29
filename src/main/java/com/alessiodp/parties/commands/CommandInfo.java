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

public class CommandInfo implements CommandInterface{
	private Parties plugin;
	 
    public CommandInfo(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.INFO.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.INFO.toString()));
			return true;
		}
		if (!tp.haveParty() && args.length == 1) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		if(args.length > 1 && !p.hasPermission(PartiesPermissions.INFO_OTHERS.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.INFO_OTHERS.toString()));
			return true;
		}
		String partyName;
		if(args.length > 1){
			partyName = args[1];
			if(!plugin.getPartyHandler().existParty(partyName)){
				tp.sendMessage(Messages.info_noexist.replace("%party%", partyName));
				return true;
			}
		} else if(tp.haveParty()){
			partyName = tp.getPartyName();
		} else {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Party party = plugin.getPartyHandler().loadParty(partyName);
		if(party == null){
			tp.sendMessage(Messages.info_noexist.replace("%party%", partyName));
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		StringBuilder sb = new StringBuilder();
		for(String str : Messages.info_content)
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
						list.append(Messages.info_separator);
					}
					op = Bukkit.getOfflinePlayer(uuid);
					if(op != null){
						if(op.isOnline())
							list.append(Messages.info_online + op.getName());
						else
							list.append(Messages.info_offline + op.getName());
					} else
						list.append(Messages.info_someone);
					counter++;
				}
			}
			text = text.replace(mat.group(), list.toString().isEmpty() ? Messages.info_empty : list.toString());
			text = text.replace("%number_" + mat.group().substring(6, mat.group().length()-1) + "%", counter+"");
		}
		sb = new StringBuilder();
		if(text.contains("%online%")){
			if(party.getOnlinePlayers().size() == 0)
				sb.append(Messages.info_empty);
			for (Player pl : party.getOnlinePlayers()) {
				if (sb.length() > 0) {
					sb.append(Messages.info_separator);
				}
				sb.append(Messages.info_online + pl.getName());
			}
			text = text.replace("%online%", sb.toString());
		}
		text = text
				.replace("%party%", party.getName())
				.replace("%onlinenumber%", party.getOnlinePlayers().size() != 0 ? party.getOnlinePlayers().size()+"" : Messages.info_empty)
				.replace("%desc%", party.getDescription().isEmpty() ? Messages.info_missing : party.getDescription())
				.replace("%motd%", party.getMOTD().isEmpty() ? Messages.info_missing : party.getMOTD())
				.replace("%prefix%", party.getPrefix().isEmpty() ? Messages.info_missing : party.getPrefix())
				.replace("%suffix%", party.getSuffix().isEmpty() ? Messages.info_missing : party.getSuffix())
				.replace("%kills%", party.getKills()+"");
		
		tp.sendMessage(text, party);
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] used command info for " + party.getName());
		return true;
	}
}