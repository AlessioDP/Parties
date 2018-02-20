package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandAccept implements ICommand {
	private Parties plugin;
	 
	public CommandAccept(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.ACCEPT.toString())) {
			pp.sendNoPermission(PartiesPermission.ACCEPT);
			return;
		}
		if (!pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_ACCEPT_ALREADYINPARTY);
			return;
		}
		if (pp.getLastInvite().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_ACCEPT_NOINVITE);
			return;
		}
		
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
		party.acceptInvite(p.getUniqueId());
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_ACCEPT
				.replace("{player}", p.getName())
				.replace("{party}", party.getName()), true);
	}
}
