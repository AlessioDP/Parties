package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.bukkit.messaging.BukkitMessageListener;
import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.parties.bukkit.messaging.bungee.BukkitPartiesBungeecordListener;
import org.jetbrains.annotations.NotNull;

public class BukkitPartiesMessageListener extends BukkitMessageListener {
	public BukkitPartiesMessageListener(@NotNull ADPPlugin plugin) {
		super(plugin, new BukkitPartiesBungeecordListener(plugin));
	}
}
