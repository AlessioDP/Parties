package com.alessiodp.parties.api.events.common.party;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public interface IPartyPostCreateEvent extends PartiesEvent {
	/**
	 * Get the creator of the party
	 * 
	 * @return Returns the {@link PartyPlayer}
	 */
	@NotNull
	PartyPlayer getCreator();
	
	/**
	 * Get the party
	 * 
	 * @return Returns the {@link Party}
	 */
	@NotNull
	Party getParty();
}
