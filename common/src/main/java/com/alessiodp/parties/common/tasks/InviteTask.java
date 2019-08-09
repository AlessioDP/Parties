package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class InviteTask implements Runnable {
	@NonNull private final PartiesPlugin plugin;
	@NonNull private final PartyImpl party;
	@NonNull private final UUID invitedPlayer;

	@Override
	public void run() {
		party.cancelInvite(invitedPlayer);
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_TASK_INVITE_EXPIRED
				.replace("{party}", party.getName())
				.replace("{uuid}", invitedPlayer.toString()), true);
	}
}
