package com.alessiodp.parties.bukkit.addons.external;

import java.util.UUID;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.api.enums.DeleteCause;
import com.alessiodp.parties.api.events.common.party.IPartyPostDeleteEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreDeleteEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.confuser.banmanager.bukkit.api.events.PlayerBannedEvent;
import me.confuser.banmanager.common.api.BmAPI;
import me.confuser.banmanager.common.data.PlayerData;
import org.bukkit.Bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class BanManagerHandler implements Listener {
	@NonNull private final PartiesPlugin plugin;
	private static final String ADDON_NAME = "BanManager";
	private static boolean active;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ADDONS_BANMANAGER_ENABLE) {
			if (Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getPluginManager().registerEvents(this, (BukkitPartiesBootstrap) plugin.getBootstrap());
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
						.replace("{addon}", ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDONS_BANMANAGER_ENABLE = false;
				active = false;
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_FAILED
						.replace("{addon}", ADDON_NAME), true);
			}
		}
	}
	
	public static boolean isMuted(UUID uuid) {
		if (active) {
			return BmAPI.isMuted(uuid);
		}
		return false;
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerBan(PlayerBannedEvent event) {
		if (BukkitConfigMain.ADDONS_BANMANAGER_AUTOKICK) {
			PlayerData pl = event.getBan().getPlayer();
			PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(pl.getUUID());
			
			// Party checking
			PartyImpl party = plugin.getPartyManager().getParty(partyPlayer.getPartyId());
			if (party != null) {
				PartyPlayerImpl kickerPp = plugin.getPlayerManager().getPlayer(event.getBan().getActor().getUUID());
				
				// Calling API event
				IPlayerPreLeaveEvent partiesPreLeaveEvent = plugin.getEventManager().preparePlayerPreLeaveEvent(partyPlayer, party, true, kickerPp);
				plugin.getEventManager().callEvent(partiesPreLeaveEvent);
				
				if (!partiesPreLeaveEvent.isCancelled()) {
					if (party.getLeader() != null && party.getLeader().equals(pl.getUUID())) {
						boolean mustDelete = true;
						// Check if leader can be changed
						if (ConfigParties.GENERAL_MEMBERS_CHANGE_LEADER_ON_LEAVE
								&& party.getMembers().size() > 1) {
							PartyPlayerImpl newLeader = party.findNewLeader();
							
							if (newLeader != null) {
								// Found a new leader
								mustDelete = false;
								
								party.changeLeader(newLeader);
								party.removeMember(partyPlayer);
								
								party.broadcastMessage(Messages.MAINCMD_LEAVE_LEADER_CHANGED, newLeader);
							}
						}
						
						if (mustDelete) {
							// Calling Pre API event
							IPartyPreDeleteEvent partiesPreDeleteEvent = plugin.getEventManager().preparePartyPreDeleteEvent(party, DeleteCause.BAN, partyPlayer, kickerPp);
							plugin.getEventManager().callEvent(partiesPreDeleteEvent);
							
							if (!partiesPreDeleteEvent.isCancelled()) {
								party.broadcastMessage(Messages.MAINCMD_LEAVE_DISBANDED, partyPlayer);
								
								party.delete();
								// Calling Post API event
								IPartyPostDeleteEvent partiesPostDeleteEvent = plugin.getEventManager().preparePartyPostDeleteEvent(party.getName(), DeleteCause.BAN, partyPlayer, kickerPp);
								plugin.getEventManager().callEvent(partiesPostDeleteEvent);
								
								plugin.getLoggerManager().log(String.format(PartiesConstants.DEBUG_LIB_BANMANAGER_BAN, party.getId().toString(), pl.getName()), true);
							} else {
								// Event is cancelled, block ban chain
								plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_DELETEEVENT_DENY_GENERIC, party.getId().toString()), true);
								return;
							}
						}
					} else {
						party.removeMember(partyPlayer);
						
						party.broadcastMessage(Messages.MAINCMD_KICK_BROADCAST, partyPlayer);
					}
					
					// Calling API event
					IPlayerPostLeaveEvent partiesPostLeaveEvent = plugin.getEventManager().preparePlayerPostLeaveEvent(partyPlayer, party, true, kickerPp);
					plugin.getEventManager().callEvent(partiesPostLeaveEvent);
				} else
					plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_API_LEAVEEVENT_DENY, pl.getUUID().toString(), party.getId().toString()), true);
			}
		}
	}
}
