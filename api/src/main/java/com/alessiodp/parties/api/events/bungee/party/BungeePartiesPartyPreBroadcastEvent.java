package com.alessiodp.parties.api.events.bungee.party;

import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.events.common.party.IPartyPreBroadcastEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BungeePartiesPartyPreBroadcastEvent extends BungeePartiesEvent implements IPartyPreBroadcastEvent {
	private boolean cancelled;
	private final Party party;
	private String message;
	private final PartyPlayer player;
	
	public BungeePartiesPartyPreBroadcastEvent(Party party, String message, PartyPlayer player) {
		this.party = party;
		this.message = message;
		this.player = player;
	}
	
	@Override
	public @NotNull Party getParty() {
		return party;
	}
	
	@Override
	public @NotNull String getMessage() {
		return message;
	}
	
	@Override
	public void setMessage(@NotNull String message) {
		this.message = message;
	}
	
	@Override
	public @Nullable PartyPlayer getPartyPlayer() {
		return player;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
