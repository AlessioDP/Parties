package com.alessiodp.parties.commands.list;

import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandSpy implements ICommand {
	private Parties plugin;
	 
	public CommandSpy(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
			pp.sendNoPermission(PartiesPermission.ADMIN_SPY);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		/*
		 * Command starts
		 */
		boolean toggleSpy = !pp.isSpy();
		
		pp.setSpy(toggleSpy);
		pp.updatePlayer();
		
		if (toggleSpy) {
			plugin.getSpyManager().addSpy(pp.getPlayerUUID());
			pp.sendMessage(Messages.MAINCMD_SPY_ENABLED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SPY_ENABLE
					.replace("{player}", pp.getName()), true);
		} else {
			plugin.getSpyManager().removeSpy(pp.getPlayerUUID());
			pp.sendMessage(Messages.MAINCMD_SPY_DISABLED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SPY_DISABLE
					.replace("{player}", pp.getName()), true);
		}
	}
}
