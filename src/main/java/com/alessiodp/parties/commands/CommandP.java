package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandP implements CommandInterface{
	private Parties plugin;
	 
    public CommandP(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.SENDMESSAGE.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.SENDMESSAGE.toString()));
			return true;
		}
		if(!plugin.getPlayerHandler().getThePlayer(p).haveParty()){
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if(r != null){
			if(!r.havePermission(PartiesPermissions.PRIVATE_SENDMESSAGE.toString())){
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_SENDMESSAGE.toString());
				if(rr!=null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_SENDMESSAGE.toString()));
				return true;
			}
		}
		if(args.length == 0){
			tp.sendMessage(Messages.p_wrongcmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		StringBuilder sb = new StringBuilder();
		for(String word:args){
			if(sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(word);
		}
		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());

		party.sendPlayerMessage(p, sb.toString());
		party.sendSpyMessage(p, sb.toString());
		
		if(Variables.log_chat)
			LogHandler.log(1, "Chat of "+party.getName()+":" + p.getName() + "[" + p.getUniqueId() + "]:"+sb.toString());
		return true;
	}
}
