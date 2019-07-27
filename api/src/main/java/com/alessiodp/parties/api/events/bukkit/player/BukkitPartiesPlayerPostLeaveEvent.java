package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPostLeaveEvent;

public abstract class BukkitPartiesPlayerPostLeaveEvent extends BukkitPartiesEvent implements IPlayerPostLeaveEvent {
	
	public BukkitPartiesPlayerPostLeaveEvent() {
		super(true);
	}
}