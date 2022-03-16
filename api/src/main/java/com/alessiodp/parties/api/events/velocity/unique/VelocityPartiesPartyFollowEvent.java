package com.alessiodp.parties.api.events.velocity.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.velocity.VelocityPartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.jetbrains.annotations.NotNull;

public class VelocityPartiesPartyFollowEvent extends VelocityPartiesEvent implements Cancellable {
	private boolean cancelled;
	private final Party party;
	private final RegisteredServer joinedServer;
	
	public VelocityPartiesPartyFollowEvent(Party party, RegisteredServer joinedServer) {
		this.party = party;
		this.joinedServer = joinedServer;
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
	public @NotNull RegisteredServer getJoinedServer() {
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
