package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

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
	@NonNull
	String getPartyName();
	
	/**
	 * Get the tag of the party
	 *
	 * @return Returns the party tag
	 */
	@Nullable
	String getPartyTag();
	
	/**
	 * Set a new name to the party
	 *
	 * @param name The name to set
	 */
	void setPartyName(String name);
	
	/**
	 * Set a new tag to the party
	 *
	 * @param tag The tag to set
	 */
	void setPartyTag(String tag);
	
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
