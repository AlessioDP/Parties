package com.alessiodp.parties.bungeecord.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPMessenger;

public class BungeePartiesMessenger extends ADPMessenger {
	public BungeePartiesMessenger(ADPPlugin plugin) {
		super(plugin);
		messageDispatcher = new BungeePartiesMessageDispatcher(plugin);
		messageListener = new BungeePartiesMessageListener(plugin);
	}
	
	@Override
	public void reload() {
		messageDispatcher.register();
		messageListener.register();
	}
}
