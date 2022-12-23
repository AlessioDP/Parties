package com.alessiodp.parties.bungeecord.messaging;

import com.alessiodp.core.bungeecord.messaging.BungeeMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.bungeecord.messaging.bungee.BungeePartiesBungeecordListener;
import com.alessiodp.parties.bungeecord.messaging.redis.BungeePartiesRedisBungeeListener;
import org.jetbrains.annotations.NotNull;

public class BungeePartiesMessageListener extends BungeeMessageListener {
	public BungeePartiesMessageListener(@NotNull ADPPlugin plugin) {
		super(
				plugin,
				new BungeePartiesBungeecordListener(plugin),
				new BungeePartiesRedisBungeeListener(plugin)
		);
	}
}