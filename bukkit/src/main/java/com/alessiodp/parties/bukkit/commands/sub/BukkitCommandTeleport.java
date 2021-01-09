package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.EssentialsHandler;
import com.alessiodp.parties.bukkit.parties.objects.BukkitPartyTeleportRequest;
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
	
	public void performTeleport(PartyImpl party, PartyPlayerImpl player) {
		Player bukkitPlayer = Bukkit.getPlayer(player.getPlayerUUID());
		if (bukkitPlayer != null) {
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
						teleportPlayer((PartiesPlugin) plugin, (PartyPlayerImpl) onlinePlayer, player, bukkitPlayer.getLocation());
					}
				}
			}
		}
	}
	
	public static void teleportPlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl playerExecutor, Location location) {
		User user = plugin.getPlayer(player.getPlayerUUID());
		if (user != null) {
			plugin.getScheduler().getSyncExecutor().execute(() -> {
				((BukkitUser) user).teleportAsync(location).thenAccept(result -> {
					if (result) {
						EssentialsHandler.updateLastTeleportLocation(user.getUUID());
						player.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTED, playerExecutor);
					} else {
						plugin.getLoggerManager().printError(PartiesConstants.DEBUG_TELEPORT_ASYNC);
					}
				});
			});
		}
	}
}
