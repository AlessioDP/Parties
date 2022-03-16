package com.alessiodp.parties.bungeecord.messaging.bungee;

import com.alessiodp.core.bungeecord.messaging.bungee.BungeeBungeecordDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import org.jetbrains.annotations.NotNull;

public class BungeePartiesBungeecordDispatcher extends BungeeBungeecordDispatcher {
	public BungeePartiesBungeecordDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, false, true, false);
	}
}
