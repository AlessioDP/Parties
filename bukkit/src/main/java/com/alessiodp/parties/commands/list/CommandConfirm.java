package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandConfirm implements ICommand {
	private Parties plugin;
	 
	public CommandConfirm(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		Object[] packet = pp.getLastCommand();
		if (packet == null
				|| ((System.currentTimeMillis() - (long)packet[0]) > ConfigMain.ADDONS_VAULT_CONFIRM_TIMEOUT)) {
			pp.sendMessage(Messages.ADDCMD_VAULT_CONFIRM_NOCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		packet[2] = true;
		pp.setLastCommand(packet);
		
		pp.sendMessage(Messages.ADDCMD_VAULT_CONFIRM_CONFIRMED);
		
		pp.getPlayer().performCommand((String)packet[1]);
	}
}
