package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreTeleportEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.bungeecord.parties.objects.BungeePartyTeleportRequest;
import com.alessiodp.parties.bungeecord.tasks.BungeeTeleportDelayTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class BungeeCommandTeleport extends CommandTeleport {
	
	public BungeeCommandTeleport(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	public void performTeleport(PartyImpl party, PartyPlayerImpl player, int delay) {
		ProxiedPlayer bungeePlayer = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(player.getPlayerUUID());
		if (bungeePlayer != null) {
			if (ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE)
				player.sendMessage(Messages.ADDCMD_TELEPORT_ACCEPT_REQUEST_SENT);
			else
				player.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTING);
			
			for (PartyPlayer onlinePlayer : party.getOnlineMembers(true)) {
				if (!onlinePlayer.getPlayerUUID().equals(player.getPlayerUUID())) {
					if (ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE) {
						BungeePartyTeleportRequest request = new BungeePartyTeleportRequest((PartiesPlugin) plugin, (PartyPlayerImpl) onlinePlayer, player);
						((PartyPlayerImpl) onlinePlayer).getPendingTeleportRequests().add(request);
						
						((PartyPlayerImpl) onlinePlayer).sendMessage(Messages.ADDCMD_TELEPORT_ACCEPT_REQUEST_RECEIVED, player, party);
						
						plugin.getScheduler().scheduleAsyncLater(
								request::timeout,
								ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_TIME,
								TimeUnit.SECONDS
						);
					} else {
						handleSinglePlayerTeleport((PartiesPlugin) plugin, (PartyPlayerImpl) onlinePlayer, player, delay);
					}
				}
			}
		}
	}
	
	@Override
	public void teleportSinglePlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer) {
		ProxiedPlayer bungeeTargetPlayer = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(targetPlayer.getPlayerUUID());
		if (bungeeTargetPlayer != null) {
			teleportSinglePlayer(
					plugin, player, targetPlayer,
					bungeeTargetPlayer.getServer().getInfo()
			);
		}
	}
	
	@Override
	public BungeeTeleportDelayTask teleportSinglePlayerWithDelay(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer, int delay) {
		return new BungeeTeleportDelayTask(
				plugin,
				player,
				delay,
				targetPlayer
		
		);
	}
	
	public static void teleportSinglePlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer, ServerInfo serverInfo) {
		ProxiedPlayer bungeePlayer = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(player.getPlayerUUID());
		if (bungeePlayer != null) {
			boolean serverChange = false;
			PartyImpl party = plugin.getPartyManager().getParty(player.getPartyId());
			IPlayerPreTeleportEvent partiesPreTeleportEvent = plugin.getEventManager().preparePlayerPreTeleportEvent(player, party, serverInfo);
			plugin.getEventManager().callEvent(partiesPreTeleportEvent);
			if (!partiesPreTeleportEvent.isCancelled()) {
				if (!bungeePlayer.getServer().getInfo().equals(serverInfo)) {
					serverChange = true;
					bungeePlayer.connect(serverInfo);
				}
				
				if (serverChange) {
					plugin.getScheduler().scheduleAsyncLater(() -> ((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
									.sendTeleport(plugin.getPlayer(player.getPlayerUUID()), targetPlayer),
							BungeeConfigParties.ADDITIONAL_TELEPORT_EXACT_LOCATION_DELAY, TimeUnit.MILLISECONDS);
					
				} else {
					((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
							.sendTeleport(plugin.getPlayer(player.getPlayerUUID()), targetPlayer);
				}
				
				player.sendMessage(Messages.ADDCMD_TELEPORT_PLAYER_TELEPORTED, targetPlayer);
				
				IPlayerPostTeleportEvent partiesPostTeleportEvent = plugin.getEventManager().preparePlayerPostTeleportEvent(player, party, serverInfo);
				plugin.getEventManager().callEvent(partiesPostTeleportEvent);
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_TELEPORTEVENT_DENY,
						player.getName(), party.getName() != null ? party.getName() : "_"), true);
		}
	}
}
