package com.alessiodp.parties.bungeecord.events.common.party;

import com.alessiodp.parties.api.events.bungee.party.BungeePartiesPartyPreCreateEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public class PartyPreCreateEventHook extends BungeePartiesPartyPreCreateEvent {
	private boolean cancelled;
	private PartyPlayer player;
	private String party;
	private boolean fixed;
	
	public PartyPreCreateEventHook(PartyPlayer player, String party, boolean fixed) {
		this.player = player;
		this.party = party;
		this.fixed = fixed;
	}
	
	@NotNull
	@Override
	public PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@NotNull
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
