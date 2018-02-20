package com.alessiodp.parties.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;
import com.alessiodp.partiesapi.interfaces.Color;


public class CommandColor implements ICommand {
	private Parties plugin;
	
	public CommandColor(Parties parties) {
		plugin = parties;
	}
	
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.COLOR.toString())) {
			pp.sendNoPermission(PartiesPermission.COLOR);
			return;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_COLOR))
			return;
		
		if (args.length > 2) {
			pp.sendMessage(Messages.ADDCMD_COLOR_WRONGCMD);
			return;
		}
		
		/*
		 * Command handling
		 */
		if (args.length == 1) {
			// Automatically pp.sendMessage put color placeholders
			if (party.getColor() != null)
				pp.sendMessage(Messages.ADDCMD_COLOR_INFO);
			else
				pp.sendMessage(Messages.ADDCMD_COLOR_EMPTY);
			return;
		}
		
		boolean isRemove = false;
		Color color = null;
		if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			color = plugin.getColorManager().searchColorByCommand(args[1]);
			if (color == null) {
				// Color doesn't exist
				pp.sendMessage(Messages.ADDCMD_COLOR_WRONGCOLOR);
				return;
			}
			
			if (VaultHandler.payCommand(VaultHandler.VaultCommand.COLOR, pp, commandLabel, args))
				return;
		}
		
		/*
		 * Command starts
		 */
		party.setColor(color);
		party.updateParty();
		party.callChange();
		
		if (isRemove) {
			pp.sendMessage(Messages.ADDCMD_COLOR_REMOVED);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_COLOR_REM
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
		} else {
			pp.sendMessage(Messages.ADDCMD_COLOR_CHANGED, party);
			party.sendBroadcast(pp, Messages.ADDCMD_COLOR_BROADCAST);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_COLOR
					.replace("{player}", p.getName())
					.replace("{party}", party.getName())
					.replace("{value}", color.getName()), true);
		}
	}
}
