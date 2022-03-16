package com.alessiodp.parties.bungeecord.addons.external;

import com.alessiodp.core.bungeecord.addons.external.RedisBungeeHandler;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bungeecord.messaging.BungeePartiesMessageDispatcher;
import org.jetbrains.annotations.NotNull;

public class PartiesRedisBungeeHandler extends RedisBungeeHandler {
	public PartiesRedisBungeeHandler(@NotNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void sendMessage(@NotNull User user, @NotNull String message, boolean colorTranslation) {
		if (active) {
			((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRedisMessage(user, message, colorTranslation);
		}
	}
	
	@Override
	public void sendTitle(@NotNull User user, @NotNull String message, int fadeInTime, int showTime, int fadeOutTime) {
		if (active) {
			((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRedisTitle(user, message, fadeInTime, showTime, fadeOutTime);
		}
	}
	
	@Override
	public void sendChat(@NotNull User user, @NotNull String message) {
		if (active) {
			((BungeePartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRedisChat(user, message);
		}
	}
}
