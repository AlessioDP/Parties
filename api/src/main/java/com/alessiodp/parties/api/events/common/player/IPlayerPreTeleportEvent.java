package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;

public interface IPlayerPreTeleportEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player who is getting teleported
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
}
