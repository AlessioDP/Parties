package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

public class CommandDeny extends AbstractCommand {
	
	public CommandDeny(PartiesPlugin instance) {
		super(instance);
	}

	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.DENY.toString())) {
			pp.sendNoPermission(PartiesPermission.DENY);
			return false;
		}
		
		if (pp.getLastInvite().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_DENY_NOINVITE);
			return false;
		}
		
		PartyImpl party = plugin.getPartyManager().getParty(pp.getLastInvite());
		if (party == null) {
			pp.sendMessage(Messages.MAINCMD_DENY_NOEXISTS);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();

		/*
		 * Command starts
		 */
		party.denyInvite(pp.getPlayerUUID());
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_DENY
				.replace("{player}", pp.getName())
				.replace("{party}", party.getName()), true);
	}
}
