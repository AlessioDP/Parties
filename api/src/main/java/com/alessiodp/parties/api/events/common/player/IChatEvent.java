package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public interface IChatEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player who sent the message
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
	PartyPlayer getPartyPlayer();
	
	/**
	 * Get the party
	 *
	 * @return Returns the the {@link Party}
	 */
	@NotNull
	Party getParty();
	
	/**
	 * Get the message of the player
	 *
	 * @return Returns the message
	 */
	@NotNull
	String getMessage();
	
	/**
	 * Set the message of the player
	 *
	 * @param message The
	 *                message to set
	 */
	void setMessage(String message);
}
