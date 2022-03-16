package com.alessiodp.parties.velocity.messaging.bungee;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.velocity.messaging.bungee.VelocityBungeecordDispatcher;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesBungeecordDispatcher extends VelocityBungeecordDispatcher {
	public VelocityPartiesBungeecordDispatcher(@NotNull ADPPlugin plugin) {
		super(plugin, false, true, false);
	}
}
