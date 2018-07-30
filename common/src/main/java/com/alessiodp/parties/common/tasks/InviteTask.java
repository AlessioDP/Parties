package com.alessiodp.parties.common.tasks;


import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.logging.LogLevel;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.parties.objects.PartyImpl;

import java.util.UUID;

public class InviteTask implements Runnable {
	private PartyImpl party;
	private UUID from;
	
	public InviteTask(PartyImpl party, UUID from) {
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
