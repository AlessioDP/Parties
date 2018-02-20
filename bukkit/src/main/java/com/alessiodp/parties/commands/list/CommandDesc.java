package com.alessiodp.parties.commands.list;

import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public class CommandDesc implements ICommand {
	private Parties plugin;
	
	public CommandDesc(Parties parties) {
		plugin = parties;
	}
	
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.DESC.toString())) {
			pp.sendNoPermission(PartiesPermission.DESC);
			return;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_DESC))
			return;
		
		if (args.length < 2) {
			pp.sendMessage(Messages.ADDCMD_DESC_WRONGCMD);
			return;
		}
		
		/*
		 * Command handling
		 */
		boolean isRemove = false;
		String description = "";
		if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			StringBuilder sb = new StringBuilder();
			for (int word = 1; word < args.length; word++) {
				if (sb.length() > 0)
					sb.append(" ");
				sb.append(args[word]);
			}
			if (!Pattern.compile(ConfigParties.DESC_ALLOWEDCHARS).matcher(sb.toString()).matches()
					|| (sb.toString().length() > ConfigParties.DESC_MAXLENGTH)
					|| (sb.toString().length() < ConfigParties.DESC_MINLENGTH)) {
				pp.sendMessage(Messages.ADDCMD_DESC_INVALID);
				return;
			}
			if (PartiesUtils.checkCensor(sb.toString())) {
				pp.sendMessage(Messages.ADDCMD_DESC_CENSORED);
				return;
			}
			
			if (VaultHandler.payCommand(VaultHandler.VaultCommand.DESC, pp, commandLabel, args))
				return;
			
			description = sb.toString();
		}
		
		/*
		 * Command starts
		 */
		party.setDescription(description);
		party.updateParty();
		
		if (isRemove) {
			pp.sendMessage(Messages.ADDCMD_DESC_REMOVED);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_DESC_REM
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
		} else {
			pp.sendMessage(Messages.ADDCMD_DESC_CHANGED);
			party.sendBroadcast(pp, Messages.ADDCMD_DESC_BROADCAST);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_DESC
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
		}
	}
}