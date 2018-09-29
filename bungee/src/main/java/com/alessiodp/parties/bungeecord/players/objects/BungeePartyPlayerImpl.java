package com.alessiodp.parties.bungeecord.players.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;

import java.util.UUID;

public class BungeePartyPlayerImpl extends PartyPlayerImpl {
	
	public BungeePartyPlayerImpl(PartiesPlugin instance, UUID uuid) {
		super(instance, uuid);
	}
	
	@Override
	public boolean isVanished() {
		return false;
	}
	
	@Override
	public void sendDirect(String message) {
		// Overriding superclass send
		User player = plugin.getPlayer(getPlayerUUID());
		if (player != null) {
			player.sendMessage(message, true);
		}
	}
}
