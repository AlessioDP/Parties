package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesUtils;
import com.alessiodp.parties.api.events.party.PartiesPartyPostCreateEvent;
import com.alessiodp.parties.api.events.party.PartiesPartyPreCreateEvent;

import java.util.regex.Pattern;

public class CommandCreate extends AbstractCommand {
	
	public CommandCreate(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.CREATE.toString())) {
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
		
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.ADMIN_CREATE_FIXED);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
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
		
		if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.CREATE, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		PartyImpl party;
		
		// Calling Pre API event
		PartiesPartyPreCreateEvent partiesPreEvent = plugin.getEventManager().preparePartyPreCreateEvent(pp, partyName, isFixed);
		plugin.getEventManager().callEvent(partiesPreEvent);
		
		String newPartyName = partiesPreEvent.getPartyName();
		boolean newIsFixed = partiesPreEvent.isFixed();
		if (!partiesPreEvent.isCancelled()) {
			party = plugin.getPartyManager().initializeParty(newPartyName);
			party.createParty(newIsFixed ? null : pp);
			party.updateParty();
			
			if (!newIsFixed) {
				pp.setPartyName(party.getName());
				pp.setRank(ConfigParties.RANK_SET_HIGHER);
				pp.updatePlayer();
			}
			plugin.getPartyManager().getListParties().put(party.getName().toLowerCase(), party);
			party.callChange();
			
			if (newIsFixed) {
				pp.sendMessage(Messages.MAINCMD_CREATE_CREATEDFIXED, party);
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_CREATE_FIXED
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()), true);
			} else {
				pp.sendMessage(Messages.MAINCMD_CREATE_CREATED, party);
				
				LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_CREATE
						.replace("{player}", pp.getName())
						.replace("{party}", party.getName()), true);
			}
			
			// Calling API event
			PartiesPartyPostCreateEvent partiesPostEvent = plugin.getEventManager().preparePartyPostCreateEvent(pp, party);
			plugin.getEventManager().callEvent(partiesPostEvent);
		} else {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_CREATEEVENT_DENY
					.replace("{party}", partyName)
					.replace("{player}", pp.getName()), true);
		}
	}
}
