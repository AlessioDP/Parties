package com.alessiodp.parties.bungeecord.events.bungee;

import com.alessiodp.parties.api.events.bungee.unique.BungeePartiesPartyFollowEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PartyFollowEventHook  extends BungeePartiesPartyFollowEvent {
	private boolean cancelled;
	private final Party party;
	private final String joinedServer;
	
	public PartyFollowEventHook(Party party, String joinedServer) {
		this.party = party;
		this.joinedServer = joinedServer;
	}
	
	@NonNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NonNull
	@Override
	public String getJoinedServer() {
		return joinedServer;
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
