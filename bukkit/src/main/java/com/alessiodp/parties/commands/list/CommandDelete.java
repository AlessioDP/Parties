package com.alessiodp.parties.commands.list;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
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

	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.ADMIN_DELETE.toString())) {
			pp.sendNoPermission(PartiesPermission.ADMIN_DELETE);
			return false;
		}
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
			pp.sendMessage(Messages.MAINCMD_DELETE_WRONGCMD);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.ADMIN_DELETE_SILENT);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		PartyEntity party = plugin.getPartyManager().getParty(commandData.getArgs()[1]);
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYNOTFOUND.replace("%party%", commandData.getArgs()[1]));
			return;
		}
		
		
		boolean isSilent = false;
		if (commandData.getArgs().length == 3) {
			if (commandData.havePermission(PartiesPermission.ADMIN_DELETE_SILENT)
					&& commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_SILENT)) {
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
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		} else {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", pp.getName()), true);
		}
	}
}
