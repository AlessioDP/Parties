package com.alessiodp.parties.commands;

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
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandAccept implements CommandInterface{
	private Parties plugin;
	 
    public CommandAccept(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.ACCEPT.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ACCEPT.toString()));
			return true;
		}
		if (tp.haveParty()) {
			tp.sendMessage(Messages.accept_alreadyinparty);
			return true;
		}
		if (tp.getInvite() == "") {
			tp.sendMessage(Messages.accept_noinvite);
			return true;
		}
		Party party = plugin.getPartyHandler().loadParty(tp.getInvite());
		if (party == null) {
			tp.sendMessage(Messages.accept_noexist);
			return true;
		}
		if((Variables.party_maxmembers != -1) && (party.getMembers().size() >= Variables.party_maxmembers)){
			tp.sendMessage(Messages.accept_maxplayers);
			return true;
		}
		
		
		party.acceptInvite(p.getUniqueId());
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] accepted invite for " + party.getName());
		return true;
	}
}
