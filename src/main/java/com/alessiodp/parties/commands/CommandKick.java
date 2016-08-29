package com.alessiodp.parties.commands;

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
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandKick implements CommandInterface{
	private Parties plugin;
	 
    public CommandKick(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.KICK.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.KICK.toString()));
			return true;
		}
		if (!tp.haveParty()) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if(r != null){
			if(!r.havePermission(PartiesPermissions.PRIVATE_KICK.toString())){
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_KICK.toString());
				if(rr!=null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_KICK.toString()));
				return true;
			}
		}
		if (args.length != 2) {
			tp.sendMessage(Messages.kick_wrongcmd);
			return true;
		}

		String playerName = args[1];
		OfflinePlayer kickedPlayer = Bukkit.getOfflinePlayer(playerName);

		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());

		if (tp.getRank() < plugin.getConfigHandler().getData().getRank(kickedPlayer.getUniqueId())) {
			tp.sendMessage(Messages.kick_uprank, kickedPlayer);
			return true;
		}

		if (!party.getMembers().contains(kickedPlayer.getUniqueId())) {
			if(p.hasPermission(PartiesPermissions.KICK_OTHERS.toString())){
				String partyName = plugin.getPlayerHandler().getThePlayer(kickedPlayer).getPartyName();
				if(partyName.isEmpty()){
					tp.sendMessage(Messages.kick_nomember, kickedPlayer);
					return true;
				}
				Party party_2 = plugin.getPartyHandler().loadParty(partyName);
				if(party_2 == null){
					tp.sendMessage(Messages.kick_nomember, kickedPlayer);
					return true;
				}
				if(party_2.getLeader().equals(kickedPlayer.getUniqueId())){
					party.sendBroadcastParty(kickedPlayer, Messages.leave_disbanded);
					party.sendSpyMessage(kickedPlayer, Messages.leave_disbanded);
					plugin.log(ConsoleColors.CYAN.getCode() + "Party " + party.getName() + " deleted by kick, from: " + p.getName());
					party_2.removeParty();
					if(kickedPlayer.isOnline())
						plugin.getPartyHandler().scoreboard_removePlayer(Bukkit.getPlayer(kickedPlayer.getName()));
					return true;
				}
				if(party_2.getMembers().contains(kickedPlayer.getUniqueId())){
					party_2.getMembers().remove(kickedPlayer.getUniqueId());
				}
				party_2.updateParty();
				if(kickedPlayer.isOnline()){
					plugin.getPartyHandler().scoreboard_removePlayer(Bukkit.getPlayer(kickedPlayer.getName()));
					plugin.getPartyHandler().scoreboard_refreshParty(party.getName());
				}
				LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] kicked "+ kickedPlayer.getName() +"["+kickedPlayer.getUniqueId()+" from " + party.getName());
			} else {
				tp.sendMessage(Messages.kick_nomember, kickedPlayer);
			}
			return true;
		}

		party.getMembers().remove(kickedPlayer.getUniqueId());

		Player onlinePlayer = plugin.getServer().getPlayer(playerName);
		if (onlinePlayer != null) {
			ThePlayer tp2 = plugin.getPlayerHandler().getThePlayer(onlinePlayer);
			tp2.sendMessage(Messages.kick_kickedfrom, p);
			tp2.removePlayer();
			
			party.getOnlinePlayers().remove(onlinePlayer);
			plugin.getPartyHandler().scoreboard_removePlayer(onlinePlayer);
			plugin.getPartyHandler().scoreboard_refreshParty(party.getName());
		} else {
			
			plugin.getPlayerHandler().getThePlayer(kickedPlayer).removePlayer();
		}
		tp.sendMessage(Messages.kick_kicksend, p);
		party.sendBroadcastParty(kickedPlayer, Messages.kick_kickedplayer);
		
		party.updateParty();
		LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] kicked "+ kickedPlayer.getName() +"["+kickedPlayer.getUniqueId()+" from " + party.getName());
		return true;
	}
}
