package com.alessiodp.parties.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.configuration.Variables;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.PlayerUtil;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.parties.utils.tasks.ChatTask;
import com.alessiodp.partiesapi.events.PartiesChatEvent;

public class ChatListener implements Listener {
	Parties plugin;
	
	public ChatListener(Parties instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			Player p = event.getPlayer();
			ThePlayer tp = plugin.getPlayerHandler().getPlayer(p.getUniqueId());
			
			if (!tp.getPartyName().isEmpty() && tp.chatParty()) {
				if (PlayerUtil.checkPlayerRankAlerter(tp, PartiesPermissions.PRIVATE_SENDMESSAGE)) {
					// Chat allowed
					boolean mustWait = false;
					
					if (Variables.chat_chatcooldown > 0
							&& !PlayerUtil.checkPlayerRank(tp, PartiesPermissions.PRIVATE_BYPASSCOOLDOWN)) {
						Long unixTimestamp = plugin.getPlayerHandler().getChatCooldown().get(p.getUniqueId());
						long unixNow = System.currentTimeMillis() / 1000L;
						// Check cooldown
						if (unixTimestamp != null) {
							tp.sendMessage(Messages.p_cooldown.replace("%seconds%", String.valueOf(Variables.chat_chatcooldown - (unixNow - unixTimestamp))));
							mustWait = true;
						} else {
							plugin.getPlayerHandler().getChatCooldown().put(p.getUniqueId(), unixNow);
							new ChatTask(p.getUniqueId(), plugin.getPlayerHandler()).runTaskLater(plugin, Variables.chat_chatcooldown * 20);
							LogHandler.log(LogLevel.DEBUG, "Started ChatTask for "+(Variables.chat_chatcooldown*20)+" by "+p.getName()+ "["+p.getUniqueId() + "]", true);
						}
					}
					
					if (!mustWait) {
						Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
						
						// Calling API event
						PartiesChatEvent partiesChatEvent = new PartiesChatEvent(p, party.getName(), event.getMessage());
						Bukkit.getServer().getPluginManager().callEvent(partiesChatEvent);
						String newMessage = partiesChatEvent.getMessage();
						if (!partiesChatEvent.isCancelled()) {
							party.sendPlayerMessage(p, newMessage);
							if (Variables.storage_log_chat)
								LogHandler.log(LogLevel.BASIC, "Chat of " + party.getName() + " by " + p.getName() + ": " + newMessage, true);
						} else
							LogHandler.log(LogLevel.DEBUG, "PartiesChatEvent is cancelled, ignoring chat of " + p.getName() + ": " + event.getMessage(), true);
					}
				}
				event.setCancelled(true);
			}
		}
	}
	
	/*
	 * Auto-command
	 */
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!event.isCancelled()
				&& Variables.autocommand_enable) {
			if (event.getMessage().endsWith("\t")) {
				// This is an auto command
				event.setMessage(event.getMessage().replace("\t", ""));
			} else {
				// This is a normal command to replicate
				boolean cancel = false;
				
				for (String str : Variables.autocommand_blacklist) {
					if (str.equalsIgnoreCase("*") || event.getMessage().toLowerCase().startsWith(str.toLowerCase())) {
						cancel = true;
						break;
					}
				}
				for (String str : Variables.autocommand_whitelist) {
					if (str.equalsIgnoreCase("*") && event.getMessage().toLowerCase().startsWith(str.toLowerCase())) {
						cancel = false;
						break;
					}
				}
				
				if (!cancel) {
					ThePlayer tp = plugin.getPlayerHandler().getPlayer(event.getPlayer().getUniqueId());
					if (!tp.getPartyName().isEmpty()) {
						if (PlayerUtil.checkPlayerRank(tp, PartiesPermissions.PRIVATE_AUTOCOMMAND)) {
							Party party = plugin.getPartyHandler().getParty(tp.getPartyName());
							
							for (Player pl : party.getOnlinePlayers()) {
								if (!pl.getUniqueId().equals(event.getPlayer().getUniqueId())) {
									pl.chat(event.getMessage() + "\t");
									LogHandler.log(LogLevel.MEDIUM, "[" + pl.getUniqueId() + "] using autocommand '" + event.getMessage() + "'", true);
								}
							}
						}
					}
				}
			}
		}
	}
}
