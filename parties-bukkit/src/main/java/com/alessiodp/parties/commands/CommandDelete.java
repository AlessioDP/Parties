package com.alessiodp.parties.commands;

import org.bukkit.Bukkit;
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
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;

public class CommandDelete implements CommandInterface {
	private Parties plugin;
	 
	public CommandDelete(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.ADMIN_DELETE.toString())) {
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
			if (args[2].equalsIgnoreCase(Variables.command_silent)) {
				if (p.hasPermission(PartiesPermissions.ADMIN_DELETE_SILENT.toString())) {
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.ADMIN_DELETE_SILENT.toString()));
					return true;
				}
				if (!plugin.getPartyHandler().existParty(args[1])) {
					tp.sendMessage(Messages.delete_noexist.replace("%party%", args[1]));
					return true;
				}
				Party party = plugin.getPartyHandler().getParty(args[1]);
				if (party == null) {
					tp.sendMessage(Messages.delete_noexist.replace("%party%", args[1]));
					return true;
				}
				
				// Calling Pre API event
				PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party.getName(), PartiesPartyPreDeleteEvent.DeleteCause.DELETE, null, p);
				Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
				if (!partiesPreDeleteEvent.isCancelled()) {
					tp.sendMessage(Messages.delete_deleted_silent, party);
					LogHandler.log(LogLevel.BASIC, "Party " + party.getName() + " deleted (silently) by: " + p.getName(), true, ConsoleColors.CYAN);
					
					party.removeParty();
					// Calling Post API event
					PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.DELETE, null, p);
					Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
				}
			} else {
				tp.sendMessage(Messages.delete_wrongcmd);
			}
		} else {
			if (!plugin.getPartyHandler().existParty(args[1])) {
				tp.sendMessage(Messages.delete_noexist.replace("%party%", args[1]));
				return true;
			}
					
			Party party = plugin.getPartyHandler().getParty(args[1]);
			if (party == null) {
				tp.sendMessage(Messages.delete_noexist);
				return true;
			}
			
			// Calling Pre API event
			PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party.getName(), PartiesPartyPreDeleteEvent.DeleteCause.DELETE, null, p);
			Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
			if (!partiesPreDeleteEvent.isCancelled()) {
				tp.sendMessage(Messages.delete_deleted, party);
				party.sendBroadcastParty(p, Messages.delete_warn);
				
				party.removeParty();
				// Calling Post API event
				PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.DELETE, null, p);
				Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
			} else {
				LogHandler.log(LogLevel.DEBUG, "PartiesDeleteEvent is cancelled, ignoring delete of " + party.getName(), true);
			}
			
			LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] deleted the party " + party.getName(), true);
		}
		return true;
	}
}
