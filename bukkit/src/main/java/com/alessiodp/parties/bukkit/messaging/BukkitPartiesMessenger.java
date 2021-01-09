package com.alessiodp.parties.bukkit.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPMessenger;
import com.alessiodp.parties.common.PartiesPlugin;

public class BukkitPartiesMessenger extends ADPMessenger {
	public BukkitPartiesMessenger(ADPPlugin plugin) {
		super(plugin);
		messageDispatcher = new BukkitPartiesMessageDispatcher(plugin);
		messageListener = new BukkitPartiesMessageListener(plugin);
	}
	
	@Override
	public void reload() {
		if (((PartiesPlugin) plugin).isBungeeCordEnabled()) {
			messageDispatcher.register();
			messageListener.register();
		} else {
			disable();
		}
	}
}
