package com.alessiodp.parties.tasks;

import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.logging.LoggerManager;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.logging.LogLevel;
import com.alessiodp.parties.parties.objects.PartyEntity;

public class InviteTask extends BukkitRunnable {
	private PartyEntity party;
	private UUID from;
	
	public InviteTask(PartyEntity party, UUID from) {
		this.party = party;
		this.from = from;
	}

	@Override
	public void run() {
		party.cancelInvite(from);
		LoggerManager.log(LogLevel.DEBUG, Constants.DEBUG_TASK_INVITE_EXPIRED
				.replace("{party}", party.getName())
				.replace("{uuid}", from.toString()), true);
	}
}
