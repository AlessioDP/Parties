package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.enums.DeleteCause;

import java.util.ArrayList;
import java.util.List;

public class CommandDelete extends PartiesSubCommand {
	private final String syntaxSilent;
	
	public CommandDelete(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.DELETE,
				PartiesPermission.ADMIN_DELETE,
				ConfigMain.COMMANDS_SUB_DELETE,
				true
		);
		
		syntax = String.format("%s <%s>",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY
		);
		
		syntaxSilent = String.format("%s <%s> [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PARTY,
				ConfigMain.COMMANDS_MISC_SILENT
		);
		
		description = Messages.HELP_MAIN_DESCRIPTIONS_DELETE;
		help = Messages.HELP_MAIN_COMMANDS_DELETE;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (user.hasPermission(PartiesPermission.ADMIN_DELETE_SILENT))
			return syntaxSilent;
		return syntax;
	}
	
	@Override
	public String getConsoleSyntax() {
		return syntaxSilent;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(PartiesPermission.ADMIN_DELETE_SILENT);
		}
		
		if (commandData.getArgs().length < 2 || commandData.getArgs().length > 3) {
			sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
					.replace("%syntax%", getSyntaxForUser(sender)));
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
					&& commandData.getArgs()[2].equalsIgnoreCase(ConfigMain.COMMANDS_MISC_SILENT)) {
				isSilent = true;
			} else {
				sendMessage(sender, ((PartiesCommandData) commandData).getPartyPlayer(), Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntaxForUser(sender)));
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
			
			party.delete(DeleteCause.DELETE, null, partyPlayer);
			
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_DELETE,
					sender.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else {
			plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_DELETEEVENT_DENY, party.getId().toString(), sender.getName(), sender.getUUID().toString()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 3 && sender.hasPermission(PartiesPermission.ADMIN_DELETE_SILENT)) {
			ret.add(ConfigMain.COMMANDS_MISC_SILENT);
			if (!args[2].isEmpty()) {
				ret = plugin.getCommandManager().getCommandUtils().tabCompleteParser(ret, args[2]);
			}
		}
		return ret;
	}
}
