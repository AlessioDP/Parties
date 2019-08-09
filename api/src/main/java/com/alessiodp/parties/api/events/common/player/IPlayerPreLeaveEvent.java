package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IPlayerPreLeaveEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player that is leaveing the party
	 *
	 * @return Returns the {@link PartyPlayer}
	 */
	@NonNull
	PartyPlayer getPartyPlayer();
	
	/**
	 * Get the party
	 *
	 * @return Returns the the {@link Party}
	 */
	@NonNull
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
