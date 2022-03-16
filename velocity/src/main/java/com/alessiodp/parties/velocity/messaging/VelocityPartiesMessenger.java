package com.alessiodp.parties.velocity.messaging;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.messaging.ADPMessenger;

public class VelocityPartiesMessenger extends ADPMessenger {
	public VelocityPartiesMessenger(ADPPlugin plugin) {
		super(plugin);
		messageDispatcher = new VelocityPartiesMessageDispatcher(plugin);
		messageListener = new VelocityPartiesMessageListener(plugin);
	}
	
	@Override
	public void reload() {
		messageDispatcher.register();
		messageListener.register();
	}
}
