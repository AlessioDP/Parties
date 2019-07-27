package com.alessiodp.parties.bukkit.events.common.party;

import com.alessiodp.parties.api.events.bukkit.party.BukkitPartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PartyPreCreateEventHook extends BukkitPartiesPartyPreCreateEvent {
	private boolean cancelled;
	private final PartyPlayer player;
	private String party;
	private boolean fixed;
	
	public PartyPreCreateEventHook(PartyPlayer player, String party, boolean fixed) {
		cancelled = false;
		this.player = player;
		this.party = party;
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
		return party;
	}
	
	@Override
	public void setPartyName(String name) {
		party = name;
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
