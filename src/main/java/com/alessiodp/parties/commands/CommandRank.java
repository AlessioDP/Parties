package com.alessiodp.parties.commands;

import org.bukkit.OfflinePlayer;
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

public class CommandRank implements CommandInterface{
	private Parties plugin;
	 
    public CommandRank(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.RANK.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.RANK.toString()));
			return true;
		}
		if(!p.hasPermission(PartiesPermissions.RANK_OTHERS.toString())){
			if (!tp.haveParty()){
				tp.sendMessage(Messages.noparty);
				return true;
			}
			Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
			if(r != null){
				if(!r.havePermission(PartiesPermissions.PRIVATE_EDIT_RANK.toString())){
					Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_EDIT_RANK.toString());
					if(rr!=null)
						tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
					else
						tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_EDIT_RANK.toString()));
					return true;
				}
			}
		}
		if (args.length != 3) {
			tp.sendMessage(Messages.rank_wrongcmd);
			return true;
		}
		
		/*
		 * 
		 * 
		 * 
		 */
		
		String playerName = args[1];
		OfflinePlayer promotedPlayer = plugin.getServer().getOfflinePlayer(playerName);
		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
		ThePlayer promoted = plugin.getPlayerHandler().getThePlayer(promotedPlayer);
		if(p.hasPermission(PartiesPermissions.RANK_OTHERS.toString())){
			party = plugin.getPartyHandler().loadParty(promoted.getPartyName());
			if(party == null){
				tp.sendMessage(Messages.rank_noparty, promotedPlayer);
				return true;
			}
		} else {
			if(party == null || !party.getMembers().contains(promoted.getUUID())){
				tp.sendMessage(Messages.rank_nomember, promotedPlayer);
				return true;
			}
		}
		
		if(!party.getMembers().contains(promotedPlayer.getUniqueId())){
			if(!p.hasPermission(PartiesPermissions.RANK_OTHERS.toString()) || !promoted.haveParty()){
				tp.sendMessage(Messages.rank_nomember, promotedPlayer);
				return true;
			}
			party = plugin.getPartyHandler().loadParty(promoted.getPartyName());
		}
		
		Rank rank = plugin.getPartyHandler().searchRank(args[2]);
		if(rank==null){
			tp.sendMessage(Messages.rank_wrongrank.replace("%rank%", args[2]));
			return true;
		}
		if(rank.getLevel() == promoted.getRank()){
			tp.sendMessage(Messages.rank_alreadyrank.replace("%rank%", rank.getName()).replace("%player%", promotedPlayer.getName()));
			return true;
		}
		if(!p.hasPermission(PartiesPermissions.RANK_OTHERS.toString())){
			if(tp.getRank() <= promoted.getRank()){
				tp.sendMessage(Messages.rank_lowrank.replace("%rank%", rank.getName()).replace("%player%", promotedPlayer.getName()));
				return true;
			}
			if(promotedPlayer.getUniqueId().equals(p.getUniqueId())){
				if(rank.getLevel() < promoted.getRank())
					tp.sendMessage(Messages.rank_nodegradeyourself);
				else
					tp.sendMessage(Messages.rank_nopromoteyourself);
				return true;
				
			}
			if((rank.getLevel() != Variables.rank_last) && (rank.getLevel() >= tp.getRank())){
				tp.sendMessage(Messages.rank_tohigherrank.replace("%rank%", rank.getName()));
				return true;
			}
		}
		/*
		 * 
		 */
		if(rank.getLevel() == Variables.rank_last){
			ThePlayer oldLeader = plugin.getPlayerHandler().getThePlayer(party.getLeader());
			oldLeader.setRank(promoted.getRank());
			oldLeader.updatePlayer();
			party.setLeader(promotedPlayer.getUniqueId());
			party.updateParty();
			
		}
		promoted.setRank(rank.getLevel());
		promoted.updatePlayer();
		
		if(!party.getName().equalsIgnoreCase(tp.getPartyName()))
			tp.sendMessage(Messages.rank_promoted.replace("%player%", promotedPlayer.getName()).replace("%rank%", rank.getName()));
		party.sendBroadcastParty(p, Messages.rank_promoted.replace("%player%", promotedPlayer.getName()).replace("%rank%", rank.getName()));
		party.sendSpyMessage(p, Messages.rank_promoted.replace("%player%", promotedPlayer.getName()).replace("%rank%", rank.getName()));
		
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] setted to leader "+ promotedPlayer.getName() +"["+promotedPlayer.getUniqueId()+" of " + party.getName());
		return true;
	}
}
