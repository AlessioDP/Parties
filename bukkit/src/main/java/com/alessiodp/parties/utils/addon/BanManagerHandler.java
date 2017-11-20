package com.alessiodp.parties.utils.addon;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;
import com.alessiodp.parties.utils.enums.ConsoleColors;
import com.alessiodp.parties.utils.enums.LogLevel;
import com.alessiodp.partiesapi.events.PartiesPartyPostDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPartyPreDeleteEvent;
import com.alessiodp.partiesapi.events.PartiesPlayerLeaveEvent;

import me.confuser.banmanager.BanManager;
import me.confuser.banmanager.BmAPI;
import me.confuser.banmanager.bukkitutil.listeners.Listeners;
import me.confuser.banmanager.data.PlayerData;
import me.confuser.banmanager.events.PlayerBannedEvent;

public class BanManagerHandler extends Listeners<BanManager>{
	Parties plugin;
	
	public BanManagerHandler(Parties instance) {
		plugin = instance;
	}
	
	public static boolean isMuted(Player pl) {
		return BmAPI.isMuted(pl);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerBan(PlayerBannedEvent event) {
		PlayerData pl = event.getBan().getPlayer();
		String partyname = plugin.getDataHandler().getPlayerPartyName(pl.getUUID());
		if (!partyname.isEmpty()) {
			Party party = plugin.getPartyHandler().getParty(partyname);
			if (party != null) {
				// Calling API event
				PartiesPlayerLeaveEvent partiesLeaveEvent = new PartiesPlayerLeaveEvent(pl.getPlayer(), party.getName(), true, event.getBan().getActor().getUUID());
				Bukkit.getServer().getPluginManager().callEvent(partiesLeaveEvent);
				if (!partiesLeaveEvent.isCancelled()) {
					if (party.getLeader().equals(pl.getUUID())) {
						// Calling Pre API event
						PartiesPartyPreDeleteEvent partiesPreDeleteEvent = new PartiesPartyPreDeleteEvent(party.getName(), PartiesPartyPreDeleteEvent.DeleteCause.BAN, pl.getUUID(), event.getBan().getActor().getPlayer());
						Bukkit.getServer().getPluginManager().callEvent(partiesPreDeleteEvent);
						if (!partiesPreDeleteEvent.isCancelled()) {
							party.sendBroadcastParty((OfflinePlayer)pl.getPlayer(), Messages.leave_disbanded);
							
							party.removeParty();
							// Calling Post API event
							PartiesPartyPostDeleteEvent partiesPostDeleteEvent = new PartiesPartyPostDeleteEvent(party.getName(), PartiesPartyPostDeleteEvent.DeleteCause.BAN, pl.getUUID(), event.getBan().getActor().getPlayer());
							Bukkit.getServer().getPluginManager().callEvent(partiesPostDeleteEvent);
							
							LogHandler.log(LogLevel.BASIC, "Party " + party.getName() + " deleted because leader got banned, by: " + pl.getName(), true, ConsoleColors.CYAN);
						} else {
							// Event is cancelled, block ban chain
							LogHandler.log(LogLevel.DEBUG, "PartiesDeleteEvent is cancelled, ignoring delete of " + party.getName(), true);
							return;
						}
					} else {
						party.getMembers().remove(pl.getUUID());
						party.remOnlinePlayer(pl.getPlayer());
						
						party.sendBroadcastParty((OfflinePlayer)pl.getPlayer(), Messages.kick_kickedplayer);
						
						party.updateParty();
					}
				} else
					LogHandler.log(LogLevel.DEBUG, "PartiesLeaveEvent is cancelled, ignoring ban of " + pl.getName(), true);
			}
			plugin.getDataHandler().removePlayer(pl.getUUID());
		}
	}
}
