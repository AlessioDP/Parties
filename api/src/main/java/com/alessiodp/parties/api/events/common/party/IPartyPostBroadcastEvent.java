package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPartyPostBroadcastEvent extends PartiesEvent {
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
	 * Get the player who sent the message
	 *
	 * @return the {@link PartyPlayer}
	 */
	@Nullable PartyPlayer getPartyPlayer();
}
