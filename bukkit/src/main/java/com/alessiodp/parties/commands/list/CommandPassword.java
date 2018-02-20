package com.alessiodp.parties.commands.list;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
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

public class CommandPassword implements ICommand {
	private Parties plugin;
	
	public CommandPassword(Parties parties) {
		plugin = parties;
	}
	
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.PASSWORD.toString())) {
			pp.sendNoPermission(PartiesPermission.PASSWORD);
			return;
		}
		
		PartyEntity party = pp.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(pp.getPartyName());
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_NOTINPARTY);
			return;
		}
		
		if (!PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_EDIT_PASSWORD))
			return;
		
		if (args.length != 2) {
			pp.sendMessage(Messages.ADDCMD_PASSWORD_WRONGCMD);
			return;
		}
		
		/*
		 * Command handling
		 */
		boolean isRemove = false;
		String password = "";
		if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_REMOVE)) {
			// Remove command
			isRemove = true;
		} else {
			// Normal command
			if (!Pattern.compile(ConfigParties.PASSWORD_ALLOWECHARS).matcher(args[1]).matches()
					|| (args[1].length() > ConfigParties.PASSWORD_MAXLENGTH)
					|| (args[1].length() < ConfigParties.PASSWORD_MINLENGTH)) {
				pp.sendMessage(Messages.ADDCMD_PASSWORD_INVALID);
				return;
			}
			
			password = hash(args[1]);
		}
		
		/*
		 * Command starts
		 */
		party.setPassword(password);
		party.updateParty();
		
		if (isRemove) {
			pp.sendMessage(Messages.ADDCMD_PASSWORD_REMOVED);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_PASSWORD_REM
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
		} else {
			pp.sendMessage(Messages.ADDCMD_PASSWORD_CHANGED);
			party.sendBroadcast(pp, Messages.ADDCMD_PASSWORD_BROADCAST);
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_PASSWORD
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
		}
	}
	
	private String hash(String text) {
		String ret = null;
		try {
			byte[] result = MessageDigest.getInstance(ConfigParties.PASSWORD_HASH)
					.digest(text.getBytes(ConfigParties.PASSWORD_ENCODE));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; ++i) {
				sb.append(Integer.toHexString((result[i] & 0xFF) | 0x100).substring(1,3));
			}
			ret = sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
