package com.alessiodp.parties.velocity.addons.external;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.velocity.addons.external.VelocityRedisBungeeHandler;
import com.alessiodp.parties.velocity.messaging.VelocityPartiesMessageDispatcher;
import org.jetbrains.annotations.NotNull;

public class PartiesVelocityRedisBungeeHandler extends VelocityRedisBungeeHandler {
	public PartiesVelocityRedisBungeeHandler(@NotNull ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public void sendMessage(@NotNull User user, @NotNull String message, boolean colorTranslation) {
		if (active) {
			((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRedisMessage(user, message, colorTranslation);
		}
	}
	
	@Override
	public void sendTitle(@NotNull User user, @NotNull String message, int fadeInTime, int showTime, int fadeOutTime) {
		if (active) {
			((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRedisTitle(user, message, fadeInTime, showTime, fadeOutTime);
		}
	}
	
	@Override
	public void sendChat(@NotNull User user, @NotNull String message) {
		if (active) {
			((VelocityPartiesMessageDispatcher) plugin.getMessenger().getMessageDispatcher()).sendRedisChat(user, message);
		}
	}
}
