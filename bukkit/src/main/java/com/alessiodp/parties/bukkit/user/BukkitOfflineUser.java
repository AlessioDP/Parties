package com.alessiodp.parties.bukkit.user;

import com.alessiodp.parties.common.user.OfflineUser;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BukkitOfflineUser implements OfflineUser {
	private OfflinePlayer player;
	
	public BukkitOfflineUser(OfflinePlayer offlinePlayer) {
		player = offlinePlayer;
	}
	
	@Override
	public UUID getUUID() {
		return player.getUniqueId();
	}
	
	@Override
	public boolean isOnline() {
		return player.isOnline();
	}
	
	@Override
	public String getName() {
		return player.getName();
	}
	
}
