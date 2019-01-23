package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import com.alessiodp.parties.common.utils.EconomyManager;
import com.alessiodp.parties.common.utils.PartiesUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandMotd extends AbstractCommand {
	
	public CommandMotd(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!sender.hasPermission(PartiesPermission.MOTD.toString())) {
			pp.sendNoPermission(PartiesPermission.MOTD);
			return false;
		}
		
		PartyImpl party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return false;
		}
		
		if (!plugin.getRankManager().checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_MOTD))
			return false;
		
		if (commandData.getArgs().length < 2) {
			pp.sendMessage(Messages.ADDCMD_MOTD_WRONGCMD);
			return false;
		}
		
		commandData.setPartyPlayer(pp);
		commandData.setParty(party);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		PartyPlayerImpl pp = commandData.getPartyPlayer();
		PartyImpl party = commandData.getParty();
		
		/*
		 * Command handling
		 */
		boolean isRemove = false;
		String motd = "";
		if (commandData.getArgs()[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			StringBuilder sb = new StringBuilder();
			for (int word = 1; word < commandData.getArgs().length; word++) {
				if (sb.length() > 0)
					sb.append(" ");
				sb.append(commandData.getArgs()[word]);
			}
			motd = sb.toString();
			
			if (!PartiesUtils.checkAllowedCharacters(ConfigParties.MOTD_ALLOWEDCHARS, motd, Constants.DEBUG_CMD_MOTD_REGEXERROR_AC)
					|| (motd.length() > ConfigParties.MOTD_MAXLENGTH)
					|| (motd.length() < ConfigParties.MOTD_MINLENGTH)) {
				pp.sendMessage(Messages.ADDCMD_MOTD_INVALID);
				return;
			}
			if (PartiesUtils.checkCensor(ConfigParties.MOTD_CENSORREGEX, motd, Constants.DEBUG_CMD_MOTD_REGEXERROR_CEN)) {
				pp.sendMessage(Messages.ADDCMD_MOTD_CENSORED);
				return;
			}
			
			if (plugin.getEconomyManager().payCommand(EconomyManager.PaidCommand.MOTD, pp, commandData.getCommandLabel(), commandData.getArgs()))
				return;
			
			motd = sb.toString();
		}
		
		/*
		 * Command starts
		 */
		party.setMotd(motd);
		party.updateParty();
		
		if (isRemove) {
			pp.sendMessage(Messages.ADDCMD_MOTD_REMOVED);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_MOTD
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		} else {
			pp.sendMessage(Messages.ADDCMD_MOTD_CHANGED);
			party.sendBroadcast(pp, Messages.ADDCMD_MOTD_BROADCAST);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_MOTD_REM
					.replace("{player}", pp.getName())
					.replace("{party}", party.getName()), true);
		}
	}
	
	@Override
	public List<String> onTabComplete(User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_SUB_REMOVE);
		}
		return ret;
	}
}