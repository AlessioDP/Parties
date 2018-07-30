package com.alessiodp.parties.bungeecord.user;

import com.alessiodp.parties.common.user.OfflineUser;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class BungeeOfflineUser implements OfflineUser {
	private ProxiedPlayer player;
	private UUID uuid;
	
	public BungeeOfflineUser(ProxiedPlayer offlinePlayer, UUID uuid) {
		player = offlinePlayer;
		this.uuid = uuid;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public boolean isOnline() {
		return player != null;
	}
	
	@Override
	public String getName() {
		return isOnline() ? player.getName() : "";
	}
	
}
