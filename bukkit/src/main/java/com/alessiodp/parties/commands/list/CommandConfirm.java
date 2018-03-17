package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandConfirm implements ICommand {
	private Parties plugin;
	 
	public CommandConfirm(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
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
		
		// Bukkit.isPrimaryThread() to check if is async, false == async
		// Make it sync
		plugin.getPartiesScheduler().runSync(() -> {
			plugin.getServer().dispatchCommand(pp.getPlayer(), (String) packet[1]);
		});
	}
}
