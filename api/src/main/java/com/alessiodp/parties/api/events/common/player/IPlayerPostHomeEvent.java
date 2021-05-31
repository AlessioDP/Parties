package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface IPlayerPostHomeEvent extends PartiesEvent {
	/**
	 * Get the player who got teleported
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull
	PartyPlayer getPartyPlayer();
	
	/**
	 * Get the party
	 *
	 * @return Returns the {@link Party}
	 */
	@NonNull
	Party getParty();
	
	/**
	 * Get the destination home
	 *
	 * @return Returns the {@link PartyHome}
	 */
	@NonNull
	PartyHome getHome();
}
