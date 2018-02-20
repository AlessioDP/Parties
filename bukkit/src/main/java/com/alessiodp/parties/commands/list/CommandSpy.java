package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

public class CommandSpy implements ICommand {
	private Parties plugin;
	 
	public CommandSpy(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
			pp.sendNoPermission(PartiesPermission.ADMIN_SPY);
			return;
		}
		
		/*
		 * Command starts
		 */
		boolean toggleSpy = !pp.isSpy();
		
		pp.setSpy(toggleSpy);
		pp.updatePlayer();
		
		if (toggleSpy) {
			plugin.getSpyManager().addSpy(p.getUniqueId());
			pp.sendMessage(Messages.MAINCMD_SPY_ENABLED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SPY_ENABLE
					.replace("{player}", p.getName()), true);
		} else {
			plugin.getSpyManager().removeSpy(p.getUniqueId());
			pp.sendMessage(Messages.MAINCMD_SPY_DISABLED);
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SPY_DISABLE
					.replace("{player}", p.getName()), true);
		}
	}
}
