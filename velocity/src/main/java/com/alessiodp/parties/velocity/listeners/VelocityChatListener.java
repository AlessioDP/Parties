package com.alessiodp.parties.velocity.listeners;

import com.alessiodp.core.velocity.user.VelocityUser;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.listeners.ChatListener;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;

public class VelocityChatListener extends ChatListener {
	
	public VelocityChatListener(PartiesPlugin instance) {
		super(instance);
	}
	
	@Subscribe
	public void onPlayerChat(PlayerChatEvent event) {
		if (event.getResult().isAllowed()
				&& !event.getMessage().startsWith("/")) {
			boolean isCancelled = super.onPlayerChat(new VelocityUser(plugin, event.getPlayer()), event.getMessage());
			if (isCancelled)
				event.setResult(PlayerChatEvent.ChatResult.denied());
		}
	}
	
	@Subscribe
	public void onPlayerCommand(PlayerChatEvent event) {
		if (event.getResult().isAllowed()
				&& event.getMessage().startsWith("/")) {
			super.onPlayerCommandPreprocess(new VelocityUser(plugin, event.getPlayer()), event.getMessage());
		}
	}
}
