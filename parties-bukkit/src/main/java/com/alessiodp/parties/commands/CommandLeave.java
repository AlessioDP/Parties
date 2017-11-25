package com.alessiodp.parties.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerLeaveEvent;

public class CommandLeave implements CommandInterface {
	private Parties plugin;
	 
	public CommandLeave(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		
		if (!p.hasPermission(PartiesPermissions.LEAVE.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.LEAVE.toString()));
			return true;
		}
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		// Calling API event
		PartiesPlayerLeaveEvent partiesLeaveEvent = new PartiesPlayerLeaveEvent(p, party.getName(), false, null);
		Bukkit.getServer().getPluginManager().callEvent(partiesLeaveEvent);
		if (!partiesLeaveEvent.isCancelled()) {
			if (party.getLeader().equals(p.getUniqueId())) {
				// Is leader
				// Calling Pre API event
				PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party.getName(), PartiesPartyPreDeleteEvent.DeleteCause.LEAVE, p.getUniqueId(), p);
				Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
				if (!partiesPreDeleteEvent.isCancelled()) {
					tp.sendMessage(Messages.leave_byeplayer.replace("%party%", party.getName()));
					party.sendBroadcastParty(p, Messages.leave_disbanded);
					
					LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] deleted " + party.getName() + " by leave", true, ConsoleColors.CYAN);
					
					party.removeParty();
					// Calling Post API event
					PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.LEAVE, p.getUniqueId(), p);
					Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
					
					plugin.getPartyHandler().tag_removePlayer(p, null);
				} else
					LogHandler.log(LogLevel.DEBUG, "PartiesDeleteEvent is cancelled, ignoring delete of " + party.getName(), true);
				return true;
			}
			party.getMembers().remove(p.getUniqueId());
			party.remOnlinePlayer(p);
			
			tp.cleanupPlayer(true);
	
			party.sendBroadcastParty(p, Messages.leave_playerleaved);
			tp.sendMessage(Messages.leave_byeplayer.replace("%party%", party.getName()));
			
			party.updateParty();
			plugin.getPartyHandler().tag_removePlayer(p, party);
			
			LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] leaved the party " + party.getName(), true);
		} else
			LogHandler.log(LogLevel.DEBUG, "PartiesLeaveEvent is cancelled, ignoring leave of " + p.getName(), true);
		return true;
	}
}
