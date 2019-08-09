package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.HashUtils;
import lombok.Getter;

public class CommandJoin extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandJoin(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.JOIN.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.JOIN);
			return false;
		}
		
		if (!partyPlayer.getPartyName().isEmpty()) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
			return false;
		}
		
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
			sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_WRONGCMD);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		commandData.addPermission(PartiesPermission.ADMIN_JOIN_BYPASS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		String partyName = commandData.getArgs()[1];
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyName);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", partyName));
			return;
		}
		
		if (commandData.getArgs().length == 2) {
			if (!commandData.havePermission(PartiesPermission.ADMIN_JOIN_BYPASS)
					&& !party.getPassword().isEmpty()) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_WRONGPASSWORD);
				return;
			}
		} else {
			if (!HashUtils.hashText(commandData.getArgs()[2]).equals(party.getPassword())) {
				sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_WRONGPASSWORD);
				return;
			}
		}
		
		if ((ConfigParties.GENERAL_MEMBERSLIMIT != -1)
				&& (party.getMembers().size() >= ConfigParties.GENERAL_MEMBERSLIMIT)) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYFULL);
			return;
		}
		
		if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.JOIN, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
			return;
		
		// Command starts
		
		// Calling API Event
		IPlayerPreJoinEvent partiesPreJoinEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPreJoinEvent(partyPlayer, party, false, null);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreJoinEvent);
		
		if (!partiesPreJoinEvent.isCancelled()) {
			
			party.getMembers().add(partyPlayer.getPlayerUUID());
			party.addMember(partyPlayer);
			
			partyPlayer.setPartyName(party.getName());
			partyPlayer.setRank(ConfigParties.RANK_SET_DEFAULT);
					
			party.updateParty();
			partyPlayer.updatePlayer();
					
			party.callChange();
			
			sendMessage(sender, partyPlayer, Messages.ADDCMD_JOIN_JOINED);
			
			party.broadcastMessage(Messages.ADDCMD_JOIN_PLAYERJOINED, partyPlayer);
			
			IPlayerPostJoinEvent partiesPostJoinEvent = ((PartiesPlugin) plugin).getEventManager().preparePlayerPostJoinEvent(partyPlayer, party, false, null);
			((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostJoinEvent);
			
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_JOIN
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		} else
			plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_JOINEVENT_DENY
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
	}
}
