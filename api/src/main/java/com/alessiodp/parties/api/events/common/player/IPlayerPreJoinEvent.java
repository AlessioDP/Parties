package com.alessiodp.parties.api.events.common.player;

import com.alessiodp.parties.api.enums.JoinCause;
import com.alessiodp.parties.api.events.Cancellable;
import com.alessiodp.parties.api.events.PartiesEvent;
import com.alessiodp.parties.api.interfaces.Party;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPlayerPreJoinEvent extends PartiesEvent, Cancellable {
	/**
	 * Get the player that is joining inside the party
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
	 * Get the join cause
	 *
	 * @return the {@code JoinCause} of the event
	 */
	@NotNull JoinCause getCause();
	
	/**
	 * Has been invited?
	 *
	 * @return {@code true} if the player has been invited
	 */
	@Deprecated
	default boolean isInvited() {
		return getCause() == JoinCause.INVITE;
	}
	
	/**
	 * Get the inviter
	 *
	 * @return the {@link PartyPlayer} of the inviter, {@code null} if the player joined via join/ask command
	 */
	@Nullable PartyPlayer getInviter();
}
