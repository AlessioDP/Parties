package com.alessiodp.parties.addons.external.hooks;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.Messages;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.ConsoleColor;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerLeaveEvent;

import me.confuser.banmanager.BanManager;
import me.confuser.banmanager.BmAPI;
import me.confuser.banmanager.bukkitutil.listeners.Listeners;
import me.confuser.banmanager.data.PlayerData;
import me.confuser.banmanager.events.PlayerBannedEvent;

public class BMHook extends Listeners<BanManager> {
	private Parties plugin;
	
	public BMHook(Parties instance) {
		plugin = instance;
		register();
	}
	
	public boolean isMuted(UUID uuid) {
		return BmAPI.isMuted(uuid);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerBan(PlayerBannedEvent event) {
		PlayerData pl = event.getBan().getPlayer();
		PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(pl.getUUID());
		
		// Spy listener
		if (pp.isSpy())
			plugin.getSpyManager().removeSpy(pp.getPlayerUUID());
		
		// Party checking
		if (!pp.getPartyName().isEmpty()) {
			PartyEntity party = plugin.getPartyManager().getParty(pp.getPartyName());
			if (party != null) {
				PartyPlayerEntity kickerPp = plugin.getPlayerManager().getPlayer(event.getBan().getActor().getUUID());
				
				// Calling API event
				PartiesPlayerLeaveEvent partiesLeaveEvent = new PartiesPlayerLeaveEvent(pp, party, true, kickerPp);
				Bukkit.getServer().getPluginManager().callEvent(partiesLeaveEvent);
				if (!partiesLeaveEvent.isCancelled()) {
					if (party.getLeader().equals(pl.getUUID())) {
						// Calling Pre API event
						PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party, PartiesPartyPreDeleteEvent.DeleteCause.BAN, pp, kickerPp);
						Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
						if (!partiesPreDeleteEvent.isCancelled()) {
							party.sendBroadcast(pp, Messages.MAINCMD_LEAVE_DISBANDED);
							
							party.removeParty();
							// Calling Post API event
							PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.BAN, pp, kickerPp);
							Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
							
							LoggerManager.log(LogLevel.BASIC, Constants.DEBUG_LIB_BANMANAGER_BAN
									.replace("{party}", party.getName())
									.replace("{player}", pl.getName()), true, ConsoleColor.CYAN);
						} else {
							// Event is cancelled, block ban chain
							LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_DELETEEVENT_DENY_GENERIC
									.replace("{party}", party.getName()), true);
							return;
						}
					} else {
						party.getMembers().remove(pl.getUUID());
						party.getOnlinePlayers().remove(pl.getPlayer());
						
						party.sendBroadcast(pp, Messages.MAINCMD_KICK_BROADCAST);
						
						party.updateParty();
					}
				} else
					LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_API_LEAVEEVENT_DENY
							.replace("{player}", pl.getName())
							.replace("{party}", party.getName()), true);
			}
			pp.cleanupPlayer(true);
		}
	}
}
