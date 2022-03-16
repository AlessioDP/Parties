package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyHome;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public interface IPlayerPostHomeEvent extends PartiesEvent {
	/**
	 * Get the player who got teleported
	 *
	 * @return the {@link PartyPlayer}
	 */
	@NotNull PartyPlayer getPartyPlayer();
	
	/**
	 * Get the party
	 *
	 * @return the {@link Party}
	 */
	@NotNull Party getParty();
	
	/**
	 * Get the destination home
	 *
	 * @return the {@link PartyHome}
	 */
	@NotNull PartyHome getHome();
}
