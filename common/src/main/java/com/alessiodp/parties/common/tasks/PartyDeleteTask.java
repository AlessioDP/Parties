package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;

public class PartyDeleteTask implements Runnable {
	private PartiesPlugin plugin;
	private String name;
	
	public PartyDeleteTask(PartiesPlugin instance, String name) {
		plugin = instance;
		this.name = name;
	}

	@Override
	public void run() {
		plugin.getPartyManager().deleteTimedParty(name, false);
	}
}
