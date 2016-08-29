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
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;

public class CommandDesc implements CommandInterface {
	private Parties plugin;

	public CommandDesc(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);

		if (!p.hasPermission(PartiesPermissions.DESC.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.DESC.toString()));
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
			if(!r.havePermission(PartiesPermissions.PRIVATE_EDIT_DESC.toString())){
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_EDIT_DESC.toString());
				if(rr!=null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_EDIT_DESC.toString()));
				return true;
			}
		}
		if (args.length < 2) {
			tp.sendMessage(Messages.desc_wrongcmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		if (args[1].equalsIgnoreCase(Variables.desc_removeword)) {
			party.setDescription("");
			party.updateParty();

			party.sendBroadcastParty(p, Messages.desc_removed);
			party.sendSpyMessage(p, Messages.desc_removed);
			LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] removed description " + party.getName());
			return true;
		}
		
		StringBuilder sb = new StringBuilder();
		for(int word=1;word<args.length;word++){
			if(sb.length() > 1)
				sb.append(" ");
			sb.append(args[word]);
		}
		if (!Pattern.compile(Variables.desc_allowedchars).matcher(sb.toString()).matches()
				|| (sb.toString().length() > Variables.desc_max)
				|| (sb.toString().length() < Variables.desc_min)) {
			tp.sendMessage(Messages.desc_invalidchars);
			return true;
		}
		for (String cen : Variables.desc_censored) {
			if (sb.toString().toLowerCase().contains(cen.toLowerCase())) {
				tp.sendMessage(Messages.desc_censored);
				return true;
			}
		}
		if(Variables.vault_enable && Variables.vault_desc_price > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())){
			if(Variables.vault_confirm_enable){
				if(tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true){
					if(plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_desc_price){
						plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_create_price);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_desc_nomoney.replace("%price%", Double.toString(Variables.vault_desc_price)));
						tp.putLastCommand(null);
						return true;
					}
				} else {
					String c = commandLabel;
					for(String s : args)
						c = c.concat(" " + s);
					tp.putLastCommand(new Object[]{System.currentTimeMillis(), c, false});
					tp.sendMessage(Messages.vault_confirm_warnonbuy.replace("%cmd%", args[0]).replace("%price%", Double.toString(Variables.vault_desc_price)));
					return true;
				}
			} else {
				if (plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_desc_price) {
			        plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_desc_price);
				} else {
					tp.sendMessage(Messages.vault_desc_nomoney.replace("%price%", Double.toString(Variables.vault_desc_price)));
					return true;
				}
			}
		}
		/*
		 * 
		 */
		party.setDescription(sb.toString());
		party.updateParty();
		
		party.sendBroadcastParty(p, Messages.desc_changed.replace("%price%", Double.toString(Variables.vault_desc_price)));
		party.sendSpyMessage(p, Messages.desc_changed.replace("%price%", Double.toString(Variables.vault_desc_price)));
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] changed description of " + party.getName());
		return true;
	}
}