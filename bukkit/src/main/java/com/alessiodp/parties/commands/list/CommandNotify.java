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

public class CommandNotify implements ICommand {
	private Parties plugin;
	 
	public CommandNotify(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.NOTIFY.toString())) {
			pp.sendNoPermission(PartiesPermission.NOTIFY);
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
		boolean toggleNotify = !pp.isPreventNotify();
		
		pp.setPreventNotify(toggleNotify);
		pp.updatePlayer();
		
		if (toggleNotify) {
			pp.sendMessage(Messages.ADDCMD_NOTIFY_ON);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_NOTIFY_ON
					.replace("{player}", pp.getName()), true);
		} else {
			pp.sendMessage(Messages.ADDCMD_NOTIFY_OFF);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_NOTIFY_OFF
					.replace("{player}", pp.getName()), true);
		}

	}
}
