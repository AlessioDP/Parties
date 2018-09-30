package com.alessiodp.parties.common.commands.executors;

import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
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


public class CommandJoin extends AbstractCommand {
	
	public CommandJoin(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.JOIN.toString())) {
			pp.sendNoPermission(PartiesPermission.JOIN);
			return false;
		}
		
		if (!pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.PARTIES_COMMON_ALREADYINPARTY);
			return false;
		}
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
			pp.sendMessage(Messages.ADDCMD_JOIN_WRONGCMD);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.ADMIN_JOIN_BYPASS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		String partyName = commandData.getArgs()[1];
		PartyImpl party = plugin.getPartyManager().getParty(partyName);
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", partyName));
			return;
		}
		
		if (commandData.getArgs().length == 2) {
			if (!commandData.havePermission(PartiesPermission.ADMIN_JOIN_BYPASS)) {
				if (party.getPassword() != null && !party.getPassword().isEmpty()) {
					pp.sendMessage(Messages.ADDCMD_JOIN_WRONGPASSWORD);
					return;
				}
			}
		} else {
			if (!PartiesUtils.hashText(commandData.getArgs()[2]).equals(party.getPassword())) {
				pp.sendMessage(Messages.ADDCMD_JOIN_WRONGPASSWORD);
				return;
			}
		}
		
		if ((ConfigParties.GENERAL_MEMBERSLIMIT != -1)
				&& (party.getMembers().size() >= ConfigParties.GENERAL_MEMBERSLIMIT)) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYFULL);
			return;
		}
		
		if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.JOIN, pp, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		/*
		 * Command starts
		 */
		// Calling API Event
		IPlayerPreJoinEvent partiesPreJoinEvent = plugin.getEventManager().preparePlayerPreJoinEvent(pp, party, false, null);
		plugin.getEventManager().callEvent(partiesPreJoinEvent);
		
		if (!partiesPreJoinEvent.isCancelled()) {
			party.getMembers().add(pp.getPlayerUUID());
			party.getOnlinePlayers().add(pp);
	
			pp.setPartyName(party.getName());
			pp.setRank(ConfigParties.RANK_SET_DEFAULT);
					
			party.updateParty();
			pp.updatePlayer();
					
			party.callChange();
			
			pp.sendMessage(Messages.ADDCMD_JOIN_JOINED);
			
			party.sendBroadcast(pp, Messages.ADDCMD_JOIN_PLAYERJOINED);
			
			IPlayerPostJoinEvent partiesPostJoinEvent = plugin.getEventManager().preparePlayerPostJoinEvent(pp, party, false, null);
			plugin.getEventManager().callEvent(partiesPostJoinEvent);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_JOIN
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_JOINEVENT_DENY
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
	}
}
