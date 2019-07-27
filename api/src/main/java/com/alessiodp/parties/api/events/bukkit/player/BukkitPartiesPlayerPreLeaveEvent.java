package com.alessiodp.parties.api.events.bukkit.player;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.player.IPlayerPreLeaveEvent;

public abstract class BukkitPartiesPlayerPreLeaveEvent extends BukkitPartiesEvent implements IPlayerPreLeaveEvent {
	
	public BukkitPartiesPlayerPreLeaveEvent() {
		super(false);
	}
}
