package com.alessiodp.parties.bungeecord.commands.sub;

import com.alessiodp.core.bungeecord.user.BungeeUser;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.events.common.player.IPlayerPostHomeEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreHomeEvent;
import com.alessiodp.parties.bungeecord.configuration.data.BungeeConfigParties;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import com.alessiodp.parties.bungeecord.tasks.BungeeHomeDelayTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandHome;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeDelayTask;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.concurrent.TimeUnit;

public class BungeeCommandHome extends CommandHome {
	
	public BungeeCommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void teleportPlayer(User player, PartyPlayerImpl partyPlayer, PartyHomeImpl home) {
		teleportToPartyHome((PartiesPlugin) plugin, player, partyPlayer, home);
	}
	
	@Override
	protected HomeDelayTask teleportPlayerWithDelay(PartyPlayerImpl partyPlayer, PartyHomeImpl home, int delay) {
		return new BungeeHomeDelayTask(
				(PartiesPlugin) plugin,
				partyPlayer,
				delay,
				home
		);
	}
	
	public static void teleportToPartyHome(PartiesPlugin plugin, User player, PartyPlayerImpl partyPlayer, PartyHomeImpl home) {
		PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyId());
		if (home.getServer() != null && !home.getServer().isEmpty()) {
			boolean serverChange = false;
			ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(home.getServer());
			
			IPlayerPreHomeEvent partiesPreHomeEvent = plugin.getEventManager().preparePlayerPreHomeEvent(partyPlayer, party, home);
			plugin.getEventManager().callEvent(partiesPreHomeEvent);
			if (!partiesPreHomeEvent.isCancelled()) {
				if (((BungeeUser) player).getServer() == null)
					return; // Cannot get player server
				
				if (BungeeConfigParties.ADDITIONAL_HOME_CROSS_SERVER && !((BungeeUser) player).getServer().getName().equalsIgnoreCase(home.getServer())) {
					if (serverInfo == null) {
						plugin.getLoggerManager().logError(String.format(PartiesConstants.DEBUG_CMD_HOME_NO_SERVER, home));
						return;
					}
					
					((BungeeUser) player).connectTo(serverInfo);
					
					serverChange = true;
				}
				
				String message = plugin.getMessageUtils().convertPlaceholders(Messages.ADDCMD_HOME_TELEPORTED, partyPlayer, party);
				if (serverChange) {
					plugin.getScheduler().scheduleAsyncLater(() -> ((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
									.sendHomeTeleport(player, home, message, serverInfo),
							BungeeConfigParties.ADDITIONAL_HOME_CROSS_SERVER_DELAY, TimeUnit.MILLISECONDS);
				} else {
					((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
							.sendHomeTeleport(player, home, message, serverInfo);
				}
				
				IPlayerPostHomeEvent partiesPostHomeEvent = plugin.getEventManager().preparePlayerPostHomeEvent(partyPlayer, party, home);
				plugin.getEventManager().callEvent(partiesPostHomeEvent);
			} else
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_HOMEEVENT_DENY,
						player.getName(), party.getName() != null ? party.getName() : "_"), true);
		} else {
			plugin.getLoggerManager().logError(String.format(PartiesConstants.DEBUG_HOME_NO_SERVER, party.getId()));
		}
	}
}