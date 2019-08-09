package com.alessiodp.parties.common.players.spy;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpyManager {
	private final PartiesPlugin plugin;
	@Getter private Set<UUID> spyList;
	
	public SpyManager(PartiesPlugin instance) {
		plugin = instance;
	}
	
	public void reload() {
		spyList = new HashSet<>();
		for (PartyPlayerImpl pp : plugin.getDatabaseManager().getAllPlayers()) {
			if (pp.isSpy())
				spyList.add(pp.getPlayerUUID());
		}
	}
	
	public void addSpy(UUID uuid) {
		spyList.add(uuid);
	}
	
	public void removeSpy(UUID uuid) {
		spyList.remove(uuid);
	}
	
	public void sendSpyMessage(SpyMessage message) {
		if (message.getMessage() != null && !message.getMessage().isEmpty()) {
			for (UUID uuid : spyList) {
				User player = plugin.getPlayer(uuid);
				if (player != null && player.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
					player.sendMessage(message.toMessage(), false);
				}
			}
		}
	}
}
