package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandDeny implements ICommand {
	private Parties plugin;
	 
	public CommandDeny(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.DENY.toString())) {
			pp.sendNoPermission(PartiesPermission.DENY);
			return;
		}
		
		if (pp.getLastInvite().isEmpty()) {
			pp.sendMessage(Messages.MAINCMD_DENY_NOINVITE);
			return;
		}
		
		PartyEntity party = plugin.getPartyManager().getParty(pp.getLastInvite());
		if (party == null) {
			pp.sendMessage(Messages.MAINCMD_DENY_NOEXISTS);
			return;
		}

		/*
		 * Command starts
		 */
		party.denyInvite(p.getUniqueId());
		
		LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_DENY
				.replace("{player}", p.getName())
				.replace("{party}", party.getName()), true);
	}
}
