package com.alessiodp.parties.bukkit.addons.external.hooks;

import java.util.UUID;

import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerLeaveEvent;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.ConsoleColor;
import com.alessiodp.parties.api.enums.DeleteCause;
import org.bukkit.event.EventHandler;

import me.confuser.banmanager.BanManager;
import me.confuser.banmanager.BmAPI;
import me.confuser.banmanager.bukkitutil.listeners.Listeners;
import me.confuser.banmanager.data.PlayerData;
import me.confuser.banmanager.events.PlayerBannedEvent;

public class BMHook extends Listeners<BanManager> {
	private PartiesPlugin plugin;
	
	public BMHook(PartiesPlugin instance) {
		plugin = instance;
		register();
	}
	
	public boolean isMuted(UUID uuid) {
		return BmAPI.isMuted(uuid);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerBan(PlayerBannedEvent event) {
		if (BukkitConfigMain.ADDONS_BANMANAGER_AUTOKICK) {
			PlayerData pl = event.getBan().getPlayer();
			PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(pl.getUUID());
			
			// Spy listener
			if (pp.isSpy())
				plugin.getSpyManager().removeSpy(pp.getPlayerUUID());
			
			// Party checking
			if (!pp.getPartyName().isEmpty()) {
				PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyName());
				if (party != null) {
					PartyPlayerImpl kickerPp = plugin.getPlayerManager().getPlayer(event.getBan().getActor().getUUID());
					
					// Calling API event
					IPlayerLeaveEvent partiesLeaveEvent = plugin.getEventManager().preparePlayerLeaveEvent(pp, party, true, kickerPp);
					plugin.getEventManager().callEvent(partiesLeaveEvent);
					
					if (!partiesLeaveEvent.isCancelled()) {
						if (party.getLeader().equals(pl.getUUID())) {
							// Calling Pre API event
							IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.BAN, pp, kickerPp);
							plugin.getEventManager().callEvent(partiesPreDeleteEvent);
							
							if (!partiesPreDeleteEvent.isCancelled()) {
								party.sendBroadcast(pp, Messages.MAINCMD_LEAVE_DISBANDED);
								
								party.removeParty();
								// Calling Post API event
								IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.BAN, pp, kickerPp);
								plugin.getEventManager().callEvent(partiesPostDeleteEvent);
								
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
							party.getOnlinePlayers().remove(kickerPp);
							
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
}
