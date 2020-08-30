package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class PartyDeleteTask implements Runnable {
	@NonNull private final PartiesPlugin plugin;
	@NonNull private final UUID id;

	@Override
	public void run() {
		plugin.getPartyManager().deleteTimedParty(id, false);
	}
}
