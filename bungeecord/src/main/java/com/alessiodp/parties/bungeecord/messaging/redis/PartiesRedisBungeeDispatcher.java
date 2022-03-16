package com.alessiodp.parties.bungeecord.messaging.redis;

import com.alessiodp.core.bungeecord.messaging.redis.RedisBungeeDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import org.jetbrains.annotations.NotNull;

public class PartiesRedisBungeeDispatcher extends RedisBungeeDispatcher {
	public PartiesRedisBungeeDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, plugin.getPluginName());
	}
}