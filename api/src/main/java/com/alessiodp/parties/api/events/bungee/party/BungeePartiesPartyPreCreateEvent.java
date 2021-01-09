package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BungeePartiesPartyPreCreateEvent extends BungeePartiesEvent implements IPartyPreCreateEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private String name;
	private boolean fixed;
	
	public BungeePartiesPartyPreCreateEvent(PartyPlayer player, String name, boolean fixed) {
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
