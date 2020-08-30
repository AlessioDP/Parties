package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BukkitFollowListener implements Listener {
	private final PartiesPlugin plugin;
	
	@EventHandler (ignoreCancelled = true)
	public void onEntityPortalEvent(PlayerPortalEvent event) {
		if (!event.isCancelled()) {
			BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
			
			if (pp.isPortalPause()) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler (ignoreCancelled = true)
	public void onChangeWorld(PlayerChangedWorldEvent event) {
		if (BukkitConfigMain.ADDITIONAL_FOLLOW_ENABLE) {
			// Make it async
			Player bukkitPlayer = event.getPlayer();
			User user = plugin.getPlayer(bukkitPlayer.getUniqueId());
			plugin.getScheduler().runAsync(() -> {
				if (BukkitConfigMain.ADDITIONAL_FOLLOW_LISTWORLDS.contains("*")
						|| BukkitConfigMain.ADDITIONAL_FOLLOW_LISTWORLDS.contains(bukkitPlayer.getWorld().getName())) {
					BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(bukkitPlayer.getUniqueId());
					PartyImpl party = plugin.getPartyManager().getParty(pp.getPartyId());
					if (party != null
							&& party.isFollowEnabled()
							&& pp.getRank() >= BukkitConfigMain.ADDITIONAL_FOLLOW_RANKNEEDED) {
						// Init teleport
						for (PartyPlayer pl : party.getOnlineMembers(true)) {
							Player bukkitPl = Bukkit.getPlayer(pl.getPlayerUUID());
							if (bukkitPl != null
									&& !pl.getPlayerUUID().equals(bukkitPlayer.getUniqueId())
									&& bukkitPl.getWorld().equals(event.getFrom())) {
								BukkitPartyPlayerImpl ppVictim = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(pl.getPlayerUUID());
								
								if (ppVictim.getRank() >= BukkitConfigMain.ADDITIONAL_FOLLOW_RANKMINIMUM) {
									// Make it sync
									plugin.getScheduler().getSyncExecutor().execute(() -> {
										ppVictim.setPortalPause(true);
										
										plugin.getScheduler().scheduleAsyncLater(() -> {
											ppVictim.setPortalPause(false);
										}, BukkitConfigMain.ADDITIONAL_FOLLOW_TIMEOUT * 50, TimeUnit.MILLISECONDS);
										
										user.sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.OTHER_FOLLOW_WORLD
												.replace("%player%", bukkitPlayer.getName())
												.replace("%world%", bukkitPlayer.getWorld().getName()), ppVictim, party), true);
										
										
										switch (BukkitConfigMain.ADDITIONAL_FOLLOW_TYPE) {
											case 1:
												bukkitPl.teleport(bukkitPlayer.getWorld().getSpawnLocation());
												break;
											case 2:
												bukkitPl.teleport(bukkitPlayer);
												break;
											default:
												// Nothing to do
												break;
										}
									});
								}
							}
						}
					}
				}
			});
		}
	}
}
