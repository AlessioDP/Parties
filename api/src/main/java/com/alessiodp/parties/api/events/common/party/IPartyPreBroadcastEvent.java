package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPartyPreBroadcastEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the message
	 *
	 * @return the message
	 */
	@NotNull String getMessage();
	
	/**
	 * Set the message of the player
	 *
	 * @param message the message to set
	 */
	void setMessage(@NotNull String message);
	
	/**
	 * Get the player who sent the message
	 *
	 * @return the {@link PartyPlayer}
	 */
	@Nullable PartyPlayer getPartyPlayer();
}
