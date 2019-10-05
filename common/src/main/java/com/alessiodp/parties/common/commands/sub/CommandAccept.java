package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommandAccept extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandAccept(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.ACCEPT.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.ACCEPT);
			return false;
		}
		
		if (!partyPlayer.getPartyName().isEmpty()) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_ALREADYINPARTY);
			return false;
		}
		
		if (commandData.getArgs().length > 2) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_WRONGCMD);
			return false;
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		HashMap<String, PartyImpl> parties = new HashMap<>();
		for (PartyImpl party : partyPlayer.getPartyInvites().keySet()) {
			parties.put(party.getName().toLowerCase(Locale.ENGLISH), party);
		}
		
		if (commandData.getArgs().length > 1
			&& !parties.containsKey(commandData.getArgs()[1].toLowerCase(Locale.ENGLISH))) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOEXISTS);
			return;
		}
		
		PartyImpl party;
		if (parties.size() > 0) {
			if (parties.size() == 1) {
				party = parties.values().iterator().next();
			} else if (commandData.getArgs().length > 1) {
				party = parties.get(commandData.getArgs()[1].toLowerCase(Locale.ENGLISH));
			} else {
				// Missing party
				sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEINVITES);
				for (Map.Entry<String, PartyImpl> entry : parties.entrySet()) {
					sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_MULTIPLEINVITES_PARTY
							.replace("%party%", entry.getKey()), entry.getValue());
				}
				return;
			}
		} else {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOINVITE);
			return;
		}
		
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.MAINCMD_ACCEPT_NOEXISTS);
			return;
		}
		
		// Command starts
		party.acceptInvite(partyPlayer.getPlayerUUID());
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_ACCEPT
				.replace("{player}", partyPlayer.getName())
				.replace("{party}", party.getName()), true);
	}
}
