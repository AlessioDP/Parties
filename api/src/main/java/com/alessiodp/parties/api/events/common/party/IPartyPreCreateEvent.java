package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.Nullable;

public interface IPartyPreCreateEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player that is creating the party
	 *
	 * @return the {@link PartyPlayer}
	 */
	@Nullable PartyPlayer getPartyPlayer();
	
	/**
	 * Get the name of the party
	 *
	 * @return the party name
	 */
	@Nullable String getPartyName();
	
	/**
	 * Set a new name to the party
	 *
	 * @param name the name to set
	 */
	void setPartyName(@Nullable String name);
	
	/**
	 * Is the party fixed?
	 *
	 * @return {@code true} if it's fixed
	 */
	boolean isFixed();
	
	/**
	 * Set if the party is fixed
	 *
	 * @param fixed {@code true} to be fixed
	 */
	void setFixed(boolean fixed);
}
