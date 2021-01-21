package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
				if (allowedWorld(event.getPlayer().getWorld().getName())) {
					PartyPlayerImpl player = plugin.getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
					PartyImpl party = plugin.getPartyManager().getParty(player.getPartyId());
					if (party != null
							&& party.isFollowEnabled()
							&& (party.getLeader() != null && party.getLeader().equals(player.getPlayerUUID()))) {
						// Let other players follow him
						sendMembers(party, player, bukkitPlayer, event.getFrom());
					}
				}
			});
		}
	}
	
	private void sendMembers(PartyImpl party, PartyPlayerImpl player, Player bukkitPlayer, World oldWorld) {
		for (PartyPlayer member : party.getOnlineMembers(true)) {
			BukkitUser memberUser = (BukkitUser) plugin.getPlayer(member.getPlayerUUID());
			Player bukkitMember = Bukkit.getPlayer(member.getPlayerUUID());
			if (bukkitMember != null
					&& !bukkitMember.getUniqueId().equals(bukkitPlayer.getUniqueId())
					&& bukkitMember.getWorld().equals(oldWorld)) {
				plugin.getScheduler().getSyncExecutor().execute(() -> {
					((BukkitPartyPlayerImpl) member).setPortalPause(true);
					
					plugin.getScheduler().scheduleAsyncLater(() -> {
						((BukkitPartyPlayerImpl) member).setPortalPause(false);
					}, BukkitConfigMain.ADDITIONAL_FOLLOW_TIMEOUT * 50L, TimeUnit.MILLISECONDS);
					
					((BukkitPartyPlayerImpl) member).sendMessage(plugin.getMessageUtils().convertPlaceholders(BukkitMessages.OTHER_FOLLOW_WORLD
							.replace("%world%", bukkitPlayer.getWorld().getName()), player, party));
					
					Location destination = BukkitConfigMain.ADDITIONAL_FOLLOW_TELEPORT_TO_SAME_LOCATION ? bukkitPlayer.getLocation() : bukkitPlayer.getWorld().getSpawnLocation();
					
					memberUser.teleportAsync(destination).thenRun(() -> {
						if (BukkitConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_ENABLE) {
							// Schedule it later
							plugin.getScheduler().scheduleAsyncLater(() -> {
								for (String command : BukkitConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_COMMANDS) {
									plugin.getScheduler().getSyncExecutor().execute(() -> bukkitMember.performCommand(plugin.getMessageUtils().convertPlaceholders(command, (PartyPlayerImpl) member, party)));
								}
							}, BukkitConfigMain.ADDITIONAL_FOLLOW_PERFORMCMD_DELAY, TimeUnit.MILLISECONDS);
						}
					});
				});
			}
		}
	}
	
	private boolean allowedWorld(String serverName) {
		boolean ret = true;
		if (BukkitConfigMain.ADDITIONAL_FOLLOW_BLOCKEDWORLDS.contains(serverName))
			ret = false;
		else {
			for (String regex : BukkitConfigMain.ADDITIONAL_FOLLOW_BLOCKEDWORLDS) {
				if (!ret)
					break;
				try {
					if (serverName.matches(regex))
						ret = false;
				} catch (Exception ex) {
					plugin.getLoggerManager().printError(PartiesConstants.DEBUG_FOLLOW_WORLD_REGEXERROR);
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}
}
