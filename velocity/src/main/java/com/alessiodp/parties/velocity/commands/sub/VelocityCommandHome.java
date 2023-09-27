package com.alessiodp.parties.velocity.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.velocity.bootstrap.ADPVelocityBootstrap;
import com.alessiodp.core.velocity.user.VelocityUser;
import com.alessiodp.parties.api.events.common.player.IPlayerPostHomeEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreHomeEvent;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandHome;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyHomeImpl;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.tasks.HomeDelayTask;
import com.alessiodp.parties.velocity.configuration.data.VelocityConfigParties;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;
import com.alessiodp.parties.velocity.tasks.VelocityHomeDelayTask;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.concurrent.TimeUnit;

public class VelocityCommandHome extends CommandHome {
	
	public VelocityCommandHome(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected void teleportPlayer(User player, PartyPlayerImpl partyPlayer, PartyHomeImpl home) {
		teleportToPartyHome((PartiesPlugin) plugin, player, partyPlayer, home);
	}
	
	@Override
	protected HomeDelayTask teleportPlayerWithDelay(PartyPlayerImpl partyPlayer, PartyHomeImpl home, int delay) {
		return new VelocityHomeDelayTask(
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
			RegisteredServer server = ((ADPVelocityBootstrap) plugin.getBootstrap()).getServer().getServer(home.getServer()).orElse(null);
			
			if (server == null) {
				plugin.getLoggerManager().logError(String.format(PartiesConstants.DEBUG_CMD_HOME_NO_SERVER, home));
				return;
			}
			
			IPlayerPreHomeEvent partiesPreHomeEvent = plugin.getEventManager().preparePlayerPreHomeEvent(partyPlayer, party, home);
			plugin.getEventManager().callEvent(partiesPreHomeEvent);
			if (!partiesPreHomeEvent.isCancelled()) {
				if (((VelocityUser) player).getServer() == null)
					return; // Cannot get player server
				
				if (VelocityConfigParties.ADDITIONAL_HOME_CROSS_SERVER && !((VelocityUser) player).getServerName().equalsIgnoreCase(home.getServer())) {
					((VelocityUser) player).connectTo(server);
					
					serverChange = true;
				}
				
				String message = plugin.getMessageUtils().convertPlaceholders(Messages.ADDCMD_HOME_TELEPORTED, partyPlayer, party);
				if (serverChange) {
					plugin.getScheduler().scheduleAsyncLater(() -> ((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
									.sendHomeTeleport(player, home, message, server),
							VelocityConfigParties.ADDITIONAL_HOME_CROSS_SERVER_DELAY, TimeUnit.MILLISECONDS);
				} else {
					((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher())
							.sendHomeTeleport(player, home, message, server);
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