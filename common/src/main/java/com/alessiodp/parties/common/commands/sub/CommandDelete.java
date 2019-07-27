package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.enums.DeleteCause;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommandDelete extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = true;
	
	public CommandDelete(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(PartiesPermission.ADMIN_DELETE.toString())) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.ADMIN_DELETE);
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(PartiesPermission.ADMIN_DELETE_SILENT);
		}
		
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
			sender.sendMessage(Messages.MAINCMD_DELETE_WRONGCMD, true);
			return false;
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND.replace("%party%", commandData.getArgs()[1]));
			return;
		}
		
		
		boolean isSilent = false;
		if (commandData.getArgs().length == 3) {
			if (commandData.havePermission(PartiesPermission.ADMIN_DELETE_SILENT)
					&& commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_SILENT)) {
				isSilent = true;
			} else {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_DELETE_WRONGCMD);
				return;
			}
		}
		
		// Command starts
		// Calling Pre API event
		IPartyPreDeleteEvent partiesPreDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.DELETE, null, partyPlayer);
		((PartiesPlugin) plugin).getEventManager().callEvent(partiesPreDeleteEvent);
		
		if (!partiesPreDeleteEvent.isCancelled()) {
			if (isSilent) {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_DELETE_DELETEDSILENTLY, party);
			} else {
				sendMessage(sender, partyPlayer, Messages.MAINCMD_DELETE_DELETED, party);
				party.broadcastMessage(Messages.MAINCMD_DELETE_BROADCAST, partyPlayer);
			}
			
			party.delete();
			
			// Calling Post API event
			IPartyPostDeleteEvent partiesPostDeleteEvent = ((PartiesPlugin) plugin).getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.DELETE, null, partyPlayer);
			((PartiesPlugin) plugin).getEventManager().callEvent(partiesPostDeleteEvent);
			
			plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_DELETE
					.replace("{player}", sender.getName())
					.replace("{party}", party.getName()), true);
		} else {
			plugin.getLoggerManager().log(PartiesConstants.DEBUG_API_DELETEEVENT_DENY
					.replace("{party}", party.getName())
					.replace("{player}", sender.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 3 && sender.hasPermission(PartiesPermission.ADMIN_DELETE_SILENT.toString())) {
			ret.add(ConfigMain.COMMANDS_SUB_SILENT);
			if (!args[2].isEmpty()) {
				ret = plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[2]);
			}
		}
		return ret;
	}
}
