package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BungeePartiesPartyPreCreateEvent extends BungeePartiesEvent implements IPartyPreCreateEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private String name;
	private String tag;
	private boolean fixed;
	
	public BungeePartiesPartyPreCreateEvent(PartyPlayer player, String name, String tag, boolean fixed) {
		this.player = player;
		this.name = name;
		this.tag = tag;
		this.fixed = fixed;
	}
	
	@NonNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NonNull
	@Override
	public String getPartyName() {
		return name;
	}
	
	@Nullable
	@Override
	public String getPartyTag() {
		return tag;
	}
	
	@Override
	public void setPartyName(@NonNull String name) {
		this.name = name;
	}
	
	@Override
	public void setPartyTag(@Nullable String tag) {
		this.tag = tag;
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
