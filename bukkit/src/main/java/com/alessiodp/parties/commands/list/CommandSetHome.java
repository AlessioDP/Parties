package com.alessiodp.parties.commands.list;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.CommandData;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public class CommandSetHome implements ICommand {
	private Parties plugin;
	
	public CommandSetHome(Parties parties) {
		plugin = parties;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		Player player = (Player) commandData.getSender();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(player.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!player.hasPermission(PartiesPermission.SETHOME.toString())) {
			pp.sendNoPermission(PartiesPermission.SETHOME);
			return false;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_HOME))
			return false;
		
		if (commandData.getArgs().length > 1 && !commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			pp.sendMessage(Messages.ADDCMD_SETHOME_WRONGCMD);
			return false;
		}
		
		commandData.setPlayer(player);
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerEntity pp = commandData.getPartyPlayer();
		PartyEntity party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		boolean isRemove = false;
		Location home = null;
		if (commandData.getArgs().length > 1 && commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			if (VaultHandler.payCommand(VaultHandler.VaultCommand.SETHOME, pp, commandData.getCommandLabel(), commandData.getArgs()))
				return;
			
			home = commandData.getPlayer().getLocation();
		}
		
		/*
		 * Command starts
		 */
		party.setHome(home);
		party.updateParty();
		
		if (isRemove) {
			pp.sendMessage(Messages.ADDCMD_SETHOME_REMOVED);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SETHOME_REM
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		} else {
			pp.sendMessage(Messages.ADDCMD_SETHOME_CHANGED);
			party.sendBroadcast(pp, Messages.ADDCMD_SETHOME_BROADCAST);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_SETHOME
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		}
	}
}