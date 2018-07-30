package com.alessiodp.parties.bukkit.players;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.bukkit.players.objects.BukkitPartyPlayerImpl;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.UUID;

public class BukkitPlayerManager extends PlayerManager {
	public BukkitPlayerManager(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void reload() {
		super.reload();
		this.bukkit_killSystem = BukkitConfigParties.KILLS_ENABLE;
	}
	
	@Override
	public PartyPlayerImpl initializePlayer(UUID playerUUID) {
		return new BukkitPartyPlayerImpl(plugin, playerUUID);
	}
}
