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
import com.alessiodp.parties.utils.tasks.TeleportTask;

public class CommandTeleport implements CommandInterface {
	private Parties plugin;

	public CommandTeleport(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.TELEPORT.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.TELEPORT.toString()));
			return true;
		}
		
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		
		if (!PlayerUtil.checkPlayerRankAlerter(tp, PartiesPermissions.PRIVATE_ADMIN_TELEPORT))
			return true;
		
		long unixNow = -1;
		if (Variables.teleport_delay > 0 && !PlayerUtil.checkPlayerRank(tp, PartiesPermissions.PRIVATE_BYPASSCOOLDOWN)) {
			Long unixTimestamp = plugin.getPlayerHandler().getTeleportCooldown().get(p.getUniqueId());
			unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				tp.sendMessage(Messages.teleport_delay.replace("%seconds%", String.valueOf(Variables.teleport_delay - (unixNow - unixTimestamp))));
				return true;
			}
		}
		
		double commandPrice = Variables.vault_command_teleport_price;
		if (Variables.vault_enable && commandPrice > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())) {
			OfflinePlayer buyer = Bukkit.getOfflinePlayer(p.getUniqueId());
			if (Variables.vault_confirm_enable) {
				if (tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true) {
					if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
						plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_teleport_nomoney
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
					tp.sendMessage(Messages.vault_teleport_nomoney
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
		
		if (unixNow != -1) {
			plugin.getPlayerHandler().getTeleportCooldown().put(p.getUniqueId(), unixNow);
			new TeleportTask(p.getUniqueId(), plugin.getPlayerHandler()).runTaskLater(plugin, Variables.teleport_delay * 20);
			LogHandler.log(LogLevel.DEBUG, "Started TeleportTask for "+(Variables.teleport_delay*20)+" by "+p.getName()+ "["+p.getUniqueId() + "]", true);
		}
		
		tp.sendMessage(Messages.teleport_teleporting);
		for (Player pl : party.getOnlinePlayers()) {
			if (!pl.getUniqueId().equals(p.getUniqueId())) {
				pl.teleport(p.getLocation());
				plugin.getPlayerHandler().getPlayer(pl.getUniqueId()).sendMessage(Messages.teleport_playerteleported, p);
			}
		}
		
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] used command teleport", true);
		return true;
	}
}
