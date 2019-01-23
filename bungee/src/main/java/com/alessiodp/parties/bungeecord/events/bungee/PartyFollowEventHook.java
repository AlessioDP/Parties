package com.alessiodp.parties.bungeecord.events.bungee;

import com.alessiodp.parties.api.events.bungee.unique.BungeePartiesPartyFollowEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.jetbrains.annotations.NotNull;

public class PartyFollowEventHook  extends BungeePartiesPartyFollowEvent {
	private boolean cancelled;
	private Party party;
	private String joinedServer;
	
	public PartyFollowEventHook(Party party, String joinedServer) {
		this.party = party;
		this.joinedServer = joinedServer;
	}
	
	@NotNull
	@Override
	public Party getParty() {
		return party;
	}
	
	@NotNull
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
