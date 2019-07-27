package com.alessiodp.parties.bungeecord.players.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;

import java.util.UUID;

public class BungeePartyPlayerImpl extends PartyPlayerImpl {
	
	public BungeePartyPlayerImpl(PartiesPlugin plugin, UUID uuid) {
		super(plugin, uuid);
	}
	
	@Override
	public boolean isVanished() {
		return false;
	}
}
