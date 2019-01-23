package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.common.utils.PartiesUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandDelete extends AbstractCommand {
	
	public CommandDelete(PartiesPlugin instance) {
		super(instance);
	}

	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.ADMIN_DELETE.toString())) {
			pp.sendNoPermission(PartiesPermission.ADMIN_DELETE);
			return false;
		}
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
			pp.sendMessage(Messages.MAINCMD_DELETE_WRONGCMD);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.addPermission(PartiesPermission.ADMIN_DELETE_SILENT);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		/*
		 * Command handling
		 */
		PartyImpl party = plugin.getPartyManager().getParty(commandData.getArgs()[1]);
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
		IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.DELETE, null, pp);
		plugin.getEventManager().callEvent(partiesPreDeleteEvent);
		
		if (!partiesPreDeleteEvent.isCancelled()) {
			if (isSilent) {
				pp.sendMessage(Messages.MAINCMD_DELETE_DELETEDSILENTLY, party);
			} else {
				pp.sendMessage(Messages.MAINCMD_DELETE_DELETED, party);
				party.sendBroadcast(pp, Messages.MAINCMD_DELETE_BROADCAST);
			}
			
			party.removeParty();
			
			// Calling Post API event
			IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.DELETE, null, pp);
			plugin.getEventManager().callEvent(partiesPostDeleteEvent);
			
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_DELETE
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		} else {
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", pp.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 3 && sender.hasPermission(PartiesPermission.ADMIN_DELETE_SILENT.toString())) {
			ret.add(ConfigMain.COMMANDS_SUB_SILENT);
			if (!args[2].isEmpty()) {
				ret = PartiesUtils.tabCompleteParser(ret, args[2]);
			}
		}
		return ret;
	}
}
