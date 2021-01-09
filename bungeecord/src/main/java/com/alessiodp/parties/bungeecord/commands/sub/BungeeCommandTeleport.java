package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bungeecord.bootstrap.BungeePartiesBootstrap;
import com.alessiodp.parties.bungeecord.parties.objects.BungeePartyTeleportRequest;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandTeleport;
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
	
	public void performTeleport(PartyImpl party, PartyPlayerImpl player) {
		ProxiedPlayer bungeePlayer = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(player.getPlayerUUID());
		if (bungeePlayer != null) {
			ServerInfo server = bungeePlayer.getServer().getInfo();
			
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
						teleportPlayer((PartiesPlugin) plugin, (PartyPlayerImpl) onlinePlayer, player, server);
					}
				}
			}
		}
	}
	
	public static void teleportPlayer(PartiesPlugin plugin, PartyPlayerImpl player, PartyPlayerImpl playerExecutor, ServerInfo serverDestination) {
		ProxiedPlayer bungeePlayer = ((BungeePartiesBootstrap) plugin.getBootstrap()).getProxy().getPlayer(player.getPlayerUUID());
		if (bungeePlayer != null) {
			bungeePlayer.connect(serverDestination);
			player.sendMessage(Messages.ADDCMD_TELEPORT_TELEPORTED, playerExecutor);
		}
	}
}
