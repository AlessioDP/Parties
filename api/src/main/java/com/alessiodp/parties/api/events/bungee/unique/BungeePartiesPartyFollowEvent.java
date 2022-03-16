package com.alessiodp.parties.api.events.bungee.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.jetbrains.annotations.NotNull;

public class BungeePartiesPartyFollowEvent extends BungeePartiesEvent implements Cancellable {
	private boolean cancelled;
	private final Party party;
	private final String joinedServer;
	
	public BungeePartiesPartyFollowEvent(Party party, String joinedServer) {
		this.party = party;
		this.joinedServer = joinedServer;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	/**
	 * Get the party
	 *
	 * @return the {@link Party}
	 */
	public @NotNull Party getParty() {
		return party;
	}
	
	/**
	 * Get the name of the server that the party will join into
	 *
	 * @return the name of the server
	 */
	public @NotNull String getJoinedServer() {
		return joinedServer;
	}
}
