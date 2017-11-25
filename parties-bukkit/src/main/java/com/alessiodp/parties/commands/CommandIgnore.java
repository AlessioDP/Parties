package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandIgnore implements CommandInterface {
	private Parties plugin;
	 
	public CommandIgnore(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.IGNORE.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.IGNORE.toString()));
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		if (args.length == 1) {
			StringBuilder builder = new StringBuilder();
			for (String name : tp.getIgnoredParties()) {
				if (builder.length() > 0) {
					builder.append(Messages.ignore_separator);
				}
				builder.append(Messages.ignore_color + name);
			}

			String ignores = builder.toString();
			if (tp.getIgnoredParties().size() == 0 || ignores == null)
				ignores = Messages.ignore_empty;

			tp.sendMessage(Messages.ignore_header
					.replace("%number%", Integer.toString(tp.getIgnoredParties().size())));
			tp.sendMessage(ignores);
			return true;
		}

		if (args.length != 2) {
			tp.sendMessage(Messages.ignore_wrongcmd);
			return true;
		}

		String ignoredParty = args[1];
		
		if (!plugin.getPartyHandler().existParty(ignoredParty)) {
			tp.sendMessage(Messages.ignore_noexist.replace("%party%", ignoredParty));
			return true;
		}
		
		if (tp.getIgnoredParties().contains(ignoredParty)) {
			tp.removeIgnoredParty(ignoredParty);
			tp.sendMessage(Messages.ignore_deignored.replace("%party%", ignoredParty));
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] stopped to ignore party " + ignoredParty, true);
		} else {
			tp.addIgnoredParty(ignoredParty);
			tp.sendMessage(Messages.ignore_ignored.replace("%party%", ignoredParty));
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] started to ignore party " + ignoredParty, true);
		}
		return true;
	}
}
