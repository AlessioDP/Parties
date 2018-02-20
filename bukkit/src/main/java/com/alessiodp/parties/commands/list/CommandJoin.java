package com.alessiodp.parties.commands.list;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.external.VaultHandler;
import com.alessiodp.parties.commands.ICommand;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.partiesapi.events.PartiesPlayerJoinEvent;

public class CommandJoin implements ICommand {
	private Parties plugin;
	 
	public CommandJoin(Parties parties) {
		plugin = parties;
	}
	public void onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player) sender;
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
		
		/*
		 * Checks for command prerequisites
		 */
		if (!p.hasPermission(PartiesPermission.JOIN.toString())) {
			pp.sendNoPermission(PartiesPermission.INVITE);
			return;
		}
		
		if (!pp.getPartyName().isEmpty()) {
			pp.sendMessage(Messages.ADDCMD_JOIN_ALREADYINPARTY);
			return;
		}
		if (args.length < 2 || args.length > 3) {
			pp.sendMessage(Messages.ADDCMD_JOIN_WRONGCMD);
			return;
		}
		
		/*
		 * Command handling
		 */
		String partyName = args[1];
		PartyEntity party = plugin.getPartyManager().getParty(partyName);
		if (party == null) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", partyName));
			return;
		}
		
		if (args.length == 2) {
			if (!p.hasPermission(PartiesPermission.JOIN_BYPASS.toString())) {
				if (party.getPassword() != null && !party.getPassword().isEmpty()) {
					pp.sendMessage(Messages.ADDCMD_JOIN_WRONGPASSWORD);
					return;
				}
			}
		} else {
			if (!hash(args[2]).equals(party.getPassword())) {
				pp.sendMessage(Messages.ADDCMD_JOIN_WRONGPASSWORD);
				return;
			}
		}
		
		if ((ConfigParties.GENERAL_MEMBERSLIMIT != -1)
				&& (party.getMembers().size() >= ConfigParties.GENERAL_MEMBERSLIMIT)) {
			pp.sendMessage(Messages.PARTIES_COMMON_PARTYFULL);
			return;
		}
		
		if (VaultHandler.payCommand(VaultHandler.VaultCommand.JOIN, pp, commandLabel, args))
			return;
		
		/*
		 * Command starts
		 */
		// Calling API Event
		PartiesPlayerJoinEvent partiesJoinEvent = new PartiesPlayerJoinEvent(pp, party, false, null);
		Bukkit.getServer().getPluginManager().callEvent(partiesJoinEvent);
		if (!partiesJoinEvent.isCancelled()) {
			pp.sendMessage(Messages.ADDCMD_JOIN_JOINED);
			
			party.sendBroadcast(pp, Messages.ADDCMD_JOIN_PLAYERJOINED);
					
			party.getMembers().add(pp.getPlayerUUID());
			party.getOnlinePlayers().add(pp.getPlayer());
	
			pp.setPartyName(party.getName());
			pp.setRank(ConfigParties.RANK_SET_DEFAULT);
					
			party.updateParty();
			pp.updatePlayer();
					
			party.callChange();
			
			LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_CMD_JOIN
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
		} else
			LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_JOINEVENT_DENY
					.replace("{player}", p.getName())
					.replace("{party}", party.getName()), true);
	}
	
	private String hash(String text) {
		byte[] result = null;
		try {
			result = MessageDigest.getInstance(ConfigParties.PASSWORD_HASH).digest(text.getBytes(ConfigParties.PASSWORD_ENCODE));
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
