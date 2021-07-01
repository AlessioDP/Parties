package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.parties.api.events.common.player.IPlayerPostTeleportEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreTeleportEvent;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyTeleportRequest;
import com.alessiodp.parties.bukkit.tasks.BukkitTeleportDelayTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class BukkitCommandTeleport extends CommandTeleport {
	
	public BukkitCommandTeleport(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public void performTeleport(PartyImpl party, PartyPlayerImpl player, int delay) {
		if (ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE)
			player.sendMessage(Messages.ADDCMD_TELEPORT_ACCEPT_REQUEST_SENT);
		else
			player.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTING);
		
		for (PartyPlayer onlinePlayer : party.getOnlineMembers(true)) {
			if (!onlinePlayer.getPlayerUUID().equals(player.getPlayerUUID())) {
				if (ConfigParties.ADDITIONAL_TELEPORT_ACCEPT_REQUEST_ENABLE) {
					BukkitPartyTeleportRequest request = new BukkitPartyTeleportRequest((PartiesPlugin) plugin, (PartyPlayerImpl) onlinePlayer, player);
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
	
	@Override
	public void teleportSinglePlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer) {
		Player bukkitTargetPlayer = Bukkit.getPlayer(targetPlayer.getPlayerUUID());
		if (bukkitTargetPlayer != null) {
			teleportSinglePlayer(
					plugin, player, targetPlayer,
					Bukkit.getPlayer(targetPlayer.getPlayerUUID()).getLocation()
			);
		}
	}
	
	@Override
	public BukkitTeleportDelayTask teleportSinglePlayerWithDelay(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer, int delay) {
		return new BukkitTeleportDelayTask(
				plugin,
				player,
				delay,
				targetPlayer
				
		);
	}
	
	public static void teleportSinglePlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl targetPlayer, Location location) {
		BukkitUser bukkitUser = (BukkitUser) plugin.getPlayer(player.getPlayerUUID());
		Player bukkitTargetPlayer = Bukkit.getPlayer(targetPlayer.getPlayerUUID());
		if (bukkitUser != null && bukkitTargetPlayer != null) {
			player.sendMessage(Messages.ADDCMD_HOME_TELEPORTED, targetPlayer);
			
			PartyImpl party = plugin.getPartyManager().getParty(player.getPartyId());
			IPlayerPreTeleportEvent partiesPreTeleportEvent = plugin.getEventManager().preparePlayerPreTeleportEvent(player, party, location);
			plugin.getEventManager().callEvent(partiesPreTeleportEvent);
			if (!partiesPreTeleportEvent.isCancelled()) {
				plugin.getScheduler().getSyncExecutor().execute(() -> {
					bukkitUser.teleportAsync(location).thenAccept(result -> {
						if (result) {
							EssentialsHandler.updateLastTeleportLocation(bukkitUser.getUUID());
							player.sendMessage(Messages.ADDCMD_TELEPORT_PLAYER_TELEPORTED, targetPlayer);
							
							IPlayerPostTeleportEvent partiesPostTeleportEvent = plugin.getEventManager().preparePlayerPostTeleportEvent(player, party, location);
							plugin.getEventManager().callEvent(partiesPostTeleportEvent);
						} else {
							plugin.getLoggerManager().printError(PartiesConstants.DEBUG_TELEPORT_ASYNC);
						}
					});
				});
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_TELEPORTEVENT_DENY,
						player.getName(), party.getName() != null ? party.getName() : "_"), true);
		}
	}
}
