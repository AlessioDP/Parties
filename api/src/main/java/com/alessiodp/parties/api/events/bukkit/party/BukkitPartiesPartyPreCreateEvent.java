package com.alessiodp.parties.api.events.bukkit.party;

import com.alessiodp.parties.api.events.bukkit.BukkitPartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.bukkit.event.Cancellable;
import org.checkerframework.checker.nullness.qual.Nullable;

// Cancellable due to Skript event support
public class BukkitPartiesPartyPreCreateEvent extends BukkitPartiesEvent implements IPartyPreCreateEvent, Cancellable {
	private boolean cancelled;
	private final PartyPlayer player;
	private String name;
	private boolean fixed;
	
	public BukkitPartiesPartyPreCreateEvent(PartyPlayer player, String name, boolean fixed) {
		super(false);
		cancelled = false;
		this.player = player;
		this.name = name;
		this.fixed = fixed;
	}
	
	@Nullable
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Nullable
	@Override
	public String getPartyName() {
		return name;
	}
	
	@Override
	public void setPartyName(@Nullable String name) {
		this.name = name;
	}
	
	@Override
	public boolean isFixed() {
		return fixed;
	}
	
	@Override
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
