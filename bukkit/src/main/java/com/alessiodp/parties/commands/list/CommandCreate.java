package com.alessiodp.parties.commands.list;

import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
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
	
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.CREATE.toString())) {
			pp.sendNoPermission(PartiesPermission.CREATE);
			return;
		}
		
		if (!pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_CREATE_ALREADYINPARTY);
			return;
		}
		
		if (args.length < 2) {
			pp.sendMessage(Messages.MAINCMD_CREATE_WRONGCMD);
			return;
		}
		
		/*
		 * Command handling
		 */
		String partyName = args[1];
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
				&& args.length > 2
				&& args[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_FIXED)) {
			if (p.hasPermission(PartiesPermission.ADMIN_CREATE_FIXED.toString())) {
				isFixed = true;
			}
		}
		
		if (VaultHandler.payCommand(VaultHandler.VaultCommand.CREATE, pp, commandLabel, args))
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
						.replace("{player}", p.getName())
						.replace("{party}", party.getName()), true);
			} else {
				// Normal creation
				party = new PartyEntity(newPartyName, plugin);
				party.getMembers().add(pp.getPlayerUUID());
				if (pp.getPlayer() != null)
					party.getOnlinePlayers().add(pp.getPlayer());
				plugin.getPartyManager().getListParties().put(party.getName().toLowerCase(), party);
				
				pp.setRank(ConfigParties.RANK_SET_HIGHER);
				
				pp.setPartyName(party.getName());
		
				party.setLeader(pp.getPlayerUUID());
				party.updateParty();
				pp.updatePlayer();
		
				party.callChange();
		
				pp.sendMessage(Messages.MAINCMD_CREATE_CREATED, party);
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_CREATE
						.replace("{player}", p.getName())
						.replace("{party}", party.getName()), true);
			}
			// Calling API event
			PartiesPartyPostCreateEvent partiesPostEvent = new PartiesPartyPostCreateEvent(pp, party);
			Bukkit.getServer().getPluginManager().callEvent(partiesPostEvent);
		} else {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_CREATEEVENT_DENY
					.replace("{party}", partyName)
					.replace("{player}", p.getName()), true);
		}
	}
}
