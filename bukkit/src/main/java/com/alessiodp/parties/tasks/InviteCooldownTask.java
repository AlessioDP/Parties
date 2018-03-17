package com.alessiodp.parties.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.alessiodp.parties.players.objects.InviteCooldown;

public class InviteCooldownTask extends BukkitRunnable {
	private InviteCooldown inviteCooldown;
	
	public InviteCooldownTask(InviteCooldown ic) {
		inviteCooldown = ic;
	}
	
	@Override
	public void run() {
		inviteCooldown.cancelTask();
	}
}
