package com.alessiodp.parties.bungeecord.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPMessenger;

public class BungeePartiesMessenger extends ADPMessenger {
	public BungeePartiesMessenger(ADPPlugin plugin) {
		super(plugin, false);
	}
	
	@Override
	public void reload() {
		// Nothing to do
	}
}
