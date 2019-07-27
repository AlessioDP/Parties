package com.alessiodp.parties.common.players;

import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
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
	
	public void sendMessageToSpies(String message, PartyImpl fromParty, PartyPlayerImpl fromPlayer) {
		for (UUID uuid : spyList) {
			User player = plugin.getPlayer(uuid);
			if (player != null) {
				if (player.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
					PartyPlayerImpl pp = plugin.getPlayerManager().getPlayer(uuid);
					
					if (!pp.getPartyName().equalsIgnoreCase(fromParty.getName())) {
						player.sendMessage(plugin.getMessageUtils().convertAllPlaceholders(message, fromParty, fromPlayer), false);
					}
				}
			}
		}
	}
}
