package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandDeny implements ICommand {
	private Parties plugin;
	 
	public CommandDeny(Parties parties) {
		plugin = parties;
	}

	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.DENY.toString())) {
			pp.sendNoPermission(PartiesPermission.DENY);
			return false;
		}
		
		if (pp.getLastInvite().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_DENY_NOINVITE);
			return false;
		}
		
		PartyEntity party = plugin.getPartyManager().getParty(pp.getLastInvite());
		if (party == null) {
			pp.sendMessage(Messages.MAINCMD_DENY_NOEXISTS);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		PartyEntity party = commandData.getParty();

		/*
		 * Command starts
		 */
		party.denyInvite(pp.getPlayerUUID());
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_DENY
				.replace("{player}", pp.getName())
				.replace("{party}", party.getName()), true);
	}
}
