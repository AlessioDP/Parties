package com.alessiodp.parties.commands;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerJoinEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerLeaveEvent;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.CommandInterface;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;

public class CommandJoin implements CommandInterface {
	private Parties plugin;
	 
	public CommandJoin(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.JOIN.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.JOIN.toString()));
			return true;
		}
		if (!tp.getPartyName().isEmpty() && !Variables.password_bypassleave) {
			tp.sendMessage(Messages.join_alreadyinparty);
			return true;
		}
		if (args.length < 2 || args.length > 3) {
			tp.sendMessage(Messages.join_wrongcmd);
			return true;
		}
		String partyname = args[1];
		Party party = plugin.getPartyHandler().getParty(partyname);
		if (party == null) {
			tp.sendMessage(Messages.join_noexist);
			return true;
		}
		if (args.length == 2) {
			if (!p.hasPermission(PartiesPermissions.JOIN_OTHERS.toString())) {
				if (party.getPassword() != null && !party.getPassword().isEmpty()) {
					tp.sendMessage(Messages.join_wrongpw);
					return true;
				}
			}
		} else {
			if (!hash(args[2]).equals(party.getPassword())) {
				tp.sendMessage(Messages.join_wrongpw);
				return true;
			}
		}
		if ((Variables.party_maxmembers != -1) && (party.getMembers().size() >= Variables.party_maxmembers)) {
			tp.sendMessage(Messages.join_maxplayers);
			return true;
		}
		
		double commandPrice = Variables.vault_command_join_price;
		if (Variables.vault_enable && commandPrice > 0 && !p.hasPermission(PartiesPermissions.ADMIN_VAULTBYPASS.toString())) {
			OfflinePlayer buyer = Bukkit.getOfflinePlayer(p.getUniqueId());
			if (Variables.vault_confirm_enable) {
				if (tp.getLastCommand() != null && ((boolean)tp.getLastCommand()[2]) == true) {
					if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
						plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
						tp.putLastCommand(null);
					} else {
						tp.sendMessage(Messages.vault_join_nomoney
								.replace("%price%", Double.toString(commandPrice)));
						tp.putLastCommand(null);
						return true;
					}
				} else {
					String c = commandLabel;
					for (String s : args)
						c = c.concat(" " + s);
					tp.putLastCommand(new Object[]{System.currentTimeMillis(), c, false});
					tp.sendMessage(Messages.vault_confirm_warnonbuy
							.replace("%cmd%", args[0])
							.replace("%price%", Double.toString(commandPrice)));
					return true;
				}
			} else {
				if (plugin.getEconomy().getBalance(buyer) >= commandPrice) {
					plugin.getEconomy().withdrawPlayer(buyer, commandPrice);
				} else {
					tp.sendMessage(Messages.vault_join_nomoney
							.replace("%price%", Double.toString(commandPrice)));
					return true;
				}
			}
		}
		/*
		 * 
		 * 
		 * 
		 */
		// Calling API Event
		PartiesPlayerJoinEvent partiesJoinEvent = new PartiesPlayerJoinEvent(p, party.getName(), false, null);
		Bukkit.getServer().getPluginManager().callEvent(partiesJoinEvent);
		if (!partiesJoinEvent.isCancelled()) {
			if (Variables.password_bypassleave && !tp.getPartyName().isEmpty()) {
				/* From CommandLeave */
				Party party_2 = plugin.getPartyHandler().getParty(tp.getPartyName());
				if (party_2 != null) {
					// Calling API event
					PartiesPlayerLeaveEvent partiesLeaveEvent = new PartiesPlayerLeaveEvent(p, party.getName(), false, null);
					Bukkit.getServer().getPluginManager().callEvent(partiesLeaveEvent);
					if (!partiesLeaveEvent.isCancelled()) {
						if (party_2.getLeader().equals(p.getUniqueId())) {
							// Is leader
							// Calling Pre API event
							PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party_2.getName(), PartiesPartyPreDeleteEvent.DeleteCause.JOIN, p.getUniqueId(), p);
							Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
							if (!partiesPreDeleteEvent.isCancelled()) {
								tp.sendMessage(Messages.leave_byeplayer.replace("%party%", party_2.getName()));
								party_2.sendBroadcastParty(p, Messages.leave_disbanded);
								
								LogHandler.log(LogLevel.BASIC, p.getName() + "[" + p.getUniqueId() + "] deleted " + party_2.getName() + " by leave (join command)", true, ConsoleColors.CYAN);
								
								party_2.removeParty();
								// Calling Post API event
								PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party_2.getName(), PartiesPartyPostDeleteEvent.DeleteCause.JOIN, p.getUniqueId(), p);
								Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
								
								plugin.getPartyHandler().tag_removePlayer(p, null);
							} else {
								// Event is cancelled, block join command
								LogHandler.log(LogLevel.DEBUG, "PartiesDeleteEvent is cancelled, ignoring delete of " + party_2.getName(), true);
								return true;
							}
						} else {
							party_2.getMembers().remove(p.getUniqueId());
							party_2.remOnlinePlayer(p);
							
							party_2.sendBroadcastParty(p, Messages.leave_playerleaved);
							tp.sendMessage(Messages.leave_byeplayer.replace("%party%", party_2.getName()));
							
							party_2.updateParty();
							plugin.getPartyHandler().tag_removePlayer(p, party_2);
						}
					} else
						LogHandler.log(LogLevel.DEBUG, "PartiesLeaveEvent is cancelled, ignoring leave of " + p.getName(), true);
				}
				/* End of CommandLeave */
			}
			tp.sendMessage(Messages.join_joined
					.replace("%price%", Double.toString(commandPrice)));
			
			party.sendBroadcastParty(tp.getPlayer(), Messages.join_playerjoined);
					
			party.getMembers().add(tp.getUUID());
			party.addOnlinePlayer(tp.getPlayer());
	
			tp.setPartyName(party.getName());
			tp.setRank(Variables.rank_default);
					
			party.updateParty();
			tp.updatePlayer();
					
			plugin.getPartyHandler().tag_addPlayer(p, party);
			
			LogHandler.log(LogLevel.MEDIUM, p.getName() + "[" + p.getUniqueId() + "] joined in the party " + party.getName(), true);
		} else
			LogHandler.log(LogLevel.DEBUG, "PartiesPlayerJoinEvent is cancelled, ignoring join of " + p.getName() + " into " + party.getName(), true);
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
