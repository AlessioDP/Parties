package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.Nullable;

public interface IPartyPreCreateEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player that is creating the party
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@Nullable
	PartyPlayer getPartyPlayer();
	
	/**
	 * Get the name of the party
	 *
	 * @return Returns the party name
	 */
	@Nullable
	String getPartyName();
	
	/**
	 * Set a new name to the party
	 *
	 * @param name The name to set
	 */
	void setPartyName(@Nullable String name);
	
	/**
	 * Is the party fixed?
	 *
	 * @return Returns {@code true} if it's fixed
	 */
	boolean isFixed();
	
	/**
	 * Set if the party is fixed
	 *
	 * @param fixed {@code True} to be fixed
	 */
	void setFixed(boolean fixed);
}
