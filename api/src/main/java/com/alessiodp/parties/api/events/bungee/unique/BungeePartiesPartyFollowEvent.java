package com.alessiodp.parties.api.events.bungee.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.checkerframework.checker.nullness.qual.NonNull;

public abstract class BungeePartiesPartyFollowEvent extends BungeePartiesEvent implements Cancellable {
	/**
	 * Get the party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	public abstract Party getParty();
	
	/**
	 * Get the name of the server that the party will join into
	 *
	 * @return Returns the name of the server
	 */
	@NonNull
	public abstract String getJoinedServer();
}
