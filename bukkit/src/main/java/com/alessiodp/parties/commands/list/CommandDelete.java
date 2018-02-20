package com.alessiodp.parties.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;

public class CommandDelete implements ICommand {
	private Parties plugin;
	 
	public CommandDelete(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.ADMIN_DELETE.toString())) {
			pp.sendNoPermission(PartiesPermission.ADMIN_DELETE);
			return;
		}
		if (args.length < 2 || args.length > 3) {
			pp.sendMessage(Messages.MAINCMD_DELETE_WRONGCMD);
			return;
		}
		
		/*
		 * Command handling
		 */
		PartyEntity party = plugin.getPartyManager().getParty(args[1]);
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYNOTFOUND.replace("%party%", args[1]));
			return;
		}
		
		
		boolean isSilent = false;
		if (args.length == 3) {
			if (p.hasPermission(PartiesPermission.ADMIN_DELETE_SILENT.toString())
					&& args[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_SILENT)) {
				isSilent = true;
			} else {
				pp.sendMessage(Messages.MAINCMD_DELETE_WRONGCMD);
				return;
			}
		}
		
		/*
		 * Command starts
		 */
		// Calling Pre API event
		PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party, PartiesPartyPreDeleteEvent.DeleteCause.DELETE, null, pp);
		Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
		
		if (!partiesPreDeleteEvent.isCancelled()) {
			if (isSilent) {
				pp.sendMessage(Messages.MAINCMD_DELETE_DELETEDSILENTLY, party);
			} else {
				pp.sendMessage(Messages.MAINCMD_DELETE_DELETED, party);
				party.sendBroadcast(pp, Messages.MAINCMD_DELETE_BROADCAST);
			}
			
			party.removeParty();
			
			// Calling Post API event
			PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.DELETE, null, pp);
			Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
			
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_DELETE
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
		} else {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", p.getName()), true);
		}
	}
}
