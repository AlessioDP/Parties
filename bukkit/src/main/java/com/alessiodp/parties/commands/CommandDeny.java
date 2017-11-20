package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandDeny implements CommandInterface {
	private Parties plugin;
	 
	public CommandDeny(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.DENY.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.DENY.toString()));
			return true;
		}
		if (tp.getInvite().isEmpty()) {
			tp.sendMessage(Messages.deny_noinvite);
			return true;
		}
		Party party = plugin.getPartyHandler().getParty(tp.getInvite());
		if (party == null) {
			tp.sendMessage(Messages.deny_noexist);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		party.denyInvite(p.getUniqueId());
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] denied invite for " + party.getName(), true);
		return true;
	}
}
