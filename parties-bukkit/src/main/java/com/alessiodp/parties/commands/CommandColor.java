package com.alessiodp.parties.commands;

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
import com.alessiodp.parties.utils.PlayerUtil;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.interfaces.Color;


public class CommandColor implements CommandInterface {
	private Parties plugin;

	public CommandColor(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.COLOR.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.COLOR.toString()));
			return true;
		}
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		
		if (!PlayerUtil.checkPlayerRankAlerter(tp, PartiesPermissions.PRIVATE_EDIT_COLOR))
			return true;
		
		if (args.length > 2) {
			tp.sendMessage(Messages.color_wrongcmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		if (args.length == 1) {
			// Automatically tp.sendMessage put color placeholders
			if (party.getColor() != null)
				tp.sendMessage(Messages.color_info);
			else
				tp.sendMessage(Messages.color_empty);
			return true;
		}
		if (args[1].equalsIgnoreCase(Variables.command_sub_remove) && args.length == 2) {
			party.setColor(null);
			party.updateParty();
			
			party.sendBroadcastParty(p, Messages.color_removed);
			plugin.getPartyHandler().tag_refresh(party);
			
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] removed color of the party " + party.getName(), true);
			return true;
		}
		Color pc = plugin.getPartyHandler().searchColorByCommand(args[1]);
		if (pc == null) {
			// Color doesn't exist
			tp.sendMessage(Messages.color_wrongcolor);
			return true;
		}
		
		double commandPrice = Variables.vault_command_color_price;
		if (Variables.vault_enable && commandPrice > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())) {
			OfflinePlayer buyer = Bukkit.getOfflinePlayer(p.getUniqueId());
			if (Variables.vault_confirm_enable) {
				if (tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true) {
					if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
						plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_prefix_nomoney
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
					tp.sendMessage(Messages.vault_prefix_nomoney
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
		party.setColor(pc);
		party.updateParty();
		plugin.getPartyHandler().tag_refresh(party);
		
		party.sendBroadcastParty(p, Messages.color_changed
				.replace("%color_name%", pc.getName())
				.replace("%color_code%", pc.getCode())
				.replace("%price%", Double.toString(commandPrice)));
		
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] set color of the party " + party.getName(), true);
		return true;
	}
}
