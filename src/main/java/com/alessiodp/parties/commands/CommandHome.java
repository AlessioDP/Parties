package com.alessiodp.parties.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitTask;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.Rank;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.PartiesPermissions;
import com.alessiodp.parties.utils.tasks.HomeTask;

public class CommandHome implements CommandInterface {
	private Parties plugin;

	public CommandHome(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getThePlayer(p);
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.HOME.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.HOME.toString()));
			return true;
		}
		if(args.length > 1 && !p.hasPermission(PartiesPermissions.HOME_OTHERS.toString())){
			tp.sendMessage(Messages.home_wrongcmd);
			return true;
		}
		
		Party party = args.length > 1 ? plugin.getPartyHandler().loadParty(args[1]) : plugin.getPartyHandler().loadParty(tp.getPartyName());
		
		if (party == null) {
			if(args.length > 1)
				tp.sendMessage(Messages.home_noexist);
			else
				tp.sendMessage(Messages.noparty);
			return true;
		}
		if(args.length <= 1){
			Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
			if(r != null){
				if(!r.havePermission(PartiesPermissions.PRIVATE_HOME.toString())){
					Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_HOME.toString());
					if(rr!=null)
						tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
					else
						tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_HOME.toString()));
					return true;
				}
			}
		}
		
		if(party.getHome() == null){
			tp.sendMessage(Messages.home_nohome);
			return true;
		}
		if(Variables.vault_enable && Variables.vault_home_price > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())){
			if(Variables.vault_confirm_enable){
				if(tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true){
					if(plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_home_price){
						plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_home_price);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_home_nomoney.replace("%price%", Double.toString(Variables.vault_home_price)));
						tp.putLastCommand(null);
						return true;
					}
				} else {
					String c = commandLabel;
					for(String s : args)
						c = c.concat(" " + s);
					tp.putLastCommand(new Object[]{System.currentTimeMillis(), c, false});
					tp.sendMessage(Messages.vault_confirm_warnonbuy.replace("%cmd%", args[0]).replace("%price%", Double.toString(Variables.vault_home_price)));
					return true;
				}
			} else {
				if (plugin.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUniqueId())) >= Variables.vault_home_price) {
			        plugin.getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), Variables.vault_home_price);
				} else {
					tp.sendMessage(Messages.vault_home_nomoney.replace("%price%", Double.toString(Variables.vault_home_price)));
					return true;
				}
			}
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		int cooldown = Variables.home_cooldown;
		for(PermissionAttachmentInfo pa : p.getEffectivePermissions()){
			String perm = pa.getPermission();
			if(perm.startsWith("parties.home.")){
				String[] splitted = perm.split("\\.");
				try{
					cooldown = Integer.parseInt(splitted[2]);
				}catch(Exception ex){}
			}
		}
		
		if(cooldown > 0){
			tp.setHomeFrom(p.getLocation());
			plugin.getPlayerHandler().addHomeCount();
			BukkitTask it = new HomeTask(plugin, tp, party.getHome()).runTaskLater(plugin, cooldown * 20);
			tp.setHomeTask(it.getTaskId());
			tp.sendMessage(Messages.home_in.replace("%time%", cooldown+"").replace("%price%", Double.toString(Variables.vault_home_price)));
		} else {
			p.teleport(party.getHome());
			tp.sendMessage(Messages.home_teleported.replace("%price%", Double.toString(Variables.vault_home_price)));
		}
		LogHandler.log(2, p.getName() + "[" + p.getUniqueId() + "] used command home of " + party.getHome());
		return true;
	}
}