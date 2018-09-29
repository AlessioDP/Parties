package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPlayerPostLeaveEvent extends PartiesEvent {
	/**
	 * Get the player that left the party
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
	 * Has the player been kicked?
	 * 
	 * @return Returns {@code true} if the player has been kicked
	 */
	boolean isKicked();
	
	/**
	 * Get the kicker
	 * 
	 * @return Returns the {@link PartyPlayer} that has kicked the player, returns {@code null} if {@link #isKicked()} returns {@code false}
	 */
	@Nullable
	PartyPlayer getKicker();
	

}
