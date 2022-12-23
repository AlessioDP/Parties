package com.alessiodp.parties.bungeecord.messaging.redis;

import com.alessiodp.core.bungeecord.messaging.redis.BungeeRedisBungeeDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import org.jetbrains.annotations.NotNull;

public class BungeePartiesRedisBungeeDispatcher extends BungeeRedisBungeeDispatcher {
	public BungeePartiesRedisBungeeDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, plugin.getPluginName());
	}
}