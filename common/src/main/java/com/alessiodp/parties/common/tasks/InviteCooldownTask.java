package com.alessiodp.parties.common.tasks;

import com.alessiodp.parties.common.players.objects.InviteCooldown;

public class InviteCooldownTask implements Runnable {
	private InviteCooldown inviteCooldown;
	
	public InviteCooldownTask(InviteCooldown ic) {
		inviteCooldown = ic;
	}
	
	@Override
	public void run() {
		inviteCooldown.cancelTask();
	}
}
