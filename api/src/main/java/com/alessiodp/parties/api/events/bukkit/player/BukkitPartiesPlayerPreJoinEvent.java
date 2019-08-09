package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreJoinEvent;

public abstract class BukkitPartiesPlayerPreJoinEvent extends BukkitPartiesEvent implements IPlayerPreJoinEvent {
	
	public BukkitPartiesPlayerPreJoinEvent() {
		super(false);
	}
}
