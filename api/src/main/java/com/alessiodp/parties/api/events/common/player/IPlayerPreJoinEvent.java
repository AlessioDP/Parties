package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IPlayerPreJoinEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player that is joining inside the party
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
	 * Has been invited?
	 *
	 * @return Returns {@code true} if the player has been invited
	 */
	@Deprecated
	default boolean isInvited() {
		return getCause() == JoinCause.INVITE;
	}
	
	/**
	 * Get the inviter
	 *
	 * @return Returns the {@link PartyPlayer} of the inviter, {@code null} if the player joined via join/ask command
	 */
	@Nullable
	PartyPlayer getInviter();
	
	/**
	 * Get the join cause
	 *
	 * @return Returns the {@code JoinCause} of the event
	 */
	@NonNull
	JoinCause getCause();
	
}
