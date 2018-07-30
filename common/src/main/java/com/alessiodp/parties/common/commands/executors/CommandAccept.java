package com.alessiodp.parties.common.commands.executors;

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

public class CommandAccept extends AbstractCommand {
	
	public CommandAccept(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.ACCEPT.toString())) {
			pp.sendNoPermission(PartiesPermission.ACCEPT);
			return false;
		}
		if (!pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_ACCEPT_ALREADYINPARTY);
			return false;
		}
		if (pp.getLastInvite().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_ACCEPT_NOINVITE);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		
		PartyImpl party = plugin.getPartyManager().getParty(pp.getLastInvite());
		if (party == null) {
			pp.sendMessage(Messages.MAINCMD_ACCEPT_NOEXISTS);
			return;
		}
		if ((ConfigParties.GENERAL_MEMBERSLIMIT != -1)
				&& (party.getMembers().size() >= ConfigParties.GENERAL_MEMBERSLIMIT)) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYFULL);
			return;
		}
		
		/*
		 * Command starts
		 */
		party.acceptInvite(pp.getPlayerUUID());
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_ACCEPT
				.replace("{player}", pp.getName())
				.replace("{party}", party.getName()), true);
	}
}
