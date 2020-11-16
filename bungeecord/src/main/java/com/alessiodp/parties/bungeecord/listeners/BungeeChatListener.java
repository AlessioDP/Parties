package com.alessiodp.parties.bungeecord.listeners;

import com.alessiodp.core.bungeecord.user.BungeeUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.listeners.ChatListener;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeChatListener extends ChatListener implements Listener {
	
	public BungeeChatListener(PartiesPlugin instance) {
		super(instance);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(ChatEvent event) {
		if (!event.isCancelled()
				&& event.getSender() instanceof ProxiedPlayer
				&& !event.getMessage().startsWith("/")) {
			boolean isCancelled = super.onPlayerChat(new BungeeUser(plugin, (ProxiedPlayer) event.getSender()), event.getMessage());
			event.setCancelled(isCancelled);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommand(ChatEvent event) {
		if (!event.isCancelled()
				&& event.getSender() instanceof ProxiedPlayer
				&& event.getMessage().startsWith("/")) {
			super.onPlayerCommandPreprocess(new BungeeUser(plugin, (ProxiedPlayer) event.getSender()), event.getMessage());
		}
	}
}
