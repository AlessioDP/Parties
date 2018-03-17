package com.alessiodp.parties.commands.list;

import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.events.PartiesPartyPostCreateEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreCreateEvent;

public class CommandCreate implements ICommand {
	private Parties plugin;
	
	public CommandCreate(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.CREATE.toString())) {
			pp.sendNoPermission(PartiesPermission.CREATE);
			return false;
		}
		
		if (!pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_CREATE_ALREADYINPARTY);
			return false;
		}
		
		if (commandData.getArgs().length < 2) {
			pp.sendMessage(Messages.MAINCMD_CREATE_WRONGCMD);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.ADMIN_CREATE_FIXED);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		String partyName = commandData.getArgs()[1];
		if (partyName.length() > ConfigParties.GENERAL_NAME_MAXLENGTH) {
			pp.sendMessage(Messages.MAINCMD_CREATE_NAMETOOLONG);
			return;
		}
		if (partyName.length() < ConfigParties.GENERAL_NAME_MINLENGTH) {
			pp.sendMessage(Messages.MAINCMD_CREATE_NAMETOOSHORT);
			return;
		}
		
		if (PartiesUtils.checkCensor(partyName)) {
			pp.sendMessage(Messages.MAINCMD_CREATE_CENSORED);
			return;
		}
		if (!Pattern.compile(ConfigParties.GENERAL_NAME_ALLOWEDCHARS).matcher(partyName).matches()) {
			pp.sendMessage(Messages.MAINCMD_CREATE_INVALIDNAME);
			return;
		}
		
		if (plugin.getPartyManager().existParty(partyName)) {
			pp.sendMessage(Messages.MAINCMD_CREATE_NAMEEXISTS.replace("%party%", partyName));
			return;
		}
		
		boolean isFixed = false;
		if (ConfigParties.FIXED_ENABLE
				&& commandData.getArgs().length > 2
				&& commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_FIXED)) {
			if (commandData.havePermission(PartiesPermission.ADMIN_CREATE_FIXED))
				isFixed = true;
		}
		
		if (VaultHandler.payCommand(VaultHandler.VaultCommand.CREATE, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		PartyEntity party;
		
		// Calling Pre API event
		PartiesPartyPreCreateEvent partiesPreEvent = new PartiesPartyPreCreateEvent(pp, partyName, isFixed);
		Bukkit.getServer().getPluginManager().callEvent(partiesPreEvent);
		
		String newPartyName = partiesPreEvent.getPartyName();
		boolean newIsFixed = partiesPreEvent.isFixed();
		if (!partiesPreEvent.isCancelled()) {
			if (newIsFixed) {
				// Fixed creation
				party = new PartyEntity(newPartyName, plugin);
				party.setLeader(UUID.fromString(Constants.FIXED_VALUE_UUID));
				party.setFixed(true);
				plugin.getPartyManager().getListParties().put(party.getName().toLowerCase(), party);
				party.updateParty();
				
				pp.sendMessage(Messages.MAINCMD_CREATE_CREATEDFIXED, party);
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_CREATE_FIXED
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()), true);
			} else {
				// Normal creation
				party = new PartyEntity(newPartyName, plugin);
				
				party.getMembers().add(pp.getPlayerUUID());
				if (pp.getPlayer() != null)
					party.getOnlinePlayers().add(pp.getPlayer());
				party.setLeader(pp.getPlayerUUID());
				plugin.getPartyManager().getListParties().put(party.getName().toLowerCase(), party);
				
				pp.setRank(ConfigParties.RANK_SET_HIGHER);
				pp.setPartyName(party.getName());
				
				party.updateParty();
				pp.updatePlayer();
		
				party.callChange();
		
				pp.sendMessage(Messages.MAINCMD_CREATE_CREATED, party);
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_CREATE
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()), true);
			}
			// Calling API event
			PartiesPartyPostCreateEvent partiesPostEvent = new PartiesPartyPostCreateEvent(pp, party);
			Bukkit.getServer().getPluginManager().callEvent(partiesPostEvent);
		} else {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_CREATEEVENT_DENY
					.replace("{party}", partyName)
					.replace("{player}", pp.getName()), true);
		}
	}
}
