package com.alessiodp.parties.players.objects;

import java.util.ArrayList;
import java.util.UUID;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.data.ConfigParties;
import com.alessiodp.parties.tasks.InviteCooldownTask;

import lombok.Getter;

public class InviteCooldown {
	@Getter private Parties plugin;
	
	@Getter private int task;
	@Getter private long startTime;
	
	@Getter private UUID from;
	@Getter private CooldownType type;
	@Getter private UUID invited;
	
	
	public InviteCooldown(Parties plugin, UUID from) {
		// Global
		type = CooldownType.GLOBAL;
		this.plugin = plugin;
		this.from = from;
	}
	
	public InviteCooldown(Parties plugin, UUID from, UUID invited) {
		// Individual
		type = CooldownType.INDIVIDUAL;
		this.plugin = plugin;
		this.from = from;
		this.invited = invited;
	}
	
	
	public void createTask(int delay) {
		ArrayList<InviteCooldown> list = plugin.getPlayerManager().getInviteCooldown().get(from);
		
		if (list == null) {
			list = new ArrayList<>();
			plugin.getPlayerManager().getInviteCooldown().put(from, list);
		}
		
		startTime = System.currentTimeMillis() / 1000L; // Unix timestamp
		task = new InviteCooldownTask(this).runTaskLater(plugin, delay * 20).getTaskId();
		list.add(this);
	}
	
	public void cancelTask() {
		ArrayList<InviteCooldown> list = plugin.getPlayerManager().getInviteCooldown().get(from);
		
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
