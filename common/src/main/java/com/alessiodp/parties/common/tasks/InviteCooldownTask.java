package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.players.objects.InviteCooldown;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InviteCooldownTask implements Runnable {
	@NonNull private final InviteCooldown inviteCooldown;
	
	@Override
	public void run() {
		inviteCooldown.cancelTask();
	}
}
