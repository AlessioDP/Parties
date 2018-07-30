package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.tasks.InviteCooldownTask;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InviteCooldown {
	@Getter private PartiesPlugin plugin;
	
	@Getter private int task;
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
		List<InviteCooldown> list = plugin.getCooldownManager().getInviteCooldown().computeIfAbsent(from, k -> new ArrayList<>());
		
		startTime = System.currentTimeMillis() / 1000L; // Unix timestamp
		task = plugin.getPartiesScheduler().scheduleTaskLater(new InviteCooldownTask(this), delay);
		list.add(this);
	}
	
	public void cancelTask() {
		List<InviteCooldown> list = plugin.getCooldownManager().getInviteCooldown().get(from);
		
		if (list != null) {
			list.remove(this);
		}
	}
	
	public long getDiffTime() {
		return (System.currentTimeMillis() / 1000L) - startTime;
	}
	
	public enum CooldownType {
		GLOBAL, INDIVIDUAL;
		
		public int getTick() {
			int ret = 0;
			switch (this) {
			case GLOBAL:
				ret = ConfigParties.GENERAL_INVITE_COOLDOWN_GLOBAL;
				break;
			case INDIVIDUAL:
				ret = ConfigParties.GENERAL_INVITE_COOLDOWN_INDIVIDUAL;
			}
			return ret;
		}
	}
}
