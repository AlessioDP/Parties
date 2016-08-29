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
import com.alessiodp.parties.utils.ConsoleColors;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandDelete implements CommandInterface{
	private Parties plugin;
	 
    public CommandDelete(Parties parties) {
		plugin = parties;
	}
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		
		/*
		 * Checks
		 */
		if(!p.hasPermission(PartiesPermissions.ADMIN_DELETE.toString())){
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_DELETE.toString()));
			return true;
		}
		if ((args.length > 3) || (args.length < 2)) {
			tp.sendMessage(Messages.delete_wrongcmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		if (args.length == 3) {
			if(args[2].equalsIgnoreCase(Variables.command_silent)){
				if(p.hasPermission(PartiesPermissions.ADMIN_DELETE_SILENT.toString())){
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_DELETE_SILENT.toString()));
					return true;
				}
				if(!plugin.getPartyHandler().existParty(args[1])){
					tp.sendMessage(Messages.delete_noexist.replace("%party%", args[1]));
					return true;
				}
				Party party = plugin.getPartyHandler().loadParty(args[1]);
				if(party == null){
					tp.sendMessage(Messages.delete_noexist.replace("%party%", args[1]));
					return true;
				}
				
				tp.sendMessage(Messages.delete_deleted_silent, party);
				plugin.log(ConsoleColors.CYAN.getCode() + "Party " + party.getName() + " deleted by command, from: " + p.getName());
				LogHandler.log(1, "Party " + party.getName() + " deleted by command, from: " + p.getName());
				
				party.removeParty();
				plugin.getPartyHandler().scoreboard_removePlayer(p);
			} else {
				tp.sendMessage(Messages.delete_wrongcmd);
			}
			return true;
		}
		
		if(!plugin.getPartyHandler().existParty(args[1])){
			tp.sendMessage(Messages.delete_noexist.replace("%party%", args[1]));
			return true;
		}
				
		Party party = plugin.getPartyHandler().loadParty(args[1]);
		if(party == null){
			tp.sendMessage(Messages.delete_noexist);
			return true;
		}
		
		tp.sendMessage(Messages.delete_deleted, party);
		party.sendBroadcastParty(p, Messages.delete_warn);
		party.sendSpyMessage(p, Messages.delete_warn);
		
		party.removeParty();
		
		LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] deleted the party " + party.getName());
		return true;
	}
}
