package com.alessiodp.parties.utils.tasks;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.handlers.LogHandler;
import com.alessiodp.parties.objects.Party;

public class InviteTask extends BukkitRunnable {
	private Party party;
	private UUID from;

	public InviteTask(Party party, UUID from) {
		this.party = party;
		this.from = from;
	}

	public void run() {
		this.party.cancelInvite(from);
		LogHandler.log(3, "Expired party invite to " + party.getName());
	}
}
