package com.alessiodp.parties.utils.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;

public class PartyDeleteTask extends BukkitRunnable {
	private Parties plugin;
	private String name;

	public PartyDeleteTask(String name) {
		plugin = Parties.getInstance();
		this.name = name;
	}

	public void run() {
		plugin.getPartyHandler().deleteTimedParty(name, false);
	}
	public String getName(){return name;}
}
