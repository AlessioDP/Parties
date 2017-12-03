package com.alessiodp.parties.commands;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.interfaces.Rank;

public class CommandPassword implements CommandInterface {
	private Parties plugin;

	public CommandPassword(Parties parties) {
		plugin = parties;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!Variables.password_enable)
			return false;
		if (!p.hasPermission(PartiesPermissions.PASSWORD.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PASSWORD.toString()));
			return true;
		}
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if (r != null && !p.hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(PartiesPermissions.PRIVATE_EDIT_PASSWORD.toString())) {
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_EDIT_PASSWORD.toString());
				if (rr != null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_EDIT_PASSWORD.toString()));
				return true;
			}
		}
		if (args.length != 2) {
			tp.sendMessage(Messages.password_wrongcmd);
			return true;
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		if (args[1].equalsIgnoreCase(Variables.command_sub_remove)) {
			party.setPassword("");
			party.updateParty();

			party.sendBroadcastParty(p, Messages.password_removed);
			
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] removed password to the party " + party.getName(), true);
			return true;
		}

		if (!Pattern.compile(Variables.password_allowedchars).matcher(args[1]).matches()
				|| (args[1].length() > Variables.password_lengthmax)
				|| (args[1].length() < Variables.password_lengthmin)) {
			tp.sendMessage(Messages.password_invalidchars);
			return true;
		}
		
		party.setPassword(hash(args[1]));
		party.updateParty();
		
		party.sendBroadcastParty(p, Messages.password_changed);
		
		LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] set new password to the party " + party.getName(), true);
		return true;
	}
	private String hash(String text) {
		byte[] result = null;
		try {
			result = MessageDigest.getInstance(Variables.password_hash).digest(text.getBytes(Variables.password_encode));
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; ++i) {
				sb.append(Integer.toHexString((result[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
