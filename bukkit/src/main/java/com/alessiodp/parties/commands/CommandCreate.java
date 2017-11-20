package com.alessiodp.parties.commands;

import java.util.UUID;
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
import com.alessiodp.partiesapi.events.PartiesPartyPostCreateEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreCreateEvent;

public class CommandCreate implements CommandInterface {
	private Parties plugin;

	public CommandCreate(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.CREATE.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.CREATE.toString()));
			return true;
		}
		if (!tp.getPartyName().isEmpty()) {
			tp.sendMessage(Messages.create_alreadyinparty);
			return true;
		}
		if (args.length < 2) {
			tp.sendMessage(Messages.create_wrongcmd);
			return true;
		}
		String partyName = args[1];
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
		boolean fixed = false;
		if (Variables.fixedparty && args.length > 2 && args[2].equalsIgnoreCase(Variables.command_sub_fixed)) {
			if (p.hasPermission(PartiesPermissions.ADMIN_CREATE_FIXED.toString())) {
				fixed = true;
			}
		}
		double commandPrice = Variables.vault_command_create_price;
		if (Variables.vault_enable && commandPrice > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())) {
			OfflinePlayer buyer = Bukkit.getOfflinePlayer(p.getUniqueId());
			if (Variables.vault_confirm_enable) {
				if (tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true) {
					if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
						plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_create_nomoney
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
					tp.sendMessage(Messages.vault_create_nomoney
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
		boolean isFixed = fixed && Variables.fixedparty ? true : false;
		Party party;
		PartiesPartyPreCreateEvent partiesPreEvent = new PartiesPartyPreCreateEvent(p, partyName, isFixed);
		Bukkit.getServer().getPluginManager().callEvent(partiesPreEvent);
		String newPartyName = partiesPreEvent.getPartyName();
		UUID newLeaderUUID = partiesPreEvent.getLeader();
		boolean newIsFixed = partiesPreEvent.isFixed();
		if (!partiesPreEvent.isCancelled()) {
			if (newIsFixed) {
				// Fixed creation
				party = new Party(newPartyName, plugin);
				party.setLeader(UUID.fromString("00000000-0000-0000-0000-000000000000"));
				party.setFixed(true);
				plugin.getPartyHandler().getListParties().put(party.getName().toLowerCase(), party);
				party.updateParty();
				
				tp.sendMessage(Messages.create_partycreated_fixed
						.replace("%price%", Double.toString(commandPrice)), party);
				
				LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] created fixed party " + party.getName(), true);
			} else {
				ThePlayer newTp = plugin.getPlayerHandler().getPlayer(newLeaderUUID);
				party = new Party(newPartyName, plugin);
				party.getMembers().add(newLeaderUUID);
				if (newTp.getPlayer() != null)
					party.addOnlinePlayer(newTp.getPlayer());
				plugin.getPartyHandler().getListParties().put(party.getName().toLowerCase(), party);
				
				newTp.setRank(Variables.rank_last);
				
				newTp.setPartyName(party.getName());
		
				party.setLeader(newLeaderUUID);
				party.updateParty();
				newTp.updatePlayer();
		
				plugin.getPartyHandler().tag_refresh(party);
		
				newTp.sendMessage(Messages.create_partycreated
						.replace("%price%", Double.toString(commandPrice)), party);
				
				LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] created party " + party.getName(), true);
			}
			// Calling API event
			PartiesPartyPostCreateEvent partiesPostEvent = new PartiesPartyPostCreateEvent(p, party.getName(), newIsFixed);
			Bukkit.getServer().getPluginManager().callEvent(partiesPostEvent);
		} else
			LogHandler.log(LogLevel.DEBUG, "PartiesCreateEvent is cancelled, ignoring creation of " + partyName + " by " + p.getPlayer(), true);
		return true;
	}
}
