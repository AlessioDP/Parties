package com.alessiodp.parties.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.tasks.ChatTask;
import com.alessiodp.parties.utils.PartiesUtils;
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
			PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(p.getUniqueId());
			
			if (!pp.getPartyName().isEmpty() && pp.isChatParty()) {
				// Make it async
				plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
					if (PartiesUtils.checkPlayerRankAlerter(pp, PartiesPermission.PRIVATE_SENDMESSAGE)) {
						// Chat allowed
						boolean mustWait = false;
						
						if (ConfigParties.GENERAL_CHAT_CHATCD > 0
								&& !PartiesUtils.checkPlayerRank(pp, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
							Long unixTimestamp = plugin.getPlayerManager().getChatCooldown().get(p.getUniqueId());
							long unixNow = System.currentTimeMillis() / 1000L;
							// Check cooldown
							if (unixTimestamp != null) {
								pp.sendMessage(Messages.MAINCMD_P_COOLDOWN
										.replace("%seconds%", String.valueOf(ConfigParties.GENERAL_CHAT_CHATCD - (unixNow - unixTimestamp))));
								mustWait = true;
							} else {
								plugin.getPlayerManager().getChatCooldown().put(p.getUniqueId(), unixNow);
								new ChatTask(p.getUniqueId(), plugin.getPlayerManager()).runTaskLater(plugin, ConfigParties.GENERAL_CHAT_CHATCD * 20);
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_CMD_P_TASK
										.replace("{value}", Integer.toString(ConfigParties.GENERAL_CHAT_CHATCD * 20))
										.replace("{player}", p.getName()), true);
							}
						}
						
						if (!mustWait) {
							PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
							
							// Calling API event
							PartiesChatEvent partiesChatEvent = new PartiesChatEvent(pp, party, event.getMessage());
							Bukkit.getServer().getPluginManager().callEvent(partiesChatEvent);
							String newMessage = partiesChatEvent.getMessage();
							if (!partiesChatEvent.isCancelled()) {
								party.sendPlayerMessage(pp, newMessage);
								if (ConfigMain.STORAGE_LOG_CHAT)
									LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_CMD_P
											.replace("{party}", party.getName())
											.replace("{player}", p.getName())
											.replace("{message}", newMessage), true);
							} else
								LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_CHATEVENT_DENY
										.replace("{player}", p.getName())
										.replace("{message}", event.getMessage()), true);
						}
					} else
						pp.sendNoPermission(PartiesPermission.PRIVATE_SENDMESSAGE);
				});
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
				&& ConfigMain.ADDITIONAL_AUTOCMD_ENABLE) {
			if (event.getMessage().endsWith("\t")) {
				// This is an auto command
				event.setMessage(event.getMessage().replace("\t", ""));
			} else {
				// This is a normal command to replicate
				// Make it async
				plugin.getPartiesScheduler().getEventsExecutor().execute(() -> {
					boolean cancel = false;
					
					for (String str : ConfigMain.ADDITIONAL_AUTOCMD_BLACKLIST) {
						if (str.equalsIgnoreCase("*") || event.getMessage().toLowerCase().startsWith(str.toLowerCase())) {
							cancel = true;
							break;
						}
					}
					for (String str : ConfigMain.ADDITIONAL_AUTOCMD_WHITELIST) {
						if (str.equalsIgnoreCase("*") && event.getMessage().toLowerCase().startsWith(str.toLowerCase())) {
							cancel = false;
							break;
						}
					}
					
					if (!cancel) {
						PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
						if (!pp.getPartyName().isEmpty()) {
							if (PartiesUtils.checkPlayerRank(pp, PartiesPermission.PRIVATE_AUTOCOMMAND)) {
								PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
								
								for (Player pl : party.getOnlinePlayers()) {
									if (!pl.getUniqueId().equals(event.getPlayer().getUniqueId())) {
										pl.chat(event.getMessage() + "\t");
										LoggerManager.log(LogLevel.MEDIUM, Constants.DEBUG_AUTOCMD_PERFORM
												.replace("{player}", pl.getName())
												.replace("{command}", event.getMessage()), true);
									}
								}
							}
						}
					}
				});
			}
		}
	}
}
