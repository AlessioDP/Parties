package com.alessiodp.parties.bungeecord.listeners;

import com.alessiodp.core.bungeecord.user.BungeeUser;
import com.alessiodp.parties.api.events.bungee.unique.BungeePartiesPartyFollowEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bungeecord.BungeePartiesPlugin;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigMain;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeMessages;
import com.alessiodp.parties.bungeecord.events.BungeeEventManager;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BungeeFollowListener implements Listener {
	private final BungeePartiesPlugin plugin;
	
	@EventHandler
	public void onConnected(ServerSwitchEvent event) {
		if (!BungeeConfigMain.ADDITIONAL_FOLLOW_ENABLE)
			return;
		
		// Not connected to the network yet
		if (event.getPlayer().getServer() == null)
			return;
		
		plugin.getScheduler().scheduleAsyncLater(() -> {
			if (allowedServer(event.getPlayer().getServer().getInfo().getName())) {
				PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
				PartyImpl party = plugin.getPartyManager().getParty(player.getPartyId());
				if (party != null
						&& party.isFollowEnabled()
						&& (party.getLeader() != null && party.getLeader().equals(player.getPlayerUUID()))) {
					String playerServer = event.getPlayer().getServer().getInfo().getName();
					ServerInfo serverInfo = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getServerInfo(playerServer);
					
					// Calling API event
					BungeePartiesPartyFollowEvent partyFollowEvent = ((BungeeEventManager) plugin.getEventManager()).preparePartyFollowEvent(party, playerServer);
					plugin.getEventManager().callEvent(partyFollowEvent);
					if (!partyFollowEvent.isCancelled()) {
						// Let other players follow him
						
						sendMembers(party, player, serverInfo);
					}
				}
			}
		}, ConfigMain.ADDITIONAL_FOLLOW_DELAY, TimeUnit.MILLISECONDS);
	}
	
	private void sendMembers(PartyImpl party, PartyPlayerImpl player, ServerInfo serverInfo) {
		for (PartyPlayer member : party.getOnlineMembers(true)) {
			BungeeUser memberUser = (BungeeUser) plugin.getPlayer(member.getPlayerUUID());
			if (memberUser != null
					&& !memberUser.getUUID().equals(player.getPlayerUUID())
					&& !memberUser.getServerName().equals(serverInfo.getName())) {
				
				memberUser.sendMessage(plugin.getMessageUtils().convertPlaceholders(BungeeMessages.OTHER_FOLLOW_SERVER
								.replace("%server%", serverInfo.getName()),
						player, party
				), true);
				memberUser.connectTo(serverInfo);
				
				if (BungeeConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE) {
					
					// Schedule it later
					plugin.getScheduler().scheduleAsyncLater(() -> {
						for (String command : BungeeConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS) {
							memberUser.chat(
									plugin.getMessageUtils().convertPlaceholders(command, (PartyPlayerImpl) member, party)
							);
						}
					}, BungeeConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_DELAY, TimeUnit.MILLISECONDS);
				}
			}
		}
	}
	
	private boolean allowedServer(String serverName) {
		boolean ret = true;
		if (BungeeConfigMain.ADDITIONAL_FOLLOW_BLOCKEDSERVERS.contains(serverName))
			ret = false;
		else {
			for (String regex : BungeeConfigMain.ADDITIONAL_FOLLOW_BLOCKEDSERVERS) {
				if (!ret)
					break;
				try {
					if (serverName.matches(regex))
						ret = false;
				} catch (Exception ex) {
					plugin.getLoggerManager().printError(PartiesConstants.DEBUG_FOLLOW_SERVER_REGEXERROR);
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}
}
