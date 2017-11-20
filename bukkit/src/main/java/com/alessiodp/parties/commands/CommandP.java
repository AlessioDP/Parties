package com.alessiodp.parties.commands;

import org.bukkit.Bukkit;
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
import com.alessiodp.parties.utils.tasks.ChatTask;
import com.alessiodp.partiesapi.events.PartiesChatEvent;
import com.alessiodp.partiesapi.interfaces.Rank;

public class CommandP implements CommandInterface {
	private Parties plugin;
	 
	public CommandP(Parties parties) {
		plugin = parties;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player)sender;
		ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
		/*
		 * Checks
		 */
		if (!p.hasPermission(PartiesPermissions.SENDMESSAGE.toString())) {
			tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.SENDMESSAGE.toString()));
			return true;
		}
		Party party = tp.getPartyName().isEmpty() ? null : plugin.getPartyHandler().getParty(tp.getPartyName());
		if (party == null) {
			tp.sendMessage(Messages.noparty);
			return true;
		}
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if (r != null && !p.hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(PartiesPermissions.PRIVATE_SENDMESSAGE.toString())) {
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), PartiesPermissions.PRIVATE_SENDMESSAGE.toString());
				if (rr != null)
					tp.sendMessage(Messages.nopermission_party.replace("%rank%", rr.getName()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", PartiesPermissions.PRIVATE_SENDMESSAGE.toString()));
				return true;
			}
		}
		if (args.length == 0) {
			tp.sendMessage(Messages.p_wrongcmd);
			return true;
		}
		if (Variables.chat_chatcooldown > 0 && !r.havePermission(PartiesPermissions.PRIVATE_BYPASSCOOLDOWN.toString())) {
			Long unixTimestamp = plugin.getPlayerHandler().getChatCooldown().get(p.getUniqueId());
			long unixNow = System.currentTimeMillis() / 1000L;
			if (unixTimestamp != null) {
				tp.sendMessage(Messages.p_cooldown.replace("%seconds%", String.valueOf(Variables.chat_chatcooldown - (unixNow - unixTimestamp))));
				return true;
			}
			plugin.getPlayerHandler().getChatCooldown().put(p.getUniqueId(), unixNow);
			new ChatTask(p.getUniqueId(), plugin.getPlayerHandler()).runTaskLater(plugin, Variables.chat_chatcooldown * 20);
			LogHandler.log(LogLevel.DEBUG, "Started ChatTask for "+(Variables.chat_chatcooldown*20)+" by "+p.getName()+ "["+p.getUniqueId() + "]", true);
		}
		/*
		 * 
		 * 
		 * 
		 */
		
		StringBuilder sb = new StringBuilder();
		for (String word:args) {
			if (sb.length() > 0) {
				sb.append(" ");
			}
			sb.append(word);
		}
		// Calling API event
		PartiesChatEvent partiesChatEvent = new PartiesChatEvent(p, party.getName(), sb.toString());
		Bukkit.getServer().getPluginManager().callEvent(partiesChatEvent);
		String newMessage = partiesChatEvent.getMessage();
		if (!partiesChatEvent.isCancelled()) {
			party.sendPlayerMessage(p, newMessage);
			
			if (Variables.log_chat)
				LogHandler.log(LogLevel.BASIC, "Chat of " + party.getName() + ": " + p.getName() + "[" + p.getUniqueId() + "]:" + newMessage, true);
		} else
			LogHandler.log(LogLevel.DEBUG, "PartiesChatEvent is cancelled, ignoring chat of " + p.getName() + ": " + sb.toString(), true);
		
		return true;
	}
}
