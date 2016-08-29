package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandDeny implements CommandInterface{
	private Parties plugin;
	 
    public CommandDeny(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.DENY.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.DENY.toString()));
			return true;
		}
		if (plugin.getPlayerHandler().getThePlayer(p).getInvite() == "") {
			tp.sendMessage(Messages.deny_noinvite);
			return true;
		}
		Party party = plugin.getPartyHandler().loadParty(tp.getInvite());
		if (party == null) {
			tp.sendMessage(Messages.deny_noexist);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		party.denyInvite(p.getUniqueId());
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] denied invite for " + party.getName());
		return true;
	}
}
