package com.alessiodp.parties.api.events.bungee.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BungeePartiesPartyFollowEvent extends BungeePartiesEvent implements Cancellable {
	private boolean cancelled;
	private final Party party;
	private final String joinedServer;
	
	public BungeePartiesPartyFollowEvent(Party party, String joinedServer) {
		this.party = party;
		this.joinedServer = joinedServer;
	}
	
	/**
	 * Get the party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	public Party getParty() {
		return party;
	}
	
	/**
	 * Get the name of the server that the party will join into
	 *
	 * @return Returns the name of the server
	 */
	@NonNull
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
