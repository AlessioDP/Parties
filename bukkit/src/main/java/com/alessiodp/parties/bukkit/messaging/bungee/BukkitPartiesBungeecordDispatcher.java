package com.alessiodp.parties.bukkit.messaging.bungee;

import com.alessiodp.core.bukkit.messaging.bungee.BukkitBungeecordDispatcher;
import com.alessiodp.core.common.ADPPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesBungeecordDispatcher extends BukkitBungeecordDispatcher {
	public BukkitPartiesBungeecordDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, true, false, false);
	}
}
