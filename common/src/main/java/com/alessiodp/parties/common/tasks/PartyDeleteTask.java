package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PartyDeleteTask implements Runnable {
	@NonNull private PartiesPlugin plugin;
	@NonNull private String name;

	@Override
	public void run() {
		plugin.getPartyManager().deleteTimedParty(name, false);
	}
}
