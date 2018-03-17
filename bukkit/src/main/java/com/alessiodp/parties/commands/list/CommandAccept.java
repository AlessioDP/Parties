package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandAccept implements ICommand {
	private Parties plugin;
	 
	public CommandAccept(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.ACCEPT.toString())) {
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
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		PartyEntity party = plugin.getPartyManager().getParty(pp.getLastInvite());
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
