package com.alessiodp.parties.common.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.CensorUtils;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class ChatListener {
	protected final PartiesPlugin plugin;
	
	protected boolean onPlayerChat(User sender, String message) {
		boolean eventCancelled = false;
		PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(sender.getUUID());
		String parsedMessage = message;
		
		boolean partyChat = false;
		PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyId());
		if (party != null) {
			if (partyPlayer.isChatParty()) {
				partyChat = true;
			} else if (ConfigParties.GENERAL_CHAT_DIRECT_ENABLED && parsedMessage.startsWith(ConfigParties.GENERAL_CHAT_DIRECT_PREFIX)) {
				partyChat = true;
				parsedMessage = parsedMessage
						.substring(ConfigParties.GENERAL_CHAT_DIRECT_PREFIX.length()) // Remove direct prefix
						.replaceAll("^\\s+|\\s+$", ""); // Trim the string
			}
		}
		
		if (partyChat) {
			final String finalMessage = parsedMessage;
			// Make it async
			plugin.getScheduler().runAsync(() -> {
				if (plugin.getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_SENDMESSAGE)) {
					// Chat allowed
					boolean mustWait = false;
					
					if (ConfigParties.GENERAL_CHAT_PREVENT_MUTED_PLAYERS && partyPlayer.isChatMuted()) {
						partyPlayer.sendMessage(Messages.MAINCMD_P_MUTED);
						return;
					}
					
					if (CensorUtils.checkCensor(ConfigParties.GENERAL_CHAT_CENSORREGEX, finalMessage, PartiesConstants.DEBUG_CMD_P_REGEXERROR)) {
						partyPlayer.sendMessage(Messages.MAINCMD_P_CENSORED);
						return;
					}
					
					boolean mustStartCooldown = false;
					if (ConfigParties.GENERAL_CHAT_COOLDOWN > 0 && !sender.hasPermission(PartiesPermission.ADMIN_COOLDOWN_CHAT_BYPASS)) {
						mustStartCooldown = true;
						long remainingCooldown = plugin.getCooldownManager().canChat(partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_CHAT_COOLDOWN);
						
						if (remainingCooldown > 0) {
							partyPlayer.sendMessage(Messages.MAINCMD_P_COOLDOWN
									.replace("%seconds%", String.valueOf(remainingCooldown)));
							mustWait = true;
						}
					}
					
					if (!mustWait && partyPlayer.performPartyMessage(finalMessage)) {
						if (mustStartCooldown)
							plugin.getCooldownManager().startChatCooldown(partyPlayer.getPlayerUUID(), ConfigParties.GENERAL_CHAT_COOLDOWN);
						
						if (ConfigMain.PARTIES_LOGGING_PARTY_CHAT) {
							plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_P,
									partyPlayer.getName(), party.getName() != null ? party.getName() : "_", finalMessage), true);
						}
					}
				} else
					partyPlayer.sendMessage(Messages.PARTIES_PERM_NOPERM
							.replace("%permission%", PartiesPermission.PRIVATE_SENDMESSAGE.toString()));
			});
			eventCancelled = true;
		}
		return eventCancelled;
	}
	
	protected void onPlayerCommandPreprocess(User sender, String message) {
		if (ConfigMain.ADDITIONAL_AUTOCMD_ENABLE) {
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
					PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyId());
					if (party != null && plugin.getRankManager().checkPlayerRank(pp, PartiesPermission.PRIVATE_AUTOCOMMAND)) {
						for (PartyPlayer pl : party.getOnlineMembers(true)) {
							if (!pl.getPlayerUUID().equals(sender.getUUID())) {
								// Make it sync
								plugin.getScheduler().getSyncExecutor().execute(() -> {
									User user = plugin.getPlayer(pl.getPlayerUUID());
									plugin.getBootstrap().executeCommandByUser(message.substring(1), user);
									
									plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_AUTOCMD_PERFORM, pl.getPlayerUUID().toString(), message), true);
								});
							}
						}
					}
				}
			});
		}
	}
}
