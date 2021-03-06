package com.alessiodp.parties.bukkit.listeners;

import com.alessiodp.core.bukkit.user.BukkitUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.listeners.ChatListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BukkitChatListener extends ChatListener implements Listener {
	
	public BukkitChatListener(PartiesPlugin instance) {
		super(instance);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!event.isCancelled()) {
			boolean isCancelled = super.onPlayerChat(new BukkitUser(plugin, event.getPlayer()), event.getMessage());
			event.setCancelled(isCancelled);
		}
	}
	
	/**
	 * Auto command listener
	 */
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!event.isCancelled()) {
			super.onPlayerCommandPreprocess(new BukkitUser(plugin, event.getPlayer()), event.getMessage());
		}
	}
}
