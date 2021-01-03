package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.parties.bukkit.bootstrap.BukkitPartiesBootstrap;
import com.alessiodp.parties.bukkit.configuration.BukkitPartiesConfigurationManager;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.listeners.JoinLeaveListener;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
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
		super.onPlayerQuit(new BukkitUser(plugin, event.getPlayer()));
	}
	
	@Override
	protected void onJoinComplete(PartyPlayerImpl partyPlayer) {
		// If its the first player to join ask for config sync
		if (plugin.isBungeeCordEnabled()
				&& BukkitConfigMain.PARTIES_BUNGEECORD_CONFIG_SYNC
				&& ((BukkitPartiesBootstrap) plugin.getBootstrap()).getServer().getOnlinePlayers().size() == 1) {
			((BukkitPartiesConfigurationManager) plugin.getConfigurationManager()).makeConfigsRequest();
		}
	}
	
	@Override
	protected void onLeaveComplete(PartyPlayerImpl partyPlayer) {
		// Nothing to do
	}
}
