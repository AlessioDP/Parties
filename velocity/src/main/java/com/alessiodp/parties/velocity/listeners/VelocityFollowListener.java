package com.alessiodp.parties.velocity.listeners;

import com.alessiodp.core.velocity.user.VelocityUser;
import com.alessiodp.parties.api.events.velocity.unique.VelocityPartiesPartyFollowEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.VelocityPartiesPlugin;
import com.alessiodp.parties.velocity.bootstrap.VelocityPartiesBootstrap;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigMain;
import com.alessiodp.parties.velocity.configuration.data.VelocityMessages;
import com.alessiodp.parties.velocity.events.VelocityEventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class VelocityFollowListener {
	private final VelocityPartiesPlugin plugin;
	
	@Subscribe
	public void onConnected(ServerConnectedEvent event) {
		if (!VelocityConfigMain.ADDITIONAL_FOLLOW_ENABLE)
			return;
		
		plugin.getScheduler().scheduleAsyncLater(() -> {
			if (allowedServer(event.getServer().getServerInfo().getName())) {
				PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
				PartyImpl party = plugin.getPartyManager().getParty(player.getPartyId());
				if (party != null
						&& party.isFollowEnabled()
						&& (party.getLeader() != null && party.getLeader().equals(player.getPlayerUUID()))) {
					RegisteredServer server = event.getServer();
					
					// Calling API event
					VelocityPartiesPartyFollowEvent partyFollowEvent = ((VelocityEventManager) plugin.getEventManager()).preparePartyFollowEvent(party, server);
					plugin.getEventManager().callEvent(partyFollowEvent);
					if (!partyFollowEvent.isCancelled()) {
						// Let other players follow him
						
						sendMembers(party, player, server);
					}
				}
			}
		}, ConfigMain.ADDITIONAL_FOLLOW_DELAY, TimeUnit.MILLISECONDS);
	}
	
	private void sendMembers(PartyImpl party, PartyPlayerImpl player, RegisteredServer server) {
		for (PartyPlayer member : party.getOnlineMembers(true)) {
			VelocityUser memberUser = (VelocityUser) plugin.getPlayer(member.getPlayerUUID());
			if (memberUser != null
					&& !memberUser.getUUID().equals(player.getPlayerUUID())
					&& !memberUser.getServerName().equals(server.getServerInfo().getName())) {
				
				memberUser.sendMessage(plugin.getMessageUtils().convertPlaceholders(VelocityMessages.OTHER_FOLLOW_SERVER
								.replace("%server%", server.getServerInfo().getName()),
						player, party
				), true);
				
				// WIP: Workaround for teleport
				Player pp = ((VelocityPartiesBootstrap)  plugin.getBootstrap()).getServer().getPlayer(memberUser.getUUID()).get();
				pp.createConnectionRequest(server).connect();
				//memberUser.connectTo(server);
				
				if (VelocityConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE) {
					
					// Schedule it later
					plugin.getScheduler().scheduleAsyncLater(() -> {
						for (String command : VelocityConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS) {
							memberUser.chat(
									plugin.getMessageUtils().convertPlaceholders(command, (PartyPlayerImpl) member, party)
							);
						}
					}, VelocityConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_DELAY, TimeUnit.MILLISECONDS);
				}
			}
		}
	}
	
	private boolean allowedServer(String serverName) {
		boolean ret = true;
		if (VelocityConfigMain.ADDITIONAL_FOLLOW_BLOCKEDSERVERS.contains(serverName))
			ret = false;
		else {
			for (String regex : VelocityConfigMain.ADDITIONAL_FOLLOW_BLOCKEDSERVERS) {
				if (!ret)
					break;
				try {
					if (serverName.matches(regex))
						ret = false;
				} catch (Exception ex) {
					plugin.getLoggerManager().logError(PartiesConstants.DEBUG_FOLLOW_SERVER_REGEXERROR, ex);
				}
			}
		}
		return ret;
	}
}
