package com.alessiodp.parties.velocity.messaging.redis;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.velocity.messaging.redis.VelocityRedisBungeeDispatcher;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesRedisBungeeDispatcher extends VelocityRedisBungeeDispatcher {
	public VelocityPartiesRedisBungeeDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, plugin.getPluginName());
	}
}
