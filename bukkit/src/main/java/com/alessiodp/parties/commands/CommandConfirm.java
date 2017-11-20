package com.alessiodp.parties.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;

public class CommandConfirm implements CommandInterface {
	private Parties plugin;
	 
	public CommandConfirm(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (args.length > 1) {
			tp.sendMessage(Messages.vault_confirm_wrongcmd);
			return true;
		}
		Object[] packet = tp.getLastCommand();
		if (packet == null) {
			tp.sendMessage(Messages.vault_confirm_anycmd);
			return true;
		}
		if ((System.currentTimeMillis() - (long)packet[0]) > Variables.vault_confirm_timeout) {
			tp.sendMessage(Messages.vault_confirm_anycmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		packet[2] = true;
		tp.putLastCommand(packet);
		
		tp.sendMessage(Messages.vault_confirm_confirmed);
		
		tp.getPlayer().performCommand((String)packet[1]);
		return true;
	}
}
