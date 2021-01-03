package com.alessiodp.parties.bukkit.addons.external;

import java.util.UUID;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
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
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				BukkitConfigMain.ADDONS_BANMANAGER_ENABLE = false;
				active = false;
				
				plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
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
			if (party != null && !ConfigParties.GENERAL_MEMBERS_ON_LEAVE_KICK_FROM_PARTY) {
				// If not handled by on leave kick, handle it
				party.memberLeftTimeout(partyPlayer, 0);
			}
		}
	}
}
