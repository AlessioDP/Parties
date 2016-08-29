package com.alessiodp.parties.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandSetHome implements CommandInterface {
	private Parties plugin;

	public CommandSetHome(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.SETHOME.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.SETHOME.toString()));
			return true;
		}
		if (!tp.haveParty()) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Party party = plugin.getPartyHandler().loadParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if(r != null){
			if(!r.havePermission(PartiesPermissions.PRIVATE_EDIT_HOME.toString())){
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_EDIT_HOME.toString());
				if(rr!=null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_EDIT_HOME.toString()));
				return true;
			}
		}
		if (args.length > 1) {
			tp.sendMessage(Messages.sethome_wrongcmd);
			return true;
		}
		if(Variables.vault_enable && Variables.vault_sethome_price > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())){
			if(Variables.vault_confirm_enable){
				if(tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true){
					if(plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_sethome_price){
						plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_sethome_price);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_sethome_nomoney.replace("%price%", Double.toString(Variables.vault_sethome_price)));
						tp.putLastCommand(null);
						return true;
					}
				} else {
					String c = commandLabel;
					for(String s : args)
						c = c.concat(" " + s);
					tp.putLastCommand(new Object[]{System.currentTimeMillis(), c, false});
					tp.sendMessage(Messages.vault_confirm_warnonbuy.replace("%cmd%", args[0]).replace("%price%", Double.toString(Variables.vault_sethome_price)));
					return true;
				}
			} else {
				if (plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_sethome_price) {
			        plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_sethome_price);
				} else {
					tp.sendMessage(Messages.vault_sethome_nomoney.replace("%price%", Double.toString(Variables.vault_sethome_price)));
					return true;
				}
			}
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		party.setHome(p.getLocation());
		party.updateParty();

		party.sendBroadcastParty(p, Messages.sethome_setted.replace("%price%", Double.toString(Variables.vault_sethome_price)));
		party.sendSpyMessage(p, Messages.sethome_setted.replace("%price%", Double.toString(Variables.vault_sethome_price)));
		
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] setted the home of the party " + party.getName());
		return true;
	}
}