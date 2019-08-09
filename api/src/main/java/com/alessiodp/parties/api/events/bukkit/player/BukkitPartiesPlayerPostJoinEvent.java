package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostJoinEvent;

public abstract class BukkitPartiesPlayerPostJoinEvent extends BukkitPartiesEvent implements IPlayerPostJoinEvent {
	
	public BukkitPartiesPlayerPostJoinEvent() {
		super(true);
	}
}