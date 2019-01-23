package com.alessiodp.parties.api.events.bungee.unique;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.bungee.BungeePartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import org.jetbrains.annotations.NotNull;

public abstract class BungeePartiesPartyFollowEvent extends BungeePartiesEvent implements Cancellable {
	/**
	 * Get the party
	 *
	 * @return Returns the {@link Party}
	 */
	@NotNull
	public abstract Party getParty();
	
	/**
	 * Get the name of the server that the party will join into
	 *
	 * @return Returns the name of the server
	 */
	@NotNull
	public abstract String getJoinedServer();
}
