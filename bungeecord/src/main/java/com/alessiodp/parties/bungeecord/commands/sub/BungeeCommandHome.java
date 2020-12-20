package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.bungeecord.user.BungeeUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.bungeecord.tasks.BungeeHomeTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandHome;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeTask;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.concurrent.TimeUnit;

public class BungeeCommandHome extends CommandHome {
	
	public BungeeCommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void executeHome(User player, PartyPlayerImpl partyPlayer, PartyImpl party, PartyHomeImpl home) {
		teleportToPartyHome(plugin, player, partyPlayer, party, home);
	}
	
	@Override
	protected HomeTask executeHomeWithDelay(PartyPlayerImpl partyPlayer, PartyImpl party, PartyHomeImpl home, int delay) {
		return new BungeeHomeTask(
				(PartiesPlugin) plugin,
				partyPlayer,
				party,
				delay,
				home
		);
	}
	
	public static void teleportToPartyHome(ADPPlugin plugin, User player, PartyPlayerImpl partyPlayer, PartyImpl party, PartyHomeImpl home) {
		if (!home.getServer().isEmpty()) {
			boolean serverChange = false;
			if (BungeeConfigParties.ADDITIONAL_HOME_CROSS_SERVER && !((BungeeUser) player).getServer().getName().equalsIgnoreCase(home.getServer())) {
				ServerInfo server = ProxyServer.getInstance().getServerInfo(home.getServer());
				
				if (server == null) {
					plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_CMD_HOME_NO_SERVER, home.toString()));
					return;
				}
				
				((BungeeUser) player).connectTo(server);
				
				serverChange = true;
			}
			
			String message = ((PartiesPlugin) plugin).getMessageUtils().convertPlaceholders(Messages.ADDCMD_HOME_TELEPORTED, partyPlayer, party);
			
			if (serverChange) {
				plugin.getScheduler().scheduleAsyncLater(() -> ((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendHomeTeleport(player, makeHomeTeleportRaw(home.toString(), message)), BungeeConfigParties.ADDITIONAL_HOME_CROSS_SERVER_DELAY, TimeUnit.MILLISECONDS);
			} else {
				((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendHomeTeleport(player, makeHomeTeleportRaw(home.toString(), message));
			}
		} else {
			plugin.getLoggerManager().printError(String.format(PartiesConstants.DEBUG_HOME_NO_SERVER, party.getId().toString()));
		}
	}
	
	private static byte[] makeHomeTeleportRaw(String home, String message) {
		ByteArrayDataOutput raw = ByteStreams.newDataOutput();
		raw.writeUTF(home);
		raw.writeUTF(message);
		return raw.toByteArray();
	}
}