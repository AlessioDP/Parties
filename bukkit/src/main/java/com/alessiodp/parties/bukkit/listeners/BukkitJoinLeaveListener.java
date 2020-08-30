package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.listeners.JoinLeaveListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitJoinLeaveListener extends JoinLeaveListener implements Listener {
	
	public BukkitJoinLeaveListener(PartiesPlugin instance) {
		super(instance);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		super.onPlayerJoin(new BukkitUser(plugin, event.getPlayer()));
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		BukkitUser user = new BukkitUser(plugin, event.getPlayer());
		super.onPlayerQuit(user);
		
		// Remove home count if active on the player
		if (BukkitConfigParties.ADDITIONAL_HOME_ENABLE) {
			BukkitPartyPlayerImpl pp = (BukkitPartyPlayerImpl) plugin.getPlayerManager().getPlayer(user.getUUID());
			if (pp.getHomeTeleporting() != null) {
				pp.getHomeTeleporting().cancel();
			}
		}
	}
}
