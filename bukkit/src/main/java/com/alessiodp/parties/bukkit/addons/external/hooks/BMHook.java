package com.alessiodp.parties.bukkit.addons.external.hooks;

import java.util.UUID;

import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.api.enums.DeleteCause;
import lombok.NonNull;
import org.bukkit.event.EventHandler;

import me.confuser.banmanager.BanManager;
import me.confuser.banmanager.BmAPI;
import me.confuser.banmanager.bukkitutil.listeners.Listeners;
import me.confuser.banmanager.data.PlayerData;
import me.confuser.banmanager.events.PlayerBannedEvent;

public class BMHook extends Listeners<BanManager> {
	private final PartiesPlugin plugin;
	
	public BMHook(@NonNull PartiesPlugin plugin) {
		this.plugin = plugin;
		register();
	}
	
	public boolean isMuted(UUID uuid) {
		return BmAPI.isMuted(uuid);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerBan(PlayerBannedEvent event) {
		if (BukkitConfigMain.ADDONS_BANMANAGER_AUTOKICK) {
			PlayerData pl = event.getBan().getPlayer();
			PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(pl.getUUID());
			
			// Spy listener
			if (partyPlayer.isSpy())
				plugin.getSpyManager().removeSpy(partyPlayer.getPlayerUUID());
			
			// Party checking
			if (!partyPlayer.getPartyName().isEmpty()) {
				PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyName());
				if (party != null) {
					PartyPlayerImpl kickerPp = plugin.getPlayerManager().getPlayer(event.getBan().getActor().getUUID());
					
					// Calling API event
					IPlayerPreLeaveEvent partiesPreLeaveEvent = plugin.getEventManager().preparePlayerPreLeaveEvent(partyPlayer, party, true, kickerPp);
					plugin.getEventManager().callEvent(partiesPreLeaveEvent);
					
					if (!partiesPreLeaveEvent.isCancelled()) {
						if (party.getLeader() != null && party.getLeader().equals(pl.getUUID())) {
							// Calling Pre API event
							IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.BAN, partyPlayer, kickerPp);
							plugin.getEventManager().callEvent(partiesPreDeleteEvent);
							
							if (!partiesPreDeleteEvent.isCancelled()) {
								party.broadcastMessage(Messages.MAINCMD_LEAVE_DISBANDED, partyPlayer);
								
								party.delete();
								// Calling Post API event
								IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.BAN, partyPlayer, kickerPp);
								plugin.getEventManager().callEvent(partiesPostDeleteEvent);
								
								plugin.getLoggerManager().log(PartiesConstants.DEBUG_LIB_BANMANAGER_BAN
										.replace("{party}", party.getName())
										.replace("{player}", pl.getName()), true);
							} else {
								// Event is cancelled, block ban chain
								plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_DELETEEVENT_DENY_GENERIC
										.replace("{party}", party.getName()), true);
								return;
							}
						} else {
							partyPlayer.removeFromParty(true);
							
							party.broadcastMessage(Messages.MAINCMD_KICK_BROADCAST, partyPlayer);
						}
						
						// Calling API event
						IPlayerPostLeaveEvent partiesPostLeaveEvent = plugin.getEventManager().preparePlayerPostLeaveEvent(partyPlayer, party, true, kickerPp);
						plugin.getEventManager().callEvent(partiesPostLeaveEvent);
					} else
						plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_API_LEAVEEVENT_DENY
								.replace("{player}", pl.getName())
								.replace("{party}", party.getName()), true);
				}
			}
		}
	}
}
