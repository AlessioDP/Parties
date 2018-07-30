package com.alessiodp.parties.bukkit.commands.executors;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.bukkit.utils.LastConfirmedCommand;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.AbstractCommand;
import com.alessiodp.parties.common.commands.CommandData;
import com.alessiodp.parties.common.user.User;
import org.bukkit.Bukkit;

public class CommandConfirm extends AbstractCommand {
	
	public CommandConfirm(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		LastConfirmedCommand packet = pp.getLastConfirmedCommand();
		if (packet == null
				|| ((System.currentTimeMillis() - packet.getTimestamp()) > BukkitConfigMain.ADDONS_VAULT_CONFIRM_TIMEOUT)) {
			pp.sendMessage(BukkitMessages.ADDCMD_VAULT_CONFIRM_NOCMD);
			return;
		}
		
		/*
		 * Command starts
		 */
		packet.setConfirmed(true);
		pp.setLastConfirmedCommand(packet);
		
		pp.sendMessage(BukkitMessages.ADDCMD_VAULT_CONFIRM_CONFIRMED);
		
		// Make it sync
		plugin.getPartiesScheduler().runSync(() -> Bukkit.getServer().dispatchCommand(Bukkit.getPlayer(pp.getPlayerUUID()), packet.getCommand()));
	}
}
