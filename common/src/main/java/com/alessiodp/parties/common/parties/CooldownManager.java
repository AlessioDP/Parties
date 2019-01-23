package com.alessiodp.parties.common.parties;

import com.alessiodp.parties.common.players.objects.InviteCooldown;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CooldownManager {
	@Getter private HashMap<UUID, Long> chatCooldown;
	@Getter private HashMap<UUID, List<InviteCooldown>> inviteCooldown;
	@Getter private HashMap<UUID, Long> teleportCooldown;
	
	public CooldownManager() {
		reload();
	}
	
	private void reload() {
		chatCooldown = new HashMap<>();
		inviteCooldown = new HashMap<>();
		teleportCooldown = new HashMap<>();
	}
}
