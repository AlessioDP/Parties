package com.alessiodp.parties.common.parties;

import com.alessiodp.parties.common.players.objects.InviteCooldown;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class CooldownManager {
	@Getter private HashMap<UUID, Long> chatCooldown;
	@Getter private HashMap<UUID, List<InviteCooldown>> inviteCooldown;
	
	protected CooldownManager() {
		reload();
	}
	
	public void reload() {
		chatCooldown = new HashMap<>();
		inviteCooldown = new HashMap<>();
	}
}
