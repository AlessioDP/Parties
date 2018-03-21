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
import com.alessiodp.parties.utils.ConsoleColor;

public class CommandReload implements ICommand {
	private Parties plugin;
	 
	public CommandReload(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		if (commandData.getSender() instanceof Player) {
			Player player = (Player) commandData.getSender();
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
			
			if (pp != null && !player.hasPermission(PartiesPermission.ADMIN_RELOAD.toString())) {
				pp.sendNoPermission(PartiesPermission.ADMIN_RELOAD);
				return false;
			}
			
			commandData.setPlayer((Player) commandData.getSender());
			commandData.setPartyPlayer(pp);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		
		plugin.reloadConfiguration();
		
		if (pp != null) {
			pp.sendMessage(Messages.PARTIES_COMMON_CONFIGRELOAD);
			
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_RELOAD
					.replace("{player}", pp.getName()), true);
		} else {
			LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_RELOAD_CONSOLE, true, ConsoleColor.GREEN);
		}
	}
}
