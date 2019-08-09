package com.alessiodp.parties.common.players.objects;

import com.alessiodp.core.common.scheduling.CancellableTask;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.tasks.InviteCooldownTask;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InviteCooldown {
	@Getter private PartiesPlugin plugin;
	
	private CancellableTask task;
	@Getter private long startTime;
	
	@Getter private UUID from;
	@Getter private CooldownType type;
	@Getter private UUID invited;
	
	
	public InviteCooldown(PartiesPlugin plugin, UUID from) {
		// Global
		type = CooldownType.GLOBAL;
		this.plugin = plugin;
		this.from = from;
	}
	
	public InviteCooldown(PartiesPlugin plugin, UUID from, UUID invited) {
		// Individual
		type = CooldownType.INDIVIDUAL;
		this.plugin = plugin;
		this.from = from;
		this.invited = invited;
	}
	
	
	public void createTask(int delay) {
		startTime = System.currentTimeMillis() / 1000L; // Unix timestamp
		task = plugin.getScheduler().scheduleAsyncLater(new InviteCooldownTask(this), delay, TimeUnit.SECONDS);
	}
	
	public void cancelTask() {
		task.cancel();
	}
	
	public long getDiffTime() {
		return (System.currentTimeMillis() / 1000L) - startTime;
	}
	
	public enum CooldownType {
		GLOBAL, INDIVIDUAL;
		
		public int getCooldown() {
			int ret = 0;
			switch (this) {
			case GLOBAL:
				ret = ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL;
				break;
			case INDIVIDUAL:
				ret = ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL;
				break;
			default:
				// Nothing to do
				break;
			}
			return ret;
		}
	}
}
