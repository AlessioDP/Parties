package com.alessiodp.parties.commands;

import org.bukkit.ChatColor;
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

public class CommandClaim implements CommandInterface{
	private Parties plugin;
	 
    public CommandClaim(Parties parties) {
		plugin = parties;
	}
    
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if(plugin.getGriefPrevention() == null){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.invalidcommand));
			return false;
		}
		if(!p.hasPermission(PartiesPermissions.CLAIM.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.CLAIM.toString()));
			return true;
		}
		if (!tp.haveParty()) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if(r != null){
			if(!r.havePermission(PartiesPermissions.PRIVATE_CLAIM.toString())){
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_CLAIM.toString());
				if(rr!=null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_CLAIM.toString()));
				return true;
			}
		}
		
		if(args.length != 2){
			tp.sendMessage(Messages.claim_wrongcmd);
			return true;
		}
		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
		if(party == null){
			tp.sendMessage(Messages.noparty);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		if(args[1].equalsIgnoreCase(Variables.griefprevention_command_trust))
			plugin.getGriefPrevention().addPartyTrust(p, party);
		else if(args[1].equalsIgnoreCase(Variables.griefprevention_command_container))
			plugin.getGriefPrevention().addPartyContainer(p, party);
		else if(args[1].equalsIgnoreCase(Variables.griefprevention_command_access))
			plugin.getGriefPrevention().addPartyAccess(p, party);
		else if(args[1].equalsIgnoreCase(Variables.griefprevention_command_remove))
			plugin.getGriefPrevention().dropParty(p, party);
		else
			tp.sendMessage(Messages.claim_wrongcmd);
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] used command party claim");
		return true;
	}
}
