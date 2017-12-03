package com.alessiodp.parties.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import com.alessiodp.parties.utils.addon.GriefPreventionHandler.Result;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.interfaces.Rank;

public class CommandClaim implements CommandInterface {
	private Parties plugin;
	 
	public CommandClaim(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (plugin.getGriefPrevention() == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Messages.invalidcommand));
			return false;
		}
		if (!p.hasPermission(PartiesPermissions.CLAIM.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.CLAIM.toString()));
			return true;
		}
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if (r != null && !p.hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(PartiesPermissions.PRIVATE_CLAIM.toString())) {
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_CLAIM.toString());
				if (rr != null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_CLAIM.toString()));
				return true;
			}
		}
		if (args.length != 2) {
			tp.sendMessage(Messages.claim_wrongcmd);
			return true;
		}
		
		// Checking if command it's okay
		// -1, 1, 2 = Something gone wrong
		// 10, 11, 12, 13 = The command it's okay
		Selection selection = Selection.FAILED_GENERAL;
		if (args[1].equalsIgnoreCase(Variables.griefprevention_command_trust))
			selection = Selection.TRUST;
		else if (args[1].equalsIgnoreCase(Variables.griefprevention_command_container))
			selection = Selection.CONTAINER;
		else if (args[1].equalsIgnoreCase(Variables.griefprevention_command_access))
			selection = Selection.ACCESS;
		else if (args[1].equalsIgnoreCase(Variables.griefprevention_command_remove))
			selection = Selection.REMOVE;
		if (!selection.equals(Selection.FAILED_GENERAL)) {
			Result res = plugin.getGriefPrevention().isManager(p);
			switch (res) {
			case NOEXIST:
				selection = Selection.FAILED_NOEXIST;
				break;
			case NOMANAGER:
				selection = Selection.FAILED_NOMANAGER;
				break;
			default:
				// Success, selection it's okay
				break;
			}
		}
		
		double commandPrice = Variables.vault_command_claim_price;
		if (!selection.isFailed() && Variables.vault_enable && commandPrice > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())) {
			OfflinePlayer buyer = Bukkit.getOfflinePlayer(p.getUniqueId());
			if (Variables.vault_confirm_enable) {
				if (tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true) {
					if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
						plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_claim_nomoney
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
					tp.sendMessage(Messages.vault_claim_nomoney
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
		switch (selection) {
		case TRUST:
			// Trust
			plugin.getGriefPrevention().addPartyTrust(p, party);
			break;
		case CONTAINER:
			// Container
			plugin.getGriefPrevention().addPartyContainer(p, party);
			break;
		case ACCESS:
			// Access
			plugin.getGriefPrevention().addPartyAccess(p, party);
			break;
		case REMOVE:
			// Remove
			plugin.getGriefPrevention().dropParty(p, party);
			break;
		case FAILED_NOEXIST:
			// Return: No exist claim
			tp.sendMessage(Messages.claim_noexistclaim);
			break;
		case FAILED_NOMANAGER:
			// Return: No manager
			tp.sendMessage(Messages.claim_nomanager);
			break;
		case FAILED_GENERAL:
			// Return: Wrong command
			tp.sendMessage(Messages.claim_wrongcmd);
		}

		if (!selection.isFailed()) {
			tp.sendMessage(Messages.claim_done
					.replace("%price%", Double.toString(commandPrice)));
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] used command party claim " + args[1].toLowerCase(), true);
		}
		return true;
	}
	
	
	
	private enum Selection {
		TRUST, CONTAINER, ACCESS, REMOVE, FAILED_GENERAL, FAILED_NOEXIST, FAILED_NOMANAGER;
		
		public boolean isFailed() {
			boolean ret;
			switch (this) {
			case FAILED_GENERAL:
			case FAILED_NOEXIST:
			case FAILED_NOMANAGER:
				ret = true;
				break;
			default:
				ret = false;
			}
			
			return ret;
		}
	}
}
