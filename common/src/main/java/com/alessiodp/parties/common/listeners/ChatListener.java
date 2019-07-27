package com.alessiodp.parties.common.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.player.IChatEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.ChatTask;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class ChatListener {
	protected final PartiesPlugin plugin;
	
	/**
	 * Used by Bukkit, Bungeecord
	 *
	 * @return Returns if the event is cancelled
	 */
	protected boolean onPlayerChat(User sender, String message) {
		boolean eventCancelled = false;
		PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(sender.getUUID());
		
		boolean partyChat = false;
		PartyImpl party = partyPlayer.getPartyName().isEmpty() ? null : plugin.getPartyManager().getParty(partyPlayer.getPartyName());
		if (party != null) {
			if (partyPlayer.isChatParty()) {
				partyChat = true;
			} else if (ConfigParties.GENERAL_CHAT_DIRECT_ENABLED && message.startsWith(ConfigParties.GENERAL_CHAT_DIRECT_PREFIX)) {
				partyChat = true;
				message = message.substring(1);
			}
		}
		
		if (partyChat) {
			String finalMessage = message;
			// Make it async
			plugin.getScheduler().runAsync(() -> {
				if (plugin.getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_SENDMESSAGE)) {
					// Chat allowed
					boolean mustWait = false;
					
					if (plugin.getCensorUtils().checkCensor(ConfigParties.GENERAL_CHAT_CENSORREGEX, finalMessage, PartiesConstants.DEBUG_CMD_P_REGEXERROR)) {
						partyPlayer.sendMessage(Messages.MAINCMD_P_CENSORED);
						return;
					}
					
					if (ConfigParties.GENERAL_CHAT_CHATCD > 0
							&& !plugin.getRankManager().checkPlayerRank(partyPlayer, PartiesPermission.PRIVATE_BYPASSCOOLDOWN)) {
						Long unixTimestamp = plugin.getCooldownManager().getChatCooldown().get(partyPlayer.getPlayerUUID());
						long unixNow = System.currentTimeMillis() / 1000L;
						// Check cooldown
						if (unixTimestamp != null && (unixNow - unixTimestamp) < ConfigParties.GENERAL_CHAT_CHATCD) {
							partyPlayer.sendMessage(Messages.MAINCMD_P_COOLDOWN
									.replace("%seconds%", String.valueOf(ConfigParties.GENERAL_CHAT_CHATCD - (unixNow - unixTimestamp))));
							mustWait = true;
						} else {
							plugin.getCooldownManager().getChatCooldown().put(partyPlayer.getPlayerUUID(), unixNow);
							
							plugin.getScheduler().scheduleAsyncLater(new ChatTask(plugin, partyPlayer.getPlayerUUID()), ConfigParties.GENERAL_CHAT_CHATCD, TimeUnit.SECONDS);
							
							plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_P_TASK
									.replace("{value}", Integer.toString(ConfigParties.GENERAL_CHAT_CHATCD * 20))
									.replace("{player}", partyPlayer.getName()), true);
						}
					}
					
					if (!mustWait) {
						// Calling API event
						IChatEvent partiesChatEvent = plugin.getEventManager().prepareChatEvent(partyPlayer, party, finalMessage);
						plugin.getEventManager().callEvent(partiesChatEvent);
						
						String newMessage = partiesChatEvent.getMessage();
						if (!partiesChatEvent.isCancelled()) {
							partyPlayer.performPartyMessage(newMessage);
							
							if (ConfigParties.GENERAL_CHAT_LOG)
								plugin.getLoggerManager().log(PartiesConstants.DEBUG_CMD_P
										.replace("{party}", party.getName())
										.replace("{player}", partyPlayer.getName())
										.replace("{message}", newMessage), true);
						} else
							plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_CHATEVENT_DENY
									.replace("{player}", partyPlayer.getName())
									.replace("{message}", finalMessage), true);
					}
				} else
					partyPlayer.sendMessage(Messages.PARTIES_PERM_NOPERM
							.replace("%permission%", PartiesPermission.PRIVATE_SENDMESSAGE.toString()));
			});
			eventCancelled = true;
		}
		return eventCancelled;
	}
	
	/**
	 * Used by Bukkit
	 *
	 * @return Returns the new message to set, null if is not changed
	 */
	protected String onPlayerCommandPreprocess(User sender, String message) {
		String ret = null;
		if (ConfigMain.ADDITIONAL_AUTOCMD_ENABLE) {
			if (message.endsWith("\t")) {
				// This is an auto command
				ret = message.replace("\t", "");
			} else {
				// This is a normal command to replicate
				// Make it async
				plugin.getScheduler().runAsync(() -> {
					boolean cancel = true;
					
					try {
						Pattern pattern = Pattern.compile(ConfigMain.ADDITIONAL_AUTOCMD_REGEXWHITELIST, Pattern.CASE_INSENSITIVE);
						Matcher matcher = pattern.matcher(message);
						
						if (matcher.find()) {
							cancel = false;
						}
					} catch (Exception ex) {
						plugin.getLoggerManager().printErrorStacktrace(PartiesConstants.DEBUG_AUTOCMD_REGEXERROR, ex);
					}
					
					if (!cancel) {
						PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(sender.getUUID());
						if (!pp.getPartyName().isEmpty()) {
							if (plugin.getRankManager().checkPlayerRank(pp, PartiesPermission.PRIVATE_AUTOCOMMAND)) {
								PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
								
								for (PartyPlayer pl : party.getOnlineMembers(true)) {
									if (!pl.getPlayerUUID().equals(sender.getUUID())) {
										// Make it sync
										plugin.getScheduler().getSyncExecutor().execute(() -> {
											plugin.getPlayer(pl.getPlayerUUID()).chat(message + "\t");
											plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_AUTOCMD_PERFORM
													.replace("{player}", pl.getName())
													.replace("{command}", message), true);
										});
									}
								}
							}
						}
					}
				});
			}
		}
		return ret;
	}
}
