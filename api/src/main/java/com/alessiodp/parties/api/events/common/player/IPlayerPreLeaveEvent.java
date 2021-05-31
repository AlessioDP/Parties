package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.enums.LeaveCause;
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
	 * @return Returns the {@link Party}
	 */
	@NonNull
	Party getParty();
	
	/**
	 * Get the leave cause
	 *
	 * @return Returns the {@code LeaveCause} of the event
	 */
	@NonNull
	LeaveCause getCause();
	
	/**
	 * Has the player been kicked?
	 *
	 * @return Returns {@code true} if the player has been kicked
	 * @deprecated Use getCause()
	 */
	@Deprecated
	default boolean isKicked() {
		return getCause() == LeaveCause.KICK || getCause() == LeaveCause.BAN;
	}
	
	/**
	 * Get the kicker
	 *
	 * @return Returns the {@link PartyPlayer} that has kicked the player, returns {@code null} if {@link #isKicked()} returns {@code false}
	 */
	@Nullable
	PartyPlayer getKicker();
}
