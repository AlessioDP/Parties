package com.alessiodp.parties.commands;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.events.PartiesPartyRenameEvent;
import com.alessiodp.partiesapi.interfaces.Rank;

public class CommandRename implements CommandInterface {
	private Parties plugin;
	 
	public CommandRename(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.RENAME.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.RENAME.toString()));
			return true;
		}
		Party party = null;
		int type = -1;
		if (args.length == 2) {
			// Own party
			if (!tp.getPartyName().isEmpty())
				party = plugin.getPartyHandler().getParty(tp.getPartyName());
			type = 1;
		} else if (args.length == 3) {
			// Another party
			if (p.hasPermission(PartiesPermissions.RENAME_OTHERS.toString())) {
				party = plugin.getPartyHandler().getParty(args[1]);
				type = 2;
			}
		}
		
		if (party == null) {
			switch (type) {
			case 1:
				// No party
				tp.sendMessage(Messages.noparty);
				break;
			case 2:
				// Party doesn't exist
				tp.sendMessage(Messages.rename_noexist.replace("%party%", args[1]));
				break;
			default:
				// Wrong command
				if (p.hasPermission(PartiesPermissions.RENAME_OTHERS.toString()))
					tp.sendMessage(Messages.rename_wrongcmd_admin);
				else
					tp.sendMessage(Messages.rename_wrongcmd);
			}
			return true;
		}
		
		if (type == 1) {
			Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
			if (r != null && !p.hasPermission(PartiesPermissions.RENAME_OTHERS.toString()) && !p.hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
				if (!r.havePermission(PartiesPermissions.PRIVATE_ADMIN_RENAME.toString())) {
					Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_ADMIN_RENAME.toString());
					if (rr != null)
						tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
					else
						tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_ADMIN_RENAME.toString()));
					return true;
				}
			}
		}
		/*
		 * 
		 * 
		 * 
		 */
		String partyName = args[type]; // type == 1 ? args[1] : args[2]
		
		if (partyName.length() > Variables.party_maxlengthname) {
			tp.sendMessage(Messages.create_toolongname);
			return true;
		}
		if (partyName.length() < Variables.party_minlengthname) {
			tp.sendMessage(Messages.create_tooshortname);
			return true;
		}
		if (Variables.censor_enable) {
			boolean censored = false;
			for (String cen : Variables.censor_contains) {
				// Contains
				if (censored)
					break;
				
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().contains(cen.toLowerCase()))
						censored = true;
				} else if (partyName.contains(cen))
					censored = true;
			}
			for (String cen : Variables.censor_startswith) {
				// Starts with
				if (censored)
					break;
				
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().startsWith(cen.toLowerCase()))
						censored = true;
				} else if (partyName.startsWith(cen))
					censored = true;
			}
			for (String cen : Variables.censor_endswith) {
				// Ends with
				if (censored)
					break;
				
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().endsWith(cen.toLowerCase()))
						censored = true;
				} else if (partyName.endsWith(cen))
					censored = true;
			}
			if (censored) {
				tp.sendMessage(Messages.create_censoredname);
				return true;
			}
		}
		if (!Pattern.compile(Variables.party_allowedchars).matcher(partyName).matches()) {
			tp.sendMessage(Messages.create_invalidname);
			return true;
		}
		
		if (plugin.getPartyHandler().existParty(partyName)) {
			tp.sendMessage(Messages.create_alreadyexist.replace("%party%", partyName));
			return true;
		}
		/*
		 * 
		 */
		String oldPartyName = party.getName();
		// Calling API event
		PartiesPartyRenameEvent partiesRenameEvent = new PartiesPartyRenameEvent(oldPartyName, partyName, p, type == 2 ? true : false);
		Bukkit.getServer().getPluginManager().callEvent(partiesRenameEvent);
		partyName = partiesRenameEvent.getNewPartyName();
		if (!partiesRenameEvent.isCancelled()) {
			if (!plugin.getDatabaseType().isNone()) {
				plugin.getDataHandler().renameParty(oldPartyName, partyName);
			}
			for (Player player : party.getOnlinePlayers()) {
				plugin.getPlayerHandler().getPlayer(player.getUniqueId()).setPartyName(partyName);
			}
			plugin.getPartyHandler().tag_delete(party);
			party.setName(partyName);
			plugin.getPartyHandler().getListParties().remove(oldPartyName.toLowerCase());
			plugin.getPartyHandler().getListParties().put(partyName.toLowerCase(), party);
			plugin.getPartyHandler().tag_refresh(party);
			
			tp.sendMessage(Messages.rename_renamed
					.replace("%old%", oldPartyName)
					.replace("%party%", partyName));
			party.sendBroadcastParty(p, Messages.rename_broadcast);
			
			LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] renamed the party " + oldPartyName + " in " + partyName, true);
		} else {
			LogHandler.log(LogLevel.DEBUG, "PartiesRenameEvent is cancelled, ignoring rename of " + oldPartyName, true);
		}
		return true;
	}
}