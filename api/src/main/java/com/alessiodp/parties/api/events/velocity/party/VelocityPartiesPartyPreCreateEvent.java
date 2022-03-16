package com.alessiodp.parties.api.events.velocity.party;

import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.Nullable;

public class VelocityPartiesPartyPreCreateEvent extends VelocityPartiesEvent implements IPartyPreCreateEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private String name;
	private boolean fixed;
	
	public VelocityPartiesPartyPreCreateEvent(PartyPlayer player, String name, boolean fixed) {
		this.player = player;
		this.name = name;
		this.fixed = fixed;
	}
	
	@Override
	public @Nullable PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public @Nullable String getPartyName() {
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
