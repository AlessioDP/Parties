package com.alessiodp.parties.commands;

import java.util.UUID;
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
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandCreate implements CommandInterface {
	private Parties plugin;

	public CommandCreate(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.CREATE.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.CREATE.toString()));
			return true;
		}
		if (tp.haveParty()) {
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
			for (String cen : Variables.censor_contains) {
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().contains(cen.toLowerCase())) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				} else {
					if (partyName.contains(cen)) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				}
			}
			for (String cen : Variables.censor_startwith) {
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().startsWith(cen.toLowerCase())) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				} else {
					if (partyName.startsWith(cen)) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				}
			}
			for (String cen : Variables.censor_endwith) {
				if (!Variables.censor_casesensitive) {
					if (partyName.toLowerCase().endsWith(cen.toLowerCase())) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				} else {
					if (partyName.endsWith(cen)) {
						tp.sendMessage(Messages.create_censoredname);
						return true;
					}
				}
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
		if(Variables.fixedparty && args.length > 2 && args[2].equalsIgnoreCase(Variables.command_sub_fixed)){
			if(p.hasPermission(PartiesPermissions.ADMIN_CREATE_FIXED.toString())){
				fixed = true;
			}
		}
		if(Variables.vault_enable && Variables.vault_create_price > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())){
			if(Variables.vault_confirm_enable){
				if(tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true){
					if(plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_create_price){
						plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_create_price);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_create_nomoney.replace("%price%", Double.toString(Variables.vault_create_price)));
						tp.putLastCommand(null);
						return true;
					}
				} else {
					String c = commandLabel;
					for(String s : args)
						c = c.concat(" " + s);
					tp.putLastCommand(new Object[]{System.currentTimeMillis(), c, false});
					tp.sendMessage(Messages.vault_confirm_warnonbuy.replace("%cmd%", args[0]).replace("%price%", Double.toString(Variables.vault_create_price)));
					return true;
				}
			} else {
				if (plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_create_price) {
			        plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_create_price);
				} else {
					tp.sendMessage(Messages.vault_create_nomoney.replace("%price%", Double.toString(Variables.vault_create_price)));
					return true;
				}
			}
		}
		/*
		 * 
		 * 
		 * 
		 */
		if(fixed && Variables.fixedparty){
			// Fixed creation
			Party party = new Party(partyName, plugin);
			party.setLeader(UUID.fromString("00000000-0000-0000-0000-000000000000"));
			party.setFixed(true);
			plugin.getPartyHandler().listParty.put(party.getName(), party);
			party.updateParty();
			
			tp.sendMessage(Messages.create_partycreated_fixed.replace("%price%", Double.toString(Variables.vault_create_price)), party);
			party.sendSpyMessage(p, Messages.create_partycreated_fixed.replace("%price%", Double.toString(Variables.vault_create_price)));
			LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] created fixed party " + party.getName());
			return true;
		}
		Party party = new Party(partyName, plugin);
		party.getMembers().add(p.getUniqueId());
		party.getOnlinePlayers().add(p);
		plugin.getPartyHandler().listParty.put(party.getName(), party);
		
		tp.setRank(Variables.rank_last);
		
		tp.setHaveParty(true);
		tp.setPartyName(partyName);

		party.setLeader(p.getUniqueId());
		party.updateParty();
		tp.updatePlayer();

		plugin.getPartyHandler().tag_refresh(party);

		tp.sendMessage(Messages.create_partycreated.replace("%price%", Double.toString(Variables.vault_create_price)), party);
		party.sendSpyMessage(p, Messages.create_partycreated.replace("%price%", Double.toString(Variables.vault_create_price)));
		LogHandler.log(1, p.getName() + "[" + p.getUniqueId() + "] created party " + party.getName());
		return true;
	}
}
