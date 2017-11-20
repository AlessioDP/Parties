package com.alessiodp.parties.commands;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
import com.alessiodp.partiesapi.interfaces.Rank;

public class CommandMotd implements CommandInterface {
	private Parties plugin;

	public CommandMotd(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());

		if (!p.hasPermission(PartiesPermissions.MOTD.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.MOTD.toString()));
			return true;
		}
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if (r != null && !p.hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(PartiesPermissions.PRIVATE_EDIT_MOTD.toString())) {
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_EDIT_MOTD.toString());
				if (rr != null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_EDIT_MOTD.toString()));
				return true;
			}
		}
		if (args.length < 2) {
			tp.sendMessage(Messages.motd_wrongcmd);
			return true;
		}
		if (args[1].equalsIgnoreCase(Variables.motd_removeword)) {
			party.setMOTD("");
			party.updateParty();

			party.sendBroadcastParty(p, Messages.motd_removed);
			
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] removed description of the party " + party.getName(), true);
			return true;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int word = 1; word < args.length; word++) {
			if (sb.length() > 0)
				sb.append(" ");
			sb.append(args[word]);
		}
		if (!Pattern.compile(Variables.motd_allowedchars).matcher(sb.toString()).matches()
				|| (sb.toString().length() > Variables.motd_max)
				|| (sb.toString().length() < Variables.motd_min)) {
			tp.sendMessage(Messages.motd_invalidchars);
			return true;
		}
		for (String cen : Variables.motd_censored) {
			if (sb.toString().toLowerCase().contains(cen.toLowerCase())) {
				tp.sendMessage(Messages.motd_censored);
				return true;
			}
		}
		
		double commandPrice = Variables.vault_command_motd_price;
		if (Variables.vault_enable && commandPrice > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())) {
			OfflinePlayer buyer = Bukkit.getOfflinePlayer(p.getUniqueId());
			if (Variables.vault_confirm_enable) {
				if (tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true) {
					if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
						plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_motd_nomoney
								.replace("%price%", Double.toString(commandPrice)));
						tp.putLastCommand(null);
						return true;
					}
				} else {
					String c = commandLabel;
					for (String s : args)
						c = c.concat(" " + s);
					tp.putLastCommand(new Object[]{System.currentTimeMillis(), c, false});
					tp.sendMessage(Messages.vault_confirm_warnonbuy
							.replace("%cmd%", args[0])
							.replace("%price%", Double.toString(commandPrice)));
					return true;
				}
			} else {
				if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
					plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
				} else {
					tp.sendMessage(Messages.vault_motd_nomoney
							.replace("%price%", Double.toString(commandPrice)));
					return true;
				}
			}
		}
		/*
		 * 
		 * 
		 * 
		 */
		party.setMOTD(sb.toString());
		party.updateParty();
		
		party.sendBroadcastParty(p, Messages.motd_changed
				.replace("%motd%", sb.toString())
				.replace("%price%", Double.toString(commandPrice)));
		
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] set description of the party " + party.getName(), true);
		return true;
	}
}