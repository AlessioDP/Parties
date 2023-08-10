package com.alessiodp.parties.velocity.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreTeleportEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.bootstrap.VelocityPartiesBootstrap;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigParties;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;
import com.alessiodp.parties.velocity.parties.objects.VelocityPartyTeleportRequest;
import com.alessiodp.parties.velocity.tasks.VelocityTeleportDelayTask;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VelocityCommandTeleport extends CommandTeleport {
	
	public VelocityCommandTeleport(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	public void performTeleport(@NotNull PartyImpl party, @NotNull PartyPlayerImpl player, int delay) {
		Player velocityPlayer = ((VelocityPartiesBootstrap) plugin.getBootstrap()).getServer().getPlayer(player.getPlayerUUID()).orElse(null);
		if (velocityPlayer != null) {
			if (ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE)
				player.sendMessage(Messages.ADDCMD_TELEPORT_ACCEPT_REQUEST_SENT);
			else
				player.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTING);
			
			for (PartyPlayer onlinePlayer : party.getOnlineMembers(true)) {
				if (!onlinePlayer.getPlayerUUID().equals(player.getPlayerUUID())) {
					if (ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE) {
						VelocityPartyTeleportRequest request = new VelocityPartyTeleportRequest((PartiesPlugin) plugin, (PartyPlayerImpl) onlinePlayer, player);
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
	public void teleportSinglePlayer(@NotNull PartiesPlugin plugin, @NotNull PartyPlayerImpl player, @NotNull PartyPlayerImpl targetPlayer) {
		((VelocityPartiesBootstrap) plugin.getBootstrap()).getServer().getPlayer(targetPlayer.getPlayerUUID()).ifPresent(velocityTargetPlayer -> {
			velocityTargetPlayer.getCurrentServer().ifPresent(serverConnection -> {
				teleportSinglePlayer(
						plugin, player, targetPlayer,
						serverConnection.getServer()
				);
			});
		});
	}
	
	@Override
	public VelocityTeleportDelayTask teleportSinglePlayerWithDelay(@NotNull PartiesPlugin plugin, @NotNull PartyPlayerImpl player, @NotNull PartyPlayerImpl targetPlayer, int delay) {
		return new VelocityTeleportDelayTask(
				plugin,
				player,
				delay,
				targetPlayer
		
		);
	}
	
	public static void teleportSinglePlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer, RegisteredServer server) {
		Player velocityPlayer = ((VelocityPartiesBootstrap) plugin.getBootstrap()).getServer().getPlayer(player.getPlayerUUID()).orElse(null);
		if (velocityPlayer != null) {
			PartyImpl party = plugin.getPartyManager().getParty(player.getPartyId());
			IPlayerPreTeleportEvent partiesPreTeleportEvent = plugin.getEventManager().preparePlayerPreTeleportEvent(player, party, server);
			plugin.getEventManager().callEvent(partiesPreTeleportEvent);
			if (!partiesPreTeleportEvent.isCancelled()) {
				velocityPlayer.getCurrentServer().ifPresent(serverConnection1 -> {
					boolean serverChange = false;
					
					if (!serverConnection1.getServer().equals(server)) {
						serverChange = true;
						velocityPlayer.createConnectionRequest(server);
					}
					
					if (VelocityConfigParties.ADDITIONAL_TELEPORT_EXACT_LOCATION || serverChange) {
						// Send message & event only if server is changed or exact location is enabled
						User velocityUser = plugin.getPlayer(player.getPlayerUUID());
						if (velocityUser != null) {
							if (VelocityConfigParties.ADDITIONAL_TELEPORT_EXACT_LOCATION) {
								// Teleports to the same location only if enabled
								if (serverChange) {
									plugin.getScheduler().scheduleAsyncLater(() -> ((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
													.sendTeleport(velocityUser, targetPlayer),
											VelocityConfigParties.ADDITIONAL_TELEPORT_EXACT_LOCATION_DELAY, TimeUnit.MILLISECONDS);
									
								} else {
									((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
											.sendTeleport(velocityUser, targetPlayer);
								}
							}
							
							player.sendMessage(Messages.ADDCMD_TELEPORT_PLAYER_TELEPORTED, targetPlayer);
							
							IPlayerPostTeleportEvent partiesPostTeleportEvent = plugin.getEventManager().preparePlayerPostTeleportEvent(player, party, server);
							plugin.getEventManager().callEvent(partiesPostTeleportEvent);
						}
					}
				});
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_TELEPORTEVENT_DENY,
						player.getName(), party.getName() != null ? party.getName() : "_"), true);
		}
	}
}
