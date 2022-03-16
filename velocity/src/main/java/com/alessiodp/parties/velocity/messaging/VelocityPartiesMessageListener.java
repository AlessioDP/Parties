package com.alessiodp.parties.velocity.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.velocity.messaging.VelocityMessageListener;
import com.alessiodp.parties.velocity.messaging.bungee.VelocityPartiesBungeecordListener;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesMessageListener extends VelocityMessageListener {
	public VelocityPartiesMessageListener(@NotNull ADPPlugin plugin) {
		super(plugin, new VelocityPartiesBungeecordListener(plugin));
	}
}
