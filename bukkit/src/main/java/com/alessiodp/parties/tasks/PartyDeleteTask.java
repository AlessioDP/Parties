package com.alessiodp.parties.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.Parties;

import lombok.Getter;

public class PartyDeleteTask extends BukkitRunnable {
	private Parties plugin;
	@Getter private String name;
	
	public PartyDeleteTask(String name) {
		plugin = Parties.getInstance();
		this.name = name;
	}

	@Override
	public void run() {
		plugin.getPartyManager().deleteTimedParty(name, false);
	}
}
