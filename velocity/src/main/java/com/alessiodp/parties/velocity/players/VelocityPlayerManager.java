package com.alessiodp.parties.velocity.players;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.PlayerManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.velocity.players.objects.VelocityPartyPlayerImpl;

import java.util.UUID;

public class VelocityPlayerManager extends PlayerManager {
	public VelocityPlayerManager(PartiesPlugin plugin) {
		super(plugin);
	}
	
	@Override
	public PartyPlayerImpl initializePlayer(UUID playerUUID) {
		return new VelocityPartyPlayerImpl(plugin, playerUUID);
	}
}
