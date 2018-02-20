package com.alessiodp.parties.players;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.addons.internal.JSONHandler;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.partiesapi.objects.PartyPlayer;

import lombok.Getter;

public class SpyManager {
	private Parties plugin;
	@Getter private Set<UUID> spyList;
	
	public SpyManager(Parties instance) {
		plugin = instance;
		reload();
	}
	
	public void reload() {
		spyList = new HashSet<UUID>();
	}
	
	public void refreshSpyList() {
		reload();
		for (PartyPlayer pp : plugin.getDatabaseManager().getAllPlayers().join()) {
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
	
	public void sendMessageToSpies(String message, String partyName) {
		for (UUID uuid : spyList) {
			Player pl = Bukkit.getPlayer(uuid);
			if (pl != null && pl.isOnline()) {
				if (pl.hasPermission(PartiesPermission.ADMIN_SPY.toString())) {
					PartyPlayerEntity pp = plugin.getPlayerManager().getPlayer(uuid);
					
					if (!pp.getPartyName().equalsIgnoreCase(partyName)) {
						if (JSONHandler.isJSON(message))
							JSONHandler.sendJSON(message, pl);
						else
							pl.sendMessage(message);
					}
				}
			}
		}
	}
}
